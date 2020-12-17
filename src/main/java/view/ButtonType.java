package view;

import common.MainCellType;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public enum ButtonType {
    ONE(initializeFieldIcon("1.png")),
    TWO(initializeFieldIcon("2.png")),
    THREE(initializeFieldIcon("3.png")),
    FOUR(initializeFieldIcon("4.png")),
    FIVE(initializeFieldIcon("5.png")),
    SIX(initializeFieldIcon("6.png")),
    SEVEN(initializeFieldIcon("7.png")),
    EIGHT(initializeFieldIcon("8.png")),
    MINE(initializeFieldIcon("mine.png")),
    FLAG(initializeFieldIcon("flag.png")),
    EMPTY(initializeFieldIcon("empty.png")),
    CLOSED(initializeFieldIcon("closed.png")),
    SMILE(initializeSmileIcon("smile.png")),
    WIN(initializeSmileIcon("win.png")),
    LOSE(initializeSmileIcon("lose.png"));

    public final static int FIELD_ICON_SIZE = 35;
    public final static int SMILE_ICON_SIZE = 64;

    private final ImageIcon imageIcon;

    ButtonType(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
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

    private static ImageIcon initializeFieldIcon(String path) {
        return initializeIcon(path, FIELD_ICON_SIZE);
    }

    private static ImageIcon initializeSmileIcon(String path) {
        return initializeIcon(path, SMILE_ICON_SIZE);
    }

    private static ImageIcon initializeIcon(String path, int size) {
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(View.class.getClassLoader().getResource(path)));
        Image image = imageIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
}
