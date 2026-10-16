package com.example;

import com.example.skilltree.model.Skill;
import com.example.skilltree.model.SkillStatus;
import com.example.skilltree.model.SkillTree;
import com.example.skilltree.util.DependencyGraphEngine;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DependencyGraphEngineTest {

    @Test
    public void testUnlockProgression() {
        SkillTree tree = new SkillTree();
        List<Skill> skills = new ArrayList<>();

        // Skill 1: Root
        Skill s1 = new Skill(1, "Fundamentos", "Base", "Desc", 1, 50, SkillStatus.AVAILABLE,
                Collections.emptyList(), Collections.emptyList());

        // Skill 2: Depends on 1
        Skill s2 = new Skill(2, "Variáveis", "Base", "Desc", 2, 75, SkillStatus.LOCKED,
                Collections.singletonList(1), Collections.emptyList());

        // Skill 3: Depends on 2
        Skill s3 = new Skill(3, "Condicionais", "Controle", "Desc", 3, 100, SkillStatus.LOCKED,
                Collections.singletonList(2), Collections.emptyList());

        skills.add(s1);
        skills.add(s2);
        skills.add(s3);
        tree.setSkills(skills);

        // Initial calculation: s1 should be AVAILABLE, s2 and s3 LOCKED
        DependencyGraphEngine.recalculateStatuses(tree);
        Assert.assertEquals(SkillStatus.AVAILABLE, s1.getStatus());
        Assert.assertEquals(SkillStatus.LOCKED, s2.getStatus());
        Assert.assertEquals(SkillStatus.LOCKED, s3.getStatus());

        // User completes s1
        s1.setStatus(SkillStatus.COMPLETED);
        List<Integer> newlyUnlocked = DependencyGraphEngine.recalculateStatuses(tree);

        // s2 should now be unlocked to AVAILABLE, s3 remains LOCKED
        Assert.assertEquals(SkillStatus.COMPLETED, s1.getStatus());
        Assert.assertEquals(SkillStatus.AVAILABLE, s2.getStatus());
        Assert.assertEquals(SkillStatus.LOCKED, s3.getStatus());
        Assert.assertTrue(newlyUnlocked.contains(2));

        // User completes s2
        s2.setStatus(SkillStatus.COMPLETED);
        newlyUnlocked = DependencyGraphEngine.recalculateStatuses(tree);

        // s3 should now be unlocked to AVAILABLE
        Assert.assertEquals(SkillStatus.COMPLETED, s2.getStatus());
        Assert.assertEquals(SkillStatus.AVAILABLE, s3.getStatus());
        Assert.assertTrue(newlyUnlocked.contains(3));
    }
}
