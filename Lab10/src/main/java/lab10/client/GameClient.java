package lab10.client;

import lab10.common.Protocol;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    private final String host;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ServerListener listener;
    private Thread listenerThread;

    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            listener = new ServerListener(in);
            listenerThread = new Thread(listener);
            listenerThread.start();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String name = scanner.nextLine().trim();
            send(Protocol.JOIN, name);

            System.out.println("Commands: 'start' to start game, 'exit' to quit, or answer numbers during game.");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("exit")) {
                    break;
                } else if (line.equalsIgnoreCase("start")) {
                    // Just inform server; actual start is triggered on server console or auto
                    System.out.println("Requesting game start... (server admin may also start)");
                    // We can send a custom command if needed; for now just print
                } else if (line.matches("\\d+")) {
                    send(Protocol.ANSWER, line);
                } else {
                    send(Protocol.PING, line);
                }
            }
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            shutdown();
        }
    }

    private void send(String command, String payload) {
        if (out != null) {
            out.println(Protocol.encode(command, payload));
        }
    }

    private void shutdown() {
        try {
            if (listener != null) listener.stop();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
        System.out.println("Client disconnected.");
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = Protocol.DEFAULT_PORT;
        if (args.length >= 1) host = args[0];
        if (args.length >= 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port. Using default " + Protocol.DEFAULT_PORT);
            }
        }
        new GameClient(host, port).start();
    }
}
