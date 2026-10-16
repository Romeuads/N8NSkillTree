package com.example.skilltree.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SkillTree implements Serializable {
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("target_level")
    private String targetLevel;

    @SerializedName("objective")
    private String objective;

    @SerializedName("daily_time")
    private String dailyTime;

    @SerializedName("total_xp")
    private int totalXp;

    @SerializedName("skills")
    private List<Skill> skills;

    private long createdAt;

    public SkillTree() {
        this.skills = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
    }

    public SkillTree(String title, String description, String targetLevel, String objective) {
        this();
        this.title = title;
        this.description = description;
        this.targetLevel = targetLevel;
        this.objective = objective;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(String targetLevel) {
        this.targetLevel = targetLevel;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getDailyTime() {
        return dailyTime;
    }

    public void setDailyTime(String dailyTime) {
        this.dailyTime = dailyTime;
    }

    public int getTotalXp() {
        if (totalXp == 0 && skills != null) {
            int sum = 0;
            for (Skill s : skills) {
                sum += s.getXpReward();
            }
            return sum;
        }
        return totalXp;
    }

    public void setTotalXp(int totalXp) {
        this.totalXp = totalXp;
    }

    public List<Skill> getSkills() {
        if (skills == null) {
            skills = new ArrayList<>();
        }
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getCompletedCount() {
        if (skills == null) return 0;
        int count = 0;
        for (Skill s : skills) {
            if (s.isCompleted()) {
                count++;
            }
        }
        return count;
    }

    public int getAvailableCount() {
        if (skills == null) return 0;
        int count = 0;
        for (Skill s : skills) {
            if (s.isAvailable() || s.isInProgress()) {
                count++;
            }
        }
        return count;
    }

    public int getLockedCount() {
        if (skills == null) return 0;
        int count = 0;
        for (Skill s : skills) {
            if (s.isLocked()) {
                count++;
            }
        }
        return count;
    }

    public int getTotalCount() {
        return skills != null ? skills.size() : 0;
    }

    public int getProgressPercent() {
        int total = getTotalCount();
        if (total == 0) return 0;
        return (int) Math.round(((double) getCompletedCount() / total) * 100.0);
    }

    public Skill getSkillById(int skillId) {
        if (skills == null) return null;
        for (Skill s : skills) {
            if (s.getId() == skillId) {
                return s;
            }
        }
        return null;
    }
}
