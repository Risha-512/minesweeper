package view;

import common.MainCellType;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public enum ButtonType {
    ONE("1.png", ButtonType.FIELD_ICON_SIZE),
    TWO("2.png", ButtonType.FIELD_ICON_SIZE),
    THREE("3.png", ButtonType.FIELD_ICON_SIZE),
    FOUR("4.png", ButtonType.FIELD_ICON_SIZE),
    FIVE("5.png", ButtonType.FIELD_ICON_SIZE),
    SIX("6.png", ButtonType.FIELD_ICON_SIZE),
    SEVEN("7.png", ButtonType.FIELD_ICON_SIZE),
    EIGHT("8.png", ButtonType.FIELD_ICON_SIZE),
    MINE("mine.png", ButtonType.FIELD_ICON_SIZE),
    FLAG("flag.png", ButtonType.FIELD_ICON_SIZE),
    EMPTY("empty.png", ButtonType.FIELD_ICON_SIZE),
    CLOSED("closed.png", ButtonType.FIELD_ICON_SIZE),
    SMILE("smile.png", ButtonType.SMILE_ICON_SIZE),
    WIN("win.png", ButtonType.SMILE_ICON_SIZE),
    LOSE("lose.png", ButtonType.SMILE_ICON_SIZE);

    public final static int FIELD_ICON_SIZE = 35;
    public final static int SMILE_ICON_SIZE = 64;

    private final ImageIcon imageIcon;

    ButtonType(String imageIconPath, int size) {
        this.imageIcon = initializeIcon(imageIconPath, size);
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public static ButtonType getTypeByNumber(int number) {
        return switch (number) {
            case 1 -> ONE;
            case 2 -> TWO;
            case 3 -> THREE;
            case 4 -> FOUR;
            case 5 -> FIVE;
            case 6 -> SIX;
            case 7 -> SEVEN;
            case 8 -> EIGHT;
            default -> EMPTY;
        };
    }

    public static ButtonType getTypeByMainCellType(MainCellType mainCellType) {
        if (mainCellType == null || mainCellType == MainCellType.NUMBER) {
            throw new IllegalArgumentException("ButtonType: Wrong data! Unable to determine ButtonType");
        }
        return switch (mainCellType) {
            case MINE -> ButtonType.MINE;
            case FLAG -> ButtonType.FLAG;
            default -> ButtonType.CLOSED;
        };
    }

    private static ImageIcon initializeIcon(String path, int size) {
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(View.class.getClassLoader().getResource(path)));
        Image image = imageIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
}
