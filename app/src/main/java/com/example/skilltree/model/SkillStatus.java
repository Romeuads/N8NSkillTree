package com.example.skilltree.model;

public enum SkillStatus {
    LOCKED("Bloqueada"),
    AVAILABLE("Disponível"),
    IN_PROGRESS("Em Andamento"),
    COMPLETED("Concluída");

    private final String displayName;

    SkillStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
