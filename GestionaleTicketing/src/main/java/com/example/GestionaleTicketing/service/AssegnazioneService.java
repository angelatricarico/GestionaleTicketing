package com.example.GestionaleTicketing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GestionaleTicketing.model.CategoriaTicket;
import com.example.GestionaleTicketing.model.Ticket;
import com.example.GestionaleTicketing.model.Utente;
import com.example.GestionaleTicketing.repository.UtenteRepository;

@Service
public class AssegnazioneService {
	
	@Autowired
	UtenteRepository utenteRepository;
	
	public Optional<Utente> findOperatoreConMinimiTicket(CategoriaTicket categoriaTicket) {
		List<Utente> operatori = utenteRepository.findAllByCategoriaTicket(categoriaTicket);
		return operatori.stream().min((utente1, utente2) -> Long.compare(
				utente1.getTicketsOperatore().stream().filter(n -> n.getStatus() != Ticket.Status.CHIUSO).count(),
				utente2.getTicketsOperatore().stream().filter(n -> n.getStatus() != Ticket.Status.CHIUSO).count()));
	}
}
