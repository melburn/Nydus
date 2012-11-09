package bot;

import gameLogic.*;

public class Nydus implements Brain {
	private GameState gamestate;
	private Snake self;
	
	public Direction getNextMove(Snake yourSnake, GameState gamestate) {
		self = yourSnake;
		this.gamestate = gamestate;
		Direction previousDirection = self.getCurrentDirection();
		Direction nextDirection;

		int forward, left, right;
		
		if (gamestate.willCollide(self, previousDirection)) {
			forward = 1000;
			Position nextPosition = previousDirection.calculateNextPosition(self.getHeadPosition());
			ArrayList<Snake> temp = GameState.getBoard().getSquare(nextPostition).getSnakes();
			if (temp.size() < 2 && temp.size() > 0 && temp.get(0).getTailPosition() == nextPosition) {
				forward = 998;
			}
		}
		if (gameState.willCollide(self, previousDirection.turnLeft())) {
			left = 1000;
			Position nextPosition = previousDirection.turnLeft().calculateNextPosition(self.getHeadPosition());
			ArrayList<Snake> temp = GameState.getBoard().getSquare(nextPostition).getSnakes();
			if (temp.size() < 2 && temp.size() > 0 && temp.get(0).getTailPosition() == nextPosition) {
				left = 998;
			}
		}
		if (gameState.willCollide(self, previousDirection.turnRight())) {
			right = 1000;
			Position nextPosition = previousDirection.turnRight().calculateNextPosition(self.getHeadPosition());
			ArrayList<Snake> temp = GameState.getBoard().getSquare(nextPostition).getSnakes();
			if (temp.size() < 2 && temp.size() > 0 && temp.get(0).getTailPosition() == nextPosition) {
				right = 998;
			}
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
