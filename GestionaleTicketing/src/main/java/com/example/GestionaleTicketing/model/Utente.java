package com.example.GestionaleTicketing.model;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity

public class Utente {
	
	public enum Ruolo {
		Utente,
		Operatore,
		Admin
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@NotBlank(message = "Nome è obbligatorio")
	private String nome;
	
	@NotBlank(message = "Cognome è obbligatorio")
	private String cognome;
	
	@NotBlank(message = "Email è obbligatoria")
	@Email(message = "Formato email non valido")
	@Column(unique = true)
	private String email;
	
	@NotBlank(message = "Password è obbligatoria")
	private String password;
	
	@JsonIgnore
	private String token;
	
	@ManyToOne
	@JoinColumn(name = "idCategoria")
	private CategoriaTicket categoriaTicket;
	
	@OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private List<Ticket> tickets;
	
	@OneToMany(mappedBy = "operatore", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.SET_NULL)
	@JsonIgnore
	private List<Ticket> ticketsOperatore;
	
	@Enumerated(EnumType.STRING)
	private Ruolo ruolo;
	
	public Utente() {
		
	}

	public Utente(Long id, String nome, String cognome, String email, String password, Ruolo ruolo) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.password = password;
		this.ruolo = ruolo;
	}
	
	public Utente(Long id, String nome, String cognome, String email, String password, Ruolo ruolo, CategoriaTicket categoriaTicket) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.password = password;
		this.ruolo = ruolo;
		this.categoriaTicket = categoriaTicket;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public CategoriaTicket getCategoriaTicket() {
		return categoriaTicket;
	}

	public void setCategoriaTicket(CategoriaTicket categoriaTicket) {
		this.categoriaTicket = categoriaTicket;
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public List<Ticket> getTicketsOperatore() {
		return ticketsOperatore;
	}

	public void setTicketsOperatore(List<Ticket> ticketsOperatore) {
		this.ticketsOperatore = ticketsOperatore;
	}
	
	
	

}
