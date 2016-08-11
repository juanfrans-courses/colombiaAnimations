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
  void plotMovement() {
    timeDuration = distance / travelSpeed;
    if ((currentFrame > startDate) && ((startDate + timeDuration) > currentFrame)) {
      currentDuration = currentFrame - startDate;
      proportionalPosition = currentDuration / timeDuration;
      float distanceX, distanceY, currentPosX, currentPosY;
      distanceX = destinationX - originX;
      distanceY = destinationY - originY;
      currentPosX = originX + (distanceX * proportionalPosition);
      currentPosY = originY + (distanceY * proportionalPosition);
      float fillColor = map(proportionalPosition, 0, 0.5, 0, 100);
      fill(19, fillColor, 100, 100);
      noStroke();
      ellipse(currentPosX, currentPosY, 0.5, 0.5);
    } else {
    }
  }

  void countActive(){
    if (currentFrame > startDate) {
      active = 1;
    }
    else {
      active = 0;
    }
  }
}