package com.example.skilltree.data.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.example.skilltree.data.api.request.GenerateTreeRequest;
import com.example.skilltree.model.SkillTree;
import com.example.skilltree.util.DependencyGraphEngine;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TreeGenerationService {

    public interface GenerationCallback {
        void onSuccess(SkillTree skillTree, boolean isFromN8n);
        void onError(String errorMessage);
    }

    private static final String PREFS_NAME = "skilltree_n8n_prefs";
    private static final String KEY_WEBHOOK_URL = "webhook_url";
    private static final String DEFAULT_WEBHOOK_URL = "https://n8n.webhook.fmpads.internal/webhook/generate-skilltree";

    private final Context context;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Gson gson = new Gson();

    public TreeGenerationService(Context context) {
        this.context = context.getApplicationContext();
    }

    public static String getSavedWebhookUrl(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_WEBHOOK_URL, DEFAULT_WEBHOOK_URL);
    }

    public static void saveWebhookUrl(Context context, String url) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_WEBHOOK_URL, url).apply();
    }

    public void generateSkillTree(final GenerateTreeRequest request, final GenerationCallback callback) {
        executor.execute(() -> {
            String webhookUrl = getSavedWebhookUrl(context);
            SkillTree generatedTree = null;
            boolean isFromN8n = false;

            // 1. Try n8n HTTP Webhook if configured and reachable
            if (webhookUrl != null && !webhookUrl.isEmpty() && !webhookUrl.contains("example.com")) {
                try {
                    URL url = new URL(webhookUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setConnectTimeout(6000);
                    conn.setReadTimeout(12000);

                    String jsonInputString = gson.toJson(request);
                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }

                    int responseCode = conn.getResponseCode();
                    if (responseCode >= 200 && responseCode < 300) {
                        try (BufferedReader br = new BufferedReader(
                                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                            StringBuilder response = new StringBuilder();
                            String responseLine;
                            while ((responseLine = br.readLine()) != null) {
                                response.append(responseLine.trim());
                            }
                            generatedTree = gson.fromJson(response.toString(), SkillTree.class);
                            if (generatedTree != null && generatedTree.getSkills() != null && !generatedTree.getSkills().isEmpty()) {
                                isFromN8n = true;
                            }
                        }
                    }
                } catch (Exception e) {
                    // Fallback to local AI generator template
                    generatedTree = null;
                }
            }

            // 2. If n8n was not reachable or returned empty, use rich built-in template generator
            if (generatedTree == null || generatedTree.getSkills() == null || generatedTree.getSkills().isEmpty()) {
                generatedTree = BuiltInTreeTemplates.getTemplateForTheme(
                        request.getTheme(),
                        request.getLevel(),
                        request.getObjective(),
                        request.getDailyTime()
                );
            }

            // 3. Compute and recalculate DAG states
            DependencyGraphEngine.recalculateStatuses(generatedTree);

            final SkillTree finalResult = generatedTree;
            final boolean finalIsFromN8n = isFromN8n;

            mainHandler.post(() -> {
                if (finalResult != null && finalResult.getSkills() != null) {
                    callback.onSuccess(finalResult, finalIsFromN8n);
                } else {
                    callback.onError("Não foi possível gerar a SkillTree para o tema informado.");
                }
            });
        });
    }
}
