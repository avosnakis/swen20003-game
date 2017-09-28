package project1;

import org.newdawn.slick.Input;

public interface Controllable {
  public void handlePlayerInput(Input input);

  public void handlePlayerInput(Input input, World word);

}
