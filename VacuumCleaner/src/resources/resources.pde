/** Use this file to play back the best robot of each generation
    from a simulation created by simulators/BasicSimulator **/

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import resources.Robot;
import resources.Room;

final boolean SAVE_FRAMES = true;

final int WIDTH = 20;
final int HEIGHT = 20;
final Room room = new Room(WIDTH, HEIGHT);
final int CELLSIZE = 20;
final int NSTEPS = 400;
final String importGenerationFromFile = "H:/AIML/VacuumCleaner/saves/2018_08_19_17_35.gen";

ArrayList<Robot> robots;
int idx;
Robot currentRobot;
int stepsLeft;
int oldX, oldY;

void setup() {
  try {
    FileInputStream fis = new FileInputStream(importGenerationFromFile);
    ObjectInputStream ois = new ObjectInputStream(fis);
    robots = (ArrayList<Robot>) ois.readObject();
    ois.close();
  } catch (Exception e) {
    noLoop();
    e.printStackTrace();
    return;
  }
  
  idx = -1;
  stepsLeft = -1;
  surface.setSize(CELLSIZE*WIDTH, CELLSIZE*HEIGHT);
  draw_room(room);
  
  noLoop();
}

void draw() {
  if (stepsLeft <= 0) {
    idx += robots.size();
    if (idx >= robots.size()) {
      noLoop();
      return;
    }
    stepsLeft = NSTEPS;
    currentRobot = robots.get(idx);
    currentRobot.setRoom(room);
    draw_room(room);
    textSize(32);
    fill(0, 0, 250);
    text(idx+1, 20, 42);
    oldX = -1; oldY = -1;
  }
  
  // update the position of the robot and the frame
  --stepsLeft;
  fill(255, 255, 255);
  rect(currentRobot.getXpos()*CELLSIZE, currentRobot.getYpos()*CELLSIZE,
        CELLSIZE, CELLSIZE);
  // move the robot
  currentRobot.move();
  // clean the dirt
  currentRobot.clean();
  // draw the robot
  fill(255, 0, 0);
  rect(currentRobot.getXpos()*CELLSIZE, currentRobot.getYpos()*CELLSIZE,
        CELLSIZE, CELLSIZE);
  // see if the robot moved
  if (oldX == currentRobot.getXpos() && oldY == currentRobot.getYpos()) {
    stepsLeft = -1;
    print("robot got stuck in gen "+idx +"\n");
    //delay(200);
  } else {
    oldX = currentRobot.getXpos();
    oldY = currentRobot.getYpos();
  }
  if (SAVE_FRAMES && idx+1 == robots.size()) {
    saveFrame("frames/frame#####.png");
  } else {
    delay(50);
  }
}

void draw_room(Room r) {
  for (int i = 0; i < WIDTH; ++i) {
      for (int j = 0; j < HEIGHT; ++j) {
        int c = (int)(255*(1-r.get(i, j)));
        fill(c);
        rect(i*CELLSIZE, j*CELLSIZE, CELLSIZE, CELLSIZE);
      }
    }
}

void keyPressed() {
  loop();
}