package scores;

import common.GameDifficulty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScores {
    private static final int MAX_SCORES_COUNT = 10;

    private final HighScoresRepository repository;
    private final List<PlayerScore> scores;

    private HighScoresErrors error;
    private boolean hasNewScores = false;

    protected HighScores(GameDifficulty gameDifficulty) {
        this.repository = new HighScoresFileRepository(System.getProperty("user.dir") + "\\" + gameDifficulty.getName() + ".bin");
        this.scores = readScores();
    }

    protected void writeScores() {
        if (!hasNewScores) {
            return;
        }
        try {
            repository.update(scores);
        } catch (IOException exception) {
            error = HighScoresErrors.SAVE_FAIL;
        }
    }

    protected List<PlayerScore> getScoresList() {
        return new ArrayList<>(scores);
    }

    protected boolean isNewValueAHighScore(int valueToCheck) {
        return scores.size() < MAX_SCORES_COUNT || scores.stream().anyMatch(score -> score.getScore() > valueToCheck);
    }

    protected void addScoreToList(int score, String playerName) {
        for (int i = 0; i < scores.size(); i++) {
            if (scores.get(i).getScore() > score) {
                addScoreToListByIndex(score, playerName, i);
                return;
            }
        }
        if (scores.size() < MAX_SCORES_COUNT) {
            hasNewScores = true;
            scores.add(new PlayerScore(score, playerName));
        }
    }

    protected HighScoresErrors getError() {
        return error;
    }

    private List<PlayerScore> readScores() {
        try {
            return repository.read();
        } catch (IOException exception) {
            error = HighScoresErrors.READ_FAIL;
            return Collections.emptyList();
        }
    }

    private void addScoreToListByIndex(int score, String playerName, int index) {
        hasNewScores = true;
        scores.add(index, new PlayerScore(score, playerName));

        if (scores.size() > MAX_SCORES_COUNT) {
            scores.remove(MAX_SCORES_COUNT);
        }
    }
}
