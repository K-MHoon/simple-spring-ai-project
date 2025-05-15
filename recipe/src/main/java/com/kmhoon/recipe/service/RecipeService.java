package com.kmhoon.recipe.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kmhoon.recipe.entity.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final ChatModel chatModel;
    private final String googleApiKey = "AIzaSyB-aWGiKNThDdS2_6_MS65AD5bT9GAJ-Fg";
    private final String googleCx = "816bd707bbda54806";
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 레시피 생성 메서드
     *
     * @param recipe
     * @return
     */
    public String createRecipe(Recipe recipe) {
        String template = """
                제목: 요리 제목을 제공해 주세요.
                다음 재료를 사용하여 요리법을 만들고 싶습니다: {ingredients}.
                선호하는 요리 유형은 {cuisine}입니다.
                다음 식이 제한을 고려해 주세요: {dietaryRestrictions}.
                재료 목록과 조리법을 포함한 상세한 요리법을 제공해 주세요.
                """;
        PromptTemplate promptTemplate = new PromptTemplate(template);
        Map<String, Object> params = Map.of(
                "ingredients", recipe.getIngredients(),
                "cuisine", recipe.getCuisine(),
                "dietaryRestrictions", recipe.getDietaryRestrictions()
        );

        Prompt prompt = promptTemplate.create(params);
        return chatModel.call(prompt).getResult().getOutput().getText();
    }

    public List<String> searchRecipeUrls(String query) {

        URI apiUrl = UriComponentsBuilder.fromUriString("https://www.googleapis.com/customsearch/v1")
                .queryParam("key", googleApiKey)
                .queryParam("cx", googleCx)
                .queryParam("q", query)
                .build()
                .toUri();

        System.out.println(apiUrl);

        String response = restTemplate.getForObject(apiUrl, String.class);
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonArray itemsArray = jsonResponse.getAsJsonArray("items");

        List<String> urls = new ArrayList<>();
        if (itemsArray != null) {
            for (JsonElement item : itemsArray) {
                urls.add(item.getAsJsonObject().get("link").getAsString());
            }
        } else {
            System.out.println("No search results found for the query: " + query);
        }
        return urls;
    }

    public Map<String, Object> createRecipeWithUrls(Recipe recipe) {
        String recipeContent = createRecipe(recipe);
        List<String> urls = searchRecipeUrls(recipe.getIngredients());
        return Map.of("recipe", recipeContent, "urls", urls);
    }
}
