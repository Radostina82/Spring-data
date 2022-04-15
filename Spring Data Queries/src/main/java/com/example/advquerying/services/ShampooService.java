package com.example.advquerying.services;

import com.example.advquerying.entities.Ingredient;
import com.example.advquerying.entities.Shampoo;
import com.example.advquerying.entities.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ShampooService {
   List<Shampoo> selectBySize(Size medium);

   List<Shampoo> selectBySizeOrLabelIdOrderByPriceAsc(Size size, long id);

   List<Shampoo> selectByPriceGreaterThanOrderByPriceDesc(BigDecimal price);

   int  selectCountByPriceLowerThan(BigDecimal price);

   Set<Shampoo> selectByIngredientNames(Set<String> names);

   List<Shampoo> selectByIngredientsLessThan(int number);
}
