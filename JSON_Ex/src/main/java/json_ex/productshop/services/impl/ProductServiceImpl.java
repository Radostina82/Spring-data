package json_ex.productshop.services.impl;

import json_ex.productshop.entities.categories.CategoryStats;
import json_ex.productshop.entities.products.ProductWithoutBuyerDTO;
import json_ex.productshop.repositories.ProductRepository;
import json_ex.productshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductWithoutBuyerDTO> getProductsInPriceRangeForSale(float from, float to) {
        BigDecimal rangeStart = BigDecimal.valueOf(from);
        BigDecimal rangeEnd = BigDecimal.valueOf(to);
        return this.productRepository.findAllByPriceBetweenAndBuyerIsNullOrderByPriceAsc(rangeStart, rangeEnd);
    }

    @Override
    public List<CategoryStats> getCategoriesStatistic() {

        return this.productRepository.getCategoryStat();
    }
}
