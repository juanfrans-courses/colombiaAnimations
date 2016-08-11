import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sketch_160708a extends PApplet {

//******** Importing Libraries **********//


//******** Global Variables **********//
int canvasWidth;
int canvasHeight;
float minLon = -90.71367297f;
float maxLon = -57.210862008f;
float minLat = -5.209789943f;
float maxLat = 13.635541224f;
Table desplazadosTable;
Table internalDesplazadosTable;
Internal[] sameMunicipality;
Desplazados[] path;
float currentFrame;
// float totalAnimationFrames = 5400;
float totalAnimationFrames = 10800;
float totalDays = 11322;
float unixStart = 473385600;
float unixEnd = 1451606400;
float totalUnixTime;
int currentUnix;
PImage img;
boolean saveFrame1x = true;
boolean saveFrame2x = false;
int activeDesplazados = 0;
String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
String currentMonth;
PFont textFont;

public void settings(){
  // fullScreen();
  if (saveFrame1x){
    canvasWidth = 1920;
    canvasHeight = 1080;
  }
  else{
    canvasWidth = 1920 * 2;
    canvasHeight = 1080 * 2;
  }
  size(canvasWidth, canvasHeight);
  smooth(8);
  pixelDensity(displayDensity());
}

public void loadData() {
  // desplazadosTable = loadTable("RandomSample.csv", "header");
  // desplazadosTable = loadTable("RandomSample_2M.csv", "header");
  desplazadosTable = loadTable("Displaced_85_15_Clean.csv", "header");
  // internalDesplazadosTable = loadTable("RandomSampleSameMuni.csv", "header");
  internalDesplazadosTable = loadTable("AllSameMuni.csv", "header");
  int numberOfSameCity = internalDesplazadosTable.getRowCount();
  int numberOfPaths = desplazadosTable.getRowCount();
  sameMunicipality = new Internal[numberOfSameCity];
  path = new Desplazados[numberOfPaths];
  float startDate, distance, originX, originY, destinationX, destinationY, time, speed;
  int pathIndex = 0;
  for (TableRow row : desplazadosTable.rows()) {
    startDate = row.getFloat("f_ocurrencia");
    distance = row.getFloat("distance");
    originX = map(row.getFloat("org_x"), minLon, maxLon, 0, width);
    originY = map(row.getFloat("org_y"), minLat, maxLat, height, 0);
    destinationX = map(row.getFloat("dest_x"), minLon, maxLon, 0, width);
    destinationY = map(row.getFloat("dest_y"), minLat, maxLat, height, 0);
    time = row.getFloat("time");
    speed = row.getFloat("speed");
    path[pathIndex] = new Desplazados(startDate, distance, originX, originY, destinationX, destinationY, time, speed);
    pathIndex++;
  }
  int sameIndex = 0;
  for (TableRow row : internalDesplazadosTable.rows()) {
    startDate = row.getFloat("f_ocurrencia");
    originX = map(row.getFloat("org_x"), minLon, maxLon, 0, width);
    originY = map(row.getFloat("org_y"), minLat, maxLat, height, 0);
    sameMunicipality[sameIndex] = new Internal(startDate, originX, originY);
    sameIndex++;
  }
  println("Finished loading the data... " + str(numberOfPaths) + " records loaded...");
}

public void setup() {
  colorMode(HSB, 360, 100, 100, 100);
  background(0);
  loadData();
  totalUnixTime = unixEnd - unixStart;
  // img = loadImage("BaseImage.png");
  textFont = createFont("Anaheim-Regular.ttf", 12, true);
}

public void draw() {
  activeDesplazados = 0;
  // background(0, 0, 0, 1);
  background(0);
  noStroke();
  // fill(0, 0, 0, 5);
  // rect(0, 0, width, height);
  // tint(0, 0, 100, 3);
  // image(img, 0, 0, canvasWidth, canvasHeight);
  currentFrame = map(frameCount, 0, totalAnimationFrames, 0, totalDays);
  for (int i=0; i<sameMunicipality.length; i++) {
    sameMunicipality[i].countActive();
    activeDesplazados = activeDesplazados + sameMunicipality[i].active;
    //sameMunicipality[i].plotLoop();
  }
  for (int i=0; i<path.length; i++) {
    path[i].countActive();
    activeDesplazados = activeDesplazados + path[i].active;
    //path[i].plotMovement();
  }
  if (frameCount > totalAnimationFrames + 200){
    exit();
  }
  noStroke();
  // fill(0, 0, 0, 100);
  // rect(0, 0, 500, 100);
  // fill(0, 0, 100, 100);
  currentUnix = round(map(frameCount, 0, totalAnimationFrames, unixStart, unixEnd));
  String date = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(currentUnix*1000L));
  String currentDate[] = split(date, '/');
  currentMonth = months[(PApplet.parseInt(currentDate[1])-1)];
  textAlign(LEFT);
  textFont(textFont);
  textSize(50);
  text(currentMonth + ", " + currentDate[2], 50, 50);
  text(nfc(activeDesplazados), 50, 150);
  text(currentDate[0], 50, 250);
  // dateLabels.endDraw();
  // dateLabels.save("outputImages/Date_1x_" + frameCount + ".png");
  if (saveFrame1x){
    saveFrame("/Volumes/SIDL_Current/02_SIDL PROJECTS/1507_Victims_and_Displacement_Colombia/07_Animations/00_Output_Frames/03_DateFrames/Date_1x_#####.png");
  } else if (saveFrame2x){
    saveFrame("/Volumes/SIDL_Current/02_SIDL PROJECTS/1507_Victims_and_Displacement_Colombia/07_Animations/00_Output_Images_Temp/Test_2x_####.png");
  } else {
  }
}
class Desplazados {
  //******* Properties *********//
  float startDate;
  float distance;
  float originX, originY, destinationX, destinationY;
  float startFrame;
  float travelSpeed;
  float timeDuration;
  float currentDuration;
  float proportionalPosition;
  float tripTime;
  int active;

  //******* Constructor *******//
  Desplazados(float start_date, float distance_km, float origin_X, float origin_Y, float destination_X, float destination_Y, float time, float speed) {
    startDate = start_date;
    originX = origin_X;
    originY = origin_Y;
    destinationX = destination_X;
    destinationY = destination_Y;
    distance = distance_km;
    tripTime = time;
    travelSpeed = map(speed, 1, 5, 1000, 2500);
    active = 0;
    // travelSpeed = random(100, 200);
  }

  //****** Methods *********//
  public void plotMovement() {
    timeDuration = distance / travelSpeed;
    if ((currentFrame > startDate) && ((startDate + timeDuration) > currentFrame)) {
      currentDuration = currentFrame - startDate;
      proportionalPosition = currentDuration / timeDuration;
      float distanceX, distanceY, currentPosX, currentPosY;
      distanceX = destinationX - originX;
      distanceY = destinationY - originY;
      currentPosX = originX + (distanceX * proportionalPosition);
      currentPosY = originY + (distanceY * proportionalPosition);
      float fillColor = map(proportionalPosition, 0, 0.5f, 0, 100);
      fill(19, fillColor, 100, 100);
      noStroke();
      ellipse(currentPosX, currentPosY, 0.5f, 0.5f);
    } else {
    }
  }

  public void countActive(){
    if (currentFrame > startDate) {
      active = 1;
    }
    else {
      active = 0;
    }
  }
}
class Internal {
  //******* Properties *********//
  float startDate;
  float originX, originY;
  float timeDuration = 125;
  float currentDuration, proportionalPosition;
  int active;

  //******* Constructor *******//
  Internal(float start_date, float origin_X, float origin_Y) {
    startDate = start_date;
    originX = origin_X;
    originY = origin_Y;
    active = 0;
  }

  //****** Methods *********//
  public void plotOrigin() {
    if (currentFrame > startDate && (startDate + timeDuration) > currentFrame) {
      stroke(0, 0, 100, 10);
      noFill();
      ellipse(originX, originY, 10, 10);
    } else {
    }
  }
  public void plotLoop() {
    if (currentFrame > startDate && (startDate + timeDuration) > currentFrame) {
      currentDuration = currentFrame - startDate;
      proportionalPosition = currentDuration / timeDuration;
      float currentX, currentY;
      currentX = originX + 3 * cos(radians(map(proportionalPosition, 0, 1, 0, 360)));
      currentY = originY + 3 * sin(radians(map(proportionalPosition, 0, 1, 0, 360)));
      float fillColor = map(proportionalPosition, 0, 1, 0, 100);
      fill(19, 100, 100, 1);
      noStroke();
      ellipse(originX, originY, 2.75f, 2.75f);
      fill(19, fillColor, 100, 100);
      ellipse(currentX, currentY, 0.5f, 0.5f);
      } else {
    }
  }
  public void countActive(){
    if (currentFrame > startDate) {
      active = 1;
    }
    else {
      active = 0;
    }
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_160708a" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
