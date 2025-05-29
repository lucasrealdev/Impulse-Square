package com.impulsesquare.data;

public class FPSCounter {
    private int frames = 0;
    private double fps = 0.0;
    private long lastTime = System.nanoTime();

    public void frameRendered() {
        frames++;
        long now = System.nanoTime();
        if (now - lastTime >= 1_000_000_000L) { // 1 segundo
            fps = frames * 1_000_000_000.0 / (now - lastTime);
            frames = 0;
            lastTime = now;
        }
    }

    public double getFPS() {
        return fps;
    }
} 