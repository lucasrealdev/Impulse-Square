package com.impulsesquare.resources;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageUtil {

    private static final Logger LOGGER = Logger.getLogger(ImageUtil.class.getName());

    // Imagens principais pré-carregadas
    public static final ImageIcon BACKGROUND_MENU = load("/com/impulsesquare/resources/images/background_menu.png");
    public static final ImageIcon BACKGROUND_BLUE = load("/com/impulsesquare/resources/images/background-blue.jpg");
    public static final ImageIcon BACKGROUND_GREEN = load("/com/impulsesquare/resources/images/background-green.jpg");
    public static final ImageIcon BACKGROUND_RED = load("/com/impulsesquare/resources/images/background-red.jpg");
    public static final ImageIcon BACKGROUND_WHITE = load("/com/impulsesquare/resources/images/background-white.png");
    public static final ImageIcon BACKGROUND_BLACK = load("/com/impulsesquare/resources/images/background-black.png");
    public static final ImageIcon BACKGROUND_YELLOW = load("/com/impulsesquare/resources/images/background-yellow.jpg");

    public static final ImageIcon ERASER = load("/com/impulsesquare/resources/images/eraser.png");
    public static final ImageIcon EXPORT = load("/com/impulsesquare/resources/images/export.png");
    public static final ImageIcon FLIP = load("/com/impulsesquare/resources/images/flip.png");
    public static final ImageIcon ROTATE = load("/com/impulsesquare/resources/images/rotate.png");
    public static final ImageIcon SELECTED = load("/com/impulsesquare/resources/images/selected.png");
    public static final ImageIcon TUTORIAL_MENU = load("/com/impulsesquare/resources/images/tutorial_menu.png");
    public static final Image ICON = load("/com/impulsesquare/resources/images/logo.png").getImage();
    public static final Image BUILD = load("/com/impulsesquare/resources/images/build.png").getImage();

    // Texturas dinâmicas (cacheadas)
    private static final Map<String, ImageIcon> textures = new TreeMap<>();

    private static ImageIcon load(String path) {
        return new ImageIcon(Objects.requireNonNull(ImageUtil.class.getResource(path)));
    }

    // Metodo público para carregar imagens de forma avulsa.
    public static ImageIcon loadIcon(String path) {
        var url = ImageUtil.class.getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        } else {
            return null;
        }
    }

    // Inicializa e carrega todas as texturas da pasta "textures".
    public static void loadTextureResources() {
        try {
            var texturesURL = ImageUtil.class.getClassLoader().getResources("com/impulsesquare/resources/textures");
            while (texturesURL.hasMoreElements()) {
                processTextureResource(texturesURL.nextElement());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading texture resources", e);
        }
    }

    private static void processTextureResource(URL url) throws IOException {
        try {
            if ("file".equals(url.getProtocol())) {
                loadTexturesFromDirectory(url);
            } else if ("jar".equals(url.getProtocol())) {
                loadTexturesFromJar(url);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing texture resource", e);
        }
    }

    private static void loadTexturesFromDirectory(URL url) throws Exception {
        Path path = Paths.get(url.toURI());
        try (var paths = Files.walk(path)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".png"))
                    .forEach(p -> loadTexture("/com/impulsesquare/resources/textures/" + p.getFileName()));
        }
    }

    private static void loadTexturesFromJar(URL url) throws Exception {
        String jarPath = url.getPath().substring(5, url.getPath().indexOf("!"));
        try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8))) {
            jar.stream()
                    .filter(e -> e.getName().startsWith("com/impulsesquare/resources/textures") && e.getName().endsWith(".png"))
                    .forEach(e -> loadTexture("/" + e.getName()));
        }
    }

    private static void loadTexture(String resourcePath) {
        ImageIcon icon = loadIcon(resourcePath);
        if (icon != null) {
            String name = extractTextureName(resourcePath);
            textures.put(name, icon);
        }
    }

    private static String extractTextureName(String resourcePath) {
        return resourcePath.substring(
                resourcePath.lastIndexOf('/') + 1,
                resourcePath.length() - 4
        );
    }

    // Recupera todas as texturas carregadas (caso precise exibir tudo no SceneBuilder por exemplo).
    public static Map<String, ImageIcon> getAllTextures() {
        return Collections.unmodifiableMap(textures);
    }

    public static String extractImagePath(ImageIcon image) {
        String description = image.getDescription();

        if (description != null) {
            // Verifica se o caminho é um caminho de arquivo local
            if (description.startsWith("file:")) {
                String path = description.substring(5);

                return path.substring(path.indexOf("/com/impulsesquare/resources"));
            }
            else if (description.startsWith("jar:file:")) {

                String path = description.substring(9);  // Remover "jar:file:"
                int exclamationIndex = path.indexOf("!");
                if (exclamationIndex != -1) {
                    path = path.substring(exclamationIndex + 2);  // Remove a parte do JAR "!" e a barra
                }
                return path.substring(path.indexOf("/com/impulsesquare/resources"));
            }
        }
        return description;  // Caso não seja um caminho esperado, retornar o original
    }

    public static Image mirror(Image image) {
        ImageIcon icon = new ImageIcon(image); // força carregamento
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        BufferedImage mirrored = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mirrored.createGraphics();
        g2d.drawImage(image, width, 0, -width, height, null);
        g2d.dispose();

        return mirrored;
    }

    public static Image rotate(Image image, int degrees) {
        ImageIcon icon = new ImageIcon(image); // força carregamento
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        BufferedImage rotated = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        g2d.rotate(Math.toRadians(degrees), width / 2.0, height / 2.0);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotated;
    }
}
