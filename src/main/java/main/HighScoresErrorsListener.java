package main;

import scores.HighScoresErrors;
import scores.HighScoresErrorsObserver;
import view.View;

public class HighScoresErrorsListener implements HighScoresErrorsObserver {
    private final View view;

    public HighScoresErrorsListener(View view) {
        this.view = view;
    }

    @Override
    public void onHighScoresError(HighScoresErrors error) {
        view.showErrorWindow(error.getErrorMessage());
    }
}
