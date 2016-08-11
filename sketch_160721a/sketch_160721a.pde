//******** Global Variables **********//
int canvasWidth;
int canvasHeight;
float originX = 50;
float originY = 50;
float originX2 = 100;
float originY2 = 100;
float destinationX = 250;
float destinationY = 50;
float totalTime = 200;
float proportionalPosition = 0;

void settings(){
  canvasWidth = 1920;
  canvasHeight = 1080;
  size(canvasWidth, canvasHeight);
  smooth(8);
  pixelDensity(displayDensity());
}

void setup() {
  colorMode(HSB, 360, 100, 100, 100);
  background(0);
}

void draw() {
  proportionalPosition = (frameCount % totalTime) / totalTime;
  noStroke();
  fill(0, 0, 0, 2);
  rect(0, 0, width, height);
  float distanceX, distanceY, currentPosX, currentPosY;
  distanceX = destinationX - originX;
  distanceY = destinationY - originY;
  currentPosX = originX + (distanceX * proportionalPosition);
  currentPosY = originY + (distanceY * proportionalPosition);
  float fillColor = map(proportionalPosition, 0, 0.5, 0, 100);
  fill(19, fillColor, 100, 100);
  noStroke();
  ellipse(currentPosX, currentPosY, 1, 1);
  // Circles //////////////////////
  float currentX, currentY;
  currentX = originX2 + 13 * cos(radians(map(proportionalPosition, 0, 1, 0, 360)));
  currentY = originY2 + 13 * sin(radians(map(proportionalPosition, 0, 1, 0, 360)));
  fillColor = map(proportionalPosition, 0, 1, 0, 100);
  fill(19, 100, 100, 1);
  noStroke();
  ellipse(originX2, originY2, 20, 20);
  fill(19, fillColor, 100, 100);
  ellipse(currentX, currentY, 1.5, 1.5);
  saveFrame("outputImages/Legend_####.png");
}