package com.example.GestionaleTicketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestionaleTicketing.model.CategoriaTicket;

@Repository

public interface CategoriaTicketRepository extends JpaRepository<CategoriaTicket, Long>{

}
