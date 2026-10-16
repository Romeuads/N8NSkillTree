package com.example.skilltree.data.api;

import com.example.skilltree.data.api.request.GenerateTreeRequest;
import com.example.skilltree.model.SkillTree;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface SkillTreeApiService {

    /**
     * Endpoint padrão para disparar o webhook do n8n que consulta a Inteligência Artificial
     * e devolve a SkillTree estruturada em JSON.
     */
    @POST("webhook/skilltree")
    Call<SkillTree> generateSkillTree(@Body GenerateTreeRequest request);

    /**
     * Permite especificar uma URL dinâmica ou customizada configurada pelo usuário.
     */
    @POST
    Call<SkillTree> generateSkillTreeCustomUrl(@Url String fullUrl, @Body GenerateTreeRequest request);
}
