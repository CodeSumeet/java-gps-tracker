package com.sumeet.gpstracker.repository;

import com.sumeet.gpstracker.model.Human;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HumanRepository extends JpaRepository<Human, String> {

}
