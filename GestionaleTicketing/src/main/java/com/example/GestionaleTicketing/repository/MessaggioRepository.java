package com.example.GestionaleTicketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestionaleTicketing.model.Messaggio;

@Repository
public interface MessaggioRepository extends JpaRepository<Messaggio, Long>{

}
