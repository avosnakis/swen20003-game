/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.Sys;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class World {
  private static final int NO_INDEX = -1;

  private ArrayList<Sprite> sprites;
  private int[][][] spriteIndices;

  public World() {
    this.sprites = Loader.loadSprites("res/levels/0.lvl");

    this.spriteIndices = new int[Loader.getWorldWidth()][Loader.getWorldHeight()][5];
    for (int[][] plane : this.spriteIndices) {
      for (int[] row : plane) {
        Arrays.fill(row, NO_INDEX);
      }
    }

    for (int i = 0; i < this.sprites.size(); i++) {
      this.insertIndex(i, this.sprites.get(i).getxCell(), this.sprites.get(i).getyCell());
    }
  }

  public void update(Input input, int delta) {
    for (Sprite sprite : this.sprites) {
      if (sprite != null) {
        sprite.update(input, delta, this);
      }
    }
  }

  public void render(Graphics g) {
    for (Sprite sprite : this.sprites) {
      if (sprite != null) {
        sprite.render(g);
      }
    }
  }

  public boolean isBlocked(int x, int y, int dir) {
    System.out.println(x);
    System.out.println(y);
    boolean cannotMove = true;
    for (int i = 0; i < this.spriteIndices[x][y].length && this.spriteIndices[x][y][i] != NO_INDEX; i++) {
      int index = this.spriteIndices[x][y][i];
      switch (this.sprites.get(index).getSpriteCategory()) {
        case "character":
          break;
        case "tile":
          cannotMove = !this.sprites.get(index).isPassable();
          if (cannotMove) {
            return true;
          }
          break;
        case "block":
          this.sprites.get(index).moveToDestination(dir, this);
          break;
      }
    }
    return false;
  }

  private void insertIndex(int index, int x, int y) {
    int i = 0;
    while (this.spriteIndices[x][y][i] != NO_INDEX) {
      i++;
    }
    this.spriteIndices[x][y][i] = index;
  }

  public void moveIndex(int fromX, int fromY, int toX, int toY, String type) {
    int i = 0;
    while (!this.sprites.get(this.spriteIndices[fromX][fromY][i]).getSpriteCategory().equals(type)) {
      i++;
    }
    int temp = this.spriteIndices[fromX][fromY][i];
    this.spriteIndices[fromX][fromY][i] = NO_INDEX;
    this.shiftDown(fromX, fromY);

    i = 0;
    while (this.spriteIndices[toX][toY][i] != NO_INDEX) {
      i++;
    }
    this.spriteIndices[toX][toY][i] = temp;
  }

  private void shiftDown(int x, int y) {
    for (int i = 0; i < this.spriteIndices[x][y].length - 1; i++) {
      if (this.spriteIndices[x][y][i] == NO_INDEX) {
        this.spriteIndices[x][y][i] = this.spriteIndices[x][y][i + 1];
        this.spriteIndices[x][y][i + 1] = NO_INDEX;
      }
    }
  }
}
