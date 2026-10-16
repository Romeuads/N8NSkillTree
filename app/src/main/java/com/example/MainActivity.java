package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skilltree.data.api.BuiltInTreeTemplates;
import com.example.skilltree.data.repository.SkillTreeRepository;
import com.example.skilltree.model.SkillTree;
import com.example.skilltree.model.UserProfile;
import com.example.skilltree.ui.create.CreateTreeActivity;
import com.example.skilltree.ui.home.SkillTreeCardAdapter;
import com.example.skilltree.ui.profile.ProfileActivity;
import com.example.skilltree.ui.settings.SettingsActivity;
import com.example.skilltree.ui.tree.SkillTreeDetailActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SkillTreeRepository repository;
    private SkillTreeCardAdapter adapter;

    private TextView tvPlayerLevelBadge;
    private TextView tvPlayerRankTitle;
    private TextView tvPlayerTotalXp;
    private ProgressBar pbLevelProgress;
    private TextView tvLevelProgressText;
    private TextView tvCompletedNodesStat;
    private TextView tvTreesCount;

    private RecyclerView rvSkillTrees;
    private LinearLayout layoutEmptyState;
    private ExtendedFloatingActionButton fabNewTree;
    private MaterialButton btnEmptyCreate;
    private ImageView btnSettings;
    private ImageView btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = SkillTreeRepository.getInstance(this);

        initViews();
        setupListeners();
        ensureInitialData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void initViews() {
        tvPlayerLevelBadge = findViewById(R.id.tvPlayerLevelBadge);
        tvPlayerRankTitle = findViewById(R.id.tvPlayerRankTitle);
        tvPlayerTotalXp = findViewById(R.id.tvPlayerTotalXp);
        pbLevelProgress = findViewById(R.id.pbLevelProgress);
        tvLevelProgressText = findViewById(R.id.tvLevelProgressText);
        tvCompletedNodesStat = findViewById(R.id.tvCompletedNodesStat);
        tvTreesCount = findViewById(R.id.tvTreesCount);

        rvSkillTrees = findViewById(R.id.rvSkillTrees);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        fabNewTree = findViewById(R.id.fabNewTree);
        btnEmptyCreate = findViewById(R.id.btnEmptyCreate);
        btnSettings = findViewById(R.id.btnSettings);
        btnProfile = findViewById(R.id.btnProfile);

        rvSkillTrees.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SkillTreeCardAdapter(tree -> {
            Intent intent = new Intent(MainActivity.this, SkillTreeDetailActivity.class);
            intent.putExtra(SkillTreeDetailActivity.EXTRA_TREE_ID, tree.getId());
            startActivity(intent);
        });
        rvSkillTrees.setAdapter(adapter);
    }

    private void setupListeners() {
        View.OnClickListener openCreate = v -> {
            Intent intent = new Intent(MainActivity.this, CreateTreeActivity.class);
            startActivity(intent);
        };
        fabNewTree.setOnClickListener(openCreate);
        btnEmptyCreate.setOnClickListener(openCreate);

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void ensureInitialData() {
        List<SkillTree> existing = repository.getAllTrees();
        if (existing == null || existing.isEmpty()) {
            // Seed initial Academic Tree (Python)
            SkillTree initial = BuiltInTreeTemplates.getTemplateForTheme(
                    "Python", "Iniciante", "Aprender lógica e automação com Python", "1 hora/dia");
            repository.saveSkillTree(initial);
        }
    }

    private void loadDashboardData() {
        // Load User Profile
        UserProfile profile = repository.getUserProfile();
        int totalXp = profile != null ? profile.getTotalXp() : 0;
        int level = 1 + (totalXp / 250);
        int currentLevelXp = totalXp % 250;

        tvPlayerLevelBadge.setText("NÍVEL " + level);
        tvPlayerTotalXp.setText("⚡ " + totalXp + " XP");

        if (level == 1) {
            tvPlayerRankTitle.setText("Novato dos Códigos");
        } else if (level <= 3) {
            tvPlayerRankTitle.setText("Aprendiz Autodidata");
        } else if (level <= 6) {
            tvPlayerRankTitle.setText("Desenvolvedor Habilidoso");
        } else {
            tvPlayerRankTitle.setText("Mestre Arquiteto de Software");
        }

        pbLevelProgress.setMax(250);
        pbLevelProgress.setProgress(currentLevelXp);
        tvLevelProgressText.setText(currentLevelXp + " / 250 XP para o nível " + (level + 1));

        int completedCount = profile != null ? profile.getCompletedSkillsCount() : 0;
        tvCompletedNodesStat.setText(completedCount + " habilidades dominadas");

        // Load Skill Trees
        List<SkillTree> trees = repository.getAllTrees();
        if (trees == null || trees.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            rvSkillTrees.setVisibility(View.GONE);
            tvTreesCount.setText("0 ativas");
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            rvSkillTrees.setVisibility(View.VISIBLE);
            tvTreesCount.setText(trees.size() + (trees.size() == 1 ? " ativa" : " ativas"));
            adapter.setTrees(trees);
        }
    }
}
