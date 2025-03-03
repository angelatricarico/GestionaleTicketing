package com.example.GestionaleTicketing.model;

import java.time.LocalDate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Ticket {

	public enum Status {
		APERTO, VISUALIZZATO, IN_LAVORAZIONE, CHIUSO
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Oggetto ticket è obbligatorio")
	private String oggetto;

	@Enumerated(EnumType.STRING)
	private Status status;

	private LocalDate dataApertura;

	private LocalDate dataChiusura;

	@ManyToOne
	@JoinColumn(name = "idCategoria")
	private CategoriaTicket categoriaTicket;

	@ManyToOne
	@JoinColumn(name = "idUtente", nullable = false)
	private Utente utente;

	@ManyToOne
	@JoinColumn(name = "idOperatore")
	private Utente operatore;

	@OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Messaggio messaggio;

	public Ticket() {

	}

	public Ticket(Long id, String oggetto, Status status, LocalDate dataApertura, CategoriaTicket categoriaTicket, Utente utente,
			Utente operatore) {
		super();
		this.id = id;
		this.oggetto = oggetto;
		this.status = status;
		this.dataApertura = dataApertura;
		this.categoriaTicket = categoriaTicket;
		this.utente = utente;
		this.operatore = operatore;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDate getDataApertura() {
		return dataApertura;
	}

	public void setDataApertura(LocalDate dataApertura) {
		this.dataApertura = dataApertura;
	}

	public LocalDate getDataChiusura() {
		return dataChiusura;
	}

	public void setDataChiusura(LocalDate dataChiusura) {
		this.dataChiusura = dataChiusura;
	}

	public CategoriaTicket getCategoriaTicket() {
		return categoriaTicket;
	}

	public void setCategoriaTicket(CategoriaTicket categoriaTicket) {
		this.categoriaTicket = categoriaTicket;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Utente getOperatore() {
		return operatore;
	}

	public void setOperatore(Utente operatore) {
		this.operatore = operatore;
	}

	public Messaggio getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(Messaggio messaggio) {
		this.messaggio = messaggio;
	}

}
