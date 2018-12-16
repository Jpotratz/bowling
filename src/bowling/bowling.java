//Class written by James Potratz. Contains functions for a bowling scoreboard including calculation of the score and printing to console.
//Written for CSIS 222 at Metropolitan Community College for Homework02 - Mid Term Project

package bowling;

import java.util.Scanner;

public class bowling {

	// GLOBAL VARIABLES
	public static int currentFrame = 1, result, runningTotal, currentRoll = 1, previousRoll;
	public static String[][] scores = new String[15][4];
	public static int[] frameScore = new int[11];
	public static boolean isGameOver = false;
	// Create Scanner
	public static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.print("Welcome to Bowling! Please enter your scores. Valid inputs accepted are [0-10] \n");
		System.out.print("Press enter after typing a number.\n");
		getScores();
		printSheet();
		System.out.println("Thanks for playing!");
	}

	// Print the score sheet
	public static void printSheet() {
		calcFrameScore();
		System.out.println("Printing Scoreboard");
		// Border
		System.out.println("-------------------------------------------------------------");
		// Print Columns
		for (int count = 1; count <= 10; count++) {
			System.out.printf("__%d___", count);
		}
		// Print Scores.
		System.out.println("");
		for (int a = 1; a <= 9; a++) {
			for (int b = 1; b <= 2; b++) {
				if (scores[a][b] == null) {
					System.out.print(" " + " " + "|");
				}
				// Print - instead of 0
				else if (scores[a][b].contains("0")) {
					System.out.print(" " + "-" + "|");
				} // Print nothing in roll 1 if strike
				else {
					System.out.print(" " + scores[a][b] + "|");
				}
			}
		}
		// Print for Frame 10
		System.out.print(" " + scores[10][1] + "|" + scores[10][2] + "|" + scores[10][3] + "|");
		System.out.println("");
		calcRunningTotal();
	}

	// Calculate the running total
	public static void calcRunningTotal() {
		System.out.println("-------------------------------------------------------------");
		System.out.println("Printing Running Totals");
		for (int a = 1; a <= 10; a++) {
			runningTotal += frameScore[a];
			System.out.print(" " + runningTotal + "|");
		}
		System.out.println("");
	}

	// Calculate the score for each frame
	public static void calcFrameScore() {
		int roll = 1;
		for (int frame = 1; frame <= 10; frame++) {
			// If Frame 10, do special logic
			try {
				if (frame == 10) {
					// If strike on first roll of frame 10
					if (scores[frame][roll].contains("X")) {
						// Check if roll 2 and 3 is a strike
						if (scores[frame][roll + 1].charAt(0) == 'X' && scores[frame][roll + 2].charAt(0) == 'X') {
							frameScore[frame] = 30;
						} // if strike on roll 2 and not on roll 3
						else if (scores[frame][roll + 1].contains("X")) {
							frameScore[frame] = 20 + Integer.parseInt(scores[frame][roll + 2]);
						} // If spare on roll 3
						else if (scores[frame][roll + 2].charAt(0) == '/') {
							frameScore[frame] = 20;
						}
					} // If not strike on first roll of frame 10
					else if (!scores[frame][roll].contains("X")) {
						// check if roll 2 is a spare
						if (scores[frame][roll + 1].contains("/")) {
							// check if roll 3 is a strike. If roll 2 is spare && roll 3 is strike.
							// frameScore = 20
							if (scores[frame][roll + 2].charAt(0) == 'X') {
								frameScore[frame] = 20;
							}
							// if roll 2 is spare, and roll 3 is not strike, framescore = 10 + roll3
							else {
								frameScore[frame] = 10 + Integer.parseInt(scores[frame][roll + 2]);
							}
						}
					} // Regular numbers, no strikes or spares
					else {
						frameScore[frame] = Integer.parseInt(scores[frame][roll])
								+ Integer.parseInt(scores[frame][roll + 1]) + Integer.parseInt(scores[frame][roll + 3]);
					}
				}
			} catch (Exception e) {
				System.out.println("Stuff broke on frame 10");
			}

			// If you have a strike
			if (scores[frame][roll + 1].contains("X")) {

				// Special condition for Frame 9, since Frame 10 can have strikes in roll 1
				if (frame == 9) {
					try {
						if (scores[frame + 1][roll].contains("X")) {
							if (scores[frame + 1][roll + 1].contains("X")) {
								frameScore[frame] = 30;
							} else {
								frameScore[frame] = 20 + Integer.parseInt(scores[frame + 1][roll + 1]);
							}
						}
					} catch (NullPointerException e) {
						System.out.println(
								"ERROR: This occurs if you attempt to check a value where Null is placed in scores array.");
					}
				}
				// If next roll is not strike
				else if (scores[frame + 1][roll] != null) {
					try { // Check if the second roll of the next frame is a spare, if so frameScore for
							// current frame is 20.
						if (scores[frame + 1][roll + 1].charAt(0) == '/') {
							frameScore[frame] = 20;
						} else if (scores[frame + 1][roll + 1].charAt(0) == 'X'
								&& scores[frame + 1][roll].charAt(0) != 'X') {
							frameScore[frame] = 20 + Integer.parseInt(scores[frame + 1][roll]);
						}

						// If not spare, write next two values + 10 into frameScore
						else {
							frameScore[frame] = 10 + Integer.parseInt(scores[frame + 1][roll])
									+ Integer.parseInt(scores[frame + 1][roll + 1]);
						}
					} catch (Exception e) {
						System.out.println("Program failed to check if next frame is a spare.");
					}
				} // If next roll is a strike
				else if (scores[frame + 1][roll] == null) {
					try { // Check if the third frame is a strike. If true, then frameScore is 30 for
							// three strikes in a row
						if (scores[frame + 2][roll] == null || scores[frame + 2][roll].charAt(0) == 'X') {
							frameScore[frame] = 30;
						}
						// If third frame is not a strike, then first roll of third frame + 20
						else {
							frameScore[frame] = 20 + Integer.parseInt(scores[frame + 2][roll]);
						}
					} catch (Exception e) {
						System.out.println("Program failed to check if third frame is a strike.");
					}
				}
				// Write next two values to frameScore + 10 for the strike
				else {
					frameScore[frame] = 10
							+ Integer.parseInt(scores[frame + 1][roll] + Integer.parseInt(scores[frame + 1][roll + 1]));
				}
				// If you have a spare
			} else if (scores[frame][roll + 1].charAt(0) == '/' && frame != 10) {
				try { // Check if the next frame is a strike
					if (scores[frame + 1][roll] == null) {
						frameScore[frame] = 20;
					}
					// Else frameScore is 10+ next roll
					else {
						frameScore[frame] = 10 + Integer.parseInt(scores[frame + 1][roll]);
					}
				} catch (Exception e) {
					System.out.println("Program failed to check for strike after spare");
				}

			}
			// If you have a regular number
			else if (frame != 10) {
				frameScore[frame] = Integer.parseInt(scores[frame][roll]) + Integer.parseInt(scores[frame][roll + 1]);
			}
		}
	}

	// Validate the inputs provided by the user and write to array. Follow if
	// statements carefully.
	public static boolean validateInput(int roll, String pins) {
		try {
			if (Integer.parseInt(pins) == 0 || Integer.parseInt(pins) == 1 || Integer.parseInt(pins) == 2
					|| Integer.parseInt(pins) == 3 || Integer.parseInt(pins) == 4 || Integer.parseInt(pins) == 5
					|| Integer.parseInt(pins) == 6 || Integer.parseInt(pins) == 7 || Integer.parseInt(pins) == 8
					|| Integer.parseInt(pins) == 9 || Integer.parseInt(pins) == 10) {
				switch (roll) {
				// Logic for roll 1. A user cannot have a spare, convert to strike. 10th Frame
				// logic will convert to strike, and proceed increment roll.
				case 1:
					// If strike and not Frame 10 increment frame
					if (Integer.parseInt(pins) == 10 && currentFrame != 10) {
						scores[currentFrame][currentRoll + 1] = "X";
						currentFrame++;
					} // If strike and frame 10, increment roll.
					else if (Integer.parseInt(pins) == 10 && currentFrame == 10) {
						scores[currentFrame][currentRoll] = "X";
						currentRoll++;
					} // If normal number, write it, increment roll
					else {
						scores[currentFrame][currentRoll] = pins;
						currentRoll++;
					}
					previousRoll = Integer.parseInt(pins);

					break;
				// Logic for roll 2
				case 2:
					// Frame 10, if roll 1 was strike then strike. Increment roll
					if (Integer.parseInt(pins) == 10 && currentFrame == 10 && previousRoll == 10) {
						scores[currentFrame][currentRoll] = "X";
						currentRoll++;
					} // Frame 10, if roll 1 wasn't strike then spare. Increment roll
					else if (Integer.parseInt(pins) == 10 && currentFrame == 10 && previousRoll != 10) {
						scores[currentFrame][currentRoll] = "/";
						currentRoll++;
					} // Convert total > 9 to spare on Roll 10 and increment roll
					else if (currentFrame == 10 && (previousRoll + Integer.parseInt(pins) > 9)
							&& (previousRoll != 10)) {
						scores[currentFrame][currentRoll] = "/";
						currentRoll++;
					} // On frame 10, increment roll
					else if (currentFrame == 10) {
						scores[currentFrame][currentRoll] = pins;
						currentRoll++;
					} // A user cannot have a strike on roll 2
					else if (Integer.parseInt(pins) == 10) {
						scores[currentFrame][currentRoll] = "/";
						currentFrame++;
						currentRoll = 1;
					} // total cannot be greater than 9, becomes a spare.
					else if (previousRoll + Integer.parseInt(pins) > 9) {
						scores[currentFrame][currentRoll] = "/";
						currentFrame++;
						currentRoll = 1;
					} // If regular number, increment frame, advance roll
					else {
						scores[currentFrame][currentRoll] = pins;
						currentFrame++;
						currentRoll = 1;
					}
					previousRoll = Integer.parseInt(pins);
					break;
				// Logic for roll 3. Only occurs on Frame 10.
				case 3:
					if (Integer.parseInt(pins) == 10) {
						scores[currentFrame][currentRoll] = "X";
					}
					// Spare logic for roll 3
					else if ((previousRoll != 10) && (previousRoll + Integer.parseInt(pins) > 9)
							&& (scores[currentFrame][currentRoll - 1].charAt(0) != '/')) {
						scores[currentFrame][currentRoll] = "/";
						currentRoll++;
					} else {
						scores[currentFrame][currentRoll] = pins;
					}
					isGameOver = true;
					break;
				}
				return true;
			} else {
				System.out.print("Please enter a valid input. [0-10] \n");
				return false;
			}
		}

		catch (IllegalArgumentException e) {
			System.out.println("Please enter a number. [0-10]");
			return false;
		}
	}

	// Helper method, get inputs from player
	public static void getScores() {
		while (!isGameOver) {
			if (currentFrame == 11) {
				isGameOver = true;
				break;
			}
			System.out.println("User score on Frame: " + currentFrame + " Roll: " + currentRoll);
			if (!validateInput(currentRoll, input.next())) {
				continue;
			}
		}
		System.out.println("Scoring over. \n");
	}
}
