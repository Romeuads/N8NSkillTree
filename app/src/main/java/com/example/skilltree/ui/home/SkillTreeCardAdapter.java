package com.example.skilltree.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.skilltree.model.Skill;
import com.example.skilltree.model.SkillStatus;
import com.example.skilltree.model.SkillTree;

import java.util.ArrayList;
import java.util.List;

public class SkillTreeCardAdapter extends RecyclerView.Adapter<SkillTreeCardAdapter.ViewHolder> {

    public interface OnTreeClickListener {
        void onTreeClick(SkillTree tree);
    }

    private final List<SkillTree> trees = new ArrayList<>();
    private final OnTreeClickListener listener;

    public SkillTreeCardAdapter(OnTreeClickListener listener) {
        this.listener = listener;
    }

    public void setTrees(List<SkillTree> newTrees) {
        trees.clear();
        if (newTrees != null) {
            trees.addAll(newTrees);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_skill_tree_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SkillTree tree = trees.get(position);
        holder.bind(tree, listener);
    }

    @Override
    public int getItemCount() {
        return trees.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCardTitle;
        private final TextView tvCardLevel;
        private final TextView tvCardDescription;
        private final ProgressBar pbCardProgress;
        private final TextView tvCardProgressText;
        private final TextView tvCardSkillsCount;
        private final TextView tvCardXp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardTitle = itemView.findViewById(R.id.tvCardTitle);
            tvCardLevel = itemView.findViewById(R.id.tvCardLevel);
            tvCardDescription = itemView.findViewById(R.id.tvCardDescription);
            pbCardProgress = itemView.findViewById(R.id.pbCardProgress);
            tvCardProgressText = itemView.findViewById(R.id.tvCardProgressText);
            tvCardSkillsCount = itemView.findViewById(R.id.tvCardSkillsCount);
            tvCardXp = itemView.findViewById(R.id.tvCardXp);
        }

        public void bind(final SkillTree tree, final OnTreeClickListener listener) {
            tvCardTitle.setText(tree.getTitle() != null ? tree.getTitle() : "SkillTree");
            tvCardLevel.setText(tree.getTargetLevel() != null ? tree.getTargetLevel() : "Geral");
            tvCardDescription.setText(tree.getDescription() != null ? tree.getDescription() : "");

            int totalSkills = (tree.getSkills() != null) ? tree.getSkills().size() : 0;
            int completedSkills = 0;
            int totalXp = 0;

            if (tree.getSkills() != null) {
                for (Skill s : tree.getSkills()) {
                    totalXp += s.getXpReward();
                    if (s.getStatus() == SkillStatus.COMPLETED) {
                        completedSkills++;
                    }
                }
            }

            int percent = totalSkills > 0 ? (int) Math.round((completedSkills * 100.0) / totalSkills) : 0;
            pbCardProgress.setProgress(percent);
            tvCardProgressText.setText(percent + "%");
            tvCardSkillsCount.setText(completedSkills + " de " + totalSkills + " habilidades concluídas");
            tvCardXp.setText("⚡ " + totalXp + " XP");

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTreeClick(tree);
                }
            });
        }
    }
}
