package json_ex.cardealer.services;

import java.io.FileNotFoundException;

public interface SeedService {
    void seedSuppliers() throws FileNotFoundException;

    void seedParts() throws FileNotFoundException;

    void seedCars() throws FileNotFoundException;

    void seedCustomers() throws FileNotFoundException;

    default void seedAll() throws FileNotFoundException {
        this.seedSuppliers();
        this.seedParts();
        this.seedCars();
        this.seedCustomers();
    }
}
