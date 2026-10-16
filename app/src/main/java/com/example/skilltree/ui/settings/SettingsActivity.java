package com.example.skilltree.ui.settings;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.skilltree.data.api.BuiltInTreeTemplates;
import com.example.skilltree.data.api.TreeGenerationService;
import com.example.skilltree.data.repository.SkillTreeRepository;
import com.example.skilltree.model.SkillTree;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton btnBackSettings;
    private TextInputEditText etWebhookUrl;
    private MaterialButton btnSaveSettings;
    private MaterialButton btnResetDefaultUrl;
    private MaterialButton btnSeedDemoTrees;

    private static final String DEFAULT_EMULATOR_URL = "http://10.0.2.2:5678/webhook/generate-skilltree";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        loadSettings();
        setupListeners();
    }

    private void initViews() {
        btnBackSettings = findViewById(R.id.btnBackSettings);
        etWebhookUrl = findViewById(R.id.etWebhookUrl);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);
        btnResetDefaultUrl = findViewById(R.id.btnResetDefaultUrl);
        btnSeedDemoTrees = findViewById(R.id.btnSeedDemoTrees);
    }

    private void loadSettings() {
        String saved = TreeGenerationService.getSavedWebhookUrl(this);
        etWebhookUrl.setText(saved);
    }

    private void setupListeners() {
        btnBackSettings.setOnClickListener(v -> finish());

        btnSaveSettings.setOnClickListener(v -> {
            String url = etWebhookUrl.getText() != null ? etWebhookUrl.getText().toString().trim() : "";
            TreeGenerationService.saveWebhookUrl(this, url);
            Toast.makeText(this, "Configuração do n8n salva com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnResetDefaultUrl.setOnClickListener(v -> {
            etWebhookUrl.setText(DEFAULT_EMULATOR_URL);
            Toast.makeText(this, "URL padrão do n8n no emulador definida.", Toast.LENGTH_SHORT).show();
        });

        btnSeedDemoTrees.setOnClickListener(v -> {
            SkillTreeRepository repo = SkillTreeRepository.getInstance(this);

            SkillTree androidTree = BuiltInTreeTemplates.getTemplateForTheme(
                    "Desenvolvimento Android", "Intermediário", "Criar apps acadêmicos em Java", "1 hora/dia");
            repo.saveSkillTree(androidTree);

            SkillTree dbTree = BuiltInTreeTemplates.getTemplateForTheme(
                    "Banco de Dados", "Iniciante", "Aprender modelagem DER e SQL", "45 min/dia");
            repo.saveSkillTree(dbTree);

            Toast.makeText(this, "Árvores de demonstração carregadas com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
