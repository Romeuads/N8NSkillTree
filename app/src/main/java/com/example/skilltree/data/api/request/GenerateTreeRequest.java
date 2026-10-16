package com.example.skilltree.data.api.request;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class GenerateTreeRequest implements Serializable {

    @SerializedName("theme")
    private String theme;

    @SerializedName("level")
    private String level;

    @SerializedName("objective")
    private String objective;

    @SerializedName("daily_time")
    private String dailyTime;

    public GenerateTreeRequest() {}

    public GenerateTreeRequest(String theme, String level, String objective, String dailyTime) {
        this.theme = theme;
        this.level = level;
        this.objective = objective;
        this.dailyTime = dailyTime;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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
}
