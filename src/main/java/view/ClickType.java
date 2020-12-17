package view;

import java.awt.event.MouseEvent;

public enum ClickType {
    LEFT_MOUSE(MouseEvent.BUTTON1),
    MIDDLE_MOUSE(MouseEvent.BUTTON2),
    RIGHT_MOUSE(MouseEvent.BUTTON3);

    private final int clickedButton;

    ClickType(int clickedButton) {
        this.clickedButton = clickedButton;
    }

    public static ClickType of(int clickedButton) {
        for (ClickType clickType : values()) {
            if (clickType.clickedButton == clickedButton) {
                return clickType;
            }
        }
        return null;
    }
}
