package project1;

import org.newdawn.slick.Input;

import static project1.App.TILE_SIZE;


/**
 * Class for controlling the player character.
 * Handles movement of the player sprite, and game controls.
 * */
public class Player {

  /**
   * Commands for controlling the player.
   * W is up.
   * S is down.
   * A is left.
   * D is right.
   * */
  private static final char UP = Input.KEY_W;
  private static final char DOWN = Input.KEY_S;
  private static final char LEFT = Input.KEY_A;
  private static final char RIGHT = Input.KEY_D;

  private Sprite playerSprite;

  public Player(Sprite playerSprite) {
    this.playerSprite = playerSprite;
  }

  /**
   * Handle all player inputs. Checks up, down, left, and right inputs.
   * */
  public void update(Input input) {
    this.handlePlayerInput(UP, input);
    this.handlePlayerInput(DOWN, input);
    this.handlePlayerInput(LEFT, input);
    this.handlePlayerInput(RIGHT, input);
  }

  /**
   * Checks if a specified key was pressed, and then updates, or does not update, the player's position based on the
   * game state and the key pressed.
   *
   * @param direction The key to check if had been pressed, and if so, to attempt to move the player based on that key.
   * @param input The input object, which contains information on whether the specified key had been pressed.
   * */
  private void handlePlayerInput(char direction, Input input) {
    // Exit the method early if the specified key has not been pressed.
    if (!input.isKeyPressed(direction)) {
      return;
    }

    float currX = this.playerSprite.getxCoordinate();
    float currY = this.playerSprite.getyCoordinate();

    // Adjust locations based on the direction the player is attempting to move in
    switch (direction) {
      case UP:
        currY -= TILE_SIZE;
        break;
      case DOWN:
        currY += TILE_SIZE;
        break;
      case LEFT:
        currX -= TILE_SIZE;
        break;
      case RIGHT:
        currX += TILE_SIZE;
        break;
      default:
        System.exit(1);
        break;
    }

    // If the next set of coordinates is invalid, exit the method
    if (Loader.isBlocked(currX, currY)) {
      return;
    }

    // Update the player's location once both checks are passed
    this.playerSprite.setxCoordinate(currX);
    this.playerSprite.setyCoordinate(currY);
  }
}
