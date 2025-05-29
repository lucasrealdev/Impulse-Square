package com.impulsesquare.scenes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

public class GameOverScene extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(40, 44, 52);
    private static final Color TEXT_COLOR = new Color(171, 178, 191);
    private static final Color BUTTON_COLOR = new Color(98, 114, 164);
    private static final Color BUTTON_HOVER_COLOR = new Color(82, 139, 255);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 48);
    private static final Font MESSAGE_FONT = new Font("Arial", Font.PLAIN, 24);
    private static final int ANIMATION_DURATION = 1000; // ms
    
    private final String message;
    private final long startTime;
    private float alpha = 0f;
    private final JButton restartButton;
    private final JButton exitButton;

    public GameOverScene(String message, ActionListener restartAction, ActionListener exitAction) {
        this.message = message;
        this.startTime = System.currentTimeMillis();
        setLayout(null); // Usando posicionamento absoluto para maior controle

        // Configurando botões
        restartButton = createStyledButton("Jogar Novamente", restartAction);
        exitButton = createStyledButton("Sair", exitAction);
        
        add(restartButton);
        add(exitButton);

        // Timer para animação de fade in
        Timer animationTimer = new Timer(16, e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            alpha = Math.min(1f, (float) elapsed / ANIMATION_DURATION);
            repaint();
            if (alpha >= 1f) {
                ((Timer) e.getSource()).stop();
            }
        });
        animationTimer.start();
    }

    private JButton createStyledButton(String text, ActionListener action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(BUTTON_HOVER_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(BUTTON_HOVER_COLOR);
                } else {
                    g2d.setColor(BUTTON_COLOR);
                }
                
                g2d.fill(new Rectangle2D.Float(0, 0, getWidth(), getHeight()));
                
                g2d.setColor(TEXT_COLOR);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(text, x, y);
                
                g2d.dispose();
            }
        };
        
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.addActionListener(action);
        button.setPreferredSize(new Dimension(200, 50));
        
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Configurar renderização
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Desenhar fundo
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Aplicar fade in
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        
        // Desenhar título
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(TITLE_FONT);
        FontMetrics titleFm = g2d.getFontMetrics();
        int titleX = (getWidth() - titleFm.stringWidth("Fim de Jogo")) / 2;
        int titleY = getHeight() / 3;
        g2d.drawString("Fim de Jogo", titleX, titleY);
        
        // Desenhar mensagem
        g2d.setFont(MESSAGE_FONT);
        FontMetrics messageFm = g2d.getFontMetrics();
        int messageX = (getWidth() - messageFm.stringWidth(message)) / 2;
        int messageY = titleY + titleFm.getHeight() + 20;
        g2d.drawString(message, messageX, messageY);
        
        // Posicionar botões
        int buttonY = messageY + messageFm.getHeight() + 40;
        restartButton.setBounds((getWidth() - 420) / 2, buttonY, 200, 50);
        exitButton.setBounds((getWidth() + 20) / 2, buttonY, 200, 50);
        
        g2d.dispose();
    }
} 