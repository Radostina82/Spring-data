package json_ex.productshop.services.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json_ex.productshop.entities.categories.CategoryImportDTO;
import json_ex.productshop.entities.categories.Category;
import json_ex.productshop.entities.products.Product;
import json_ex.productshop.entities.products.ProductImportDTO;
import json_ex.productshop.entities.users.User;
import json_ex.productshop.entities.users.UserImportDTO;
import json_ex.productshop.repositories.CategoryRepository;
import json_ex.productshop.repositories.ProductRepository;
import json_ex.productshop.services.SeedService;
import json_ex.productshop.repositories.UserRepository;
import json_ex.productshop.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeedServiceImpl implements SeedService {
    private static final String USERS_JSON_PATH = "src\\main\\resources\\productshop\\users.json";
    private static final String CATEGORIES_JSON_PATH = "src\\main\\resources\\productshop\\categories.json";
    private static final String PRODUCTS_JSON_PATH = "src\\main\\resources\\productshop\\products.json";;

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;


    private final ModelMapper mapper;
    private final Gson gson;

    @Autowired
    public SeedServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.mapper = new ModelMapper();
        this.gson = new GsonBuilder()
                .setPrettyPrinting().create();
    }

    @Override
    public void seedUsers() throws FileNotFoundException {
        FileReader fileReader = new FileReader(USERS_JSON_PATH);
        UserImportDTO[] userImportDTOS = this.gson.fromJson(fileReader, UserImportDTO[].class);

     List<User> users = Arrays.stream(userImportDTOS).map(importDTO -> this.mapper.map(importDTO, User.class))
                .collect(Collectors.toList());

     this.userRepository.saveAll(users);
    }

    @Override
    public void seedCategories() throws FileNotFoundException {
        FileReader fileReader = new FileReader(CATEGORIES_JSON_PATH);
        CategoryImportDTO[] categoryImportDTOS = this.gson.fromJson(fileReader, CategoryImportDTO[].class);

        List<Category> categories = Arrays.stream(categoryImportDTOS)
                .map(importDTO -> this.mapper.map(importDTO, Category.class))
                .collect(Collectors.toList());

        this.categoryRepository.saveAll(categories);
    }

    @Override
    public void seedProducts() throws FileNotFoundException {
        FileReader fileReader = new FileReader(PRODUCTS_JSON_PATH);
        ProductImportDTO[] productImportDTOS = this.gson.fromJson(fileReader, ProductImportDTO[].class);

        List<Product> products = Arrays.stream(productImportDTOS)
                .map(productDTO -> this.mapper.map(productDTO, Product.class))
                .map(this::setRandomSeller)
                .map(this::setRandomBuyer)
                .map(this::setRandomCategory)
                .collect(Collectors.toList());

        this.productRepository.saveAll(products);
    }

    private Product setRandomSeller(Product product){
        long usersCount = userRepository.count();
        int randomUserId = new Random().nextInt((int) usersCount) + 1;

        Optional<User> seller = this.userRepository.findById(randomUserId);

        product.setSeller(seller.get());

        return product;
    }

    private Product setRandomBuyer(Product product){
        if (product.getPrice().compareTo(BigDecimal.valueOf(944))>0){
            return product;
        }

        long usersCount = userRepository.count();
        int randomUserId = new Random().nextInt((int) usersCount) + 1;

        Optional<User> buyer = this.userRepository.findById(randomUserId);
        product.setBuyer(buyer.get());

        return product;
    }

    private Product setRandomCategory(Product product){
        Random random = new Random();
        long categoryCount = this.categoryRepository.count();
        int randomCategory= random.nextInt((int) categoryCount);

        Set<Category> categories = new HashSet<>();
        for (int i = 0; i < randomCategory; i++) {
            int randomId = random.nextInt((int) categoryCount) + 1;
            Optional<Category> category = this.categoryRepository.findById(randomId);
            categories.add(category.get());
        }
        product.setCategories(categories);
        return product;
    }
}
