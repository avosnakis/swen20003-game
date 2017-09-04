package project1;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import project1.tiles.Tile;


/**
 * Class for controlling the player character.
 * Handles movement of the player sprite, and game controls.
 */
public class Player {

  /**
   * Commands for controlling the player.
   * W is up.
   * S is down.
   * A is left.
   * D is right.
   */
  public static final char UP = Input.KEY_W;
  public static final char DOWN = Input.KEY_S;
  public static final char LEFT = Input.KEY_A;
  public static final char RIGHT = Input.KEY_D;

  private Sprite playerSprite;

  private int playerX;
  private int playerY;

  public Player(Sprite playerSprite, int playerX, int playerY) {
    this.playerSprite = playerSprite;

    this.playerX = playerX;
    this.playerY = playerY;
  }

  @Override
  public String toString() {
    return "Player{" +
        "playerSprite=" + this.playerSprite +
        ", playerX=" + this.playerX +
        ", playerY=" + this.playerY +
        '}';
  }

  /**
   * Handle all player inputs. Checks up, down, left, and right inputs.
   */
  public void update(Input input, Tile[][][] grid) {
    // Check each of the directions to see if the player will move
    this.handlePlayerInput(UP, input, grid);
    this.handlePlayerInput(DOWN, input, grid);
    this.handlePlayerInput(LEFT, input, grid);
    this.handlePlayerInput(RIGHT, input, grid);
  }

  public void render(Graphics g) {
    this.playerSprite.render(g);
  }

  /**
   * Checks if a specified key was pressed, and then updates, or does not update, the player's position based on the
   * game state and the key pressed.
   *
   * @param direction The key to check if had been pressed, and if so, to attempt to move the player based on that key.
   * @param input     The input object, which contains information on whether the specified key had been pressed.
   * @param grid      The grid of all tiles in the current world.
   */
  private void handlePlayerInput(char direction, Input input, Tile[][][] grid) {
    // Exit the method early if the specified key has not been pressed.
    if (!input.isKeyPressed(direction)) {
      return;
    }

    int currentPlayerX = this.playerX;
    int currentPlayerY = this.playerY;

    // Adjust locations based on the direction the player is attempting to move in
    switch (direction) {
      case UP:
        currentPlayerY--;
        break;
      case DOWN:
        currentPlayerY++;
        break;
      case LEFT:
        currentPlayerX--;
        break;
      case RIGHT:
        currentPlayerX++;
        break;
      default:
        System.exit(1);
        break;
    }

    // If the next set of coordinates is invalid, exit the method
    for (int i = 0; i < grid[currentPlayerX][currentPlayerY].length; i++) {
      if (grid[currentPlayerX][currentPlayerY][i] != null && !grid[currentPlayerX][currentPlayerY][i].isPassable()) {
        return;
      }
    }

    // Update the player sprite location once both checks are passed
    this.playerSprite.incrementByOneTile(direction);

    // Update the player's location
    this.playerX = currentPlayerX;
    this.playerY = currentPlayerY;
  }
}
