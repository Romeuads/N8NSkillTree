package com.example.skilltree.data.repository;

import android.content.Context;

import com.example.skilltree.data.database.SkillTreeDbHelper;
import com.example.skilltree.model.Skill;
import com.example.skilltree.model.SkillStatus;
import com.example.skilltree.model.SkillTree;
import com.example.skilltree.model.UserProfile;
import com.example.skilltree.util.DependencyGraphEngine;

import java.util.List;

public class SkillTreeRepository {

    private static SkillTreeRepository instance;
    private final SkillTreeDbHelper dbHelper;

    private SkillTreeRepository(Context context) {
        this.dbHelper = SkillTreeDbHelper.getInstance(context);
    }

    public static synchronized SkillTreeRepository getInstance(Context context) {
        if (instance == null) {
            instance = new SkillTreeRepository(context.getApplicationContext());
        }
        return instance;
    }

    public List<SkillTree> getAllTrees() {
        return dbHelper.getAllSkillTrees();
    }

    public SkillTree getTreeById(long treeId) {
        return dbHelper.getSkillTreeById(treeId);
    }

    public long saveSkillTree(SkillTree tree) {
        DependencyGraphEngine.recalculateStatuses(tree);
        return dbHelper.insertSkillTree(tree);
    }

    public boolean completeSkill(SkillTree tree, int skillId) {
        if (tree == null || tree.getSkills() == null) return false;

        Skill target = null;
        for (Skill s : tree.getSkills()) {
            if (s.getId() == skillId) {
                target = s;
                break;
            }
        }

        if (target == null || target.getStatus() != SkillStatus.AVAILABLE) {
            return false;
        }

        // 1. Mark as completed
        target.setStatus(SkillStatus.COMPLETED);
        dbHelper.updateSkillStatus(tree.getId(), skillId, SkillStatus.COMPLETED);

        // 2. Award XP
        int xpReward = target.getXpReward() > 0 ? target.getXpReward() : 100;
        dbHelper.addXp(xpReward);

        // 3. Recalculate dependent skills
        List<Integer> newlyUnlocked = DependencyGraphEngine.recalculateStatuses(tree);
        for (Integer unlockedId : newlyUnlocked) {
            dbHelper.updateSkillStatus(tree.getId(), unlockedId, SkillStatus.AVAILABLE);
        }

        return true;
    }

    public UserProfile getUserProfile() {
        return dbHelper.getUserProfile();
    }

    public void deleteTree(long treeId) {
        dbHelper.deleteSkillTree(treeId);
    }
}
