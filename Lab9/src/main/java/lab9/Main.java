package lab9;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int rows = 15;
        int cols = 20;
        int robotCount = 3;
        long timeLimitMs = 60_000; // 60 seconds

        Maze maze = new Maze(rows, cols);
        SharedMemory sharedMemory = new SharedMemory(maze);

        Bunny bunny = new Bunny("Bunny", maze, sharedMemory, 0, 0);
        bunny.setMoveDelay(400);

        List<Robot> robots = new ArrayList<>();
        Random rand = new Random();
        Set<String> occupied = new HashSet<>();
        occupied.add("0,0");

        for (int i = 0; i < robotCount; i++) {
            int r, c;
            do {
                r = rand.nextInt(rows);
                c = rand.nextInt(cols);
            } while (occupied.contains(r + "," + c));
            occupied.add(r + "," + c);
            Robot robot = new Robot("Robot-" + (i + 1), maze, sharedMemory, r, c);
            robot.setMoveDelay(600);
            robots.add(robot);
        }

        GameManager gameManager = new GameManager(maze, sharedMemory, bunny, robots, timeLimitMs);
        gameManager.start();

        KeyboardController keyboard = new KeyboardController(bunny, robots);
        Thread keyboardThread = new Thread(keyboard);
        keyboardThread.setDaemon(true);
        keyboardThread.start();

        // Wait for game to finish
        while (!sharedMemory.isGameOver()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        gameManager.stopAll();
        keyboard.stop();
        System.out.println("\nGame Over.");
    }
}
