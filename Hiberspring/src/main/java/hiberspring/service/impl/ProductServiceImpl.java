package hiberspring.service.impl;

import hiberspring.domain.dtos.ImportProductDTO;
import hiberspring.domain.dtos.ImportProductRootDTO;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Product;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.ProductRepository;
import hiberspring.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final Path path = Path.of("src", "main", "resources", "files", "products.xml");
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;
    private final Unmarshaller unmarshaller;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, BranchRepository branchRepository) throws JAXBException {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        JAXBContext context = JAXBContext.newInstance(ImportProductRootDTO.class);
        this.unmarshaller= context.createUnmarshaller();
    }

    @Override
    public Boolean productsAreImported() {
        return this.productRepository.count()>0;
    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return Files.readString(path);
    }

    @Override
    public String importProducts() throws JAXBException, FileNotFoundException {
       ImportProductRootDTO importProductRootDTO = (ImportProductRootDTO) this.unmarshaller.unmarshal(new FileReader(path.toAbsolutePath().toString()));
        return importProductRootDTO.getProducts().stream().map(productDto -> importProduct(productDto))
                .collect(Collectors.joining("\n"));
    }

    private String importProduct(ImportProductDTO productDto) {
        Set<ConstraintViolation<ImportProductDTO>> validateErrors = this.validator.validate(productDto);
        if(!validateErrors.isEmpty()){
            return "Error: Invalid data.";
        }
        Optional<Branch> branch = this.branchRepository.findByName(productDto.getBranch());
        if (branch.isEmpty()){
            return "Error: Invalid data.";
        }

        Product product = this.modelMapper.map(productDto, Product.class);
        product.setBranch(branch.get());
        this.productRepository.save(product);

        return String.format("Successfully imported Product %s.", product.getName());
    }
}
