package project2;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Interface for all classes that can change their position. Gives them movement and undo capability.
 * <p>
 * Undo is generally the same for all blocks and the player, with the only slight variation on Tnt and Ice.
 * Whenever a block or the player moves, the World records in a stack the time at which the world changed, and
 * that sprite saves its previous position in a HashMap, with the matching timestamp as its key.
 * When undo is pressed, the stack in the World is popped, and every Movable block is checked to see if it can undo.
 * If it can, then its HashMap<Integer, Position<Integer>> is is queried to see if it contains that timestamp. If it
 * does, then its current Position is overwritten with the one from the HashMap, and the one in the HashMap is removed.
 *
 * @author Alex Vosnakis 743936
 */
public interface Movable {
  public void moveToDestination(Direction direction, World world);

  public void undo(int time);

  public void addPastPosition(int time);
}
