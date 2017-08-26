package project1;

import org.newdawn.slick.Input;

import static project1.App.TILE_SIZE;

public class Player {

  /**
   * Commands for controlling the player.
   * W is up
   * S is down
   * A is left
   * D is right
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
   * Handle all player inputs.
   * Checks up, down, left, and right inputs.
   * */
  public void update(Input input) {
    this.handleInput(UP, input);
    this.handleInput(DOWN, input);
    this.handleInput(LEFT, input);
    this.handleInput(RIGHT, input);
  }

  private void handleInput(char direction, Input input) {
    // Exit the method early if the specified key has not been pressed.
    if (!input.isKeyPressed(direction)) {
      return;
    }

    float currX = this.playerSprite.getxCoordinate();
    float currY = this.playerSprite.getyCoordinate();

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

    // If the next move is invalid, exit the method
    if (Loader.isBlocked(currX, currY)) {
      return;
    }

    // If all checks are passed, update the player's location
    this.playerSprite.setxCoordinate(currX);
    this.playerSprite.setyCoordinate(currY);
  }
}
