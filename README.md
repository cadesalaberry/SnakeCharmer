`SnakeCharmer`
==============

The classic “snake” game takes place in a square grid, wrapping left/right and top/bottom. An `n`-length snake occupies a chain of `n` adjacent (N/S/E/W) cells and moves discretely, at a regular rate, with the head defining the direction of movement, and each subsequent body cell shifting to occupy the location of the previous cell as the snake moves.
Here is a simulation of `p` snakes, each of length `n`. Initially each snake is laid out in a unique row of the grid (and thus certainly non-overlapping with other snakes). Once all snakes are placed they begin concurrent movement, each snake controlled by a single thread. Each snake head chooses a random direction of movement, shifting into an empty grid cell, and then the body cells are updated one by one, from the head down to the tail. Once the entire body is moved, the snake waits for a fixed time `t`, and then moves again.
Note that snakes cannot move into occupied grid cells, and that snakes are always contiguous—it is never be possible for one snake to move through another or to overlap with another. The snake movement will thus appear atomic to other snakes. Snakes that cannot interfere, however, are allowed to move concurrently, and as much as possible I tried to avoid sequential bottlenecks.

The program is called Snakes, and takes four arguments:

```bash
$ java Snakes p n m t
```

Create `1 ≤ p < m` snakes of length `1 ≤ n < m` in an `m × m` grid, assuming `m > 10`. Each snake moves every `t`ms, for `t > 0`, and keeps track of how many successful movements it made. The simulation should end after approximately `1`min, or when no snake can move, at which point snakes should stop (if any can move) and you should print out how many total movements were made by each snake.


# Sample Output

```bash
$ java Snakes
Invalid args. Running default simulation.

. . . . . . . . . . . . . . . . . . . . 
. . . . . . . . . . . . . . . . . . . . 
o . . . . . . . . . . . . . . . . . o o 
o . . . . . . . . . . . . . . . . . o o 
o . . . . . . . . . . . . . . . . . o o 
o . . . . . . . . . . . . . . . . . . o 
o o o . . . . . . . . . . . . . . . . o 
2 o o . . . . . . . . . . . . . . . . o 
o o o o o . . . . . . . . . . . . . . o 
4 o o o o . . . . . . . . . . . . . . o 
o o o 0 o . . . . . . . . . . . . . . o 
. . o o o . . . . . . . . . . . . . . . 
. . . o o . . . . . . . . . . . . . . . 
. . . . . . . . . . . . o o o o o o . . 
. . . . . . . . . . o o o . o o 1 o . . 
. . . . . . . . . . o 3 o o o o o o . . 
. . . . . . . . . . . o o . . . . o o o 
o . . . . . . . . . . . . . . . . . . o 
. . . . . . . . . . . . . . . . . . . . 
. . . . . . . . . . . . . . . . . . . . 
#0 did 465 moves.
#1 did 52 moves.
#2 did 17 moves.
#3 did 256 moves.
#4 did 21 moves.
```

