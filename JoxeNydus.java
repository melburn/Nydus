package bot;

import java.util.ArrayList;

import gameLogic.*;

public class JoxeNydus implements Brain {
	private GameState gamestate;
	private Snake self;
	private Position goal = new Position(0, 0);
	private Direction previousDirection;

	public double getDistanceValue(Position a_start, Position a_end) {
		return Math.sqrt(Math.pow(a_start.getX() - a_end.getX(), 2) + Math.pow(a_start.getY() - a_end.getY(), 2));
	}
	
	public Direction aStar() {
		if (!gamestate.getBoard().hasFruit(goal)) {
			ArrayList<Position> l_fruit = gamestate.getFruits();
			if (l_fruit.isEmpty()) {
				return null;
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
		Position l_forwPos  = previousDirection.calculateNextPosition(self.getHeadPosition());

		double l_left    = getDistanceValue(l_leftPos, goal)  + (gamestate.getBoard().isLethal(l_leftPos)  ? 1000 : 0);
		double l_right   = getDistanceValue(l_rightPos, goal) + (gamestate.getBoard().isLethal(l_rightPos) ? 1000 : 0);
		double l_forward = getDistanceValue(l_nextPos, goal)  + (gamestate.getBoard().isLethal(l_nextPos)  ? 1000 : 0);

		if (l_left < l_forward) {
			return l_right < l_left ? self.getCurrentDirection().turnRight() : self.getCurrentDirection().turnLeft();
		} else if (l_right < l_forward) {
			return l_left < l_right ? self.getCurrentDirection().turnLeft() : self.getCurrentDirection().turnRight();
		}
		return self.getCurrentDirection();

		/*
		if (l_right < l_left) {
			if (l_next < l_right) {
				return self.getCurrentDirection();
			}
			return self.getCurrentDirection().turnRight();
		} else if (l_next < l_left) {
			return self.getCurrentDirection();
		}
		return self.getCurrentDirection().turnLeft();
		*/
	}


	public Direction getNextMove(Snake yourSnake, GameState newGamestate) {
		self = yourSnake;
		this.gamestate = newGamestate;
		Direction nextDirection;

		int forward = 0, left = 0, right = 0;
		Position nextPosition;
		ArrayList<Snake> temp;

		if ((nextDirection = aStar()) == null) {
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

			nextDirection = self.getCurrentDirection();

			if (left < forward) {
				return right < left ? self.getCurrentDirection().turnRight() : self.getCurrentDirection().turnLeft();
				/*
				if (right < left) {
					return self.getCurrentDirection().turnRight();
				} else {
					return self.getCurrentDirection().turnLeft();				
				}
				*/
			} else if (right < forward) {
				return left < right ? self.getCurrentDirection().turnLeft() : self.getCurrentDirection().turnRight();
				/*
				if (left < right) {
					return self.getCurrentDirection().turnLeft();
				} else {
					return self.getCurrentDirection().turnRight();
				}
				*/
			}
		}
		return nextDirection;
	}
}
