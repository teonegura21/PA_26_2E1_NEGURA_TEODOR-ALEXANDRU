package lab10.server;

import lab10.common.Protocol;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Advanced: Virtual Thread Server demonstration.
 * Uses Java 21 virtual threads to handle many concurrent clients efficiently.
 * Run with: java --enable-preview lab10.server.VirtualThreadServer <port>
 */
public class VirtualThreadServer {
    private final int port;
    private volatile boolean running = true;

    public VirtualThreadServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port);
             ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            System.out.println("VirtualThreadServer started on port " + port);
            System.out.println("Using virtual threads for high concurrency.");

            while (running) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private void handleClient(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.equalsIgnoreCase(Protocol.STOP)) {
                    out.println(Protocol.encode(Protocol.STOP, "Server stopped"));
                    running = false;
                    break;
                } else {
                    out.println(Protocol.encode(Protocol.PING, "Server received: " + line));
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = Protocol.DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port. Using default " + Protocol.DEFAULT_PORT);
            }
        }
        new VirtualThreadServer(port).start();
    }
}
