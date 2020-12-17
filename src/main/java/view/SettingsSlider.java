package view;

import javax.swing.*;

public class SettingsSlider {
    private final JSlider jSlider;
    private final JLabel jLabel;

    protected SettingsSlider(JSlider jSlider, JLabel jLabel) {
        this.jSlider = jSlider;
        this.jLabel = jLabel;
    }

    protected JSlider getSlider() {
        return jSlider;
    }

    protected JLabel getLabel() {
        return jLabel;
    }
}
