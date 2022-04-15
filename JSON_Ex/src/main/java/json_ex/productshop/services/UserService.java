package json_ex.productshop.services;

import json_ex.productshop.entities.users.ExportSellersWithCountsDTO;
import json_ex.productshop.entities.users.User;
import json_ex.productshop.entities.users.UserWithSoldProductDTO;

import java.util.List;

public interface UserService {

    List<UserWithSoldProductDTO> findAllWithSoldProduct();

    ExportSellersWithCountsDTO findAllWithSoldProductsAndCounts();
}
