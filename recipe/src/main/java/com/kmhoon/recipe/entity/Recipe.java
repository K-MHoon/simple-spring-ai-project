package com.kmhoon.recipe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    private String ingredients;
    private String cuisine;
    private String dietaryRestrictions;
}
