package com.kmhoon.recipe.controller;

import com.kmhoon.recipe.entity.Recipe;
import com.kmhoon.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("/recipe")
    public Map<String, Object> recipe(@RequestBody Recipe recipe) {
        return recipeService.createRecipeWithUrls(recipe);
    }
}
