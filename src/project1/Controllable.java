package project1;

import org.newdawn.slick.Input;

import java.util.ArrayList;

public interface Controllable {
  public void handlePlayerInput(Input input);

  public void handlePlayerInput(ArrayList<Integer> keysPressed, World world);

}
