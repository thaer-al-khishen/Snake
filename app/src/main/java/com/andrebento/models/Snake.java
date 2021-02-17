package com.andrebento.models;

import android.content.Context;
import android.graphics.Canvas;

import com.andrebento.interfaces.Constants;

import java.util.ArrayList;

/**
 * Created by andre on 18/01/2017.
 */

public class Snake {
    private Context context;
    private ArrayList<SnakePiece> pieces;

    public Snake(Context context) {
        this.context = context;
        this.pieces = new ArrayList<>();
        this.pieces.add(new SnakeHead(context));
        this.pieces.add(new SnakeTail(context, pieces.get(0).getPosX() - 1,
                pieces.get(0).getPosY(), pieces.get(0).getDirection()));
    }

    public void setHeadDirection(int newDirection) {
        SnakeHead snakeHead = (SnakeHead) pieces.get(0);
        if(newDirection == Constants.UP && snakeHead.getDirection() != Constants.DOWN) {
            snakeHead.setDirection(newDirection);
            snakeHead.setHeadUp();
        } else if(newDirection == Constants.RIGHT && snakeHead.getDirection() != Constants.LEFT) {
            snakeHead.setDirection(newDirection);
            snakeHead.setHeadRight();
        } else if(newDirection == Constants.DOWN && snakeHead.getDirection() != Constants.UP) {
            snakeHead.setDirection(newDirection);
            snakeHead.setHeadDown();
        } else if(newDirection == Constants.LEFT && snakeHead.getDirection() != Constants.RIGHT) {
            snakeHead.setDirection(newDirection);
            snakeHead.setHeadLeft();
        }
    }

    public void move() {
        for (int i = pieces.size() - 1; i >= 0; i--) {
            SnakePiece snakePiece = pieces.get(i);
            int newPosition;
            switch (snakePiece.getDirection()) {
                case Constants.UP:
                    newPosition = snakePiece.getPosY() - 1;
                    if (newPosition < 0)
                        newPosition = Constants.nVerticalCells - 1;
                    snakePiece.setPosY(newPosition);
                    break;
                case Constants.RIGHT:
                    newPosition = snakePiece.getPosX() + 1;
                    if (newPosition >= Constants.nHorizontalCells)
                        newPosition = 0;
                    snakePiece.setPosX(newPosition);
                    break;
                case Constants.DOWN:
                    newPosition = snakePiece.getPosY() + 1;
                    if (newPosition >= Constants.nVerticalCells)
                        newPosition = 0;
                    snakePiece.setPosY(newPosition);
                    break;
                case Constants.LEFT:
                    newPosition = snakePiece.getPosX() - 1;
                    if (newPosition < 0)
                        newPosition = Constants.nHorizontalCells - 1;
                    snakePiece.setPosX(newPosition);
                    break;
            }

            if (i - 1 >= 0 && i != 0)
                if (snakePiece.getDirection() != pieces.get(i - 1).getDirection())
                    snakePiece.setDirection(pieces.get(i - 1).getDirection());
        }

        updateImageDirections();
    }

    private void updateImageDirections() {
        for(SnakePiece snakePiece : pieces) {
            switch (snakePiece.getDirection()) {
                case Constants.UP:
                    if(snakePiece instanceof SnakeBody)
                        ((SnakeBody)snakePiece).setBodyVertical();
                    else if(snakePiece instanceof SnakeTail)
                        ((SnakeTail)snakePiece).setTailUp();
                    break;
                case Constants.RIGHT:
                    if(snakePiece instanceof SnakeBody)
                        ((SnakeBody)snakePiece).setBodyHorizontal();
                    else if(snakePiece instanceof SnakeTail)
                        ((SnakeTail)snakePiece).setTailRight();
                    break;
                case Constants.DOWN:
                    if(snakePiece instanceof SnakeBody)
                        ((SnakeBody)snakePiece).setBodyVertical();
                    else if(snakePiece instanceof SnakeTail)
                        ((SnakeTail)snakePiece).setTailDown();
                    break;
                case Constants.LEFT:
                    if(snakePiece instanceof SnakeBody)
                        ((SnakeBody)snakePiece).setBodyHorizontal();
                    else if(snakePiece instanceof SnakeTail)
                        ((SnakeTail)snakePiece).setTailLeft();
                    break;
            }
        }

        for(int i = pieces.size() - 1; i >= 0; i--)
            if(i - 1 > 0) {
                SnakePiece actualPiece = pieces.get(i);
                SnakePiece nextPiece = pieces.get(i - 1);
                if(actualPiece.getDirection() != nextPiece.getDirection()) {
                    if(actualPiece.getDirection() == Constants.UP
                            && nextPiece.getDirection() == Constants.LEFT
                            || actualPiece.getDirection() == Constants.RIGHT
                            && nextPiece.getDirection() == Constants.DOWN)
                        ((SnakeBody)nextPiece).setBodyDownLeft();
                    else if(actualPiece.getDirection() == Constants.UP
                            && nextPiece.getDirection() == Constants.RIGHT
                            || actualPiece.getDirection() == Constants.LEFT
                            && nextPiece.getDirection() == Constants.DOWN)
                        ((SnakeBody)nextPiece).setBodyRightDown();
                    else if(actualPiece.getDirection() == Constants.DOWN
                            && nextPiece.getDirection() == Constants.LEFT
                            || actualPiece.getDirection() == Constants.RIGHT
                            && nextPiece.getDirection() == Constants.UP)
                        ((SnakeBody)nextPiece).setBodyUpLeft();
                    else if(actualPiece.getDirection() == Constants.DOWN
                            && nextPiece.getDirection() == Constants.RIGHT
                            || actualPiece.getDirection() == Constants.LEFT
                            && nextPiece.getDirection() == Constants.UP)
                        ((SnakeBody)nextPiece).setBodyUpRight();
                }
            }
    }

    private void shrink() {
        SnakePiece snakeTail = pieces.get(pieces.size() - 1);
        SnakePiece snakeBodyBeforeTail = pieces.get(pieces.size() - 2);
        pieces.remove(snakeTail);
        pieces.remove(snakeBodyBeforeTail);
        pieces.add(snakeTail);
        switch (snakeBodyBeforeTail.getDirection()) {
            case Constants.UP:
                snakeTail.setPosY(snakeTail.getPosY() - 1);
                break;
            case Constants.RIGHT:
                snakeTail.setPosX(snakeTail.getPosX() + 1);
                break;
            case Constants.DOWN:
                snakeTail.setPosY(snakeTail.getPosY() + 1);
                break;
            case Constants.LEFT:
                snakeTail.setPosX(snakeTail.getPosX() - 1);
                break;
        }
    }

    public void eat(Game snakeGame, Food food, RedBox redBox, BlueBox blueBox) {
        SnakePiece snakeHead = pieces.get(0);
        if(snakeHead.getPosX() == food.getPosX() && snakeHead.getPosY() == food.getPosY()) {
            snakeGame.setScore(snakeGame.getScore() + 1);
            growth();
            food.setRandomPosition();
        }

        if (snakeHead.getPosX() == redBox.getPosX() && snakeHead.getPosY() == redBox.getPosY()) {
            //The red box only works if the snake is larger than 2 pieces
            if (pieces.size() > 2) {
                shrink();
                updateImageDirections();
            }
            //Make both the red and blue boxes disappear upon taking the red box
            blueBox.hideBluebox();
            redBox.hideRedbox();
            waitThenAddRedBox(redBox);
            waitThenAddBlueBox(blueBox);
        }

        if (snakeHead.getPosX() == blueBox.getPosX() && snakeHead.getPosY() == blueBox.getPosY()) {
            //Increase the snake's size without increasing the score
            growth();

            //Make both the red and blue boxes disappear upon taking the blue box
            blueBox.hideBluebox();
            redBox.hideRedbox();
            waitThenAddRedBox(redBox);
            waitThenAddBlueBox(blueBox);
        }
    }

    //Function that makes the red box disappear for 10 seconds then reappear for 3 seconds or until user consumes the red box
    private void waitThenAddRedBox(final RedBox redBox) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        redBox.setRandomPosition();  //After 10 seconds
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {// After 13 seconds
                                        //If the hideRedBox function is not triggered enter this block of code, this helps avoid repositioning the box twice
                                        //This hideRedBox function is only triggered either here or when the user consumes the red box
                                        if (redBox.getPosX() != -50 && redBox.getPosY() != -50) {
                                            redBox.hideRedbox();
                                            waitThenAddRedBox(redBox);
                                        }
                                    }
                                },
                                3000
                        );
                    }
                },
                10000
        );
    }

    //Function that makes the blue box disappear for 10 seconds then reappear for 3 seconds or until user consumes the red box
    private void waitThenAddBlueBox(final BlueBox blueBox) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        blueBox.setRandomPosition();  //After 10 seconds
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {// After 13 seconds
                                        //If the hideBlueBox function is not triggered enter this block of code, this helps avoid repositioning the box twice
                                        //This hideBlueBox function is only triggered either here or when the user consumes the blue box
                                        if (blueBox.getPosX() != -50 && blueBox.getPosY() != -50) {
                                            blueBox.hideBluebox();
                                            waitThenAddBlueBox(blueBox);
                                        }
                                    }
                                },
                                3000
                        );
                    }
                },
                10000
        );
    }

    private void growth() {
        SnakeTail snakeTail = (SnakeTail) pieces.get(pieces.size() - 1);
        pieces.remove(snakeTail);
        SnakeBody snakeBody = new SnakeBody(context, snakeTail.getPosX(), snakeTail.getPosY(), snakeTail.getDirection());
        pieces.add(snakeBody);
        pieces.add(snakeTail);
        switch(snakeBody.getDirection()) {
            case Constants.UP:
                snakeTail.setPosY(snakeTail.getPosY() + 1);
                break;
            case  Constants.RIGHT:
                snakeTail.setPosX(snakeTail.getPosX() - 1);
                break;
            case  Constants.DOWN:
                snakeTail.setPosY(snakeTail.getPosY() - 1);
                break;
            case  Constants.LEFT:
                snakeTail.setPosX(snakeTail.getPosX() + 1);
                break;
        }

    }

    public boolean detectColision() {
        SnakeHead snakeHead = (SnakeHead) pieces.get(0);

        //Implement collision between snake and walls
        SnakePiece snakePiece = pieces.get(1);
        if (Math.abs(snakeHead.getPosX() - snakePiece.getPosX()) > 1) return true;
        if (Math.abs(snakeHead.getPosY() - snakePiece.getPosY()) > 1) return true;

        for(int i = 1; i < pieces.size() - 1; i++)
            if(snakeHead.getPosX() == pieces.get(i).getPosX() &&
                    snakeHead.getPosY() == pieces.get(i).getPosY())
                return true;
        return false;
    }

    public void draw(Canvas canvas) {
        int i = 0;
        for(SnakePiece snakePiece : pieces) {
            System.out.println("PECA."+i+",X:"+snakePiece.getPosX()+" - Y:"+snakePiece.getPosY());
            snakePiece.draw(canvas);
            i++;
        }
    }
}
