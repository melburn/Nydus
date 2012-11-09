package bot;

import java.util.ArrayList;

import gameLogic.*;
import Math;

public class JoxeNydus implements Brain {
	private GameState gamestate;
	private Snake self;
	private Position goal = new Position(0, 0);

	public double getDistanceValue(Position a_start, Position a_end) {
		return Math.sqrt(Math.pow(a_start.getX() - a_end.getX(), 2) + Math.Pow(a_start.getY() - a_end.getY(), 2));
	}
	
	public Direction aStar() {
		if (!b.hasFruit(goal)) {
			ArrayList<Position> l_fruit = gamestate.getFruits();
			if (l_fruit.isEmpty()) {
				return previousDirection;
			}
			
			goal = l_fruit.get(0);
			double l_length = 1000;
			
			for (Position l_pos : l_fruit) {
				if (getDistanceValue(self.getHeadPosition(), l_pos) < l_length) {
					goal = l_pos;
				}
			}
		}
		
		Position l_leftPos  = previousDirection.turnLeft().calculateNextPosition(self.getHeadPosition());
		Position l_rightPos = previousDirection.turnRight().calculateNextPosition(self.getHeadPosition());
		Position l_nextPos  = previousDirection.calculateNextPosition(self.getHeadPosition());

		double l_left = getDistance(l_leftPos, goal);
		double l_right = getDistance(l_rightPos, goal);
		double l_next = getDistance(l_nextPos, goal);

		if (l_right < l_left) {
			if (l_next < l_right) {
				return previousDirection;
			}
			return previousDirection.turnRight();
		} else if (l_next < l_left) {
			return previousDirection;
		}
		return previousDirection.turnLeft();
	}


	public Direction getNextMove(Snake yourSnake, GameState newGamestate) {
		self = yourSnake;
		this.gamestate = newGamestate;
		Direction previousDirection = self.getCurrentDirection();
		Direction nextDirection;

		int forward = 0, left = 0, right = 0;
		Position nextPosition;
		ArrayList<Snake> temp;
		
		if (gamestate.willCollide(self, previousDirection)) {
			forward = 1000;
			nextPosition = previousDirection.calculateNextPosition(self.getHeadPosition());
			temp = gamestate.getBoard().getSquare(nextPosition).getSnakes();
			if (temp.size() < 2 && temp.size() > 0 && temp.get(0).getTailPosition() == nextPosition) {
				forward = 998;
			}
		}
		if (gamestate.willCollide(self, previousDirection.turnLeft())) {
			left = 1000;
			nextPosition = previousDirection.turnLeft().calculateNextPosition(self.getHeadPosition());
			temp = gamestate.getBoard().getSquare(nextPosition).getSnakes();
			if (temp.size() < 2 && temp.size() > 0 && temp.get(0).getTailPosition() == nextPosition) {
				left = 998;
			}
		}
		if (gamestate.willCollide(self, previousDirection.turnRight())) {
			right = 1000;
			nextPosition = previousDirection.turnRight().calculateNextPosition(self.getHeadPosition());
			temp = gamestate.getBoard().getSquare(nextPosition).getSnakes();
			if (temp.size() < 2 && temp.size() > 0 && temp.get(0).getTailPosition() == nextPosition) {
				right = 998;
			}
		}

		if (forward == 0 && left == 0 && right == 0) {
			nextDirection = aStar();
		}

		nextDirection = self.getCurrentDirection();

		if (left < forward) {
			nextDirection = self.getCurrentDirection().turnLeft();
		}
		
		if (right < left) {
			nextDirection = self.getCurrentDirection().turnRight();
		}
		
		return nextDirection;
	}
}
