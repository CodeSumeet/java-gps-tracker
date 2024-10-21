package com.sumeet.gpstracker.repository;

import com.sumeet.gpstracker.model.Leopard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeopardRepository extends JpaRepository<Leopard, Long> {
    // Additional query methods can be defined here
}
