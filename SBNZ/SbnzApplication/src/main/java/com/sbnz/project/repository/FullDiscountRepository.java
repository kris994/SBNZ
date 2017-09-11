package com.sbnz.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbnz.project.model.FullDiscount;

@Repository
public interface FullDiscountRepository extends JpaRepository<FullDiscount, Integer>{

}
