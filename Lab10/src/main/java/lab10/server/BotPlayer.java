package lab10.server;

import lab10.common.GameConfig;
import lab10.common.Question;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BotPlayer implements Runnable {
    private final String name;
    private final GameServer server;
    private final Random random = new Random();
    private volatile boolean running = true;
    private volatile Question currentQuestion;
    private volatile boolean questionPending = false;
    private int score = 0;
    private long totalResponseTime = 0;
    private int questionsAnswered = 0;
    private final double correctnessProbability;

    public BotPlayer(String name, GameServer server, double correctnessProbability) {
        this.name = name;
        this.server = server;
        this.correctnessProbability = correctnessProbability;
    }

    @Override
    public void run() {
        server.registerBot(this);
        while (running) {
            if (questionPending && currentQuestion != null) {
                long delay = GameConfig.BOT_DELAY_MIN_MS +
                        random.nextInt((int)(GameConfig.BOT_DELAY_MAX_MS - GameConfig.BOT_DELAY_MIN_MS));
                try {
                    TimeUnit.MILLISECONDS.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                int answer;
                if (random.nextDouble() < correctnessProbability) {
                    answer = currentQuestion.getCorrectIndex();
                } else {
                    answer = random.nextInt(currentQuestion.getOptions().size());
                }
                server.submitBotAnswer(this, String.valueOf(answer));
                questionPending = false;
            } else {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    public void setQuestion(Question q) {
        this.currentQuestion = q;
        this.questionPending = true;
    }

    public void stop() {
        running = false;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
    public long getTotalResponseTime() { return totalResponseTime; }

    public void recordAnswer(boolean correct, long timeMs) {
        questionsAnswered++;
        if (correct) score++;
        totalResponseTime += timeMs;
    }
}
