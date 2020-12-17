package scores;

import common.GameDifficulty;
import observable.BasicObservable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighScoresManager {
    private final Map<GameDifficulty, HighScores> scoresMap = new HashMap<>();
    private final BasicObservable<HighScoresErrorsObserver> errorsObservable = new BasicObservable<>();

    public HighScoresManager() {
        Arrays.stream(GameDifficulty.values()).forEach(difficulty -> scoresMap.put(difficulty, new HighScores(difficulty)));
    }

    public void addObserver(HighScoresErrorsObserver observer) {
        errorsObservable.addObserver(observer);
    }

    public boolean isInAHighScore(int score, GameDifficulty gameDifficulty) {
        return scoresMap.containsKey(gameDifficulty) && scoresMap.get(gameDifficulty).isNewValueAHighScore(score);
    }

    public void addScore(int score, String playerName, GameDifficulty gameDifficulty) {
        if (scoresMap.containsKey(gameDifficulty)) {
            scoresMap.get(gameDifficulty).addScoreToList(score, playerName);
        }
    }

    public void saveScores() {
        scoresMap.forEach((difficulty, scores) -> scores.writeScores());
        notifyObserversIfHasError();
    }

    public Map<GameDifficulty, List<PlayerScore>> getHighScoresListMap() {
        Map<GameDifficulty, List<PlayerScore>> scoresListMap = new HashMap<>();
        scoresMap.forEach(((difficulty, scores) -> scoresListMap.put(difficulty, scores.getScoresList())));
        notifyObserversIfHasError();

        return scoresListMap;
    }

    private void notifyObserversIfHasError() {
        for (HighScores scores : scoresMap.values()) {
            if (scores.getError() != null) {
                errorsObservable.notifyAllObservers(observer -> observer.onHighScoresError(scores.getError()));
                break;
            }
        }
    }
}
