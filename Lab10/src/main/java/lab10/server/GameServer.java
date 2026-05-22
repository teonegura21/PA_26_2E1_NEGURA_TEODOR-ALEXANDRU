package lab10.server;

import lab10.common.GameConfig;
import lab10.common.PlayerResult;
import lab10.common.Protocol;
import lab10.common.Question;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class GameServer {
    private final int port;
    private ServerSocket serverSocket;
    private final ThreadPoolExecutor executor;
    private final List<ClientThread> clients = new CopyOnWriteArrayList<>();
    private final List<BotPlayer> bots = new CopyOnWriteArrayList<>();
    private final List<Question> questions;
    private volatile boolean running = true;
    private volatile boolean gameInProgress = false;

    public GameServer(int port) {
        this.port = port;
        this.questions = QuestionLoader.loadQuestions("questions.json");
        this.executor = new ThreadPoolExecutor(
                4, 64, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>()
        );
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownServer));
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("GameServer started on port " + port);
            System.out.println("Loaded " + questions.size() + " questions.");
            System.out.println("Waiting for players... (type 'stop' in console to stop)");

            // Console listener for stop command
            executor.execute(this::consoleListener);

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New connection from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                    ClientThread client = new ClientThread(clientSocket, this);
                    clients.add(client);
                    executor.execute(client);
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Accept error: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
        } finally {
            shutdownServer();
        }
    }

    private void consoleListener() {
        Scanner scanner = new Scanner(System.in);
        while (running && scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if ("stop".equalsIgnoreCase(line)) {
                System.out.println("Stop command received. Shutting down...");
                shutdownServer();
                break;
            } else if ("start".equalsIgnoreCase(line)) {
                startGame();
            } else if ("addbot".equalsIgnoreCase(line)) {
                addBot();
            } else if ("status".equalsIgnoreCase(line)) {
                System.out.println("Players: " + clients.size() + ", Bots: " + bots.size() + ", Game in progress: " + gameInProgress);
            }
        }
    }

    public synchronized void registerPlayer(ClientThread client) {
        System.out.println("Registered player: " + client.getPlayerName());
        if (clients.size() >= GameConfig.MAX_PLAYERS) {
            client.send(Protocol.encode(Protocol.ERROR, "Server full"));
        }
    }

    public void removePlayer(ClientThread client) {
        clients.remove(client);
        System.out.println("Removed player: " + client.getPlayerName());
    }

    public void registerBot(BotPlayer bot) {
        bots.add(bot);
        System.out.println("Registered bot: " + bot.getName());
    }

    public void addBot() {
        String botName = "Bot-" + (bots.size() + 1);
        double correctness = 0.5 + Math.random() * 0.4; // 50-90% correctness
        BotPlayer bot = new BotPlayer(botName, this, correctness);
        bots.add(bot);
        executor.execute(bot);
        System.out.println("Added bot: " + botName + " (correctness=" + String.format("%.0f%%", correctness * 100) + ")");
    }

    public synchronized void startGame() {
        if (gameInProgress) {
            System.out.println("Game already in progress.");
            return;
        }
        int totalPlayers = clients.size() + bots.size();
        if (totalPlayers < GameConfig.MIN_PLAYERS) {
            System.out.println("Need at least " + GameConfig.MIN_PLAYERS + " players to start. Current: " + totalPlayers);
            return;
        }
        gameInProgress = true;
        executor.execute(this::runGame);
    }

    private void runGame() {
        List<Question> gameQuestions = selectQuestions();
        broadcast(Protocol.encode(Protocol.START, "Game starting with " + (clients.size() + bots.size()) + " players!"));

        for (int i = 0; i < gameQuestions.size(); i++) {
            Question q = gameQuestions.get(i);
            int questionNum = i + 1;
            int totalQuestions = gameQuestions.size();
            broadcast(Protocol.encode(Protocol.QUESTION, "NEXT"));
            try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // Send to clients
            for (ClientThread c : clients) {
                c.setInGame(true);
                executor.execute(() -> c.askQuestion(q, questionNum, totalQuestions));
            }
            // Send to bots
            for (BotPlayer b : bots) {
                b.setQuestion(q);
            }

            // Wait for answers
            try {
                Thread.sleep(GameConfig.QUESTION_TIMEOUT_MS + 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Compute results
        List<PlayerResult> results = new ArrayList<>();
        for (ClientThread c : clients) {
            if (c.getResult() != null) results.add(c.getResult());
        }
        for (BotPlayer b : bots) {
            results.add(new PlayerResult(b.getName(), b.getScore(), b.getTotalResponseTime()));
        }
        results.sort(null);

        broadcast(Protocol.encode(Protocol.END, "Game Over!"));
        for (ClientThread c : clients) {
            c.sendScoreBoard(results);
        }

        System.out.println("=== GAME RESULTS ===");
        for (PlayerResult r : results) {
            System.out.println(r);
        }

        gameInProgress = false;
    }

    private List<Question> selectQuestions() {
        List<Question> copy = new ArrayList<>(questions);
        Collections.shuffle(copy);
        return copy.subList(0, Math.min(GameConfig.QUESTIONS_PER_GAME, copy.size()));
    }

    public void submitBotAnswer(BotPlayer bot, String answer) {
        // Simulate timing
        long delay = GameConfig.BOT_DELAY_MIN_MS + (long)(Math.random() * (GameConfig.BOT_DELAY_MAX_MS - GameConfig.BOT_DELAY_MIN_MS));
        boolean correct = false;
        try {
            int ans = Integer.parseInt(answer);
            // We need current question to verify; simplified: bots know via setQuestion
            // For simplicity, correctness handled inside BotPlayer
            correct = true; // BotPlayer already decides correctness probability
        } catch (NumberFormatException ignored) {}
        bot.recordAnswer(correct, delay);
    }

    private void broadcast(String message) {
        for (ClientThread c : clients) {
            c.send(message);
        }
    }

    public void shutdownServer() {
        if (!running) return;
        running = false;
        gameInProgress = false;
        System.out.println("Shutting down server...");
        for (BotPlayer b : bots) b.stop();
        for (ClientThread c : clients) {
            c.send(Protocol.encode(Protocol.STOP, "Server stopped"));
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
        System.out.println("Server stopped.");
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
        GameServer server = new GameServer(port);
        server.start();
    }
}
