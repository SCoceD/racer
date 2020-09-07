package com.javarush.games.racer;

import com.javarush.games.racer.road.RoadManager;

public class PlayerCar extends GameObject {
    private static int playerCarHeight = ShapeMatrix.PLAYER.length;
    public int speed = 1;
    private Direction direction;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public PlayerCar() {
        super(RacerGame.WIDTH/2+2, RacerGame.HEIGHT - playerCarHeight-1, ShapeMatrix.PLAYER);
    }
    public void move(){
        if(RoadManager.LEFT_BORDER > this.x){
            x = RoadManager.LEFT_BORDER;
        }else if (RoadManager.RIGHT_BORDER < this.x){
            x = RoadManager.RIGHT_BORDER-width;
        }
        if(direction == Direction.RIGHT){x ++;}
        else if (direction == Direction.LEFT){x --;}
    }
    public void stop(){
        this.matrix = ShapeMatrix.PLAYER_DEAD;
    }
}
