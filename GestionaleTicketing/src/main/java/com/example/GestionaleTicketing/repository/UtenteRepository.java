package com.example.GestionaleTicketing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestionaleTicketing.model.CategoriaTicket;
import com.example.GestionaleTicketing.model.Utente;
import com.example.GestionaleTicketing.model.Utente.Ruolo;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long>{
	
	//Cerca utente per email
	Optional <Utente> findByEmail(String email);
	
	//Cerca utente per token
	Optional <Utente> findByToken(String token);

	//Cerca lista utenti per ruolo
	List <Utente> findAllByRuolo(Ruolo ruolo);
	
	//Cerca lista utenti per Categoria
	List<Utente> findAllByCategoriaTicket(CategoriaTicket categoriaTicket);

}
