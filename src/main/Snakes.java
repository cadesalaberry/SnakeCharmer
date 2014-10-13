package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Snakes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length < 4) {

			System.out.println("Invalid args. Running default simulation.");
			int snakeCount = 5;
			int snakeLength = 15;
			int gridSize = 20;
			int heartbeat = 10;

			runSimulation(snakeCount, snakeLength, gridSize, heartbeat);
			return;
		}

		int p, n, m, t;

		p = Integer.parseInt(args[0]);
		n = Integer.parseInt(args[1]);
		m = Integer.parseInt(args[2]);
		t = Integer.parseInt(args[3]);

		if (!validateArgs(p, n, m, t)) {
			return;
		}
		runSimulation(p, n, m, t);
	}

	public static void runSimulation(int p, int n, int m, int time) {

		MTGrid<Snake> mtGrid = new MTGrid<>(m, m);
		List<Snake> snakes = new ArrayList<Snake>();
		List<Thread> threads = new ArrayList<>();

		for (int i = 0; i < p; i++) {

			Cell<Snake> start = mtGrid.getCell(i, i);

			Snake s = new Snake(mtGrid, start, n, time);

			snakes.add(s);

		}

		for (Snake s : snakes) {
			Thread t = new Thread(s);
			threads.add(t);
			t.start();
		}

		HashSet<Snake> stucked = new HashSet<>();

		long t0 = System.currentTimeMillis();

		do {
			for (Snake s : snakes)
				if (s.isStuck())
					stucked.add(s);
				else
					stucked.remove(s);

			try {
				// Waits for the grid to be modified.
				synchronized (mtGrid) {
					mtGrid.wait(time);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (System.currentTimeMillis() - t0 > 60000) {
				System.out.println("Timeout.");
				break;
			}

		} while (stucked.size() != snakes.size());

		for (Thread t : threads)
			t.interrupt();

		System.out.print(mtGrid);

		for (Snake s : snakes)
			System.out.println(s.report());

		System.exit(0);

	}

	public static boolean validateArgs(int p, int n, int m, int t) {

		boolean valid = true;

		// Checks the number of snakes to spawn.
		if (!(1 <= p && p < m)) {

			System.err.println("Error: q should be between 1 and 4.");
			valid = false;
		}

		// Checks the size for the snakes.
		if (!(1 <= p && p < m)) {

			System.err.println("Error: q should be between 1 and 4.");
			valid = false;
		}

		// Checks that we have a grid big enough.
		if (!(10 < m)) {

			System.err.println("Error: Dude, that grid size is really small.");
			valid = false;
		}

		// Checks that the number of threads is below the size of the problem
		if (!(0 < t)) {

			System.err.println("Error: Please specify a valid delay.");
			valid = false;
		}

		return valid;
	}
}
