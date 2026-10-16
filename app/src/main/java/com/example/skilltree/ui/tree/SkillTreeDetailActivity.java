package com.example.skilltree.ui.tree;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.skilltree.data.repository.SkillTreeRepository;
import com.example.skilltree.model.Skill;
import com.example.skilltree.model.SkillStatus;
import com.example.skilltree.model.SkillTree;
import com.example.skilltree.ui.tree.dialog.SkillDetailDialog;
import com.example.skilltree.ui.tree.view.SkillTreeView;

public class SkillTreeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TREE_ID = "extra_tree_id";

    private long treeId = -1;
    private SkillTree skillTree;
    private SkillTreeRepository repository;

    private ImageButton btnBackDetail;
    private ImageView ivHeaderIcon;
    private TextView tvDetailTitle;
    private TextView tvDetailSubtitle;
    private TextView tvDetailXpHeader;
    private ProgressBar pbDetailProgress;
    private TextView tvDetailProgressPercent;

    private SkillTreeView skillTreeView;
    private ImageButton btnFitScreen;
    private LinearLayout btnCenterTarget;

    private LinearLayout cardSelectionPreview;
    private ImageView ivPreviewIcon;
    private TextView tvPreviewTitle;
    private TextView tvPreviewDescription;
    private TextView btnViewDetails;

    private Skill currentSelectedSkill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_tree_detail);

        treeId = getIntent().getLongExtra(EXTRA_TREE_ID, -1);
        repository = SkillTreeRepository.getInstance(this);

        initViews();
        setupListeners();
        loadTreeData();
    }

    private void initViews() {
        btnBackDetail = findViewById(R.id.btnBackDetail);
        ivHeaderIcon = findViewById(R.id.ivHeaderIcon);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailSubtitle = findViewById(R.id.tvDetailSubtitle);
        tvDetailXpHeader = findViewById(R.id.tvDetailXpHeader);
        pbDetailProgress = findViewById(R.id.pbDetailProgress);
        tvDetailProgressPercent = findViewById(R.id.tvDetailProgressPercent);

        skillTreeView = findViewById(R.id.skillTreeView);
        btnFitScreen = findViewById(R.id.btnFitScreen);
        btnCenterTarget = findViewById(R.id.btnCenterTarget);

        cardSelectionPreview = findViewById(R.id.cardSelectionPreview);
        ivPreviewIcon = findViewById(R.id.ivPreviewIcon);
        tvPreviewTitle = findViewById(R.id.tvPreviewTitle);
        tvPreviewDescription = findViewById(R.id.tvPreviewDescription);
        btnViewDetails = findViewById(R.id.btnViewDetails);
    }

    private void setupListeners() {
        btnBackDetail.setOnClickListener(v -> finish());

        btnFitScreen.setOnClickListener(v -> skillTreeView.fitToScreen());
        btnCenterTarget.setOnClickListener(v -> skillTreeView.centerOnSelectedOrRoot());

        skillTreeView.setOnSkillSelectedListener(skill -> {
            currentSelectedSkill = skill;
            updatePreviewCard(skill);
        });

        View.OnClickListener openDetailsListener = v -> {
            if (currentSelectedSkill != null) {
                openSkillDialog(currentSelectedSkill);
            }
        };

        btnViewDetails.setOnClickListener(openDetailsListener);
        cardSelectionPreview.setOnClickListener(openDetailsListener);
    }

    private void loadTreeData() {
        if (treeId != -1) {
            skillTree = repository.getTreeById(treeId);
        }

        if (skillTree == null) {
            Toast.makeText(this, "Árvore de Habilidades não encontrada.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvDetailTitle.setText(skillTree.getTitle());
        tvDetailSubtitle.setText(skillTree.getDescription() != null ? skillTree.getDescription() : "Trilha de Aprendizagem");

        updateHeaderProgress();

        skillTreeView.setSkillTree(skillTree);

        if (skillTree.getSkills() != null && !skillTree.getSkills().isEmpty()) {
            currentSelectedSkill = skillTree.getSkills().get(0);
            updatePreviewCard(currentSelectedSkill);
        }
    }

    private void updateHeaderProgress() {
        if (skillTree == null || skillTree.getSkills() == null) return;

        int total = skillTree.getSkills().size();
        int completed = 0;
        int earnedXp = 0;
        int totalXp = 0;

        for (Skill s : skillTree.getSkills()) {
            totalXp += s.getXpReward();
            if (s.getStatus() == SkillStatus.COMPLETED) {
                completed++;
                earnedXp += s.getXpReward();
            }
        }

        int percent = total > 0 ? (int) Math.round((completed * 100.0) / total) : 0;
        pbDetailProgress.setProgress(percent);
        tvDetailProgressPercent.setText(percent + "%");
        tvDetailXpHeader.setText("⭐ " + earnedXp + " / " + totalXp + " XP");
    }

    private void updatePreviewCard(Skill skill) {
        if (skill == null) {
            cardSelectionPreview.setVisibility(View.GONE);
            return;
        }

        cardSelectionPreview.setVisibility(View.VISIBLE);
        tvPreviewTitle.setText(skill.getName());
        tvPreviewDescription.setText(skill.getDescription() != null ? skill.getDescription() : "");

        if (skill.getStatus() == SkillStatus.COMPLETED) {
            btnViewDetails.setText("✓ Concluído");
        } else if (skill.getStatus() == SkillStatus.AVAILABLE) {
            btnViewDetails.setText("→ Ver detalhes");
        } else {
            btnViewDetails.setText("🔒 Bloqueada");
        }
    }

    private void openSkillDialog(Skill skill) {
        SkillDetailDialog dialog = SkillDetailDialog.newInstance(skill, skillTree);
        dialog.setOnSkillActionListener(completedSkill -> {
            boolean success = repository.completeSkill(skillTree, completedSkill.getId());
            if (success) {
                Toast.makeText(SkillTreeDetailActivity.this,
                        "Parabéns! +" + completedSkill.getXpReward() + " XP conquistados!",
                        Toast.LENGTH_SHORT).show();

                // Reload tree from DB to get refreshed statuses
                skillTree = repository.getTreeById(treeId);
                skillTreeView.setSkillTree(skillTree);

                // Re-find selected skill
                for (Skill s : skillTree.getSkills()) {
                    if (s.getId() == completedSkill.getId()) {
                        currentSelectedSkill = s;
                        break;
                    }
                }

                updateHeaderProgress();
                updatePreviewCard(currentSelectedSkill);
            }
        });
        dialog.show(getSupportFragmentManager(), "skill_detail");
    }
}
