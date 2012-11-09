package bot;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import gameLogic.*;

public class Nydus implements Brain {
	private GameState gamestate;
	private Snake self;
	PriorityQueue<Position> pq = new PriorityQueue<Position>();
	int stepsTaken = 5;
	PriorityQueue<Integer> marks = new PriorityQueue<Integer>();
	int stepsToMark = 0;
	
	public Direction getNextMove(Snake yourSnake, GameState newGamestate) {
		self = yourSnake;
		this.gamestate = newGamestate;
		Direction previousDirection = self.getCurrentDirection();
		Direction nextDirection;

		int forward = 0, left = 0, right = 0;
		int l = 15, r = 15, f = 15;
		
		pq.add(previousDirection.calculateNextPosition(self.getHeadPosition()));
		ArrayList<Position> al = new ArrayList<Position>();
		stepsTaken = 5;
		marks = new PriorityQueue<Integer>();
		marks.add(1);
		stepsToMark = 0;
		while(pq.size() > 0){
			forward += assertThreat(al, f--, pq.poll());
		}//asd
		stepsTaken = 5;
		marks = new PriorityQueue<Integer>();
		marks.add(1);
		stepsToMark = 0;
		pq = new PriorityQueue<Position>();
		pq.add(previousDirection.turnLeft().calculateNextPosition(self.getHeadPosition()));
		al = new ArrayList<Position>();
		while(pq.size() > 0){
			left += assertThreat(al, l--, pq.poll());
		}
		pq = new PriorityQueue<Position>();
		pq.add(previousDirection.turnRight().calculateNextPosition(self.getHeadPosition()));
		al = new ArrayList<Position>();
		stepsTaken = 5;
		marks = new PriorityQueue<Integer>();
		marks.add(1);
		stepsToMark = 0;
		while(pq.size() > 0){
			right += assertThreat(al, r--, pq.poll());
		}
		pq = new PriorityQueue<Position>();
		
		
		if (gamestate.willCollide(self, previousDirection)) {
			forward += 10000;
			Position nextPosition = previousDirection.calculateNextPosition(self.getHeadPosition());
			ArrayList<Snake> temp = gamestate.getBoard().getSquare(nextPosition).getSnakes();
			if(temp.size() < 2 && temp.size() > 0 && temp.get(0).getTailPosition() == nextPosition)
				forward -= 600;
		}
		if (gamestate.willCollide(self, previousDirection.turnLeft())) {
			left += 10000;
			Position nextPosition = previousDirection.turnLeft().calculateNextPosition(self.getHeadPosition());
			ArrayList<Snake> temp = gamestate.getBoard().getSquare(nextPosition).getSnakes();
			if(temp.size() < 2 && temp.size() > 0 && temp.get(0).getTailPosition() == nextPosition)
				left -= 600;
		}
		if (gamestate.willCollide(self, previousDirection.turnRight())) {
			right += 10000;
			Position nextPosition = previousDirection.turnRight().calculateNextPosition(self.getHeadPosition());
			ArrayList<Snake> temp = gamestate.getBoard().getSquare(nextPosition).getSnakes();
			if(temp.size() < 2 && temp.size() > 0 && temp.get(0).getTailPosition() == nextPosition)
				right -= 600;
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
		private int assertThreat(ArrayList<Position> al, int steps, Position current) {
			if (!al.contains(self.getHeadPosition()))
				al.add(self.getHeadPosition());
			List<Position> neigh = current.getNeighbours();
			al.add(current);
			if((int)marks.peek() - stepsToMark > 0) {
				stepsToMark++;
			}
			else {
				marks.poll();
				stepsToMark = 0;
				stepsTaken--;
			}
			
			
			if(gamestate.getBoard().isLethal(current)){
				return stepsTaken*stepsTaken*stepsTaken;
			}
			
			if(steps < 1)
				for(int i = 0; i < 4; i++) {
					marks.add(pq.size());
					if(!al.contains(neigh.get(i)))
					{
						pq.add(neigh.get(i));
					}
				}
			return 0;
		}
}
