package com.kmhoon.recipe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RouteController {

    @GetMapping("/recipeView")
    public String recipeView() {
        return "recipe";
    }
}
