//******** Importing Libraries **********//
import java.util.*;

//******** Global Variables **********//
int canvasWidth;
int canvasHeight;
float minLon = -90.71367297;
float maxLon = -57.210862008;
float minLat = -5.209789943;
float maxLat = 13.635541224;
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

void settings(){
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

void loadData() {
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

void setup() {
  colorMode(HSB, 360, 100, 100, 100);
  background(0);
  loadData();
  totalUnixTime = unixEnd - unixStart;
  // img = loadImage("BaseImage.png");
  textFont = createFont("Anaheim-Regular.ttf", 12, true);
}

void draw() {
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
  currentMonth = months[(int(currentDate[1])-1)];
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