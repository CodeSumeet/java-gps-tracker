package com.sumeet.gpstracker.repository;

import com.sumeet.gpstracker.model.Leopard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeopardRepository extends JpaRepository<Leopard, String> {
    Optional<Leopard> findByCallerId(String callerId);
}
