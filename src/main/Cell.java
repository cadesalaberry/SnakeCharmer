package main;

public class Cell<E> {

	int x, y;

	private E e = null;

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	synchronized public void setObject(E e) {
		this.e = e;
	}

	synchronized public E getObject() {
		return this.e;
	}

	synchronized public E empty() {
		E temp = this.e;
		this.e = null;
		return temp;
	}

	public boolean isEmpty() {
		return this.e == null;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String toString() {
		
		if (e instanceof Snake) {
			
			Snake s = (Snake) e;
			
			if (this == s.head())
				return "" + s.id();
		}
		
		return e == null ? "." : this.e.toString();
	}
}
