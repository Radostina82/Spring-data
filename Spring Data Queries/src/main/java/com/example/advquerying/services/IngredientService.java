package com.example.advquerying.services;

import com.example.advquerying.entities.Ingredient;

import java.util.List;

public interface IngredientService {
    List<Ingredient> selectByNameStartingWith(String letter);

    List<Ingredient> selectByNameInOrderByPriceAsc(List<String> names);

    int deleteByName(String name);

    void increasePricePerPercent(double v);
}
