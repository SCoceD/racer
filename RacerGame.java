package com.javarush.games.racer;

import com.javarush.engine.cell.*;
import com.javarush.games.racer.road.RoadManager;
import com.javarush.games.racer.road.RoadObject;

import static com.javarush.engine.cell.Key.*;

public class RacerGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int CENTER_X = WIDTH / 2;
    public static final int ROADSIDE_WIDTH = 14;
    private RoadMarking roadMarking;
    private PlayerCar player;
    private RoadManager roadManager;
    private boolean isGameStopped;
    private FinishLine finishLine;
    private static final int RACE_GOAL_CARS_COUNT = 40;
    private ProgressBar progressBar;
    private int score ;

    private void createGame() {
        score = 3500;
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        isGameStopped = false;
        roadMarking = new RoadMarking();
        player = new PlayerCar();
        roadManager = new RoadManager();
        finishLine = new FinishLine();
        drawScene();
        setTurnTimer(40);
    }

    private void drawScene() {
        drawField();
        progressBar.draw(this);
        roadManager.draw(this);
        roadMarking.draw(this);
        player.draw(this);
        finishLine.draw(this);
    }

    private void drawField() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (x == CENTER_X)
                    setCellColor(CENTER_X, y, Color.WHITE);
                else if (x >= ROADSIDE_WIDTH && x < (WIDTH - ROADSIDE_WIDTH))
                    setCellColor(x, y, Color.DIMGREY);
                else
                    setCellColor(x, y, Color.GREEN);
            }
        }
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x > 63 || y > 63 || x < 0 || y < 0) {
            return;
        }
        super.setCellColor(x, y, color);
    }

    @Override
    public void initialize() {
        showGrid(false);
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void moveAll() {
        roadMarking.move(player.speed);
        player.move();
        roadManager.move(player.speed);
        finishLine.move(player.speed);
        progressBar.move(roadManager.getPassedCarsCount());
    }

    @Override
    public void onKeyReleased(Key key) {
        if (player.getDirection() == Direction.RIGHT && key == RIGHT) {
            player.setDirection(Direction.NONE);
        }
        if (player.getDirection() == Direction.LEFT && key == LEFT) {
            player.setDirection(Direction.NONE);
        }
        if (key == Key.UP) {
            player.speed = 1;
        }
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case RIGHT:
                player.setDirection(Direction.RIGHT);
                return;
            case LEFT:
                player.setDirection(Direction.LEFT);
                return;
            case SPACE:
                if (isGameStopped) createGame();
                return;
            case UP:
                player.speed *= 2;
        }
    }

    @Override
    public void onTurn(int step) {
        score -= 5;
        setScore(score);
        if (finishLine.isCrossed(player)) {
            win();
            drawScene();
            return;
        }
        if (roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT) {
            finishLine.show();
        }
        if (roadManager.checkCrush(player)) {
            gameOver();
            drawScene();
            return;
        }
        moveAll();
        roadManager.generateNewRoadObjects(this);
        drawScene();
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.GREEN, "WIN", Color.PINK, 75);
        stopTurnTimer();
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.RED, "GAME OVER", Color.YELLOW, 75);
        stopTurnTimer();
        player.stop();
    }
}
