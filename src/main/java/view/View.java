package view;

import common.Callback;
import common.GameDifficulty;
import observable.BasicObservable;
import scores.PlayerScore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

public class View {
    private static final String WINDOW_TITLE = "Minesweeper";
    private static final String TIMER_LABEL_TITLE = "0";

    private final BasicObservable<ViewObserver> viewObservable = new BasicObservable<>();
    private final JFrame window;
    private final JButton resetButton;
    private final JLabel minesCountLabel;
    private final JLabel timerLabel;

    private JButton[][] field;
    private int fieldHeight;
    private int fieldWidth;
    private int flaggedMinesCount;

    public View(int fieldHeight, int fieldWidth, int bombsCount) {
        this.window = createWindow();
        this.resetButton = createResetButton();
        this.timerLabel = createTopPanelLabel();
        this.minesCountLabel = createTopPanelLabel();

        initializeView(fieldHeight, fieldWidth, bombsCount);
    }

    public View(GameDifficulty gameDifficulty) {
        this(gameDifficulty.getHeight(), gameDifficulty.getWidth(), gameDifficulty.getMinesCount());
    }

    public void initializeView(int fieldHeight, int fieldWidth, int bombsCount) {
        initializeValues(fieldHeight, fieldWidth, bombsCount);
        initializeWindow();
    }

    public void initializeView(GameDifficulty gameDifficulty) {
        initializeView(gameDifficulty.getHeight(), gameDifficulty.getWidth(), gameDifficulty.getMinesCount());
    }

    public void reset() {
        timerLabel.setText(TIMER_LABEL_TITLE);
        resetButton.setIcon(ButtonType.SMILE.getImageIcon());

        doActionForEachCell((x, y) -> {
            field[x][y].setEnabled(true);
            field[x][y].setIcon(ButtonType.CLOSED.getImageIcon());
        });
    }

    public void closeWindow() {
        window.dispose();
    }

    public void addObserver(ViewObserver observer) {
        viewObservable.addObserver(observer);
    }

    public void updateTime(int timeInSeconds) {
        timerLabel.setText(String.valueOf(timeInSeconds));
    }

    public void setFieldCellIcon(int x, int y, ButtonType cell) {
        field[x][y].setIcon(cell.getImageIcon());
    }

    public void updateFlaggedBombsCount(int flaggedBombsCount) {
        this.flaggedMinesCount = flaggedBombsCount;
        minesCountLabel.setText(String.valueOf(this.flaggedMinesCount));
    }

    public void disableFieldButtons() {
        doActionForEachCell((x, y) -> {
            field[x][y].setEnabled(false);
            field[x][y].setDisabledIcon(field[x][y].getIcon());
        });
    }

    public void setWinningIcon() {
        resetButton.setIcon(ButtonType.WIN.getImageIcon());
    }

    public void setLosingIcon() {
        resetButton.setIcon(ButtonType.LOSE.getImageIcon());
    }

    public void openNewGameSettingsWindow() {
        final JDialog settingsDialog = new JDialog(window, "Create new game");

        settingsDialog.add(createSettingsPanel(settingsDialog));
        settingsDialog.pack();
        settingsDialog.setResizable(false);
        settingsDialog.setModal(true);
        settingsDialog.setVisible(true);
    }

    public String askPlayerName() {
        return (String) JOptionPane.showInputDialog(
                window,
                "Enter your name",
                "You've got a high score!",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                ""
        );
    }

    public void showHighScores(Map<GameDifficulty, List<PlayerScore>> scoresListMap) {
        final JDialog scoresDialog = new JDialog(window, "High Scores");

        scoresDialog.add(new HighScoresPane(scoresListMap).getTabbedPane());
        scoresDialog.pack();
        scoresDialog.setResizable(false);
        scoresDialog.setModal(true);
        scoresDialog.setVisible(true);
    }

    public void showErrorWindow(String errorMessage) {
        createDialogWindow("Error", errorMessage);
    }

    private void initializeWindow() {
        window.setContentPane(createMainPanel());
        window.setJMenuBar(createMenuBar());
        window.setResizable(false);
        window.pack();
    }

    private void initializeValues(int fieldHeight, int fieldWidth, int minesCount) {
        this.fieldHeight = fieldHeight;
        this.fieldWidth = fieldWidth;
        this.flaggedMinesCount = minesCount;
        this.field = new JButton[fieldHeight][fieldWidth];

        this.timerLabel.setText(TIMER_LABEL_TITLE);
        this.minesCountLabel.setText(String.valueOf(flaggedMinesCount));
    }

    private JFrame createWindow() {
        final JFrame jFrame = new JFrame(WINDOW_TITLE);
        jFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(View.class.getClassLoader().getResource("flag.png")));
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

        return jFrame;
    }

    private JPanel createMainPanel() {
        final JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel.add(createWindowTopPanel(), gridBagConstraints);
        gridBagConstraints.gridy = 1;
        jPanel.add(createFieldPanel(), gridBagConstraints);

        return jPanel;
    }

    private JMenuBar createMenuBar() {
        final JMenuBar jMenuBar = new JMenuBar();

        jMenuBar.add(createMenuItem("New Game", action -> viewObservable.notifyAllObservers(ViewObserver::onNewGameButtonPressed)));

        jMenuBar.add(createMenuItem("High Scores", action -> viewObservable.notifyAllObservers(ViewObserver::onShowHighScoresButtonPressed)));

        jMenuBar.add(createMenuItem("About", action -> createDialogWindow("About", "Minesweeper for Focus Start by Lagunova Marina.")));

        jMenuBar.add(createMenuItem("Exit", action -> viewObservable.notifyAllObservers(ViewObserver::onExitButtonPressed)));

        return jMenuBar;
    }

    private void createDialogWindow(String windowLabel, String text) {
        final JDialog jDialog = new JDialog(window, windowLabel);
        final JLabel jLabel = new JLabel(text);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);

        jDialog.add(jLabel);
        jDialog.setSize(350, 80);
        jDialog.setResizable(false);
        jDialog.setModal(true);
        jDialog.setVisible(true);
    }

    private JMenuItem createMenuItem(String title, ActionListener actionListener) {
        final JMenuItem jMenuItem = new JMenuItem(title);
        jMenuItem.addActionListener(actionListener);
        jMenuItem.setPreferredSize(new Dimension(title.length() * 10, 20));

        return jMenuItem;
    }

    private JPanel createWindowTopPanel() {
        final JPanel jPanel = new JPanel();
        final FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(20);
        jPanel.setLayout(flowLayout);
        jPanel.add(minesCountLabel, FlowLayout.LEFT);
        jPanel.add(resetButton, FlowLayout.CENTER);
        jPanel.add(timerLabel, FlowLayout.RIGHT);

        return jPanel;
    }

    private JButton createResetButton() {
        final JButton jButton = new JButton(ButtonType.SMILE.getImageIcon());
        jButton.setPreferredSize(new Dimension(ButtonType.SMILE.getImageIcon().getIconWidth(), ButtonType.SMILE.getImageIcon().getIconHeight()));
        jButton.addActionListener(action -> viewObservable.notifyAllObservers(ViewObserver::onResetButtonPressed));

        return jButton;
    }

    private JLabel createTopPanelLabel() {
        final JLabel jLabel = new JLabel();
        jLabel.setFont(new Font("Arial", Font.BOLD, 20));
        jLabel.setPreferredSize(new Dimension(50, 30));
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setOpaque(true);
        jLabel.setBackground(new Color(222, 222, 222));
        jLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
        jLabel.setForeground(Color.RED);

        return jLabel;
    }

    private JPanel createFieldPanel() {
        final JPanel jPanel = new JPanel();
        final GridLayout layout = new GridLayout(fieldHeight, fieldWidth);
        final MouseAdapter mouseAdapter = createFieldButtonMouseAdapter();
        layout.preferredLayoutSize(jPanel);
        jPanel.setLayout(layout);

        doActionForEachCell((x, y) -> {
            field[x][y] = createFieldButton(x, y, mouseAdapter);
            jPanel.add(field[x][y]);
        });
        return jPanel;
    }

    private MouseAdapter createFieldButtonMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JButton source = ((JButton) e.getSource());
                int i = source.getY() / ButtonType.FIELD_ICON_SIZE;
                int j = source.getX() / ButtonType.FIELD_ICON_SIZE;

                View.this.viewObservable.notifyAllObservers((ViewObserver observer) ->
                        observer.onButtonPressedAtCoordinates(i, j, ClickType.of(e.getButton())));
            }
        };
    }

    private JButton createFieldButton(int x, int y, MouseAdapter mouseAdapter) {
        final JButton jButton = new JButton(ButtonType.CLOSED.getImageIcon());
        jButton.setBorder(null);
        jButton.setLocation(x, y);
        jButton.addMouseListener(mouseAdapter);
        return jButton;
    }

    private void doActionForEachCell(Callback callback) {
        for (int i = 0; i < fieldHeight; i++) {
            for (int j = 0; j < fieldWidth; j++) {
                callback.call(i, j);
            }
        }
    }

    private JPanel createSettingsPanel(JDialog jDialog) {
        return new SettingsPanel(jDialog, viewObservable).getPanel();
    }
}
