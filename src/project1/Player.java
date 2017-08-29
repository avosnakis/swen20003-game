package project1;

import org.newdawn.slick.Input;

import static project1.App.TILE_SIZE;

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
  private float playerX;
  private float playerY;

  public Player(Sprite playerSprite) {
    this.playerSprite = playerSprite;

    // Set the player's x and y coordinates to the center of its sprite. This is for collision checking later.
    this.playerX = this.playerSprite.getxCoordinate() + (TILE_SIZE / 2);
    this.playerY = this.playerSprite.getyCoordinate() + (TILE_SIZE / 2);
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
  public void update(Input input, Sprite[] levelSprites) {
    // Check each of the directions to see if the player will move
    this.handlePlayerInput(UP, input, levelSprites);
    this.handlePlayerInput(DOWN, input, levelSprites);
    this.handlePlayerInput(LEFT, input, levelSprites);
    this.handlePlayerInput(RIGHT, input, levelSprites);
  }

  /**
   * Checks if a specified key was pressed, and then updates, or does not update, the player's position based on the
   * game state and the key pressed.
   *
   * @param direction The key to check if had been pressed, and if so, to attempt to move the player based on that key.
   * @param input     The input object, which contains information on whether the specified key had been pressed.
   */
  private void handlePlayerInput(char direction, Input input, Sprite[] levelSprites) {
    // Exit the method early if the specified key has not been pressed.
    if (!input.isKeyPressed(direction)) {
      return;
    }

    float currentPlayerX = this.playerX;
    float currentPlayerY = this.playerY;

    // Adjust locations based on the direction the player is attempting to move in
    switch (direction) {
      case UP:
        currentPlayerY -= TILE_SIZE;
        break;
      case DOWN:
        currentPlayerY += TILE_SIZE;
        break;
      case LEFT:
        currentPlayerX -= TILE_SIZE;
        break;
      case RIGHT:
        currentPlayerX += TILE_SIZE;
        break;
      default:
        System.exit(1);
        break;
    }

    // If the next set of coordinates is invalid, exit the method
    if (Loader.isBlocked(currentPlayerX, currentPlayerY, levelSprites)) {
      return;
    }

    // Update the player sprite location once both checks are passed
    this.playerSprite.incrementByOneTile(direction);

    // Update the player's location
    this.playerX = currentPlayerX;
    this.playerY = currentPlayerY;
  }
}
