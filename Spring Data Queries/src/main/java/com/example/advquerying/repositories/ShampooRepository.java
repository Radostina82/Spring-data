package com.example.advquerying.repositories;

import com.example.advquerying.entities.Ingredient;
import com.example.advquerying.entities.Shampoo;
import com.example.advquerying.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ShampooRepository extends JpaRepository<Shampoo, Long> {
    List<Shampoo> findBySizeOrderById(Size size);

    List<Shampoo> findBySizeOrLabelIdOrderByPriceAsc(Size size, long id);

    List<Shampoo> findByPriceGreaterThanOrderByPriceDesc(BigDecimal price);

    List<Shampoo> findByPriceLessThan(BigDecimal price);
    @Query(value = "SELECT s FROM Shampoo s" +
            " JOIN s.ingredients i" +
            " WHERE i.name IN :ingredientsName")
    Set<Shampoo> findByIngredientNames(@Param(value = "ingredientsName") Set<String> names);
    @Query(value = "SELECT s FROM Shampoo s" +
            " WHERE s.ingredients.size < :count")
    List<Shampoo> findByIngredientsLessThan(int count);
}
