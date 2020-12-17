package view;

import common.GameDifficulty;
import scores.PlayerScore;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class HighScoresPane {
    private final JTabbedPane jTabbedPane;

    HighScoresPane(Map<GameDifficulty, List<PlayerScore>> scoresListMap) {
        jTabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

        jTabbedPane.add("Easy mode", getScoresPanel(scoresListMap.get(GameDifficulty.EASY)));
        jTabbedPane.add("Medium mode", getScoresPanel(scoresListMap.get(GameDifficulty.MEDIUM)));
        jTabbedPane.add("Hard mode", getScoresPanel(scoresListMap.get(GameDifficulty.HARD)));
    }

    protected JTabbedPane getTabbedPane() {
        return jTabbedPane;
    }

    private JPanel getScoresPanel(List<PlayerScore> scores) {
        final JPanel jPanel = new JPanel();

        if (scores.size() == 0) {
            jPanel.add(new JLabel("No high scores yet!"));
        } else {
            final JTable jTable = createScoresTable(scores);
            final JScrollPane jScrollPane = new JScrollPane(jTable);

            jScrollPane.setPreferredSize(new Dimension(275, 183));
            jPanel.add(jScrollPane);
        }

        return jPanel;
    }

    private JTable createScoresTable(List<PlayerScore> scores) {
        final String[] columns = {"#", "Time (sec)", "Player name"};
        final JTable jTable = new JTable(getScoresDataTable(scores), columns);

        jTable.getColumnModel().getColumn(0).setMaxWidth(20);
        jTable.getColumnModel().getColumn(1).setMaxWidth(105);
        jTable.getColumnModel().getColumn(2).setMaxWidth(150);
        jTable.setEnabled(false);

        return jTable;
    }

    private String[][] getScoresDataTable(List<PlayerScore> scores) {
        final String[][] scoresData = new String[scores.size()][3];

        for (int i = 0; i < scores.size(); i++) {
            scoresData[i][0] = String.valueOf(i + 1);
            scoresData[i][1] = String.valueOf(scores.get(i).getScore());
            scoresData[i][2] = scores.get(i).getPlayerName();
        }

        return scoresData;
    }
}
