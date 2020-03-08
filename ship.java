package battleships;

import java.util.ArrayList;
import java.util.Random;

public class ship {
	int length;
	ArrayList<Coord> squares = new ArrayList<>();

	/* Create a randomly-placed ship of length "length" */
	public ship(ArrayList<Coord> allSquares, int length, int boardSize) {
		Random random = new Random();
		double x = Math.floor(Math.random() * 2); // this is basically a coinflip and stores it in x

		// populate horizontal ships
		if (x == 0) { // if the coinflip value is 0 place Horizontal
			while (true) {
				Coord start = new Coord(random.nextInt(boardSize), random.nextInt(boardSize));
				if ((start.x + length) <= boardSize) { // for loop comparing the x value of start and it's lengths 
					// and checks if it is less than or equal to boardsize, then do everything
					/* See if this ship overlaps any existing one */
					boolean match = false;
					for (int i = 0; i < length; i++) {
						Coord potentialSquare = new Coord(start.x + i, start.y);
						// checks if the potential Square is equal to any value of the allSquares Array
						for (int j = 0; j < allSquares.size(); j++) {
							if (potentialSquare.equals(allSquares.get(j))) {
								match = true;
							}
						}
					}

					if (match == false) {
						/* if there is no match We can place this ship, so add it's coords to the array */
						for (int i = 0; i < length; i++) {
							squares.add(new Coord(start.x + i, start.y));
							allSquares.add(new Coord(start.x + i, start.y));
						}
						break;
					}
				}
			}
		}

		else {
			//  sames as verticle aslong as the coinflip doesnt land on 0
			while (true) {
				Coord start = new Coord(random.nextInt(boardSize), random.nextInt(boardSize));
				if ((start.y + length) <= boardSize) { // same as before but compares Y instead of X so it can place down

					/* See if this ship overlaps any existing one */
					boolean match = false;
					for (int i = 0; i < length; i++) {
						Coord potentialSquare = new Coord(start.x, start.y + i); // same as before, but uses Y instead of X so it can
																				// place down instead of accross
						for (int j = 0; j < allSquares.size(); j++) {
							if (potentialSquare.equals(allSquares.get(j))) {
								match = true;
							}
						}
					}
					if (match == false) {
						/* We can place this ship  and adds the values to the allsquares array*/
						for (int i = 0; i < length; i++) {
							squares.add(new Coord(start.x, start.y + i));
							allSquares.add(new Coord(start.x, start.y + i));

						}
						break;
					}
				}
			}
		}
	}
}
