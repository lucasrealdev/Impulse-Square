package com.impulsesquare.scenes;

import com.impulsesquare.data.MapData;
import com.impulsesquare.data.SaveData;
import com.impulsesquare.resources.ImageUtil;
import com.impulsesquare.utils.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class GameScreen extends JFrame {
    private static final Path LEVELS_DIR = Paths.get("src", "com", "impulsesquare", "levels");
    private LoadLevels gamePanel;
    private final String currentLevel;
    private final SaveData saveData;
    private Timer fpsTimer;
    private JLabel fpsLabel;

    public GameScreen(String levelFile) {
        super();
        this.currentLevel = levelFile;
        this.saveData = SaveData.loadProgress();
        MapData mapData = loadMapData(levelFile);
        if (mapData == null) return;
        setupGamePanel(mapData);
        setTitle(formatLevelName(levelFile));
        setIconImage(ImageUtil.ICON);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        add(GuiUtils.createHeaderPanel(formatLevelName(levelFile), "Voltar às Fases", _ -> {
            dispose();
            new LevelMenu().setVisible(true);
        }, fpsLabel), BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) { gamePanel.requestFocusInWindow(); }
            @Override
            public void windowClosing(WindowEvent e) {
                if (fpsTimer != null) fpsTimer.stop();
                gamePanel.reset();
                new LevelMenu().setVisible(true);
            }
        });
    }

    private MapData loadMapData(String levelFile) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(LEVELS_DIR.resolve(levelFile).toFile()))) {
            return (MapData) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar fase:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            dispose();
            new LevelMenu().setVisible(true);
            return null;
        }
    }

    private void setupGamePanel(MapData mapData) {
        gamePanel = new LoadLevels(mapData);
        gamePanel.setOnLevelComplete(this::handleLevelComplete);
        int frameWidth = mapData.gridColumns() * mapData.cellSize();
        int frameHeight = mapData.gridRows() * mapData.cellSize();
        gamePanel.setPreferredSize(new Dimension(frameWidth, frameHeight));
        fpsLabel = new JLabel("FPS: 0.0");
        fpsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        fpsLabel.setForeground(Color.WHITE);
        fpsLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        fpsTimer = new Timer(16, _ -> updateFPS());
        fpsTimer.start();
    }

    private String formatLevelName(String levelFile) {
        return levelFile.replace(".dat", "").replace("level_", "Level ").toUpperCase();
    }

    private void updateFPS() {
        if (gamePanel != null) {
            fpsLabel.setText(String.format("FPS: %.1f", gamePanel.fpsCounter.getFPS()));
        }
    }

    private void handleLevelComplete() {
        List<String> levels = Stream.of(Objects.requireNonNull(LEVELS_DIR.toFile().list((_, name) ->
                        name.endsWith(".dat") && !name.equals("save.dat"))))
            .sorted()
            .toList();
        int currentIndex = levels.indexOf(currentLevel);
        if (currentIndex < levels.size() - 1) {
            String nextLevel = levels.get(currentIndex + 1);
            saveData.unlockLevel(nextLevel);
            loadLevel(nextLevel);
        } else {
            showGameOverScene("Parabéns! Você completou todas as fases!");
        }
    }

    private void showGameOverScene(String message) {
        Container parent = getParent();
        if (parent instanceof JFrame frame) {
            frame.getContentPane().removeAll();
            GameOverScene gameOverScene = new GameOverScene(
                message,
                _ -> {
                    frame.getContentPane().removeAll();
                    String firstLevel = Stream.of(Objects.requireNonNull(LEVELS_DIR.toFile().list((_, name) ->
                        name.endsWith(".dat") && !name.equals("save.dat"))))
                        .sorted()
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Nenhuma fase encontrada!"));
                    GameScreen newGame = new GameScreen(firstLevel);
                    frame.getContentPane().add(newGame);
                    frame.revalidate();
                    frame.repaint();
                },
                _ -> System.exit(0)
            );
            frame.getContentPane().add(gameOverScene);
            frame.revalidate();
            frame.repaint();
        }
    }

    private void loadLevel(String levelFile) {
        if (fpsTimer != null) fpsTimer.stop();
        if (gamePanel != null) gamePanel.reset();
        dispose();
        SwingUtilities.invokeLater(() -> new GameScreen(levelFile).setVisible(true));
    }
}