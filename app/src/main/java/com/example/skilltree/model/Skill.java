package com.example.skilltree.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Skill implements Serializable {
    @SerializedName("id")
    private int id;

    private long treeId;

    @SerializedName("name")
    private String name;

    @SerializedName("category")
    private String category;

    @SerializedName("description")
    private String description;

    @SerializedName("level")
    private int level;

    @SerializedName("xp_reward")
    private int xpReward;

    @SerializedName("status")
    private SkillStatus status;

    @SerializedName("prerequisites")
    private List<Integer> prerequisites;

    @SerializedName("study_resources")
    private List<String> studyResources;

    private float layoutX;
    private float layoutY;

    public Skill() {
        this.prerequisites = new ArrayList<>();
        this.studyResources = new ArrayList<>();
        this.status = SkillStatus.LOCKED;
        this.xpReward = 100;
        this.level = 1;
    }

    public Skill(int id, String name, String category, String description, int level,
                 int xpReward, SkillStatus status, List<Integer> prerequisites,
                 List<String> studyResources) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.level = level;
        this.xpReward = xpReward;
        this.status = status;
        this.prerequisites = prerequisites != null ? prerequisites : new ArrayList<>();
        this.studyResources = studyResources != null ? studyResources : new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTreeId() {
        return treeId;
    }

    public void setTreeId(long treeId) {
        this.treeId = treeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXpReward() {
        return xpReward;
    }

    public void setXpReward(int xpReward) {
        this.xpReward = xpReward;
    }

    public SkillStatus getStatus() {
        return status != null ? status : SkillStatus.LOCKED;
    }

    public void setStatus(SkillStatus status) {
        this.status = status;
    }

    public List<Integer> getPrerequisites() {
        if (prerequisites == null) {
            prerequisites = new ArrayList<>();
        }
        return prerequisites;
    }

    public void setPrerequisites(List<Integer> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public List<String> getStudyResources() {
        if (studyResources == null) {
            studyResources = new ArrayList<>();
        }
        return studyResources;
    }

    public void setStudyResources(List<String> studyResources) {
        this.studyResources = studyResources;
    }

    public float getLayoutX() {
        return layoutX;
    }

    public void setLayoutX(float layoutX) {
        this.layoutX = layoutX;
    }

    public float getLayoutY() {
        return layoutY;
    }

    public void setLayoutY(float layoutY) {
        this.layoutY = layoutY;
    }

    public boolean isCompleted() {
        return status == SkillStatus.COMPLETED;
    }

    public boolean isAvailable() {
        return status == SkillStatus.AVAILABLE;
    }

    public boolean isLocked() {
        return status == SkillStatus.LOCKED;
    }

    public boolean isInProgress() {
        return status == SkillStatus.IN_PROGRESS;
    }
}
