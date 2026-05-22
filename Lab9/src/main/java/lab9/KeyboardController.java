package lab9;

import java.util.*;

public class KeyboardController implements Runnable {
    private final Bunny bunny;
    private final List<Robot> robots;
    private volatile boolean running = true;

    public KeyboardController(Bunny bunny, List<Robot> robots) {
        this.bunny = bunny;
        this.robots = robots;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Keyboard commands:");
        System.out.println("  + / - : speed up / slow down bunny");
        System.out.println("  r+ / r- : speed up / slow down all robots");
        System.out.println("  p : pause all");
        System.out.println("  c : resume all");
        System.out.println("  q : quit");
        while (running && scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            switch (line) {
                case "+":
                    bunny.setMoveDelay(bunny.getMoveDelay() - 100);
                    System.out.println("Bunny delay: " + bunny.getMoveDelay() + "ms");
                    break;
                case "-":
                    bunny.setMoveDelay(bunny.getMoveDelay() + 100);
                    System.out.println("Bunny delay: " + bunny.getMoveDelay() + "ms");
                    break;
                case "r+":
                    for (Robot r : robots) r.setMoveDelay(r.getMoveDelay() - 100);
                    System.out.println("Robot delays decreased.");
                    break;
                case "r-":
                    for (Robot r : robots) r.setMoveDelay(r.getMoveDelay() + 100);
                    System.out.println("Robot delays increased.");
                    break;
                case "p":
                    bunny.pause();
                    for (Robot r : robots) r.pause();
                    System.out.println("Paused.");
                    break;
                case "c":
                    bunny.resume();
                    for (Robot r : robots) r.resume();
                    System.out.println("Resumed.");
                    break;
                case "q":
                    running = false;
                    break;
                default:
                    System.out.println("Unknown command: " + line);
            }
        }
    }
}
