package com.example.GestionaleTicketing.controller;

import java.time.LocalDate;
import java.util.Collections;
	import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.GestionaleTicketing.dto.TicketCountDto;
import com.example.GestionaleTicketing.dto.TicketDto;
import com.example.GestionaleTicketing.model.CategoriaTicket;
import com.example.GestionaleTicketing.model.Messaggio;
import com.example.GestionaleTicketing.model.Ticket;
import com.example.GestionaleTicketing.model.Ticket.Status;
import com.example.GestionaleTicketing.model.Utente;
import com.example.GestionaleTicketing.repository.CategoriaTicketRepository;
import com.example.GestionaleTicketing.repository.MessaggioRepository;
import com.example.GestionaleTicketing.repository.TicketRepository;
import com.example.GestionaleTicketing.repository.UtenteRepository;
import com.example.GestionaleTicketing.service.AssegnazioneService;
import com.example.GestionaleTicketing.service.DataService;
import com.example.GestionaleTicketing.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ticket")
@CrossOrigin ("*")

public class TicketController {
	@Autowired
	DataService dataService;	
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	TicketRepository ticketRepository;
	
	@Autowired
	MessaggioRepository messaggioRepository;
	
	@Autowired
	UtenteRepository utenteRepository;
	
	@Autowired
	CategoriaTicketRepository categoriaTicketRepository;
	
	@Autowired
	AssegnazioneService assegnazioneService;
	


	//Visualizzazione tickets a seconda dei controlli effettuati sul token e ruolo
	@GetMapping
	public List <Ticket> getAllTickets(HttpServletRequest request, HttpServletResponse response ) {
		
		Optional <Utente> utente = getAuthUser(request);
		
		if (utente.get().getRuolo() == Utente.Ruolo.Utente) {
			return utente.get().getTickets();
		} else if (utente.get().getRuolo() == Utente.Ruolo.Operatore) {
			return utente.get().getTicketsOperatore();			
		} else {
			return ticketRepository.findAll();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
	    Ticket ticket = ticketRepository.findById(id)
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket non trovato"));

	    TicketDto ticketDto = new TicketDto();
	    ticketDto.setOggetto(ticket.getOggetto());
	    ticketDto.setStatus(ticket.getStatus());
	    ticketDto.setIdCategoria(ticket.getCategoriaTicket() != null ? ticket.getCategoriaTicket().getId() : null);

	    // Se il messaggio esiste, assegna il corpo utente
	    if (ticket.getMessaggio() != null) {
	        ticketDto.setTestoMessaggio(ticket.getMessaggio().getCorpoUtente());
	    } else {
	        ticketDto.setTestoMessaggio(null);
	    }

	    return ResponseEntity.ok(ticketDto);
	}


	
	
	
	//Creazione nuovo ticket da utente
	@PostMapping
	@Transactional
	public Object createTicket(@Valid @RequestBody TicketDto ticketDto, HttpServletRequest request, HttpServletResponse response) {
	    Optional<Utente> optionalUtente = getAuthUser(request);
	   
	    if (!optionalUtente.isPresent()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Utente non trovato"));
	    }

	    Optional<CategoriaTicket> optionalCategoria = categoriaTicketRepository.findById(ticketDto.getIdCategoria());
	    if (!optionalCategoria.isPresent()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Categoria non trovata"));
	    }

	    Ticket ticket = new Ticket();
	    ticket.setOggetto(ticketDto.getOggetto());  
	    ticket.setStatus(Status.APERTO); 
	    ticket.setDataApertura(LocalDate.now());  
	    ticket.setCategoriaTicket(optionalCategoria.get());  
	    ticket.setUtente(optionalUtente.get());
	    ticket.setOperatore(assegnazioneService.findOperatoreConMinimiTicket(ticket.getCategoriaTicket()).get());  //assegnazione poi automatica di operatore
	    ticket = ticketRepository.save(ticket);  
	    
	    Messaggio messaggio = new Messaggio();
	    messaggio.setCorpoUtente(ticketDto.getTestoMessaggio());  
	    messaggio.setTicket(ticket);  //associazione messaggio a ticket per avere id valido di messaggio
	    messaggio = messaggioRepository.save(messaggio);  

	    ticket.setMessaggio(messaggio);  //nuova associazione di messaggio a ticket per salvare l'id di messaggio
	    ticketRepository.save(ticket);


	    return ResponseEntity.ok(Collections.singletonMap("message", "Ticket creato con successo! ID: " + ticket.getId()));
	}
	
	
	
	//Aggiornamento ticket in particolare da operatore (manuale e automatico per la data di chiusura)
	@PutMapping("/{id}")
	public Object updateTicket(@PathVariable Long id, @Valid @RequestBody TicketDto ticketDto, HttpServletRequest request, HttpServletResponse response) {
	    Optional<Utente> optOperatore = getAuthUser(request);
		
	    if (!optOperatore.isPresent() || optOperatore.get().getRuolo() != Utente.Ruolo.Operatore) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Autenticazione richiesta o operatore non presente"));
	    }

	    Optional<Ticket> optTicket = ticketRepository.findById(id);
	    if (!optTicket.isPresent()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Ticket non trovato"));
	    }
	    
	    Ticket existingTicket = optTicket.get();
	    
	    if (existingTicket.getStatus() == Ticket.Status.CHIUSO || optOperatore.get().getCategoriaTicket() != existingTicket.getCategoriaTicket()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Non sei autorizzato"));

	    }
	    

	    if (ticketDto.getStatus() == Ticket.Status.CHIUSO) {
	    	if (ticketDto.getTestoMessaggio() != null && ticketDto.getTestoMessaggio().trim() != "") {
	    	  	existingTicket.setDataChiusura(LocalDate.now());
			    Messaggio messaggio = existingTicket.getMessaggio();
			    messaggio.setCorpoOperatore(ticketDto.getTestoMessaggio());
			    messaggioRepository.save(messaggio);	
	    	} else {
		        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.singletonMap("message", "Non puoi chiudere il ticket senza aggiungere un messaggio"));
	    	}
	  
	    }
	    
	    existingTicket.setStatus(ticketDto.getStatus());
	    ticketRepository.save(existingTicket);

	    return ResponseEntity.ok(existingTicket);
	}
	
	@GetMapping("/data")
	public Object getData(HttpServletRequest request) {
	    Optional<Utente> optOperatore = getAuthUser(request);
		
	    if (!optOperatore.isPresent() || optOperatore.get().getRuolo() != Utente.Ruolo.Admin) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Autenticazione richiesta o operatore non presente"));
	    }
	    
		Map<String, List<TicketCountDto>> result = new LinkedHashMap<>();
		result.put("opened", dataService.getAllTicketsCountByMonth());
		result.put("closed", dataService.getAllClosedTicketsCountByMonth());
		return result;
		
	}
	
	private Optional<Utente> getAuthUser(HttpServletRequest request) {
        // Legge l'header "Authorization"
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isEmpty()) {
            String token;
            // Se il token è inviato come "Bearer <token>", lo estrae
            if (authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            } else {
                token = authHeader;
            }
            // Usa il TokenService per ottenere l'utente associato al token
            return tokenService.getAuthUser(token);
        }
        System.out.println("Se non c'è header \"Authorization\", restituisce null");
        // Se non c'è header "Authorization", restituisce null
        return Optional.empty();
	}
}
