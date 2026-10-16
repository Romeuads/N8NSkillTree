package com.example.skilltree.model;

import java.io.Serializable;

public class UserProfile implements Serializable {
    private int totalXp;
    private int completedSkillsCount;
    private int activeTreesCount;

    public UserProfile() {
        this.totalXp = 0;
        this.completedSkillsCount = 0;
        this.activeTreesCount = 0;
    }

    public UserProfile(int totalXp, int completedSkillsCount, int activeTreesCount) {
        this.totalXp = totalXp;
        this.completedSkillsCount = completedSkillsCount;
        this.activeTreesCount = activeTreesCount;
    }

    public int getTotalXp() {
        return totalXp;
    }

    public void setTotalXp(int totalXp) {
        this.totalXp = totalXp;
    }

    public int getCompletedSkillsCount() {
        return completedSkillsCount;
    }

    public void setCompletedSkillsCount(int completedSkillsCount) {
        this.completedSkillsCount = completedSkillsCount;
    }

    public int getActiveTreesCount() {
        return activeTreesCount;
    }

    public void setActiveTreesCount(int activeTreesCount) {
        this.activeTreesCount = activeTreesCount;
    }

    public int getLevel() {
        return 1 + (totalXp / 250);
    }

    public int getXpInCurrentLevel() {
        return totalXp % 250;
    }

    public int getXpRequiredForNextLevel() {
        return 250;
    }

    public float getLevelProgressRatio() {
        return (float) getXpInCurrentLevel() / (float) getXpRequiredForNextLevel();
    }

    public String getRankTitle() {
        int level = getLevel();
        if (level <= 2) return "Novato dos Códigos";
        if (level <= 4) return "Aprendiz Autodidata";
        if (level <= 7) return "Desenvolvedor Explorer";
        if (level <= 10) return "Arquiteto de Habilidades";
        return "Mestre Lendário da SkillTree";
    }
}
