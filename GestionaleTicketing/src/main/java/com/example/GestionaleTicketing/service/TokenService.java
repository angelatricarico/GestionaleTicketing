package com.example.GestionaleTicketing.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GestionaleTicketing.model.Utente;
import com.example.GestionaleTicketing.repository.UtenteRepository;

@Service
public class TokenService {

	@Autowired
	private UtenteRepository utenteRepository;

	public Utente generateToken(String email, String password) {

		Optional<Utente> optionalUser = utenteRepository.findByEmail(email);

		if (!optionalUser.isPresent()) {
			throw new IllegalArgumentException("L'utente non esiste");
		}

		Utente user = optionalUser.get();

		if (!user.getEmail().equals(email) || !user.getPassword().equals(password)) {
			throw new IllegalArgumentException("Credenziali non valide");
		}

		String token = UUID.randomUUID().toString();

		user.setToken(token);

		return utenteRepository.save(user);
	}

	public Optional<Utente> getAuthUser(String token) {
		Optional<Utente> optionalUser = utenteRepository.findByToken(token);
		if (!optionalUser.isPresent()) {
			throw new IllegalArgumentException("Utente non trovato");
		}
		return optionalUser;
	}

	public void removeToken(String token) {
		Optional<Utente> optionalUser = utenteRepository.findByToken(token);
		if (!optionalUser.isPresent()) {
			throw new IllegalArgumentException("Utente non trovato");
		}
		Utente user = optionalUser.get();
		user.setToken(null);
		utenteRepository.save(user);
	}

}
