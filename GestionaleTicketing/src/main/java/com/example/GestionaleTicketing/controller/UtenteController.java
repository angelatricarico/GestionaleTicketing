package com.example.GestionaleTicketing.controller;

import java.util.Collections;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.GestionaleTicketing.model.Utente;
import com.example.GestionaleTicketing.repository.UtenteRepository;
import com.example.GestionaleTicketing.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/utenti")
@CrossOrigin ("*")

public class UtenteController {
	
	@Autowired
	UtenteRepository utenteRepository;
	
	@Autowired
	TokenService tokenService;
	
	//Creazione profilo utente da utente
	@PostMapping
	public Utente createUtente(@Valid @RequestBody Utente utente) {
		utente.setRuolo(Utente.Ruolo.Utente);
		System.out.println(utente.getRuolo() + "utente1");
		return utenteRepository.save(utente);
	}
	
	//Creazione profilo utente operatore da admin
	@PostMapping("/operatore")
	public Object createOperatore(@Valid @RequestBody Utente utente, HttpServletRequest request, HttpServletResponse response) {
		Optional <Utente> admin = getAuthUser(request);
		
		if (admin.get().getRuolo() != Utente.Ruolo.Admin) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return Collections.singletonMap("message", "Autenticazione richiesta");
		}
		utente.setRuolo(Utente.Ruolo.Operatore);
		System.out.println(utente.getRuolo());
		return utenteRepository.save(utente);
	}
	
	//Visualizzazione di tutti gli utenti da admin
	@GetMapping("/utentiAdmin")
	public Object getUtenti(@RequestParam(value = "ruolo", defaultValue = " ") String ruolo, HttpServletRequest request, HttpServletResponse response ) {
		Optional <Utente> admin = getAuthUser(request);
		
		if (admin.get().getRuolo() != Utente.Ruolo.Admin) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return Collections.singletonMap("message", "Autenticazione richiesta");
		}
		
		if (ruolo.equalsIgnoreCase("all")) {
			return utenteRepository.findAll();
		} else if (ruolo.equalsIgnoreCase(Utente.Ruolo.Operatore.toString())) {
			return utenteRepository.findAllByRuolo(Utente.Ruolo.Operatore);
		} else if (ruolo.equalsIgnoreCase(Utente.Ruolo.Utente.toString())) {
			return utenteRepository.findAllByRuolo(Utente.Ruolo.Utente);
		}
    	return Collections.emptyList(); 
		
	}
	
	//Visualizzazione utente specifico da utente
	@GetMapping
	public Utente getUtente(HttpServletRequest request, HttpServletResponse response ) {
		Optional <Utente> utente = getAuthUser(request);
		return utente.get();
	}
	
	//Aggiornamento profilo utente da utente
	@PutMapping
	public Object updateUtente(@RequestBody Utente utente, HttpServletRequest request, HttpServletResponse response) {
       	Optional<Utente> authUser = getAuthUser(request);

    	if (!authUser.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return Collections.singletonMap("message", "Utente non trovato");
        }
    	
    	Utente existingUtente = authUser.get();
        
    	existingUtente.setNome(utente.getNome());
    	existingUtente.setCognome(utente.getCognome());
    	existingUtente.setEmail(utente.getEmail());
    	if (utente.getPassword() != null && !utente.getPassword().trim().equalsIgnoreCase("")) {
    		existingUtente.setPassword(utente.getPassword());    		
    	}
    	
    	utenteRepository.save(existingUtente); 
    	return new ResponseEntity<>(existingUtente, HttpStatus.OK);
		
	}
	
	//Eliminazione profilo utente da utente
	@DeleteMapping
	public void deleteUtente(HttpServletRequest request, HttpServletResponse response) {
		Optional <Utente> utente = getAuthUser(request);
		utenteRepository.delete(utente.get());
	}
	
	//Eliminazione profilo operatore da admin
	@DeleteMapping("/operatore/{id}")
	public Object deleteOperatore(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Optional <Utente> admin = getAuthUser(request);
		
		if (admin.get().getRuolo() != Utente.Ruolo.Admin) {
	           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            return Collections.singletonMap("message", "Autenticazione richiesta");

		}
		
		Optional <Utente> operatore = utenteRepository.findById(id);
		
		if (operatore.isEmpty()) {
	           response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	            return Collections.singletonMap("message", "Operatore non trovato");
		}
		
		if (operatore.get().getRuolo() != Utente.Ruolo.Operatore) {
	           response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	            return Collections.singletonMap("message", "L'utente non è un operatore");
		}
		
		utenteRepository.delete(operatore.get());
    	return new ResponseEntity<>(HttpStatus.OK);
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
        return null;
    }
	
	
	

}
