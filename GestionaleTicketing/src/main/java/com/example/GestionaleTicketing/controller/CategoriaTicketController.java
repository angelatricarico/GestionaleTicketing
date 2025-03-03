package com.example.GestionaleTicketing.controller;

import java.util.Collections;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

import com.example.GestionaleTicketing.model.CategoriaTicket;
import com.example.GestionaleTicketing.model.Utente;
import com.example.GestionaleTicketing.repository.CategoriaTicketRepository;
import com.example.GestionaleTicketing.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorieTicket")
@CrossOrigin ("*")

public class CategoriaTicketController {
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	CategoriaTicketRepository categoriaTicketRepository;
	
	
	//Visualizzazione tutte le categorie da utente, operatore e admin
	@GetMapping
	public List <CategoriaTicket> getAllCategoriaTicket(HttpServletRequest request, HttpServletResponse response ) {
		
		return categoriaTicketRepository.findAll();
	}
	
	
	//Visualizzazione categoria in particolare da utente, operatore e admin
	@GetMapping("/{id}")
	public Object getCategoriaTicket(@PathVariable Long id) {
		return categoriaTicketRepository.findById(id);
	}
	
	
	//Creazione nuova categoria da admin
	@PostMapping
	public Object createCategoriaTicket(@Valid @RequestBody CategoriaTicket categoriaTicket, HttpServletRequest request, HttpServletResponse response ) {
		Optional <Utente> admin = getAuthUser(request);
		
		if (admin.get().getRuolo() != Utente.Ruolo.Admin) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return Collections.singletonMap("message", "Autenticazione richiesta");
		}
		
		return categoriaTicketRepository.save(categoriaTicket);
	}
	
	
	//Modifica categoria esistente da admin
	@PutMapping("/{id}")
	public Object updateCategoriaTicket(@PathVariable Long id, @Valid @RequestBody CategoriaTicket categoriaTicket, HttpServletRequest request, HttpServletResponse response) {
		Optional <Utente> admin = getAuthUser(request);
		
		if (admin.get().getRuolo() != Utente.Ruolo.Admin) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return Collections.singletonMap("message", "Autenticazione richiesta");
		}
		
		
		Optional <CategoriaTicket> cat = categoriaTicketRepository.findById(id);
		
    	if (!cat.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return Collections.singletonMap("message", "Categoria non presente");
        }
		
    	CategoriaTicket existingCategoria = cat.get();
        
    	existingCategoria.setNomeCategoria(categoriaTicket.getNomeCategoria());
    	
    	categoriaTicketRepository.save(existingCategoria); 
    	return new ResponseEntity<>(existingCategoria, HttpStatus.OK);
	}
	
	
	
	//Eliminazione categoria in particolare da admin
	@DeleteMapping("/{id}")
	public Object deleteCategoriaTicket(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Optional <Utente> admin = getAuthUser(request);
		
		if (admin.get().getRuolo() != Utente.Ruolo.Admin) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return Collections.singletonMap("message", "Autenticazione richiesta");
		}
		
		Optional <CategoriaTicket> cat = categoriaTicketRepository.findById(id);
		
    	if (!cat.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return Collections.singletonMap("message", "Categoria non presente");
        }
    	
    	categoriaTicketRepository.delete(cat.get());
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
