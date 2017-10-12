package project2;

import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public class Tnt extends Block implements Destructible {

  private Notifier<CrackedWall> tntNotifier;
  private boolean unDetonated;
  private Explosion explosion;

  public Tnt(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/tnt.png", "tnt", cellPosition, windowPosition);

    explosion = new Explosion(cellPosition, windowPosition);
    unDetonated = false;

    tntNotifier = null;
  }

  @Override
  public void update(ArrayList<Integer> keysPressed, int delta, World world) {
    // On the first frame, find the unique cracked wall and make it an observer
    if (tntNotifier == null) {
      tntNotifier = new Notifier<>((CrackedWall) world.findUniqueSprite("cracked"));
    }

    if (unDetonated && !explosion.finishedExploding()) {
      explosion.increment(delta);
    } else if (explosion.finishedExploding()) {
      tntNotifier.alert();
    }
  }

  @Override
  public void render(Graphics g) {
    if (!unDetonated) {
      super.render(g);
    } else if (!explosion.finishedExploding()) {
      explosion.render(g);
    }
  }

  @Override
  public void moveToDestination(Direction direction, World world) {
    if (unDetonated) {
      return;
    }

    float speed = App.TILE_SIZE;
    int cellSpeed = GameUtils.CELL_SIZE;
    // Translate the direction to an x and y displacement
    float deltaX = GameUtils.directionDelta('x', direction, speed);
    float deltaY = GameUtils.directionDelta('y', direction, speed);
    int deltaXCell = GameUtils.directionDelta('x', direction, cellSpeed);
    int deltaYCell = GameUtils.directionDelta('y', direction, cellSpeed);


    Position<Integer> explosionPosition = new Position<>(getxCell() + deltaXCell, getyCell() + deltaYCell);

    // Destroy the cracked wall and the TNT if there is a cracked wall at the next location
    if (world.typeAtLocation(explosionPosition, "cracked")) {
      explode(explosionPosition, deltaX, deltaY);
      return;
    }

    addPastPosition(world.getTimer());
    world.setChangedThisFrame(true);

    // Make sure the position isn't occupied!
    if (!world.isBlocked(explosionPosition, direction)) {
      setCellPosition(explosionPosition);
      snapToGrid(getX() + deltaX, getY() + deltaY);
    }
  }

  /**
   * Starts the unDetonated animation, and destroys the CrackedWall at the target location.
   *
   * @param wallLocation The location of the CrackedWall and the explosion.
   * @param deltaX       The x position in the window of the explosion.
   * @param deltaY       The y position in the window of the explosion.
   */
  private void explode(Position<Integer> wallLocation, float deltaX, float deltaY) {
    setCellPosition(wallLocation);
    snapToGrid(getX() + deltaX, getY() + deltaY);

    unDetonated = true;
    explosion.start(wallLocation);
  }

  public boolean isUnDetonated() {
    return unDetonated;
  }
}
