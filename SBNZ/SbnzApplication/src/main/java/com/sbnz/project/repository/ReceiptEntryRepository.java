package com.sbnz.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbnz.project.model.ReceiptEntry;

@Repository
public interface ReceiptEntryRepository extends JpaRepository<ReceiptEntry, Integer>{

}
