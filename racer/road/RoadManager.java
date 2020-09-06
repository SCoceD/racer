package com.javarush.games.racer.road;

import com.javarush.engine.cell.Game;
import com.javarush.games.racer.GameObject;
import com.javarush.games.racer.PlayerCar;
import com.javarush.games.racer.RacerGame;

import java.util.ArrayList;
import java.util.List;

import static com.javarush.games.racer.road.RoadObjectType.*;

public class RoadManager {
    public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
    private static final int FIRST_LANE_POSITION = 16;
    private static final int FOURTH_LANE_POSITION = 44;
    private List<RoadObject> items = new ArrayList<>();
    private static final int PLAYER_CAR_DISTANCE = 12;
    private int passedCarsCount = 0;


    public int getPassedCarsCount() {
        return passedCarsCount;
    }

    private RoadObject createRoadObject(RoadObjectType type, int x, int y) {
        if (type == RoadObjectType.THORN) {
            return new Thorn(x, y);
        } else if (type == RoadObjectType.DRUNK_CAR) return new MovingCar(x, y);
        else return new Car(type, x, y);
    }

    private void addRoadObject(RoadObjectType type, Game game) {
        int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
        int y = -1 * RoadObject.getHeight(type);
        RoadObject roadObject = createRoadObject(type, x, y);
        if (roadObject != null && isRoadSpaceFree(roadObject)) {
            items.add(roadObject);
        }
    }

    public void draw(Game game) {
        for (RoadObject o : items) {
            o.draw(game);
        }
    }

    public void move(int i) {
        for (RoadObject o : items) {
            o.move(i + o.speed, items);
        }
        deletePassedItems();
    }

    private boolean isThornExists() {
        for (RoadObject o : items) {
            if (o.type == THORN) return true;
        }
        return false;
    }

    private boolean isMovingCarExists() {
        for (RoadObject o : items) {
            if (o.type == DRUNK_CAR) return true;
        }
        return false;
    }

    private void generateThorn(Game game) {
        if (game.getRandomNumber(100) < 10 && !isThornExists()) {
            addRoadObject(RoadObjectType.THORN, game);
        }
    }

    private void generateMovingCar(Game game) {
        if (game.getRandomNumber(100) < 10 && !isMovingCarExists()) {
            addRoadObject(RoadObjectType.DRUNK_CAR, game);
        }
    }

    public void generateNewRoadObjects(Game game) {
        generateThorn(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }

    private void deletePassedItems() {
        List<RoadObject> itemTemp = new ArrayList<>(items);
        for (RoadObject o : itemTemp) {
            if (o.y >= RacerGame.HEIGHT) {
                items.remove(o);
                if(o.type != THORN) passedCarsCount++;
            }
        }
    }

    public boolean checkCrush(PlayerCar playerCar) {
        for (GameObject o : items) {
            if (o.isCollision(playerCar)) {
                return true;
            }
        }
        return false;
    }

    private void generateRegularCar(Game game) {
        int carTypeNumber = game.getRandomNumber(4);
        if (game.getRandomNumber(100) < 30) {
            addRoadObject(RoadObjectType.values()[carTypeNumber], game);
        }
    }

    private boolean isRoadSpaceFree(RoadObject object) {
        for (RoadObject o : items) {
            if (o.isCollisionWithDistance(object, PLAYER_CAR_DISTANCE)) {
                return false;
            }
        }
        return true;
    }
}