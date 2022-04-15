package json_ex.productshop.repositories;

import json_ex.productshop.entities.categories.CategoryStats;
import json_ex.productshop.entities.products.Product;
import json_ex.productshop.entities.products.ProductWithoutBuyerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT new json_ex.productshop.entities.products.ProductWithoutBuyerDTO(p.name, p.price," +
            "p.seller.firstName, p.seller.lastName ) FROM Product p" +
            " WHERE p.price > :rangeStart AND p.price < :rangeEnd AND p.buyer IS NULL" +
            " ORDER BY p.price Asc")
    List<ProductWithoutBuyerDTO> findAllByPriceBetweenAndBuyerIsNullOrderByPriceAsc(BigDecimal rangeStart, BigDecimal rangeEnd);

    @Query("SELECT new json_ex.productshop.entities.categories.CategoryStats(c.name, COUNT(p)," +
            " AVG(p.price), SUM(p.price)) FROM Product p" +
            " JOIN p.categories c" +
            " GROUP BY c")
    List<CategoryStats> getCategoryStat();
}
