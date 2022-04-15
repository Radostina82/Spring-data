package json_ex.productshop.services.impl;

import json_ex.productshop.entities.products.ExportNamePriceProductDTO;
import json_ex.productshop.entities.products.ExportSoldProductsDTO;
import json_ex.productshop.entities.users.ExportSellersWithCountsDTO;
import json_ex.productshop.entities.users.ExportUserWithSoldCountDTO;
import json_ex.productshop.entities.users.User;
import json_ex.productshop.entities.users.UserWithSoldProductDTO;
import json_ex.productshop.repositories.UserRepository;
import json_ex.productshop.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public List<UserWithSoldProductDTO> findAllWithSoldProduct() {
        List<User> allWithSoldProduct = this.userRepository.findAllWithSoldProduct();
      return   allWithSoldProduct.stream()
                .map(u-> this.modelMapper.map(u, UserWithSoldProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ExportSellersWithCountsDTO findAllWithSoldProductsAndCounts() {
        List<User> allWithSoldProductsOrderByCount = this.userRepository.findAllWithSoldProductsOrderByCount();
        List<ExportUserWithSoldCountDTO> dtos = allWithSoldProductsOrderByCount.stream()
                .map(this::createExportUserWithSoldCountDTO)
                .collect(Collectors.toList());
        return new ExportSellersWithCountsDTO(dtos);
    }

    private ExportUserWithSoldCountDTO createExportUserWithSoldCountDTO(User user) {
        ExportUserWithSoldCountDTO userDto = this.modelMapper.map(user, ExportUserWithSoldCountDTO.class);
        List<ExportNamePriceProductDTO> namePriceProductDTOS = user.getSellingItems().stream().
                map(p -> modelMapper.map(p, ExportNamePriceProductDTO.class)).collect(Collectors.toList());
        ExportSoldProductsDTO soldProductsDTO = new ExportSoldProductsDTO(namePriceProductDTOS);
        userDto.setSoldProducts(soldProductsDTO);

        return userDto;
    }
}
