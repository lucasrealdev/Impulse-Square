package com.impulsesquare.objects;

import com.impulsesquare.data.MapData;
import com.impulsesquare.data.CellData;
import com.impulsesquare.engine.PhysicsEngine;
import com.impulsesquare.resources.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Player {
    private int x, y, originalX, originalY;
    private static final int MOVE_SPEED = 8;
    private Direction currentDirection = null;
    private String color, originalColor;
    private Image characterImage, originalCharacterImage;
    private int imageSize;
    private final boolean[][] collisionGrid;
    private final boolean[][] portalGrid;
    private final Map<Point, CellData> cellMap;
    private final Map<Point, CellData> portalData;
    private final List<Image> explosionFrames;
    private final List<Image> portalFrames;
    private boolean portalReached;
    private boolean isAnimating = false;
    private boolean close = false;
    private int portalIndex = 0;
    private boolean isInitialFall = true;
    private final PhysicsEngine physics;
    private final ScheduledExecutorService animationExecutor;
    private final Rectangle playerBounds = new Rectangle();
    private CellData playerCellData;
    private float scale = 1.0f;
    private static final float PORTAL_ANIMATION_SPEED = 0.05f;
    // Cache de imagens escaladas para cada cor
    private final Map<String, Image> scaledCharacterImages = new HashMap<>();
    // Reutilização de objetos Point
    private final Point directionPoint = new Point();
    private final Point positionPoint = new Point();

    private enum Direction {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);
        final int dx, dy;
        Direction(int dx, int dy) { this.dx = dx; this.dy = dy; }
    }

    public Player(MapData mapData) {
        int cellSize = mapData.cellSize();
        this.collisionGrid = new boolean[mapData.gridColumns()][mapData.gridRows()];
        this.portalGrid = new boolean[mapData.gridColumns()][mapData.gridRows()];

        // Pre-allocate data structures with expected capacity
        int mapSize = mapData.gridColumns() * mapData.gridRows();
        this.cellMap = new HashMap<>(mapSize);
        this.portalData = new HashMap<>(mapSize / 10); // Estimate fewer portals

        initializeMapData(mapData);

        // Agora passamos o colorChangerData para o PhysicsEngine
        this.physics = new PhysicsEngine(
                cellSize,
                mapData.height(),
                collisionGrid,
                portalGrid,
                cellMap,
                portalData
        );

        // Load animations once
        this.explosionFrames = loadAnimationFrames("explosion", 6);
        this.portalFrames = loadAnimationFrames("portal", 8);

        this.animationExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "AnimationThread");
            t.setDaemon(true);
            return t;
        });

        startPortalAnimation();
    }

    private List<Image> loadAnimationFrames(String prefix, int count) {
        String base = "/com/impulsesquare/resources/animations/";
        List<Image> frames = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            String path = base + prefix + "_" + String.format("%02d", i) + ".png";
            frames.add(Objects.requireNonNull(ImageUtil.loadIcon(path)).getImage());
        }
        return Collections.unmodifiableList(frames);
    }

    private void initializeMapData(MapData mapData) {
        for (CellData cd : mapData.cells()) {
            int col = cd.col();
            int row = cd.row();
            Point key = new Point(col, row);
            cellMap.put(key, cd);
            if (cd.collidable() && !cd.isColorChanger()) {
                collisionGrid[col][row] = true;
            }
            if (cd.isPortal()) {
                portalGrid[col][row] = true;
                portalData.put(key, cd);
            }
            if (cd.isCharacter()) {
                this.x = cd.x();
                this.y = cd.y();
                this.originalX = x;
                this.originalY = y;
                this.playerCellData = cd;
                this.imageSize = cd.imageSize();
                cacheCharacterImage("green");
                cacheCharacterImage("blue");
                cacheCharacterImage("red");
                cacheCharacterImage("yellow");
                String replace = cd.textureName().replace("character-", "").replace(".png", "");
                this.characterImage = scaledCharacterImages.get(replace);
                this.originalCharacterImage = characterImage;
                this.color = replace;
                this.originalColor = color;
            }
        }
    }

    private void cacheCharacterImage(String color) {
        String imagePath = "/com/impulsesquare/resources/textures/character-" + color + ".png";
        ImageIcon icon = ImageUtil.loadIcon(imagePath);
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
            scaledCharacterImages.put(color, img);
        }
    }

    public void update(double deltaTime) {
        if (isAnimating) return;
        if (isInitialFall) {
            simulateInitialFall(deltaTime);
        } else if (!portalReached && currentDirection != null) {
            move(deltaTime);
            updatePlayerBounds();
            if (physics.isOnPortal(playerBounds) && !portalReached) {
                portalReached = true;
            }
        }
    }

    private void updatePlayerBounds() {
        playerBounds.setBounds(x, y, imageSize, imageSize);
    }

    private void simulateInitialFall(double deltaTime) {
        currentDirection = Direction.DOWN;
        move(deltaTime);
        if (currentDirection == null) {
            isInitialFall = false;
        }
    }

    private void move(double deltaTime) {
        if (currentDirection == null) return;
        directionPoint.setLocation(currentDirection.dx, currentDirection.dy);
        updatePlayerBounds();
        positionPoint.setLocation(x, y);
        int moveAmount = (int) (MOVE_SPEED * deltaTime * 60); // 60 é o FPS de referência
        if (moveAmount < 1) moveAmount = 1;
        boolean collided = physics.move(playerBounds, positionPoint, directionPoint, moveAmount, this::handleCollision);
        x = positionPoint.x;
        y = positionPoint.y;
        if (collided) currentDirection = null;
    }

    private void handleCollision(CellData block) {
        if (isInitialFall && currentDirection == Direction.DOWN) {
            y = block.y() - imageSize;
        }

        if (block.isPortal()) {
            System.out.println("[DEBUG] Colisão com portal detectada!");
            startPortalEnterAnimation();
            return;
        }

        if (block.isColorChanger()) {
            updateColor(block);
        } else if (!"empty".equals(block.textureName()) && !block.textureName().contains(color)) {
            triggerDeathAnimation();
        }
    }

    private void updateColor(CellData colorChanger) {
        String textureName = colorChanger.textureName();
        if (textureName != null && textureName.startsWith("change_")) {
            // Extract color from the textureName (change_green.png, change_red.png, etc.)
            String newColor = textureName.replace("change_", "").replace(".png", "");

            // Validate that it's one of our supported colors
            if (scaledCharacterImages.containsKey(newColor)) {
                // Atualiza a cor e imagem do personagem usando cache
                this.color = newColor;
                this.characterImage = scaledCharacterImages.get(newColor);
            }
        }
    }

    private void triggerDeathAnimation() {
        if (!isAnimating) {
            isAnimating = true;
            currentDirection = null;

            final int[] frameIndex = {0};
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    if (frameIndex[0] < explosionFrames.size()) {
                        characterImage = explosionFrames.get(frameIndex[0]++);
                        animationExecutor.schedule(this, 50, TimeUnit.MILLISECONDS);
                    } else {
                        resetPlayer();
                        isAnimating = false;
                    }
                }
            };
            animationExecutor.schedule(task, 0, TimeUnit.MILLISECONDS);
        }
    }

    private void startPortalEnterAnimation() {
        if (!isAnimating) {
            isAnimating = true;
            currentDirection = null;

            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    if (scale > 0.1f) {
                        scale -= PORTAL_ANIMATION_SPEED;
                        animationExecutor.schedule(this, 16, TimeUnit.MILLISECONDS);
                    } else {
                        portalReached = true;
                        isAnimating = false;
                        scale = 1.0f;
                    }
                }
            };
            animationExecutor.schedule(task, 0, TimeUnit.MILLISECONDS);
        }
    }

    private void resetPlayer() {
        characterImage = originalCharacterImage;
        color = originalColor;
        x = originalX;
        y = originalY;
        currentDirection = null;
        isInitialFall = true;
        scale = 1.0f;
        isAnimating = false;
    }

    private void startPortalAnimation() {
        animationExecutor.scheduleAtFixedRate(() -> {
            if (!close) {
                portalIndex = (portalIndex + 1) % portalFrames.size();
            }
        }, 0, 200, TimeUnit.MILLISECONDS);
    }

    public Image getCurrentPortalFrame() {
        return portalFrames.get(portalIndex);
    }

    private Direction getRotatedDirection(Direction baseDirection) {
        if (playerCellData == null) return baseDirection;
        
        int rotation = playerCellData.rotation();
        boolean flipped = playerCellData.flipped();
        
        // Se estiver espelhado, inverte esquerda e direita
        if (flipped) {
            if (baseDirection == Direction.LEFT) baseDirection = Direction.RIGHT;
            else if (baseDirection == Direction.RIGHT) baseDirection = Direction.LEFT;
        }
        
        // Ajusta a direção baseado na rotação
        return switch (rotation) {
            case 90 -> switch (baseDirection) {
                case UP -> Direction.RIGHT;
                case RIGHT -> Direction.DOWN;
                case DOWN -> Direction.LEFT;
                case LEFT -> Direction.UP;
            };
            case 180 -> switch (baseDirection) {
                case UP -> Direction.DOWN;
                case RIGHT -> Direction.LEFT;
                case DOWN -> Direction.UP;
                case LEFT -> Direction.RIGHT;
            };
            case 270 -> switch (baseDirection) {
                case UP -> Direction.LEFT;
                case RIGHT -> Direction.UP;
                case DOWN -> Direction.RIGHT;
                case LEFT -> Direction.DOWN;
            };
            default -> baseDirection;
        };
    }

    public void keyPressed(KeyEvent e) {
        // Não permitir entrada de tecla em certas condições
        if (portalReached || isAnimating || currentDirection != null || isInitialFall) {
            return;
        }

        // Define direção base baseada na tecla
        Direction baseDirection = switch (e.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> Direction.UP;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> Direction.DOWN;
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> Direction.LEFT;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> Direction.RIGHT;
            default -> null;
        };

        // Se uma direção válida foi pressionada, ajusta baseado na rotação
        if (baseDirection != null) {
            currentDirection = getRotatedDirection(baseDirection);
        }
    }

    public void reset() {
        close = true;
        resetPlayer();
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public Image getCharacterImage() { return characterImage; }
    public boolean hasReachedPortal() { return portalReached; }
    public int getSize() { return imageSize; }
    public CellData getPlayerCellData() {
        return playerCellData;
    }
}