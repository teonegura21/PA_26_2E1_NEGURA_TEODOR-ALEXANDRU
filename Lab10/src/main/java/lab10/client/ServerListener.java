package lab10.client;

import lab10.common.Protocol;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerListener implements Runnable {
    private final BufferedReader in;
    private volatile boolean running = true;

    public ServerListener(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String line;
            while (running && (line = in.readLine()) != null) {
                String[] parts = Protocol.decode(line);
                String cmd = parts[0];
                String payload = parts.length > 1 ? parts[1] : "";

                switch (cmd) {
                    case Protocol.QUESTION:
                        if (payload.startsWith("NEXT")) {
                            System.out.println("\n--- New Question ---");
                        } else {
                            String[] qParts = payload.split("\\|", 3);
                            if (qParts.length >= 3) {
                                System.out.println("\nQuestion " + qParts[0]);
                                System.out.println(qParts[1]);
                                for (String opt : qParts[2].split(",")) {
                                    System.out.println("  " + opt);
                                }
                                System.out.print("Your answer (number): ");
                            }
                        }
                        break;
                    case Protocol.RESULT:
                        String[] rParts = payload.split("\\|", 3);
                        System.out.println("Result: " + rParts[0] + " (time: " + rParts[1] + "ms)" + (rParts.length > 2 ? " " + rParts[2] : ""));
                        break;
                    case Protocol.SCORE:
                        System.out.println("\n=== SCOREBOARD ===\n" + payload);
                        break;
                    case Protocol.START:
                        System.out.println("GAME STARTED: " + payload);
                        break;
                    case Protocol.END:
                        System.out.println("GAME ENDED: " + payload);
                        break;
                    case Protocol.WAIT:
                        System.out.println("INFO: " + payload);
                        break;
                    case Protocol.STOP:
                        System.out.println("SERVER STOPPED: " + payload);
                        running = false;
                        break;
                    case Protocol.ERROR:
                        System.out.println("ERROR: " + payload);
                        break;
                    default:
                        System.out.println("SERVER: " + payload);
                }
            }
        } catch (IOException e) {
            if (running) {
                System.out.println("Lost connection to server.");
            }
        }
    }

    public void stop() {
        running = false;
    }
}
