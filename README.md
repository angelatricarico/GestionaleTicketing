PROGETTO n.2: Sistema di Ticketing per Supporto Clienti 

1. Introduzione 

Il progetto prevede lo sviluppo di un sistema di ticketing per il supporto clienti, che consenta 
consente agli utenti di segnalare problemi, ricevere assistenza e monitorare lo stato delle richieste. Il sistema sarà basato su Spring Boot per il backend, API REST per la comunicazione, Bootstrap per l'interfaccia utente e JavaScript per garantire un'esperienza interattiva. 

2. Obiettivi del Progetto 
Fornire un sistema efficiente per la gestione delle richieste di supporto. 
Consentire agli utenti di creare, visualizzare e aggiornare i ticket. 
Integrare un sistema di assegnazione automatica per i ticket. 

3. Architettura del Sistema 

3.1 Tecnologie Utilizzate 
Backend: Spring Boot, Spring Data JPA Database: MySQL Frontend: Bootstrap, JavaScript (Fetch API) 

3.2 Modello Entity-Relationship Sviluppare lo schema (e relative relazioni) per almeno le seguenti entità: 
Utente 
Ticket 
Categorie ticket 
Messaggio 

4. Funzionalità Principali

4.1 Gestione Utenti Registrazione e login con autenticazione. Dashboard per clienti e operatori di supporto. Possibilità di visualizzare e aggiornare il profilo utente. 

4.2 Creazione e Gestione Ticket Creazione di nuovi ticket con dettagli e priorità. Assegnazione automatica/manuale ai membri del supporto. Filtri avanzati per la ricerca dei ticket. Storico dei ticket gestiti per ogni utente. 

4.3 Dashboard di Gestione Visualizzazione globale di ticket aperti, in lavorazione e chiusi. Reportistica e analisi del tempo di risposta e risoluzione. (Opzionale) Possibilità di esportare dati in CSV/PDF. (Opzionale) 

5. Sicurezza Ruoli e permessi per limitare l’accesso alle funzionalità.
