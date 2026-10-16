package com.example.skilltree.ui.profile;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.skilltree.data.repository.SkillTreeRepository;
import com.example.skilltree.model.UserProfile;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton btnBackProfile;
    private TextView tvProfileRankTitle;
    private TextView tvProfileLevelBadge;
    private TextView tvProfileTotalXp;
    private TextView tvProfileCompletedCount;

    private SkillTreeRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        repository = SkillTreeRepository.getInstance(this);

        initViews();
        loadProfileData();
    }

    private void initViews() {
        btnBackProfile = findViewById(R.id.btnBackProfile);
        tvProfileRankTitle = findViewById(R.id.tvProfileRankTitle);
        tvProfileLevelBadge = findViewById(R.id.tvProfileLevelBadge);
        tvProfileTotalXp = findViewById(R.id.tvProfileTotalXp);
        tvProfileCompletedCount = findViewById(R.id.tvProfileCompletedCount);

        btnBackProfile.setOnClickListener(v -> finish());
    }

    private void loadProfileData() {
        UserProfile profile = repository.getUserProfile();
        int totalXp = profile != null ? profile.getTotalXp() : 0;
        int level = 1 + (totalXp / 250);
        int completedCount = profile != null ? profile.getCompletedSkillsCount() : 0;

        tvProfileTotalXp.setText(String.valueOf(totalXp));
        tvProfileCompletedCount.setText(String.valueOf(completedCount));
        tvProfileLevelBadge.setText("Nível " + level + " de Estudante");

        if (level == 1) {
            tvProfileRankTitle.setText("Novato dos Códigos");
        } else if (level <= 3) {
            tvProfileRankTitle.setText("Aprendiz Autodidata");
        } else if (level <= 6) {
            tvProfileRankTitle.setText("Desenvolvedor Habilidoso");
        } else {
            tvProfileRankTitle.setText("Mestre Arquiteto de Software");
        }
    }
}
