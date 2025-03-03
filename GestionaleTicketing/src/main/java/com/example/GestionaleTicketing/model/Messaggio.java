package com.example.GestionaleTicketing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Messaggio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@NotBlank(message = "Messaggio ticket è obbligatorio")
	private String corpoUtente;
	
	private String corpoOperatore;
	
	
	@OneToOne
	@JoinColumn(name = "idTicket")
	private Ticket ticket;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public String getCorpoUtente() {
		return corpoUtente;
	}

	public void setCorpoUtente(String corpoUtente) {
		this.corpoUtente = corpoUtente;
	}

	public String getCorpoOperatore() {
		return corpoOperatore;
	}

	public void setCorpoOperatore(String corpoOperatore) {
		this.corpoOperatore = corpoOperatore;
	}
	
	

}
