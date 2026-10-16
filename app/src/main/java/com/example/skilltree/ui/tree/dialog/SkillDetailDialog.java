package com.example.skilltree.ui.tree.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.R;
import com.example.skilltree.model.Skill;
import com.example.skilltree.model.SkillStatus;
import com.example.skilltree.model.SkillTree;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillDetailDialog extends BottomSheetDialogFragment {

    public interface OnSkillActionListener {
        void onCompleteSkillRequested(Skill skill);
    }

    private Skill skill;
    private SkillTree skillTree;
    private OnSkillActionListener actionListener;

    public static SkillDetailDialog newInstance(Skill skill, SkillTree tree) {
        SkillDetailDialog dialog = new SkillDetailDialog();
        dialog.skill = skill;
        dialog.skillTree = tree;
        return dialog;
    }

    public void setOnSkillActionListener(OnSkillActionListener listener) {
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_skill_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (skill == null) return;

        TextView tvDetailStatusBadge = view.findViewById(R.id.tvDetailStatusBadge);
        TextView tvDetailXpReward = view.findViewById(R.id.tvDetailXpReward);
        TextView tvDetailSkillName = view.findViewById(R.id.tvDetailSkillName);
        TextView tvDetailCategoryTier = view.findViewById(R.id.tvDetailCategoryTier);
        TextView tvDetailDescription = view.findViewById(R.id.tvDetailDescription);

        TextView tvPrereqHeader = view.findViewById(R.id.tvPrereqHeader);
        LinearLayout layoutPrerequisitesList = view.findViewById(R.id.layoutPrerequisitesList);

        TextView tvResourcesHeader = view.findViewById(R.id.tvResourcesHeader);
        LinearLayout layoutResourcesList = view.findViewById(R.id.layoutResourcesList);

        MaterialButton btnCompleteSkill = view.findViewById(R.id.btnCompleteSkill);

        // Bind Data
        tvDetailSkillName.setText(skill.getName());
        tvDetailCategoryTier.setText("Categoria: " + skill.getCategory() + " • Nível " + skill.getLevel());
        tvDetailDescription.setText(skill.getDescription() != null ? skill.getDescription() : "Sem descrição detalhada.");
        tvDetailXpReward.setText("Recompensa: +" + skill.getXpReward() + " XP");

        // Status Badge & Action Button
        SkillStatus status = skill.getStatus();
        if (status == SkillStatus.COMPLETED) {
            tvDetailStatusBadge.setText("CONCLUÍDA ✓");
            tvDetailStatusBadge.setTextColor(Color.parseColor("#00E5FF"));
            btnCompleteSkill.setText("HABILIDADE CONCLUÍDA");
            btnCompleteSkill.setEnabled(false);
            btnCompleteSkill.setBackgroundColor(Color.parseColor("#1B333F"));
            btnCompleteSkill.setTextColor(Color.parseColor("#80FFFFFF"));
        } else if (status == SkillStatus.AVAILABLE) {
            tvDetailStatusBadge.setText("DISPONÍVEL ✦");
            tvDetailStatusBadge.setTextColor(Color.parseColor("#00E5FF"));
            btnCompleteSkill.setText("CONCLUIR HABILIDADE (+" + skill.getXpReward() + " XP)");
            btnCompleteSkill.setEnabled(true);
            btnCompleteSkill.setBackgroundColor(Color.parseColor("#00E5FF"));
            btnCompleteSkill.setTextColor(Color.parseColor("#000000"));
        } else {
            tvDetailStatusBadge.setText("BLOQUEADA 🔒");
            tvDetailStatusBadge.setTextColor(Color.parseColor("#8B949E"));
            btnCompleteSkill.setText("BLOQUEADA - CONCLUA OS PRÉ-REQUISITOS");
            btnCompleteSkill.setEnabled(false);
            btnCompleteSkill.setBackgroundColor(Color.parseColor("#21262D"));
            btnCompleteSkill.setTextColor(Color.parseColor("#64748B"));
        }

        // Prerequisites
        Map<Integer, Skill> skillLookup = new HashMap<>();
        if (skillTree != null && skillTree.getSkills() != null) {
            for (Skill s : skillTree.getSkills()) {
                skillLookup.put(s.getId(), s);
            }
        }

        layoutPrerequisitesList.removeAllViews();
        List<Integer> prereqs = skill.getPrerequisites();
        if (prereqs == null || prereqs.isEmpty()) {
            tvPrereqHeader.setVisibility(View.GONE);
            layoutPrerequisitesList.setVisibility(View.GONE);
        } else {
            tvPrereqHeader.setVisibility(View.VISIBLE);
            layoutPrerequisitesList.setVisibility(View.VISIBLE);
            for (Integer reqId : prereqs) {
                Skill reqSkill = skillLookup.get(reqId);
                String name = reqSkill != null ? reqSkill.getName() : "Habilidade #" + reqId;
                boolean isDone = reqSkill != null && reqSkill.getStatus() == SkillStatus.COMPLETED;

                TextView itemTv = new TextView(getContext());
                itemTv.setText((isDone ? "✓ " : "🔒 ") + name + (isDone ? " (Concluído)" : " (Pendente)"));
                itemTv.setTextColor(isDone ? Color.parseColor("#00E5FF") : Color.parseColor("#8B949E"));
                itemTv.setTextSize(13f);
                itemTv.setPadding(0, 4, 0, 4);
                layoutPrerequisitesList.addView(itemTv);
            }
        }

        // Suggested Resources
        layoutResourcesList.removeAllViews();
        List<String> resources = skill.getStudyResources();
        if (resources == null || resources.isEmpty()) {
            tvResourcesHeader.setVisibility(View.GONE);
            layoutResourcesList.setVisibility(View.GONE);
        } else {
            tvResourcesHeader.setVisibility(View.VISIBLE);
            layoutResourcesList.setVisibility(View.VISIBLE);
            for (String res : resources) {
                TextView itemTv = new TextView(getContext());
                itemTv.setText("📚 " + res);
                itemTv.setTextColor(Color.parseColor("#C9D1D9"));
                itemTv.setTextSize(13f);
                itemTv.setPadding(0, 4, 0, 4);
                layoutResourcesList.addView(itemTv);
            }
        }

        btnCompleteSkill.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onCompleteSkillRequested(skill);
            }
            dismiss();
        });
    }
}
