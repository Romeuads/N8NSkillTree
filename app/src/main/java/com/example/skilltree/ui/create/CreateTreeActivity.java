package com.example.skilltree.ui.create;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.skilltree.data.api.TreeGenerationService;
import com.example.skilltree.data.api.request.GenerateTreeRequest;
import com.example.skilltree.data.repository.SkillTreeRepository;
import com.example.skilltree.model.SkillTree;
import com.example.skilltree.ui.tree.SkillTreeDetailActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

public class CreateTreeActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ScrollView scrollForm;
    private TextInputEditText etTheme;
    private TextInputEditText etObjective;

    private Chip chipAndroid;
    private Chip chipPython;
    private Chip chipDb;
    private Chip chipJava;
    private Chip chipEnglish;
    private Chip chipNetworks;

    private ChipGroup chipGroupLevel;
    private ChipGroup chipGroupTime;
    private MaterialButton btnGenerateTree;

    private LinearLayout layoutLoading;
    private TextView tvLoadingStep;

    private TreeGenerationService generationService;
    private SkillTreeRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tree);

        generationService = new TreeGenerationService(this);
        repository = SkillTreeRepository.getInstance(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        scrollForm = findViewById(R.id.scrollForm);
        etTheme = findViewById(R.id.etTheme);
        etObjective = findViewById(R.id.etObjective);

        chipAndroid = findViewById(R.id.chipAndroid);
        chipPython = findViewById(R.id.chipPython);
        chipDb = findViewById(R.id.chipDb);
        chipJava = findViewById(R.id.chipJava);
        chipEnglish = findViewById(R.id.chipEnglish);
        chipNetworks = findViewById(R.id.chipNetworks);

        chipGroupLevel = findViewById(R.id.chipGroupLevel);
        chipGroupTime = findViewById(R.id.chipGroupTime);
        btnGenerateTree = findViewById(R.id.btnGenerateTree);

        layoutLoading = findViewById(R.id.layoutLoading);
        tvLoadingStep = findViewById(R.id.tvLoadingStep);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Suggestion Chips
        View.OnClickListener chipListener = v -> {
            if (v instanceof Chip) {
                Chip chip = (Chip) v;
                etTheme.setText(chip.getText().toString());
                etTheme.setSelection(etTheme.getText() != null ? etTheme.getText().length() : 0);
            }
        };

        chipAndroid.setOnClickListener(chipListener);
        chipPython.setOnClickListener(chipListener);
        chipDb.setOnClickListener(chipListener);
        chipJava.setOnClickListener(chipListener);
        chipEnglish.setOnClickListener(chipListener);
        chipNetworks.setOnClickListener(chipListener);

        btnGenerateTree.setOnClickListener(v -> submitGeneration());
    }

    private void submitGeneration() {
        String theme = etTheme.getText() != null ? etTheme.getText().toString().trim() : "";
        if (theme.isEmpty()) {
            etTheme.setError("Por favor informe o tema que deseja aprender");
            etTheme.requestFocus();
            return;
        }

        String level = "Iniciante";
        int checkedLevelId = chipGroupLevel.getCheckedChipId();
        if (checkedLevelId == R.id.chipLevelIntermediate) {
            level = "Intermediário";
        } else if (checkedLevelId == R.id.chipLevelAdvanced) {
            level = "Avançado";
        }

        String objective = etObjective.getText() != null ? etObjective.getText().toString().trim() : "";

        String dailyTime = "1 hora/dia";
        int checkedTimeId = chipGroupTime.getCheckedChipId();
        if (checkedTimeId == R.id.chipTime30) {
            dailyTime = "30 min/dia";
        } else if (checkedTimeId == R.id.chipTime120) {
            dailyTime = "2 horas/dia";
        }

        // Show loading animation
        showLoadingState(theme);

        GenerateTreeRequest request = new GenerateTreeRequest(theme, level, objective, dailyTime);

        generationService.generateSkillTree(request, new TreeGenerationService.GenerationCallback() {
            @Override
            public void onSuccess(SkillTree skillTree, boolean isFromN8n) {
                // Save tree to SQLite database
                long treeId = repository.saveSkillTree(skillTree);
                skillTree.setId(treeId);

                hideLoadingState();

                String feedback = isFromN8n
                        ? "SkillTree gerada via n8n e IA com sucesso!"
                        : "SkillTree gerada com sucesso!";
                Toast.makeText(CreateTreeActivity.this, feedback, Toast.LENGTH_SHORT).show();

                // Open SkillTreeDetailActivity
                Intent intent = new Intent(CreateTreeActivity.this, SkillTreeDetailActivity.class);
                intent.putExtra(SkillTreeDetailActivity.EXTRA_TREE_ID, treeId);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                hideLoadingState();
                Toast.makeText(CreateTreeActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoadingState(String theme) {
        scrollForm.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.VISIBLE);
        tvLoadingStep.setText("Conectando à IA para analisar '" + theme + "' e estruturar a árvore...");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (layoutLoading.getVisibility() == View.VISIBLE) {
                tvLoadingStep.setText("Calculando grafo de dependências, níveis e pré-requisitos...");
            }
        }, 1200);
    }

    private void hideLoadingState() {
        layoutLoading.setVisibility(View.GONE);
        scrollForm.setVisibility(View.VISIBLE);
    }
}
