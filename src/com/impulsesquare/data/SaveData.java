package com.impulsesquare.data;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Path SAVE_PATH = Paths.get("src", "com", "impulsesquare", "fases", "save.dat");
    
    private Set<String> unlockedLevels;
    
    public SaveData() {
        this.unlockedLevels = new HashSet<>();
        // Sempre começa com a primeira fase desbloqueada
        this.unlockedLevels.add("fase_1.dat");
    }
    
    public boolean isLevelUnlocked(String levelName) {
        return unlockedLevels.contains(levelName);
    }
    
    public void unlockLevel(String levelName) {
        unlockedLevels.add(levelName);
        saveProgress();
    }
    
    public static SaveData loadProgress() {
        if (!SAVE_PATH.toFile().exists()) {
            SaveData newSave = new SaveData();
            newSave.saveProgress();
            return newSave;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_PATH.toFile()))) {
            return (SaveData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new SaveData();
        }
    }
    
    public void saveProgress() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_PATH.toFile()))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 