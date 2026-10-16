package com.example.skilltree.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.skilltree.model.SkillTree;

import java.util.ArrayList;
import java.util.List;

public class SkillTreeCardAdapter extends RecyclerView.Adapter<SkillTreeCardAdapter.ViewHolder> {

    public interface OnTreeClickListener {
        void onTreeClick(SkillTree tree);
        void onTreeLongClick(SkillTree tree);
    }

    private List<SkillTree> treeList = new ArrayList<>();
    private final OnTreeClickListener listener;

    public SkillTreeCardAdapter(OnTreeClickListener listener) {
        this.listener = listener;
    }

    public void setTrees(List<SkillTree> trees) {
        this.treeList = trees != null ? trees : new ArrayList<>();
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
        SkillTree tree = treeList.get(position);
        holder.bind(tree, listener);
    }

    @Override
    public int getItemCount() {
        return treeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvLevel;
        private final TextView tvDescription;
        private final ProgressBar pbProgress;
        private final TextView tvProgressText;
        private final TextView tvSkillsCount;
        private final TextView tvXp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCardTitle);
            tvLevel = itemView.findViewById(R.id.tvCardLevel);
            tvDescription = itemView.findViewById(R.id.tvCardDescription);
            pbProgress = itemView.findViewById(R.id.pbCardProgress);
            tvProgressText = itemView.findViewById(R.id.tvCardProgressText);
            tvSkillsCount = itemView.findViewById(R.id.tvCardSkillsCount);
            tvXp = itemView.findViewById(R.id.tvCardXp);
        }

        public void bind(SkillTree tree, OnTreeClickListener listener) {
            tvTitle.setText(tree.getTitle());
            tvLevel.setText(tree.getTargetLevel() != null ? tree.getTargetLevel() : "Geral");
            tvDescription.setText(tree.getDescription());

            int percent = tree.getProgressPercent();
            pbProgress.setProgress(percent);
            tvProgressText.setText(percent + "%");

            int completed = tree.getCompletedCount();
            int total = tree.getTotalCount();
            tvSkillsCount.setText(completed + " de " + total + " habilidades concluídas");

            tvXp.setText("⚡ " + tree.getTotalXp() + " XP");

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onTreeClick(tree);
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) listener.onTreeLongClick(tree);
                return true;
            });
        }
    }
}
