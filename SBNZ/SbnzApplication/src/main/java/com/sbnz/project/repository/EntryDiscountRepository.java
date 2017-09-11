package com.sbnz.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbnz.project.model.EntryDiscount;

@Repository
public interface EntryDiscountRepository extends JpaRepository<EntryDiscount, Integer>{

}
