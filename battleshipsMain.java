package battleships;

// packages
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class battleshipsMain extends Application {
// declaring variables used for the program
	@FXML
	private GridPane player;
	@FXML
	private GridPane cpu;
	public Boolean play = false;
	private final int boardSize = 10;
	private Rectangle[][] playerRects = new Rectangle[boardSize][boardSize];
	private Rectangle[][] cpuRects = new Rectangle[boardSize][boardSize];
	private int shipLength = 5;
	private int cpuhit = 0;
	private int playerhit = 0;
	private ArrayList<ship> aiShips = new ArrayList<>();
	ArrayList<Coord> cpu_picks = new ArrayList<Coord>();
	ArrayList<Coord> cpu_ship_hits = new ArrayList<Coord>();

	// main method> launches the program
	public static void main(String[] args) {
		launch(args);

	}

// stage method, this loads up the FMXL file and opens the interface
	@Override
	public void start(Stage s) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("battleship.fxml"));
		Scene scene = new Scene(root);
		s.setScene(scene);
		s.show();
	}

	// uses the Ship.java file and generates an arraylist of coordinates
	// representing each ship on the board
	private void createAIShips() {
		ArrayList<Coord> allSquares = new ArrayList<>();
		for (int i = 2; i <= 5; i++) {
			aiShips.add(new ship(allSquares, i, boardSize)); // adds an object of ship to the aiShips array
		}

		/*
		 * for (ship s : aiShips) { System.out.print("AI ship");
		 * System.out.println(s.squares.toString()); // debug to show AI ship locations
		 * 
		 * }
		 */
	}

// whether the tile is hittable or not. white and green are uncovered, so cpu shouldn't
// try to press them twice.

	boolean is_hittable(Rectangle c) {
		return c.getFill().equals(Color.WHITE) || c.getFill().equals(Color.BLUE); // sets the only "hit'abble"
																					// rectangles to be White and Blue
	}

	// creates a method called shoot
	public void shoot()

	{
		Coord pick = new Coord(-1, -1);

		// see if we have any ship hits (green squares)
		if (cpu_ship_hits.size() > 0) {
			// if there are hits, go through them and try neighbouring Coordinates
			for (Coord hit : cpu_ship_hits) {
				// always tries a certain combination. it should determine which direction the
				// ship is
				// aiming in based on the hits it has registered and only try that direction.
				if (hit.x > 0 && is_hittable(playerRects[hit.x - 1][hit.y])) {
					pick = new Coord(hit.x - 1, hit.y);
					break;
				} else if (hit.x < boardSize - 1 && is_hittable(playerRects[hit.x + 1][hit.y])) {
					pick = new Coord(hit.x + 1, hit.y);
					break;
				} else if (hit.y > 0 && is_hittable(playerRects[hit.x][hit.y - 1])) {
					pick = new Coord(hit.x, hit.y - 1);
					break;
				} else if (hit.y < boardSize - 1 && is_hittable(playerRects[hit.x][hit.y + 1])) {
					pick = new Coord(hit.x, hit.y + 1);
					break;
				}
			}
		}

		// didn't find one, so pull a random one
		if (pick.x == -1)

		{
			int pick_id = (int) (Math.random() * cpu_picks.size());
			pick = cpu_picks.get(pick_id);
			cpu_picks.remove(pick_id);
		}
		// if found one, so remove it by value
		else
			cpu_picks.remove(pick);

		// if the x and y coordinates have a fill of White
		if (playerRects[pick.x][pick.y].getFill().equals(Color.WHITE)) {
			// fill the square Grey
			playerRects[pick.x][pick.y].setFill(Color.GRAY);
		} // if the x and y coodinates have a fill of blue
		else if (playerRects[pick.x][pick.y].getFill().equals(Color.BLUE)) {
			// fills with green, adds the values to the "hit" array so they dont get hit
			// again and increments
			playerRects[pick.x][pick.y].setFill(Color.GREEN); // playerhit by 1
			cpu_ship_hits.add(pick);
			playerhit++;
		}

		// CPU win condition: cpu_ship_hits.size == total number of ship squares
		if (playerhit == 14) {
			cpu.setDisable(true);
			System.out.println("You Loose");
		}
	}

// initialised the program
	public void initialize() {
		createAIShips();

		// populate cpu picks
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				cpu_picks.add(new Coord(x, y));
			}
		}

		// "creates" and populates the Gridpane Cpu, populates it with 100 30/30 pixel
		// rectangles
		/* Computer Board */
		for (int y = 0; y < boardSize; y++) {
			for (int x = 0; x < boardSize; x++) {
				Rectangle r = new Rectangle();
				r.setWidth(30);
				r.setHeight(30);
				r.setStroke(Color.BLACK);
				r.setFill(Color.WHITE);
				cpuRects[x][y] = r;
				cpu.add(r, x, y);
				final int thisX = x;
				final int thisY = y;

				// begins an event handler and sets it to the rectangles in the grid
				r.setOnMouseClicked(e -> {

					if (play == false) {
						System.out.println("False"); // if the boolean play is false, the game cannot be played
						return;
					}

					else if (play == true) {

						// assume it's a miss on click
						r.setFill(Color.BLUE);
						r.setDisable(true); // this makes it so each rectangle can only be clicked on once, to prevent
											// the game bing beaten by spamming
						System.out.println("miss");// a single button
						shoot(); // runs the shoot methd from earlier allowing it to interact with the grid

						// see if it's a hit
						for (ship s : aiShips) {
							for (Coord c : s.squares) {
								if (c.x == thisX && c.y == thisY) // check to see if c.x and c.y are the same as the
																	// rectangle clicked

								{ // if the selected square is the coordinate of a ship, set it to red, disable
									// the square and increment
									r.setFill(Color.RED); // the cpuhit counter by 1
									r.setDisable(true);
									System.out.println("Hit");
									cpuhit++;

									if (cpuhit == 14) {
										System.out.println("you win"); // sets a score to win the game, in this case
																		// when all 14 coordinates
										cpu.setDisable(true); // stored in the array for ship have been found, the game
																// is won
									}

								}
							}

						}

					}
				});

			}

		}

		/* player Board */
// this creates and populates the board for the player in the same way as the computer board
		for (int y = 0; y < boardSize; y++) {
			for (int x = 0; x < boardSize; x++) {
				Rectangle r = new Rectangle();
				r.setWidth(30);
				r.setHeight(30);
				r.setStroke(Color.BLACK);
				r.setFill(Color.WHITE);
				playerRects[x][y] = r;
				player.add(r, x, y);

				final int thisX = x;
				final int thisY = y;

				r.setOnMouseClicked(e -> {

					/* Vertical */
// this section allowed the player to place thier ships vertical
					if (e.getButton() == MouseButton.SECONDARY) {

						// makes it so ships cant be placed outside/overlap the board
						if ((thisY + shipLength) > boardSize) {
							System.out.println("Cant Place a ship here!");
							return;
						}
						// makes it so ships cant be placed ontop of anything filled blue
						if (r.getFill().equals(Color.BLUE)) {
							System.out.println("Ship Already Occupies this Area");
							return;
						}
						// makes it so ships cant be placed ontop of populated squares
						for (int i = 0; i < shipLength; i++) {
							if (playerRects[thisX][thisY + i].getFill().equals(Color.BLUE)) {
								System.out.println("Ship already accupies area");
								return;
							}
						}
						// fills x y coords equal to the value of shiplength to be blue
						// this loop "places" the "ships"
						for (int i = 0; i < shipLength; i++) {
							playerRects[thisX][thisY + i].setFill(Color.BLUE);

						}

						if (shipLength <= 2) {
							shipLength = 0;
							player.setDisable(true);
						}
					}
					/* Horizontal */ // this method is the same as the previous, jsut places ships Horizontal
					if (e.getButton() == MouseButton.PRIMARY) { // (x axis) instead of Vertical

						if ((thisX + shipLength) > boardSize) {
							System.out.println("cant place a ship here");
							return;
						}

						if (r.getFill().equals(Color.BLUE)) {
							System.out.println("Ship Already Occupies this Area");
							return;
						}

						for (int i = 0; i < shipLength; i++) {
							if (playerRects[thisX + i][thisY].getFill().equals(Color.BLUE)) {
								System.out.println("Ship alredy accupies area");
								return;
							}
						}

						for (int i = 0; i < shipLength; i++) {
							playerRects[thisX + i][thisY].setFill(Color.BLUE);

						}

					}
					/* detect length enable game */

					shipLength--;
					if (shipLength < 2) { // if the shipLength is less than 2, sets it to 0 so no more ships can be
											// placed
						shipLength = 0;
						player.setDisable(true);
						play = true;
						System.out.println("Ready to Play");
						return;
					}
				});
			}
		}
	}
}
