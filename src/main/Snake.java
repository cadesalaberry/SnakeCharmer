package main;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Snake implements Runnable {

	private int size = 0;
	private int heartbeat = 0;
	
	private List<Cell<Snake>> snake;
	private MTGrid<Snake> mtGrid;

	private int id;

	private int moveCount = 0;

	private static volatile int globalID = 0;

	private SecureRandom rand = new SecureRandom();

	public Snake(MTGrid<Snake> mtGrid, Collection<Cell<Snake>> coll, int heartbeat) {
		this.snake = new ArrayList<>(coll);
		this.heartbeat = heartbeat;
		this.mtGrid = mtGrid;

		// Do not forget to set the snake id.
		this.id = globalID++;

		this.size = this.snake.size();
	}

	/**
	 * A negative size will lead to an infinite snake.
	 * 
	 * @param mtGrid
	 * @param start
	 * @param size
	 */
	public Snake(MTGrid<Snake> mtGrid, Cell<Snake> start, int size, int heartbeat) {
		this.snake = new ArrayList<>();
		this.heartbeat = heartbeat;
		this.mtGrid = mtGrid;
		this.size = size;

		// Do not forget to set the snake id.
		this.id = globalID++;

		this.snake.add(start);
	}

	/**
	 * Returns false if the snake is stuck.
	 * 
	 * @return
	 */
	public boolean safeRandomMove() {

		int size = Directions.values().length;

		Directions d;
		Cell<Snake> next;

		if (this.isStuck())
			return false;

		do {
			d = Directions.values()[rand.nextInt(size)];
			next = mtGrid.getCellFromDirection(this.head(), d);

			// Tries another move is selected one not available.
		} while (!next.isEmpty());

		// Locks the cell before moving into it.
		synchronized (next) {
			unsafeMove(next);
		}

		return true;
	}

	public boolean isStuck() {

		// Checks if the snake can still move.
		return mtGrid.getFreeCells(this.head()).size() == 0;
	}

	/**
	 * Low level move on the grid. Also takes care of the snake spawning.
	 * 
	 * @param c
	 */
	public void unsafeMove(Cell<Snake> c) {
		mtGrid.set(c, this);
		snake.add(0, c);

		// Checks the size of the snake. continue spawning snake
		if (snake.size() >= this.size || this.size < 0)
			// Gets the tail of the snake and takes it off the grid.
			mtGrid.empty(snake.remove(snake.size() - 1));
		
		this.moveCount++;
	}

	@Override
	public void run() {
		
		boolean run = true;
		
		while (run) {
			this.safeRandomMove();
			try {
				Thread.sleep(this.heartbeat);
			} catch (InterruptedException e) {
				run = false;
			}
		}
	}

	public Cell<Snake> head() {
		return this.snake.get(0);
	}

	public Cell<Snake> tail() {
		return this.snake.get(snake.size() - 1);
	}

	public int id() {
		return this.id;
	}

	public String toString() {
		return "o";
	}

	public String report() {
		return "#" + this.id() + " did " + this.moveCount + " moves.";
	}
}
