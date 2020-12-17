package view;

import observable.BasicObservable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SettingsPanel {
    private final JRadioButton jRadioButtonEasy;
    private final JRadioButton jRadioButtonMedium;
    private final JRadioButton jRadioButtonHard;
    private final JRadioButton jRadioButtonCustom;

    private final SettingsSlider heightSettingsSlider;
    private final SettingsSlider widthSettingsSlider;
    private final SettingsSlider minesPercentageSettingsSlider;

    private final ButtonGroup buttonGroup;
    private final JButton createGameButton;
    private final JPanel settingsPanel;

    private SettingsChoiceType settingsChoiceType = SettingsChoiceType.EASY;

    SettingsPanel(JDialog parentFrame, BasicObservable<ViewObserver> observable) {
        heightSettingsSlider = createSettingsSlider(SettingsParameter.HEIGHT);
        widthSettingsSlider = createSettingsSlider(SettingsParameter.WIDTH);
        minesPercentageSettingsSlider = createSettingsSlider(SettingsParameter.MINES_COUNT);

        jRadioButtonCustom = createDifficultyRadioButton("Custom", SettingsChoiceType.CUSTOM);

        jRadioButtonEasy = createDifficultyRadioButton("Easy", SettingsChoiceType.EASY);
        jRadioButtonMedium = createDifficultyRadioButton("Medium", SettingsChoiceType.MEDIUM);
        jRadioButtonHard = createDifficultyRadioButton("Hard", SettingsChoiceType.HARD);
        jRadioButtonEasy.setSelected(true);

        buttonGroup = createRadioButtonGroup(List.of(jRadioButtonEasy, jRadioButtonMedium, jRadioButtonHard, jRadioButtonCustom));
        createGameButton = createGameCreationButton(parentFrame, observable);
        settingsPanel = initializePanel();
    }

    protected JPanel getPanel() {
        return settingsPanel;
    }

    protected int getCustomHeight() {
        return heightSettingsSlider.getSlider().getValue();
    }

    protected int getCustomWidth() {
        return widthSettingsSlider.getSlider().getValue();
    }

    protected int getCustomMinesPercentage() {
        return minesPercentageSettingsSlider.getSlider().getValue();
    }

    private JRadioButton createDifficultyRadioButton(String title, SettingsChoiceType choiceType) {
        final JRadioButton jRadioButton = new JRadioButton(title);
        boolean isSlidersEnabled = choiceType == SettingsChoiceType.CUSTOM;
        jRadioButton.addActionListener(action -> {
            this.settingsChoiceType = choiceType;
            setEnabledForAllSliders(isSlidersEnabled);
        });
        return jRadioButton;
    }

    private void setEnabledForAllSliders(boolean isEnabled) {
        heightSettingsSlider.getSlider().setEnabled(isEnabled);
        widthSettingsSlider.getSlider().setEnabled(isEnabled);
        minesPercentageSettingsSlider.getSlider().setEnabled(isEnabled);
    }

    private JPanel initializePanel() {
        final JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);

        addComponentsToGrid(jPanel, createTopPanel(), gridBagConstraints);
        addComponentsToGrid(jPanel, heightSettingsSlider.getLabel(), gridBagConstraints);
        addComponentsToGrid(jPanel, heightSettingsSlider.getSlider(), gridBagConstraints);

        addComponentsToGrid(jPanel, widthSettingsSlider.getLabel(), gridBagConstraints);
        addComponentsToGrid(jPanel, widthSettingsSlider.getSlider(), gridBagConstraints);

        addComponentsToGrid(jPanel, minesPercentageSettingsSlider.getLabel(), gridBagConstraints);
        addComponentsToGrid(jPanel, minesPercentageSettingsSlider.getSlider(), gridBagConstraints);
        addComponentsToGrid(jPanel, createGameButton, gridBagConstraints);

        return jPanel;
    }

    private JPanel createTopPanel() {
        final JPanel topPanel = new JPanel();
        final FlowLayout flowLayout = new FlowLayout();

        topPanel.setLayout(flowLayout);
        topPanel.add(jRadioButtonEasy);
        topPanel.add(jRadioButtonMedium);
        topPanel.add(jRadioButtonHard);
        topPanel.add(jRadioButtonCustom);

        return topPanel;
    }

    private JButton createGameCreationButton(JDialog parentFrame, BasicObservable<ViewObserver> observable) {
        final JButton jButton = new JButton("Create game");
        jButton.addActionListener(action -> {
            observable.notifyAllObservers(settingsChoiceType.getCreateGameButtonAction(this));
            parentFrame.dispose();
        });

        return jButton;
    }

    private void addComponentsToGrid(JPanel jPanel, Component component, GridBagConstraints gridBagConstraints) {
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        jPanel.add(component, gridBagConstraints);
        gridBagConstraints.gridy++;
    }

    private SettingsSlider createSettingsSlider(SettingsParameter settingsParameter) {
        final JSlider jSlider = new JSlider(settingsParameter.getMinValue(), settingsParameter.getMaxValue());
        configureSlider(jSlider);

        final JLabel jLabel = new JLabel(settingsParameter.getParameterName() + ": " + jSlider.getValue());
        jSlider.addChangeListener(action -> jLabel.setText(settingsParameter.getParameterName() + ": " + jSlider.getValue()));

        return new SettingsSlider(jSlider, jLabel);
    }

    private ButtonGroup createRadioButtonGroup(List<JRadioButton> buttons) {
        final ButtonGroup buttonGroup = new ButtonGroup();
        buttons.forEach(buttonGroup::add);
        return buttonGroup;
    }

    private void configureSlider(JSlider jSlider) {
        jSlider.setPaintTrack(true);
        jSlider.setPaintTicks(true);
        jSlider.setPaintLabels(true);
        jSlider.setMajorTickSpacing((jSlider.getMaximum() - jSlider.getMinimum()) / 5);
        jSlider.setEnabled(false);
    }
}
