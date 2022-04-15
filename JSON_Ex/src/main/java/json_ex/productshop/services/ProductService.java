package json_ex.productshop.services;

import json_ex.productshop.entities.categories.CategoryStats;
import json_ex.productshop.entities.products.ProductWithoutBuyerDTO;

import java.util.List;

public interface ProductService {
    List<ProductWithoutBuyerDTO> getProductsInPriceRangeForSale(float from, float to);

    List<CategoryStats> getCategoriesStatistic();
}
