package json_ex.cardealer.repositories;

import json_ex.cardealer.entities.parts.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, Integer> {
}
