package com.example.advquerying.services;

import com.example.advquerying.entities.Ingredient;
import com.example.advquerying.entities.Shampoo;
import com.example.advquerying.entities.Size;
import com.example.advquerying.repositories.ShampooRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class ShampooServiceImpl implements ShampooService {
    private final ShampooRepository shampooRepository;

    @Autowired
    public ShampooServiceImpl(ShampooRepository shampooRepository) {
        this.shampooRepository = shampooRepository;
    }

    @Override
    public List<Shampoo> selectBySize(Size medium) {
        return shampooRepository.findBySizeOrderById(medium);
    }

    @Override
    public List<Shampoo> selectBySizeOrLabelIdOrderByPriceAsc(Size size, long id) {
        return shampooRepository.findBySizeOrLabelIdOrderByPriceAsc(size, id);
    }

    @Override
    public List<Shampoo> selectByPriceGreaterThanOrderByPriceDesc(BigDecimal price) {
        return shampooRepository.findByPriceGreaterThanOrderByPriceDesc(price);
    }

    @Override
    public int selectCountByPriceLowerThan(BigDecimal price) {
       return this.shampooRepository.findByPriceLessThan(price).size();
    }

    @Override
    public Set<Shampoo> selectByIngredientNames(Set<String> names) {
        return this.shampooRepository.findByIngredientNames(names);
    }

    @Override
    public List<Shampoo> selectByIngredientsLessThan(int number) {
        return this.shampooRepository.findByIngredientsLessThan(number);
    }
}
