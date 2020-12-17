package scores;

public enum HighScoresErrors {
    READ_FAIL("Unable to read scores!"),
    SAVE_FAIL("Unable to save scores!");

    private final String errorMessage;

    HighScoresErrors(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
