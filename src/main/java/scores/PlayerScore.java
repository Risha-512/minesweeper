package scores;

import java.io.Serializable;

public class PlayerScore implements Serializable {
    private final int score;
    private final String playerName;

    public PlayerScore(int score, String playerName) {
        this.score = score;
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }
}
