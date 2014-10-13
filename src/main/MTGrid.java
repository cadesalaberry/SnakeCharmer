package main;

import java.util.ArrayList;
import java.util.List;

public class MTGrid<E> {
	int length, height;
	Cell<E>[][] grid;

	@SuppressWarnings("unchecked")
	MTGrid(int length, int height) {
		this.length = length;
		this.height = height;
		grid = new Cell[length][height];
		resetGrid();
	}

	private void resetGrid() {
		for (int x = 0; x < this.length; x++) {
			for (int y = 0; y < this.height; y++) {
				grid[x][y] = new Cell<E>(x, y);
			}
		}
	}

	public void set(Cell<E> c, E e) {
		this.getCell(c.getX(), c.getY()).setObject(e);
	}

	public synchronized void set(int x, int y, E e) {
		this.getCell(x, y).setObject(e);
		this.notify();
	}

	public Cell<E> getCell(Cell<E> c) {
		return this.getCell(c.getX(), c.getY());
	}

	public Cell<E> getCell(int x, int y) {

		x = x < 0 ? length + x : x;
		y = y < 0 ? length + y : y;

		return this.grid[x % length][y % height];
	}

	public Cell<E> getCellFromDirection(Cell<E> c, Directions d) {

		switch (d) {
		case NORTH:
			return getCell(c.getX(), c.getY() + 1);
		case EAST:
			return getCell(c.getX() + 1, c.getY());
		case SOUTH:
			return getCell(c.getX(), c.getY() - 1);
		case WEST:
			return getCell(c.getX() - 1, c.getY());
		default:
			return null;
		}
	}

	public boolean isEmpty(int x, int y) {
		return this.getCell(x, y).isEmpty();
	}

	public E empty(Cell<E> c) {
		return this.getCell(c.getX(), c.getY()).empty();
	}

	public synchronized E empty(int x, int y) {
		E e = this.getCell(x, y).empty();
		this.notify();
		return e;
	}

	private void addIfEmpty(int x, int y, List<Cell<E>> free) {
		if (this.isEmpty(x, y)) {
			free.add(this.getCell(x, y));
		}
	}

	public List<Cell<E>> getFreeCells(Cell<E> c) {
		return getFreeCells(c.getX(), c.getY());
	}

	public List<Cell<E>> getFreeCells(int x, int y) {
		List<Cell<E>> free = new ArrayList<>();

		addIfEmpty(x, y + 1, free);
		addIfEmpty(x + 1, y, free);
		addIfEmpty(x, y - 1, free);
		addIfEmpty(x - 1, y, free);

		return free;
	}

	public int getHeight() {
		return height;
	}

	public int getLenght() {
		return length;
	}

	public String toString() {
		String out = "\n";

		for (Cell<E>[] vertical : grid) {
			for (Cell<E> horizontal : vertical) {
				String snake = horizontal.toString();
				out += snake + " ";
			}
			out += "\n";
		}
		return out;
	}
}
