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
  void plotOrigin() {
    if (currentFrame > startDate && (startDate + timeDuration) > currentFrame) {
      stroke(0, 0, 100, 10);
      noFill();
      ellipse(originX, originY, 10, 10);
    } else {
    }
  }
  void plotLoop() {
    if (currentFrame > startDate && (startDate + timeDuration) > currentFrame) {
      currentDuration = currentFrame - startDate;
      proportionalPosition = currentDuration / timeDuration;
      float currentX, currentY;
      currentX = originX + 3 * cos(radians(map(proportionalPosition, 0, 1, 0, 360)));
      currentY = originY + 3 * sin(radians(map(proportionalPosition, 0, 1, 0, 360)));
      float fillColor = map(proportionalPosition, 0, 1, 0, 100);
      fill(19, 100, 100, 1);
      noStroke();
      ellipse(originX, originY, 2.75, 2.75);
      fill(19, fillColor, 100, 100);
      ellipse(currentX, currentY, 0.5, 0.5);
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