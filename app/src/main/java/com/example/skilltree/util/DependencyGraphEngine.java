package com.example.skilltree.util;

import com.example.skilltree.model.Skill;
import com.example.skilltree.model.SkillStatus;
import com.example.skilltree.model.SkillTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyGraphEngine {

    public static List<Integer> recalculateStatuses(SkillTree tree) {
        List<Integer> newlyUnlockedSkillIds = new ArrayList<>();
        if (tree == null || tree.getSkills() == null || tree.getSkills().isEmpty()) {
            return newlyUnlockedSkillIds;
        }

        Map<Integer, Skill> skillMap = new HashMap<>();
        for (Skill skill : tree.getSkills()) {
            skillMap.put(skill.getId(), skill);
        }

        boolean changed;
        int passLimit = tree.getSkills().size() + 1;
        int pass = 0;

        do {
            changed = false;
            pass++;

            for (Skill skill : tree.getSkills()) {
                if (skill.getStatus() == SkillStatus.COMPLETED) {
                    continue;
                }

                List<Integer> prereqs = skill.getPrerequisites();
                if (prereqs == null || prereqs.isEmpty()) {
                    if (skill.getStatus() == SkillStatus.LOCKED) {
                        skill.setStatus(SkillStatus.AVAILABLE);
                        newlyUnlockedSkillIds.add(skill.getId());
                        changed = true;
                    }
                    continue;
                }

                boolean allPrereqsCompleted = true;
                for (Integer prereqId : prereqs) {
                    Skill prereqSkill = skillMap.get(prereqId);
                    if (prereqSkill == null || prereqSkill.getStatus() != SkillStatus.COMPLETED) {
                        allPrereqsCompleted = false;
                        break;
                    }
                }

                if (allPrereqsCompleted) {
                    if (skill.getStatus() == SkillStatus.LOCKED) {
                        skill.setStatus(SkillStatus.AVAILABLE);
                        newlyUnlockedSkillIds.add(skill.getId());
                        changed = true;
                    }
                } else {
                    if (skill.getStatus() != SkillStatus.LOCKED) {
                        skill.setStatus(SkillStatus.LOCKED);
                        changed = true;
                    }
                }
            }
        } while (changed && pass < passLimit);

        return newlyUnlockedSkillIds;
    }

    public static void computeTreeLayout(SkillTree tree, float canvasWidth, float startY, float levelSpacing) {
        if (tree == null || tree.getSkills() == null || tree.getSkills().isEmpty()) {
            return;
        }

        List<Skill> skills = tree.getSkills();
        Map<Integer, List<Skill>> levelMap = new HashMap<>();
        int maxLevel = 1;

        for (Skill s : skills) {
            int lvl = s.getLevel() > 0 ? s.getLevel() : 1;
            maxLevel = Math.max(maxLevel, lvl);
            if (!levelMap.containsKey(lvl)) {
                levelMap.put(lvl, new ArrayList<>());
            }
            levelMap.get(lvl).add(s);
        }

        float currentY = startY;
        for (int lvl = 1; lvl <= maxLevel; lvl++) {
            List<Skill> tierSkills = levelMap.get(lvl);
            if (tierSkills == null || tierSkills.isEmpty()) {
                currentY += levelSpacing * 0.7f;
                continue;
            }

            int count = tierSkills.size();
            float spacingX = canvasWidth / (count + 1);

            for (int i = 0; i < count; i++) {
                Skill s = tierSkills.get(i);
                float posX = spacingX * (i + 1);
                s.setLayoutX(posX);
                s.setLayoutY(currentY);
            }

            currentY += levelSpacing;
        }
    }
}
