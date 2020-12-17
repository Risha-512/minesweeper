package view;

import common.Command;
import common.GameDifficulty;

public enum SettingsChoiceType {
    EASY {
        @Override
        public Command<ViewObserver> getCreateGameButtonAction(SettingsPanel settingsPanel) {
            return (ViewObserver observer) -> observer.createGame(GameDifficulty.EASY);
        }
    },
    MEDIUM {
        @Override
        public Command<ViewObserver> getCreateGameButtonAction(SettingsPanel settingsPanel) {
            return (ViewObserver observer) -> observer.createGame(GameDifficulty.MEDIUM);
        }
    },
    HARD {
        @Override
        public Command<ViewObserver> getCreateGameButtonAction(SettingsPanel settingsPanel) {
            return (ViewObserver observer) -> observer.createGame(GameDifficulty.HARD);
        }
    },
    CUSTOM {
        @Override
        public Command<ViewObserver> getCreateGameButtonAction(SettingsPanel settingsPanel) {
            return (ViewObserver observer) -> {
                observer.createGame(settingsPanel.getCustomHeight(),
                            settingsPanel.getCustomWidth(),
                            settingsPanel.getCustomMinesPercentage());
            };
        }
    };

    public abstract Command<ViewObserver> getCreateGameButtonAction(SettingsPanel settingsPanel);
}
