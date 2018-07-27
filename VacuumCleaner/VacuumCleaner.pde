static final int WIDTH = 30;
static final int HEIGHT = 30;
static final int CELLSIZE = 20;
static float[][] room;
static Robot r;

enum Move {
  mLEFTUP (-1, -1),
  mUP (0, -1),
  mRIGHTUP (1, -1),
  mLEFT (-1, 0),
  mQUIET (0, 0),
  mRIGHT (1, 0),
  mLEFTDOWN (-1, 1),
  mDOWN (-1, 0),
  mRIGHTDOWN (-1, 1);
  
  final int dx;
  final int dy;
  
  Move (int dx, int dy) {
    this.dx = dx;
    this.dy = dy;
  }
};
Move moves[] = {Move.mLEFTUP, Move.mUP, Move.mRIGHTUP, Move.mLEFT, Move.mQUIET, Move.mRIGHT, Move.mLEFTDOWN, Move.mDOWN, Move.mRIGHTDOWN};

class DecisionTree {
  DecisionTree lt, rt;
  Move lookingAt, action;
  float threeshold;
  
  DecisionTree (Move action) {
    this.action = action;
  }
  
  DecisionTree (float threeshold, Move lookingAt, DecisionTree l, DecisionTree r) {
    this.threeshold = threeshold;
    this.lookingAt = lookingAt;
    this.lt = l;
    this.rt = r;
  }
  
  Move getAction(float[][] room, int xpos, int ypos) {
    if (this.action != null) {
      return this.action;
    } else {
      int x = xpos + this.lookingAt.dx;
      int y = ypos + this.lookingAt.dy;
      float v = ((x < 0 || x >= WIDTH) || (y < 0 || y >= HEIGHT)) ? 10.0 : room[x][y];
      if (v < this.threeshold) {
        return lt.getAction(room, xpos, ypos);
      } else {
        return rt.getAction(room, xpos, ypos);
      }
    }
  }
  
  String toString() {
    return this.toString(0);
  }
  
  String toString(int lvl) {
    String tabs = "";
    for (int i = 0; i < lvl; i++) tabs += "\t";
    if (this.action != null) {
      return tabs + this.action;
    } else {
      return tabs + this.threeshold + " (look at " + this.lookingAt + ")\n" + 
              lt.toString(lvl+1) + "\n" + rt.toString(lvl+1);
    }
  }
}

DecisionTree makeRandomTree() {
  return makeRandomTree(1);
}

DecisionTree makeRandomTree(int lvl) {
  if (random(1) >= 1/(pow(2, lvl))) {
    // this branch ends here
    return new DecisionTree(moves[(int)random(9)]);
  } else {
    DecisionTree lt = makeRandomTree(lvl+1);
    DecisionTree rt = makeRandomTree(lvl+1);
    float threeshold = random(1.05);
    Move m = moves[(int)random(9)];
    return new DecisionTree(threeshold, m, lt, rt);
  }
}

class Robot {
  DecisionTree t;
  float score;
  int xpos, ypos;
  int counter;
  
  Robot (DecisionTree t) {
    this.t = t;
    this.score = 0;
    this.xpos = WIDTH/2;
    this.ypos = HEIGHT/2;
    this.counter = 0;
  }
  
  void move(float[][] room) {
    Move m = t.getAction(room, this.xpos, this.ypos);
    if (this.xpos + m.dx < 0 || this.xpos + m.dx >= WIDTH || m.dx == 0) {
      if (this.ypos + m.dy < 0 || this.ypos + m.dy >= HEIGHT || m.dy == 0) {
        this.counter++;
        return;
      }
    }
    this.xpos += m.dx;
    this.ypos += m.dy;
    this.counter = 0;
  }
}

void create_room(float[][] room) {
  for (int x = 0; x < WIDTH; x++) {
    for (int y = 0; y < HEIGHT; y++) {
      room[x][y] = random(1);
    }
  }
}

void draw_room(float[][] room) {
  int gray = 0;
  for (int x = 0; x < WIDTH; x++) {
    for (int y = 0; y < HEIGHT; y++) {
      gray = (int)(room[x][y]*255);
      fill(gray);
      rect(x*CELLSIZE, y*CELLSIZE, CELLSIZE, CELLSIZE);
    }
  }
}

void degrade_room(float[][] room) {
  for (int x = 0; x < WIDTH; x++) {
    for (int y = 0; y < HEIGHT; y++) {
      room[x][y] -= 0.001;
      room[x][y] = max(room[x][y], 0);
    }
  }
}
      
void draw() {
  r.score += 1 - room[r.xpos][r.ypos];
  room[r.xpos][r.ypos] = 1;
  degrade_room(room);
  draw_room(room);
  r.move(room);
  if (r.counter >= 10) {
    noLoop();
    println(r.score);
  }
  fill(color(255, 0, 0));
  rect(r.xpos*CELLSIZE, r.ypos*CELLSIZE, CELLSIZE, CELLSIZE);
}

void setup() {
  surface.setSize(WIDTH*CELLSIZE, HEIGHT*CELLSIZE);
  room = new float[WIDTH][HEIGHT];
  create_room(room);
  r = new Robot(makeRandomTree());
}