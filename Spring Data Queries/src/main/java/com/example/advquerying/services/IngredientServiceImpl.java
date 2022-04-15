package com.example.advquerying.services;

import com.example.advquerying.entities.Ingredient;
import com.example.advquerying.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<Ingredient> selectByNameStartingWith(String letter) {
        return ingredientRepository.findByNameStartingWith(letter);
    }

    @Override
    public List<Ingredient> selectByNameInOrderByPriceAsc(List<String> names) {
        return ingredientRepository.findByNameInOrderByPriceAsc(names);
    }

    @Override
    @Transactional
    public int deleteByName(String name) {
        return this.ingredientRepository.deleteByName(name);
    }

    @Override
    @Transactional
    public void increasePricePerPercent(double v) {
        BigDecimal pricePerPercent = BigDecimal.valueOf(v);
        this.ingredientRepository.increasePricePerPercent(pricePerPercent);
    }
}
