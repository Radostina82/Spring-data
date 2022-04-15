package exam.repository;

import exam.model.Laptop;
import exam.model.WarrantyType;
import exam.model.dto.BestLaptopDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Long> {
    Optional<Laptop> findByMacAddress(String macAddress);

    Optional<Laptop> findByWarrantyType(WarrantyType warrantyType);

    @Query("SELECT new exam.model.dto.BestLaptopDTO (l.macAddress, l.cpuSpeed, l.ram, l.storage, l.price, l.shop) FROM Laptop l" +
            " JOIN l.shop s" +
            " ORDER BY l.cpuSpeed DESC," +
            " l.ram DESC," +
            " l.storage DESC," +
            " l.macAddress")

    List<BestLaptopDTO> findBestLaptop();
}
