package lab10.server;

import lab10.common.GameConfig;
import lab10.common.PlayerResult;
import lab10.common.Protocol;
import lab10.common.Question;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;

public class ClientThread implements Runnable {
    private final Socket socket;
    private final GameServer server;
    private PrintWriter out;
    private BufferedReader in;
    private String playerName;
    private PlayerResult result;
    private volatile boolean running = true;
    private volatile boolean inGame = false;
    private final BlockingQueue<String> answerQueue = new LinkedBlockingQueue<>();

    public ClientThread(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            while (running && (line = in.readLine()) != null) {
                handleMessage(line.trim());
            }
        } catch (IOException e) {
            if (running) {
                System.out.println("Client disconnected: " + playerName);
            }
        } finally {
            cleanup();
        }
    }

    private void handleMessage(String msg) {
        if (msg.isEmpty()) return;
        String[] parts = Protocol.decode(msg);
        String cmd = parts[0];
        String payload = parts.length > 1 ? parts[1] : "";

        switch (cmd) {
            case Protocol.JOIN:
                this.playerName = payload.isEmpty() ? "Player" + socket.getPort() : payload;
                this.result = new PlayerResult(playerName, 0, 0);
                server.registerPlayer(this);
                send(Protocol.encode(Protocol.WAIT, "Welcome " + playerName + ". Waiting for game to start..."));
                break;
            case Protocol.ANSWER:
                answerQueue.offer(payload);
                break;
            case Protocol.PING:
                send(Protocol.encode(Protocol.PING, "pong"));
                break;
            case Protocol.STOP:
                send(Protocol.encode(Protocol.STOP, "Server stopped"));
                server.shutdownServer();
                break;
            default:
                send(Protocol.encode(Protocol.ERROR, "Unknown command: " + cmd));
        }
    }

    public void send(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayerResult getResult() {
        return result;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public boolean askQuestion(Question question, int questionNumber, int total) {
        String qText = questionNumber + "/" + total + "|" + question.getText() + "|" + String.join(",", question.getOptions());
        send(Protocol.encode(Protocol.QUESTION, qText));

        long startTime = System.currentTimeMillis();
        String answerStr = null;
        try {
            answerStr = answerQueue.poll(GameConfig.QUESTION_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long elapsed = System.currentTimeMillis() - startTime;

        boolean correct = false;
        if (answerStr != null) {
            try {
                int ans = Integer.parseInt(answerStr.trim());
                correct = question.isCorrect(ans);
            } catch (NumberFormatException ignored) {}
        }

        if (correct) {
            result.incrementScore();
            result.addResponseTime(elapsed);
            send(Protocol.encode(Protocol.RESULT, "CORRECT|" + elapsed));
        } else {
            result.addResponseTime(elapsed);
            send(Protocol.encode(Protocol.RESULT, "WRONG|" + elapsed + "|Correct was: " + question.getCorrectIndex()));
        }
        return correct;
    }

    public void sendScoreBoard(List<PlayerResult> results) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < results.size(); i++) {
            sb.append(i + 1).append(". ").append(results.get(i).toString()).append("\n");
        }
        send(Protocol.encode(Protocol.SCORE, sb.toString()));
    }

    public void sendEnd(String message) {
        send(Protocol.encode(Protocol.END, message));
    }

    private void cleanup() {
        running = false;
        server.removePlayer(this);
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
    }
}
