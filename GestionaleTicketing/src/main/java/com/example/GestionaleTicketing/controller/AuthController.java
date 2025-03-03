package com.example.GestionaleTicketing.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.GestionaleTicketing.model.Utente;
import com.example.GestionaleTicketing.repository.UtenteRepository;
import com.example.GestionaleTicketing.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin ("*")
public class AuthController {
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	UtenteRepository utenteRepository;
	
	@PostMapping("/login")
	public Map<String, String> login(@RequestBody Map<String, String> body, HttpServletResponse response) {
        
   	 // Estrae username e password dalla richiesta JSON
	String email = body.get("email");
    String password = body.get("password");

       // Mappa per la risposta
       Map<String, String> result = new HashMap<>();

       if (email == null || password == null) {
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           result.put("message", "Credenziali non valide");
           return result;
       }

       // Genera un token associato all'utente
       Utente utente = tokenService.generateToken(email, password);

       // Costruisce la risposta con messaggio, ruolo e token
       result.put("message", "Login effettuato con successo");
       result.put("role", utente.getRuolo().toString());
       result.put("token", utente.getToken());
       return result;
	}
	
	
	@GetMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		Optional<Utente> optUtente = getAuthUser(request);
		optUtente.get().setToken(null);
		utenteRepository.save(optUtente.get());
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
