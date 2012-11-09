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
		}
		if (gameState.willCollide(self, previousDirection.turnLeft())) {
			left = 1000;
		}
		if (gameState.willCollide(self, previousDirection.turnRight())) {
			right = 1000;
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
