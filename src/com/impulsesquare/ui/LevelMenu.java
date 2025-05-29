package com.impulsesquare.scenes;

import com.impulsesquare.data.SaveData;
import com.impulsesquare.resources.ImageUtil;
import com.impulsesquare.utils.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LevelMenu extends JFrame {
    private static final Path LEVELS_DIR = Paths.get("src", "com", "impulsesquare", "levels");
    private static final Color LOCKED_COLOR = new Color(150, 150, 150);
    private static final Color UNLOCKED_COLOR = new Color(76, 175, 80);
    private static final int BUTTON_SIZE = 100;
    private static final int BUTTON_GAP = 20;
    private final SaveData saveData;
    private final JPanel levelsPanel;

    public LevelMenu() {
        saveData = SaveData.loadProgress();
        setTitle("Seleção de Fases");
        setIconImage(ImageUtil.ICON);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        add(GuiUtils.createHeaderPanel("Selecione uma Fase", "Voltar ao Menu", _ -> {
            dispose();
            new Menu().setVisible(true);
        }, null), BorderLayout.NORTH);
        levelsPanel = new JPanel();
        levelsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        createLevelButtons();
        JScrollPane scrollPane = new JScrollPane(levelsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        add(GuiUtils.createInfoPanel("Complete cada fase para desbloquear a próxima!"), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(500, 400));
    }

    private void createLevelButtons() {
        List<String> levelFiles = getLevelFiles();
        int cols = Math.max(1, (int) Math.sqrt(levelFiles.size()));
        levelsPanel.setLayout(new GridLayout(0, cols, BUTTON_GAP, BUTTON_GAP));
        for (String levelFile : levelFiles) {
            levelsPanel.add(createLevelButton(levelFile));
        }
    }

    private JButton createLevelButton(String levelFile) {
        String displayName = levelFile.replace(".dat", "").replace("level_", "Level ").toUpperCase();
        boolean isUnlocked = saveData.isLevelUnlocked(levelFile);
        JButton button = GuiUtils.createStyledButton(displayName, BUTTON_SIZE, isUnlocked ? UNLOCKED_COLOR : LOCKED_COLOR, Color.WHITE, 16);
        if (isUnlocked) {
            button.addActionListener(_ -> {
                dispose();
                new GameScreen(levelFile);
            });
            button.setToolTipText("Clique para jogar!");
        } else {
            button.setEnabled(false);
            button.setToolTipText("Complete a fase anterior para desbloquear");
        }
        return button;
    }

    private List<String> getLevelFiles() {
        File[] files = LEVELS_DIR.toFile().listFiles((_, name) -> name.endsWith(".dat") && !name.equals("save.dat"));
        if (files == null) return List.of();
        return Arrays.stream(files)
                .map(File::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}