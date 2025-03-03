package com.example.GestionaleTicketing;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.GestionaleTicketing.model.CategoriaTicket;
import com.example.GestionaleTicketing.model.Messaggio;
import com.example.GestionaleTicketing.model.Ticket;
import com.example.GestionaleTicketing.model.Utente;
import com.example.GestionaleTicketing.repository.CategoriaTicketRepository;
import com.example.GestionaleTicketing.repository.MessaggioRepository;
import com.example.GestionaleTicketing.repository.TicketRepository;
import com.example.GestionaleTicketing.repository.UtenteRepository;

@Component
public class DataLoader implements CommandLineRunner {
	
	private final UtenteRepository utenteRepository;
	private final CategoriaTicketRepository categoriaTicketRepository;
	private final MessaggioRepository messaggioRepository;
	private final TicketRepository ticketRepository;

	private static final String[] CATEGORIA = { "Ordine e spedizioni", "Account accesso", "Pagamento e fatturazione",
			"Rimborso" };

	private static final List<Map<String, String>> ADMINS = List.of(
			Map.of("nome", "Hazem", "cognome", "Rafat", "email", "Haze@gmail.com", "password", "12345"),
			Map.of("nome", "Angela", "cognome", "Tricario", "email", "Angela@gmail.com", "password", "12345"),
			Map.of("nome", "Fiamma", "cognome", "Trillo", "email", "Fiamma@gmail.com", "password", "12345"),
			Map.of("nome", "John", "cognome", "Yriarte", "email", "John@gmail.com", "password", "12345"));
	private static final List<Map<String, String>> OPERATORI = List.of(
			Map.of("nome", "Alice", "cognome", "Rossi", "email", "alice.rossi@gmail.com", "password", "12345"),
			Map.of("nome", "Marco", "cognome", "Bianchi", "email", "marco.bianchi@gmail.com", "password", "12345"),
			Map.of("nome", "Elena", "cognome", "Verdi", "email", "elena.verdi@gmail.com", "password", "12345"),
			Map.of("nome", "Luca", "cognome", "Neri", "email", "luca.neri@gmail.com", "password", "12345"),
			Map.of("nome", "Giulia", "cognome", "Russo", "email", "giulia.russo@gmail.com", "password", "12345"),
			Map.of("nome", "Davide", "cognome", "Moretti", "email", "davide.moretti@gmail.com", "password", "12345"),
			Map.of("nome", "Francesca", "cognome", "Ferrari", "email", "francesca.ferrari@gmail.com", "password",
					"12345"),
			Map.of("nome", "Matteo", "cognome", "Conti", "email", "matteo.conti@gmail.com", "password", "12345"));

	private static final List<Map<String, String>> UTENTI = List.of(
			Map.of("nome", "Antonio", "cognome", "Gallo", "email", "antonio.gallo@gmail.com", "password", "12345"),
			Map.of("nome", "Sara", "cognome", "Lombardi", "email", "sara.lombardi@gmail.com", "password", "12345"),
			Map.of("nome", "Stefano", "cognome", "Giordano", "email", "stefano.giordano@gmail.com", "password",
					"12345"));

	private static final String[] OGGETTO = { "Richiesta di assistenza tecnica", "Problema con il mio account",
			"Errore nel sistema", "Problemi con il pagamento", "Richiesta di supporto urgente",
			"Domanda sul mio ordine", "Richiesta di informazioni", "Errore di sistema durante la navigazione",
			"Richiesta di modifica dati", "Problema con la connessione", "Ticket di supporto urgente",
			"Impossibile completare la transazione", "Errore nella configurazione dell'account",
			"Richiesta di restituzione prodotto", "Assistenza per il reset della password" };
	private static final String[] MESSAGGI_UTENTE = {
			"Non riesco ad accedere al mio account. Mi dice che la password è errata, ma sono sicuro che sia corretta.",
			"Ho ricevuto il prodotto sbagliato. Vorrei avviare una procedura di restituzione.",
			"Il mio pagamento è stato rifiutato, ma la mia carta di credito non ha alcun problema.",
			"Il mio ordine è in ritardo e non riesco a ottenere informazioni sulla spedizione.",
			"Non riesco a recuperare la password del mio account. Puoi aiutarmi?",
			"Ho bisogno di assistenza per aggiornare il mio indirizzo di spedizione per un ordine recente.",
			"Ho provato a fare un pagamento, ma il sistema non lo ha accettato. Vorrei sapere perché.",
			"Il mio ordine non è stato consegnato entro i tempi previsti. Vorrei un rimborso o una soluzione.",
			"Posso modificare l'indirizzo di spedizione dopo aver effettuato l'ordine?",
			"Il sistema non mi permette di completare l'ordine. Mi dà un errore ogni volta che provo.",
			"La fattura per il mio acquisto è sbagliata, vorrei una correzione.",
			"Come posso restituire un prodotto? Vorrei un rimborso.",
			"Ho bisogno di assistenza con la configurazione del mio account.",
			"Il pagamento con la mia carta di credito è stato rifiutato, ma i fondi sono disponibili.",
			"C'è un errore nel tracking della mia spedizione, potrebbe verificare?",
			"Il mio ordine è stato cancellato, ma non sono stato avvisato. Perché?",
			"Sto cercando di aggiornare i miei dati di pagamento ma non riesco ad accedere alla sezione corretta.",
			"Ho bisogno di chiarimenti sul mio piano di abbonamento, vorrei maggiori informazioni.",
			"Ho bisogno di supporto per la configurazione del mio dispositivo.",
			"Il mio rimborso è stato elaborato, ma non ho ancora ricevuto i fondi. Puoi verificare?",
			"C'è stato un problema con la mia fattura, non corrisponde all'importo pagato." };
	private static final String[] MESSAGGI_OPERATORE = {
			"Grazie per averci contattato. Siamo spiacenti per il problema riscontrato e provvederemo a risolverlo il prima possibile.",
			"Abbiamo ricevuto la tua richiesta di rimborso. Ti informeremo non appena il processo sarà completato.",
			"Il tuo ordine è in fase di spedizione. Ti forniremo il numero di tracking non appena disponibile.",
			"Il pagamento è stato rifiutato. Ti consigliamo di controllare la tua carta di credito o provare un altro metodo di pagamento.",
			"Stiamo esaminando il problema con il login del tuo account. Ti invieremo un'email con ulteriori istruzioni.",
			"Abbiamo aggiornato il tuo indirizzo di spedizione. Il tuo ordine verrà inviato al nuovo indirizzo.",
			"Per favore, prova a ripetere il pagamento con una carta diversa. Se il problema persiste, contattaci di nuovo.",
			"Il tuo ordine è stato spedito e dovrebbe arrivare entro 3-5 giorni lavorativi. Puoi controllare il tracking per ulteriori dettagli.",
			"Abbiamo avviato il processo di restituzione del prodotto. Ti invieremo le istruzioni per restituirlo e ricevere il rimborso.",
			"Abbiamo aggiornato la fattura come richiesto. Puoi verificarla nel tuo account ora.",
			"Ci scusiamo per l'inconveniente. Stiamo risolvendo il problema tecnico. Ti aggiorneremo non appena il sistema sarà di nuovo attivo.",
			"Siamo spiacenti per il ritardo nell'elaborazione del rimborso. Stiamo facendo il possibile per accelerare il processo.",
			"Il tuo problema con il pagamento è stato risolto. Puoi provare di nuovo a completare l'acquisto.",
			"Abbiamo avviato il processo di recupero della tua password. Ti invieremo un'email con le istruzioni per reimpostarla.",
			"Grazie per la tua pazienza. Abbiamo risolto il problema di accesso e il tuo account è ora attivo.",
			"Il rimborso è stato elaborato correttamente. Ti invieremo una conferma una volta che il pagamento sarà stato restituito.",
			"Per favore, invia il numero dell'ordine o altre informazioni per poter risolvere il problema più rapidamente.",
			"Abbiamo riscontrato un errore nel sistema, ma stiamo lavorando per risolverlo. Ti faremo sapere non appena sarà tutto risolto.",
			"Il problema con la spedizione è stato risolto. Ora il tuo pacco dovrebbe essere in consegna.",
			"Abbiamo completato la modifica richiesta e il tuo account è stato aggiornato con successo.",
			"Ti invitiamo a fornire maggiori dettagli sul problema per poterti aiutare al meglio." };

	@Autowired
	public DataLoader(UtenteRepository utenteRepository, CategoriaTicketRepository categoriaTicketRepository,
			MessaggioRepository messaggioRepository, TicketRepository ticketRepository) {
		this.utenteRepository = utenteRepository;
		this.categoriaTicketRepository = categoriaTicketRepository;
		this.messaggioRepository = messaggioRepository;
		this.ticketRepository = ticketRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		if (utenteRepository.count() == 0) {

			// Creazione ADMINS
			List<Utente> admins = ADMINS.stream().map(admin -> new Utente(null, admin.get("nome"), admin.get("cognome"),
					admin.get("email"), admin.get("password"), Utente.Ruolo.Admin)).collect(Collectors.toList());
			utenteRepository.saveAll(admins);

			// Creazione Utenti
			List<Utente> utenti = UTENTI
					.stream().map(utente -> new Utente(null, utente.get("nome"), utente.get("cognome"),
							utente.get("email"), utente.get("password"), Utente.Ruolo.Utente))
					.collect(Collectors.toList());
			List<Utente> savedUtenti = utenteRepository.saveAll(utenti);

			// Creazione Categorie
			List<CategoriaTicket> categorie = Stream.of(CATEGORIA)
					.map(categoria -> new CategoriaTicket(null, categoria)).collect(Collectors.toList());
			List<CategoriaTicket> savedCategoria = categoriaTicketRepository.saveAll(categorie);

			// Creazione Operatori
			Iterator<Map<String, String>> operatoriIterator = OPERATORI.iterator();
			Random random = new Random();
			Ticket.Status[] status = Ticket.Status.values();
			savedCategoria.forEach(categoria -> {

				for (int i = 0; i < 2 && operatoriIterator.hasNext(); i++) {
					Map<String, String> utente = operatoriIterator.next();
					Utente savedOperatore = utenteRepository
							.save(new Utente(null, utente.get("nome"), utente.get("cognome"), utente.get("email"),
									utente.get("password"), Utente.Ruolo.Operatore, categoria));

					for (int j = 0; j < 10; j++) {
						// Creazione Ticket + Messaggio
						Ticket.Status ticketStatus = getRandomTicketStatus(random, status);
						LocalDate dataApertura = getRandomDate(random, LocalDate.now().minusYears(1), LocalDate.now());
						Ticket ticket = new Ticket(null, getRandomOggetto(random), ticketStatus, dataApertura, categoria,
								getRandomUtente(random, savedUtenti), savedOperatore);
						
						Messaggio messaggio = new Messaggio();
						if (ticketStatus == Ticket.Status.CHIUSO) {
							messaggio.setCorpoOperatore(getRandomMessaggioOperatore(random));
							ticket.setDataChiusura(getRandomDate(random, dataApertura, LocalDate.now()));
						}
						Ticket savedTicket = ticketRepository.save(ticket);
						messaggio.setCorpoUtente(getRandomMessaggioUtente(random));
						messaggio.setTicket(savedTicket);
						Messaggio savedMessaggio = messaggioRepository.save(messaggio);
					}
				}

			});

		}

	}

	public static Utente getRandomUtente(Random random, List<Utente> savedUtenti) {
		int index = random.nextInt(savedUtenti.size());
		return savedUtenti.get(index);
	}

	public static String getRandomOggetto(Random random) {
		int index = random.nextInt(OGGETTO.length);
		return OGGETTO[index];
	}

	public static String getRandomMessaggioUtente(Random random) {
		int index = random.nextInt(MESSAGGI_UTENTE.length);
		return MESSAGGI_UTENTE[index];
	}

	public static String getRandomMessaggioOperatore(Random random) {
		int index = random.nextInt(MESSAGGI_OPERATORE.length);
		return MESSAGGI_OPERATORE[index];
	}

	public static Ticket.Status getRandomTicketStatus(Random random, Ticket.Status[] status) {
		int index = random.nextInt(status.length);
		return status[index];
	}

	public static LocalDate getRandomDate(Random random, LocalDate start, LocalDate end) {
		long startEpoch = start.toEpochDay();
		long endEpoch = end.toEpochDay();
		long randomEpochDay = startEpoch + random.nextLong(endEpoch - startEpoch);
		return LocalDate.ofEpochDay(randomEpochDay);
	}
}

