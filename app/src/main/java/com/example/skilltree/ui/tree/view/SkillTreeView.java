package com.example.skilltree.ui.tree.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.skilltree.model.Skill;
import com.example.skilltree.model.SkillStatus;
import com.example.skilltree.model.SkillTree;
import com.example.skilltree.util.DependencyGraphEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillTreeView extends View {

    public interface OnSkillSelectedListener {
        void onSkillSelected(Skill skill);
    }

    private SkillTree skillTree;
    private final Map<Integer, Skill> skillMap = new HashMap<>();
    private Skill selectedSkill;
    private OnSkillSelectedListener selectionListener;

    // Canvas transformations
    private float scaleFactor = 1.0f;
    private float translateX = 0.0f;
    private float translateY = 0.0f;

    private float minScale = 0.4f;
    private float maxScale = 2.5f;

    // Node sizing (in pixels)
    private float nodeRadius = 42f;
    private final float density;

    // Paints
    private Paint gridPaint;
    private Paint lineCompletedPaint;
    private Paint lineAvailablePaint;
    private Paint lineLockedPaint;

    private Paint nodeCompletedBgPaint;
    private Paint nodeCompletedBorderPaint;
    private Paint nodeCompletedGlowPaint;

    private Paint nodeAvailableBgPaint;
    private Paint nodeAvailableBorderPaint;
    private Paint nodeAvailableGlowPaint;

    private Paint nodeLockedBgPaint;
    private Paint nodeLockedBorderPaint;

    private Paint selectedRingPaint;
    private Paint iconPaint;
    private TextPaint textPaint;
    private TextPaint categoryPaint;

    // Gestures
    private ScaleGestureDetector scaleDetector;
    private GestureDetector gestureDetector;
    private boolean isDragging = false;
    private float lastTouchX;
    private float lastTouchY;

    // Layout boundaries
    private float contentWidth = 1200f;
    private float contentHeight = 1800f;

    public SkillTreeView(Context context) {
        this(context, null);
    }

    public SkillTreeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkillTreeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        density = getResources().getDisplayMetrics().density;
        nodeRadius = 40f * density;
        initPaints();
        initGestureDetectors();
    }

    private void initPaints() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        // Background subtle grid
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(Color.parseColor("#121927"));
        gridPaint.setStrokeWidth(1.2f * density);

        // Lines / Edges
        lineCompletedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lineCompletedPaint.setColor(Color.parseColor("#00E5FF"));
        lineCompletedPaint.setStyle(Paint.Style.STROKE);
        lineCompletedPaint.setStrokeWidth(3.5f * density);
        lineCompletedPaint.setStrokeCap(Paint.Cap.ROUND);
        lineCompletedPaint.setShadowLayer(8f * density, 0, 0, Color.parseColor("#8000E5FF"));

        lineAvailablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lineAvailablePaint.setColor(Color.parseColor("#00B4D8"));
        lineAvailablePaint.setStyle(Paint.Style.STROKE);
        lineAvailablePaint.setStrokeWidth(2.5f * density);
        lineAvailablePaint.setStrokeCap(Paint.Cap.ROUND);

        lineLockedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lineLockedPaint.setColor(Color.parseColor("#252E3E"));
        lineLockedPaint.setStyle(Paint.Style.STROKE);
        lineLockedPaint.setStrokeWidth(2f * density);
        lineLockedPaint.setPathEffect(new DashPathEffect(new float[]{10f * density, 8f * density}, 0));

        // Nodes - COMPLETED
        nodeCompletedBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nodeCompletedBgPaint.setColor(Color.parseColor("#062438"));
        nodeCompletedBgPaint.setStyle(Paint.Style.FILL);

        nodeCompletedBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nodeCompletedBorderPaint.setColor(Color.parseColor("#00E5FF"));
        nodeCompletedBorderPaint.setStyle(Paint.Style.STROKE);
        nodeCompletedBorderPaint.setStrokeWidth(3.5f * density);

        nodeCompletedGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nodeCompletedGlowPaint.setColor(Color.parseColor("#3300E5FF"));
        nodeCompletedGlowPaint.setStyle(Paint.Style.FILL);

        // Nodes - AVAILABLE
        nodeAvailableBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nodeAvailableBgPaint.setColor(Color.parseColor("#0D2033"));
        nodeAvailableBgPaint.setStyle(Paint.Style.FILL);

        nodeAvailableBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nodeAvailableBorderPaint.setColor(Color.parseColor("#00E5FF"));
        nodeAvailableBorderPaint.setStyle(Paint.Style.STROKE);
        nodeAvailableBorderPaint.setStrokeWidth(2.5f * density);

        nodeAvailableGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nodeAvailableGlowPaint.setColor(Color.parseColor("#2200E5FF"));
        nodeAvailableGlowPaint.setStyle(Paint.Style.FILL);

        // Nodes - LOCKED
        nodeLockedBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nodeLockedBgPaint.setColor(Color.parseColor("#121722"));
        nodeLockedBgPaint.setStyle(Paint.Style.FILL);

        nodeLockedBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nodeLockedBorderPaint.setColor(Color.parseColor("#2B3547"));
        nodeLockedBorderPaint.setStyle(Paint.Style.STROKE);
        nodeLockedBorderPaint.setStrokeWidth(2f * density);

        // Selected Ring
        selectedRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedRingPaint.setColor(Color.parseColor("#FFD700"));
        selectedRingPaint.setStyle(Paint.Style.STROKE);
        selectedRingPaint.setStrokeWidth(3.5f * density);
        selectedRingPaint.setShadowLayer(10f * density, 0, 0, Color.parseColor("#AAFFD700"));

        // Icon text inside node
        iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        iconPaint.setTextAlign(Paint.Align.CENTER);
        iconPaint.setTextSize(20f * density);

        // Text below node
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#E6EDF3"));
        textPaint.setTextSize(12f * density);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);

        categoryPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        categoryPaint.setColor(Color.parseColor("#8B949E"));
        categoryPaint.setTextSize(9.5f * density);
        categoryPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void initGestureDetectors() {
        scaleDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float previousScale = scaleFactor;
                scaleFactor *= detector.getScaleFactor();
                scaleFactor = Math.max(minScale, Math.min(scaleFactor, maxScale));

                // Zoom around focus point
                float focusX = detector.getFocusX();
                float focusY = detector.getFocusY();
                translateX = focusX - (focusX - translateX) * (scaleFactor / previousScale);
                translateY = focusY - (focusY - translateY) * (scaleFactor / previousScale);

                invalidate();
                return true;
            }
        });

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                handleNodeClick(e.getX(), e.getY());
                return true;
            }
        });
    }

    public void setSkillTree(SkillTree tree) {
        this.skillTree = tree;
        this.skillMap.clear();
        if (tree != null && tree.getSkills() != null) {
            for (Skill s : tree.getSkills()) {
                skillMap.put(s.getId(), s);
            }
            // Auto layout
            computeTreeLayoutPositions();
            if (!tree.getSkills().isEmpty()) {
                selectSkill(tree.getSkills().get(0));
            }
        }
        invalidate();
    }

    public void setOnSkillSelectedListener(OnSkillSelectedListener listener) {
        this.selectionListener = listener;
    }

    public void selectSkill(Skill skill) {
        this.selectedSkill = skill;
        if (selectionListener != null && skill != null) {
            selectionListener.onSkillSelected(skill);
        }
        invalidate();
    }

    public Skill getSelectedSkill() {
        return selectedSkill;
    }

    private void computeTreeLayoutPositions() {
        if (skillTree == null || skillTree.getSkills() == null || skillTree.getSkills().isEmpty()) {
            return;
        }
        float measuredW = getWidth() > 0 ? getWidth() : 1080f;
        float canvasWidth = Math.max(measuredW, 1080f);
        float startY = 120f * density;
        float levelSpacing = 160f * density;

        DependencyGraphEngine.computeTreeLayout(skillTree, canvasWidth, startY, levelSpacing);

        // Calculate max bounds
        float maxY = startY;
        for (Skill s : skillTree.getSkills()) {
            if (s.getLayoutY() > maxY) {
                maxY = s.getLayoutY();
            }
        }
        contentHeight = maxY + 240f * density;
        contentWidth = canvasWidth;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (skillTree != null && !skillTree.getSkills().isEmpty()) {
            computeTreeLayoutPositions();
            fitToScreen();
        }
    }

    public void fitToScreen() {
        if (skillTree == null || skillTree.getSkills() == null || skillTree.getSkills().isEmpty()) {
            return;
        }
        float viewW = getWidth();
        float viewH = getHeight();
        if (viewW <= 0 || viewH <= 0) return;

        float padding = 40f * density;
        float targetScale = Math.min((viewW - padding * 2) / contentWidth, (viewH - padding * 2) / contentHeight);
        targetScale = Math.max(minScale, Math.min(targetScale, 1.2f));

        scaleFactor = targetScale;
        translateX = (viewW - contentWidth * scaleFactor) / 2f;
        translateY = 40f * density;

        invalidate();
    }

    public void centerOnSelectedOrRoot() {
        Skill target = selectedSkill;
        if (target == null && skillTree != null && !skillTree.getSkills().isEmpty()) {
            target = skillTree.getSkills().get(0);
        }
        if (target != null) {
            centerOnNode(target);
        } else {
            fitToScreen();
        }
    }

    public void centerOnNode(Skill skill) {
        if (skill == null) return;
        float viewW = getWidth();
        float viewH = getHeight();
        if (viewW <= 0 || viewH <= 0) return;

        scaleFactor = 1.0f;
        translateX = viewW / 2f - skill.getLayoutX() * scaleFactor;
        translateY = viewH / 2.8f - skill.getLayoutY() * scaleFactor;

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean scaleHandled = scaleDetector.onTouchEvent(event);
        boolean gestureHandled = gestureDetector.onTouchEvent(event);

        if (scaleDetector.isInProgress()) {
            return true;
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                isDragging = false;
                return true;

            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - lastTouchX;
                float dy = event.getY() - lastTouchY;
                if (Math.abs(dx) > 4 || Math.abs(dy) > 4) {
                    isDragging = true;
                }
                if (isDragging) {
                    translateX += dx;
                    translateY += dy;
                    invalidate();
                }
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                break;
        }

        return scaleHandled || gestureHandled || super.onTouchEvent(event);
    }

    private void handleNodeClick(float touchX, float touchY) {
        if (skillTree == null || skillTree.getSkills() == null) return;

        // Convert touch to world coordinates
        float worldX = (touchX - translateX) / scaleFactor;
        float worldY = (touchY - translateY) / scaleFactor;

        float hitRadius = nodeRadius * 1.5f;
        Skill closest = null;
        float minDistance = Float.MAX_VALUE;

        for (Skill s : skillTree.getSkills()) {
            float dx = s.getLayoutX() - worldX;
            float dy = s.getLayoutY() - worldY;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist <= hitRadius && dist < minDistance) {
                minDistance = dist;
                closest = s;
            }
        }

        if (closest != null) {
            selectSkill(closest);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(translateX, translateY);
        canvas.scale(scaleFactor, scaleFactor);

        drawBackgroundGrid(canvas);

        if (skillTree != null && skillTree.getSkills() != null) {
            drawConnections(canvas);
            drawNodes(canvas);
        }

        canvas.restore();
    }

    private void drawBackgroundGrid(Canvas canvas) {
        float step = 60f * density;
        float startX = -600f;
        float endX = contentWidth + 600f;
        float startY = -600f;
        float endY = contentHeight + 600f;

        for (float x = startX; x <= endX; x += step) {
            canvas.drawLine(x, startY, x, endY, gridPaint);
        }
        for (float y = startY; y <= endY; y += step) {
            canvas.drawLine(startX, y, endX, y, gridPaint);
        }
    }

    private void drawConnections(Canvas canvas) {
        for (Skill toSkill : skillTree.getSkills()) {
            List<Integer> prereqs = toSkill.getPrerequisites();
            if (prereqs == null || prereqs.isEmpty()) continue;

            for (Integer fromId : prereqs) {
                Skill fromSkill = skillMap.get(fromId);
                if (fromSkill == null) continue;

                float startX = fromSkill.getLayoutX();
                float startY = fromSkill.getLayoutY();
                float endX = toSkill.getLayoutX();
                float endY = toSkill.getLayoutY();

                Paint paint;
                if (fromSkill.getStatus() == SkillStatus.COMPLETED && toSkill.getStatus() == SkillStatus.COMPLETED) {
                    paint = lineCompletedPaint;
                } else if (fromSkill.getStatus() == SkillStatus.COMPLETED && toSkill.getStatus() == SkillStatus.AVAILABLE) {
                    paint = lineAvailablePaint;
                } else {
                    paint = lineLockedPaint;
                }

                // Smooth S-curve (Bezier) connecting parent bottom to child top
                Path path = new Path();
                path.moveTo(startX, startY);
                float midY = (startY + endY) / 2f;
                path.cubicTo(startX, midY, endX, midY, endX, endY);
                canvas.drawPath(path, paint);

                // Small directional dot / arrow indicator near target
                float dirX = endX;
                float dirY = endY - nodeRadius - 6f * density;
                Paint dotPaint = (fromSkill.getStatus() == SkillStatus.COMPLETED) ? lineCompletedPaint : lineLockedPaint;
                canvas.drawCircle(dirX, dirY, 3.5f * density, dotPaint);
            }
        }
    }

    private void drawNodes(Canvas canvas) {
        for (Skill skill : skillTree.getSkills()) {
            float cx = skill.getLayoutX();
            float cy = skill.getLayoutY();
            boolean isSelected = (selectedSkill != null && selectedSkill.getId() == skill.getId());

            // 1. Draw outer selection glow if selected
            if (isSelected) {
                canvas.drawCircle(cx, cy, nodeRadius + 12f * density, selectedRingPaint);
            }

            // 2. Outer pulse / glow according to status
            if (skill.getStatus() == SkillStatus.COMPLETED) {
                canvas.drawCircle(cx, cy, nodeRadius + 6f * density, nodeCompletedGlowPaint);
                canvas.drawCircle(cx, cy, nodeRadius, nodeCompletedBgPaint);
                canvas.drawCircle(cx, cy, nodeRadius, nodeCompletedBorderPaint);

                // Checkmark symbol
                iconPaint.setColor(Color.parseColor("#00E5FF"));
                canvas.drawText("✓", cx, cy + 7f * density, iconPaint);

            } else if (skill.getStatus() == SkillStatus.AVAILABLE) {
                canvas.drawCircle(cx, cy, nodeRadius + 4f * density, nodeAvailableGlowPaint);
                canvas.drawCircle(cx, cy, nodeRadius, nodeAvailableBgPaint);
                canvas.drawCircle(cx, cy, nodeRadius, nodeAvailableBorderPaint);

                // Star / diamond symbol
                iconPaint.setColor(Color.parseColor("#00E5FF"));
                canvas.drawText("✦", cx, cy + 7f * density, iconPaint);

            } else { // LOCKED
                canvas.drawCircle(cx, cy, nodeRadius, nodeLockedBgPaint);
                canvas.drawCircle(cx, cy, nodeRadius, nodeLockedBorderPaint);

                // Lock padlock symbol
                iconPaint.setColor(Color.parseColor("#64748B"));
                canvas.drawText("🔒", cx, cy + 7f * density, iconPaint);
            }

            // 3. Node Label (below node)
            drawNodeLabels(canvas, skill, cx, cy);
        }
    }

    private void drawNodeLabels(Canvas canvas, Skill skill, float cx, float cy) {
        float textTop = cy + nodeRadius + 10f * density;

        // Skill Name
        String name = skill.getName() != null ? skill.getName() : "Habilidade";
        if (skill.getStatus() == SkillStatus.LOCKED) {
            textPaint.setColor(Color.parseColor("#64748B"));
        } else if (skill.getStatus() == SkillStatus.COMPLETED) {
            textPaint.setColor(Color.parseColor("#E6EDF3"));
        } else {
            textPaint.setColor(Color.parseColor("#00E5FF"));
        }

        // Multi-line wrap if too long
        if (name.length() > 16) {
            int spaceIdx = name.lastIndexOf(' ', 16);
            if (spaceIdx > 3) {
                String line1 = name.substring(0, spaceIdx);
                String line2 = name.substring(spaceIdx + 1);
                canvas.drawText(line1, cx, textTop + 4f * density, textPaint);
                canvas.drawText(line2, cx, textTop + 18f * density, textPaint);
                textTop += 14f * density;
            } else {
                canvas.drawText(name, cx, textTop + 4f * density, textPaint);
            }
        } else {
            canvas.drawText(name, cx, textTop + 4f * density, textPaint);
        }

        // Category Badge / Level
        String cat = skill.getCategory() != null ? skill.getCategory() : "Nível " + skill.getLevel();
        canvas.drawText(cat.toUpperCase(), cx, textTop + 18f * density, categoryPaint);
    }
}
