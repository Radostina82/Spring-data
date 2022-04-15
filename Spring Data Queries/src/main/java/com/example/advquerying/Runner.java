package com.example.advquerying;

import com.example.advquerying.services.IngredientService;
import com.example.advquerying.services.ShampooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Component
public class Runner implements CommandLineRunner {
    private final ShampooService shampooService;
    private final IngredientService ingredientService;
    @Autowired
    public Runner(ShampooService shampooService, IngredientService ingredientService) {
        this.shampooService = shampooService;
        this.ingredientService = ingredientService;
    }

    @Override
    public void run(String... args) throws Exception {
        //shampooService.selectBySize(Size.MEDIUM).forEach(System.out::println);
       // shampooService.selectBySizeOrLabelIdOrderByPriceAsc(Size.MEDIUM, 10).forEach(System.out::println);
        //shampooService.selectByPriceGreaterThanOrderByPriceDesc(BigDecimal.valueOf(5)).forEach(System.out::println);
       // ingredientService.selectByNameStartingWith("M").forEach(i-> System.out.println(i.getName()));
       // ingredientService.selectByNameInOrderByPriceAsc(List.of("Lavender", "Herbs", "Apple")).forEach(i -> System.out.println(i.getName()));
       // System.out.println(this.shampooService.selectCountByPriceLowerThan(BigDecimal.valueOf(8.5)));
       // this.shampooService.selectByIngredientNames(Set.of("Berry", "Mineral-Collagen")).forEach(s-> System.out.println(s.getBrand()));
      //  this.shampooService.selectByIngredientsLessThan(2).forEach(s-> System.out.println(s.getBrand()));
       // System.out.println(this.ingredientService.deleteByName("Apple"));
        this.ingredientService.increasePricePerPercent(0.1);
    }
}
