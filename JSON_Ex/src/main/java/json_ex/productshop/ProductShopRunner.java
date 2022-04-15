package json_ex.productshop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json_ex.productshop.entities.categories.CategoryStats;
import json_ex.productshop.entities.products.ProductWithoutBuyerDTO;
import json_ex.productshop.entities.users.ExportSellersWithCountsDTO;
import json_ex.productshop.entities.users.UserWithSoldProductDTO;
import json_ex.productshop.services.ProductService;
import json_ex.productshop.services.SeedService;
import json_ex.productshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ProductShopRunner implements CommandLineRunner {
    private final SeedService seedService;
    private final ProductService productService;
    private final UserService userService;

    private final Gson gson;
    @Autowired
    public ProductShopRunner(SeedService seedService, ProductService productService, UserService userService) {
        this.seedService = seedService;
        this.productService = productService;
        this.userService = userService;

        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
       //this.seedService.seedUsers();
        //this.seedService.seedCategories();
        //this.seedService.seedProducts();
        //productBetweenPriceWithoutBuyer();
     // userWithSoldProduct();
       // categoryStatistic();
        ExportSellersWithCountsDTO allWithSoldProductsAndCounts = this.userService.findAllWithSoldProductsAndCounts();
        String json = this.gson.toJson(allWithSoldProductsAndCounts);
        System.out.println(json);
    }

    private void categoryStatistic() {
        List<CategoryStats> categoriesStatistic = this.productService.getCategoriesStatistic();
        String json = this.gson.toJson(categoriesStatistic);
        System.out.println(json);
    }

    private void productBetweenPriceWithoutBuyer(){
        List<ProductWithoutBuyerDTO> productsForSell = this.productService.getProductsInPriceRangeForSale(500, 1000);
        String json = this.gson.toJson(productsForSell);

        System.out.println(json);
    }

    private void userWithSoldProduct(){
        List<UserWithSoldProductDTO> allWithSoldProduct = this.userService.findAllWithSoldProduct();
        String json = this.gson.toJson(allWithSoldProduct);
        System.out.println(json);
    }

}
