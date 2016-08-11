import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sketch_160721a extends PApplet {

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

public void settings(){
  canvasWidth = 1920;
  canvasHeight = 1080;
  size(canvasWidth, canvasHeight);
  smooth(8);
  pixelDensity(displayDensity());
}

public void setup() {
  colorMode(HSB, 360, 100, 100, 100);
  background(0);
}

public void draw() {
  proportionalPosition = (frameCount % totalTime) / totalTime;
  noStroke();
  fill(0, 0, 0, 2);
  rect(0, 0, width, height);
  float distanceX, distanceY, currentPosX, currentPosY;
  distanceX = destinationX - originX;
  distanceY = destinationY - originY;
  currentPosX = originX + (distanceX * proportionalPosition);
  currentPosY = originY + (distanceY * proportionalPosition);
  float fillColor = map(proportionalPosition, 0, 0.5f, 0, 100);
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
  ellipse(currentX, currentY, 1.5f, 1.5f);
  saveFrame("outputImages/Legend_####.png");
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_160721a" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
