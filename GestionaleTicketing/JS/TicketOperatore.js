
document.addEventListener("DOMContentLoaded", function() {
    createTicket();
});

// Funzione per caricare e mostrare i ticket
async function createTicket() {
    const URL = `http://localhost:8080/ticket`;
    const token = localStorage.getItem("authToken");

    try {
        const response = await fetch(URL, {
            headers: {
                "Authorization" : token
            }
        });
        if (!response.ok) {
            throw new Error("Errore nel recupero dei ticket");
          }
    const dati = await response.json();

var num = 1;
  dati.forEach (ticket => {
        const numero = document.createElement("th");
        numero.id = "id_ticket" + ticket.id;
        numero.textContent = num;
        num += 1;

        const tr = document.createElement("tr");
        tr.id = "riga" + ticket.id;

        const utente = document.createElement("td");
        utente.id = "utente_ticket" + ticket.id;
        utente.textContent = ticket.utente.email;

        const data_ap = document.createElement("td");
        data_ap.id = "data_ticket_ap"+ ticket.id;
        data_ap.textContent = convertToItalianDate(ticket.dataApertura);
        data_ap.classList.add("data-tb");

        const data_chi = document.createElement("td");
        data_chi.id = "data_ticket_chi"+ ticket.id;
        data_chi.textContent = convertToItalianDate(ticket.dataChiusura);
        data_chi.classList.add("data-tb");

        const oggetto = document.createElement("td");
        oggetto.id = "oggetto_ticket"+ ticket.id;
        oggetto.textContent = ticket.oggetto;   
       
        tr.appendChild(numero);
        tr.appendChild(utente);
        tr.appendChild(data_ap);
        tr.appendChild(data_chi);
        tr.appendChild(oggetto);
//Aggiunta bottone
        
const selectStato = document.createElement("select");
const th = document.createElement("th");

selectStato.id = "stato_ticket" + ticket.id;
th.classList.add("stato-th");

selectStato.addEventListener("change", () => {
    modificaStato(ticket.id, ticket.id, ticket.categoriaTicket.id, ticket.messaggio.corpoUtente);
    aggiornaColoreBottone(selectStato);
}
    
);
 selectStato.classList.add("btn", "btn-primary", "stato");

const stati = ["APERTO", "VISUALIZZATO", "IN_LAVORAZIONE", "CHIUSO"];

stati.forEach(status => {
  const stato_btn = document.createElement("option");
  stato_btn.value = status;
  stato_btn.textContent = status;
  selectStato.appendChild(stato_btn);

});

selectStato.value = ticket.status;

if (ticket.status == "CHIUSO") {
    selectStato.disabled = true;
}



th.appendChild(selectStato);
tr.appendChild(th);
tbody.appendChild(tr);

aggiornaColoreBottone(selectStato);
});

  } catch (error) {
    console.error("Errore:", error);
    
  }
}



// Funzione colori per bottone STATUS ticket
function aggiornaColoreBottone(stato_btn) {
    if (stato_btn.innerHTML === "APERTO") {
        stato_btn.style.backgroundColor = "red";
        stato_btn.style.borderColor = "red";
        stato_btn.style.color = "white";
    } else if (stato_btn.innerHTML === "VISUALIZZATO") {
        stato_btn.style.backgroundColor = "blue";
        stato_btn.style.borderColor = "blue";
        stato_btn.style.color = "white";
    } else if (stato_btn.innerHTML === "IN_LAVORAZIONE") {
        stato_btn.style.backgroundColor = "yellow";
        stato_btn.style.borderColor = "yellow";
        stato_btn.style.color = "black";
        stato_btn.innerHTML = "IN LAVORAZIONE";
    } else if (stato_btn.innerHTML === "CHIUSO") {
        stato_btn.style.backgroundColor = "gray";
        stato_btn.style.borderColor = "gray";
        stato_btn.style.color = "white";
    }
}
    
// Funzione per inviare messaggio
async function inviaMessaggio(ticketId, messaggio, idCategoria) {
        console.log("invia messaggio");
        const token = localStorage.getItem("authToken"); 
        if (!idCategoria) {
            console.error("ERRORE: idCategoria è undefined!");
            alert("Errore: ID Categoria non è definito. Impossibile inviare il messaggio.");
            return;
        }
    
        try {
            const requestBody = {
                status: "CHIUSO",
                testoMessaggio: messaggio,
                idCategoria: parseInt(idCategoria)
            };
    
            const response = await fetch(`http://localhost:8080/ticket/${ticketId}`, {
                method: "PUT",
                headers: {
                    "Authorization": token,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(requestBody)
            });
    
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Errore API: ${response.status} - ${errorText}`);
            }
    
            return await response.json();
        } catch (error) {
            console.error("Errore durante l'invio del messaggio:", error);
            alert(`Errore nell'invio del messaggio: ${error.message}`);
            throw error;
        }
}
    
    
// Funzione per aprire il popup di invio messaggio
function apriPopup(ticketId, idCategoria, oggetto, messaggio, callback) {   
        console.log("ID Categoria ricevuto nel popup:", idCategoria); 
        console.log("Messaggio ricevuto da backend: " + messaggio);
    
        // Rimuove il popup esistente se già presente
        const esistente = document.getElementById("popup-messaggio");
        if (esistente) esistente.remove();
    
        // Creazione dell'overlay
        const overlay = document.createElement("div");
        overlay.id = "popup-messaggio";
        overlay.style.position = "fixed";
        overlay.style.top = "0";
        overlay.style.left = "0";
        overlay.style.width = "100%";
        overlay.style.height = "100%";
        overlay.style.background = "rgba(0, 0, 0, 0.5)";
        overlay.style.display = "flex";
        overlay.style.alignItems = "center";
        overlay.style.justifyContent = "center";
        overlay.style.zIndex = "1000";
    
        // Creazione del popup
        const popupBody = document.createElement("div");
        popupBody.style.background = "#ffaa00";
        popupBody.style.padding = "20px";
        popupBody.style.borderRadius = "10px";
        popupBody.style.width = "600px";
        popupBody.style.textAlign = "center";
        popupBody.style.boxShadow = "0px 4px 10px rgba(0, 0, 0, 0.3)";
        popupBody.style.position = "relative";
    
        popupBody.innerHTML = `
            <button class="popup-close" style="
                position: absolute;
                top: 10px;
                right: 10px;
                border: none;
                background: none;
                font-size: 20px;
                cursor: pointer;
            ">×</button>
            <h2>Invia un Messaggio</h2>
    
            <div style="text-align: left; background: white; padding: 10px; border-radius: 5px; margin-bottom: 10px; color: black; word-wrap: break-word; overflow-wrap: break-word;">
                <strong>Oggetto:</strong> ${oggetto || "N/A"} <br>
                <strong>Messaggio:</strong> ${messaggio || "Nessun messaggio iniziale"}
            </div>
    
            <textarea id="messaggio-input" placeholder="Scrivi un messaggio..." style="
                width: 100%;
                height: 150px;
                margin-top: 10px;
                padding: 5px;
                border-radius: 5px;
                border: 1px solid #ccc;
            "></textarea>
    
            <button id="invia-messaggio" class="btn btn-primary" style="
                margin-top: 10px;
                padding: 10px 20px;
                border: none;
                background: #007bff;
                color: white;
                border-radius: 5px;
                cursor: pointer;
            ">Invia</button>
        `;
    
        overlay.appendChild(popupBody);
        document.body.appendChild(overlay);
    
        // Chiudi il popup quando si clicca sulla "X"
        document.querySelector(".popup-close").onclick = () => overlay.remove();
    
        // Gestione dell'invio del messaggio
        document.getElementById("invia-messaggio").onclick = async () => {
            const nuovoMessaggio = document.getElementById("messaggio-input").value.trim();
            if (!nuovoMessaggio) {
                alert("Inserisci un messaggio.");
                return;
            }
    
            await inviaMessaggio(ticketId, nuovoMessaggio, idCategoria);
            overlay.remove();
            if (callback) callback();
        };
}
    
    
// Filtro ticket
document.addEventListener("DOMContentLoaded", function() {
        const filtri = document.querySelectorAll(".dropdown-item");
    
        filtri.forEach(filtro => {
            filtro.addEventListener("click", function() {
                const statoSelezionato = this.id;
                const righe = document.querySelectorAll("#tbody tr"); //seleziona tutte le righe della tabella
    
                filtri.forEach(btn => btn.classList.remove("active"));
                this.classList.add("active");
    
                console.log(statoSelezionato);
                righe.forEach(riga => {
                   
                    const statoBtn = riga.querySelector(".stato");
                    if (statoBtn) {
                        const statoTicket = statoBtn.value;
    
                        if (statoTicket === statoSelezionato || statoSelezionato === "tutti" ) {
                            riga.style.display = "";
                        } else if (statoTicket !== statoSelezionato) {
                            riga.style.display = "none";
                        }
                    }
                });
            });
        });
});
    
    
// Funzione per aggiornare lo stato (con messaggio se si sceglie CHIUSO)
async function modificaStato(idTicket, num, idCategoria, messaggio) { 
    const stato_btn = document.getElementById("stato_ticket" + num);

    if (!stato_btn) {
        console.error("Bottone stato non trovato!");
        return;
    }

    if (stato_btn.value === "CHIUSO") {
        alert("Devi inviare un messaggio prima di chiudere il ticket.");

        try {
            const response = await fetch(`http://localhost:8080/ticket/${idTicket}`, {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("authToken")}`
                }
            });

            if (!response.ok) {
                throw new Error(`Errore nel recupero del ticket: ${response.status}`);
            }

            const ticket = await response.json();

            apriPopup(idTicket, idCategoria, ticket.oggetto, messaggio, async () => {        
                console.log("Messaggio inviato, ora chiudiamo il ticket...");

                try {
                    stato_btn.disabled = true;
                    stato_btn.setAttribute("disabled", "true");
                    stato_btn.style.pointerEvents = "none";

                    const updateResponse = await fetch(`http://localhost:8080/ticket/${idTicket}`, {
                        method: "PUT",
                        headers: {
                            "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            "status": "CHIUSO",
                            "idCategoria": parseInt(idCategoria)
                        })
                    });

                    if (!updateResponse.ok) {
                        throw new Error(`Errore nell'aggiornamento dello stato: ${updateResponse.status}`);
                    }

                    const updatedTicket = await updateResponse.json();
                    console.log("Ticket chiuso con successo:", updatedTicket);

                } catch (error) {
                    console.error("Errore nell'aggiornamento dello stato:", error);
                }
            });

        } catch (error) {
            console.error("Errore nel recupero del ticket:", error);
        }

        return; 
    }

    // Se lo stato non è CHIUSO, aggiorniamolo normalmente
    fetch(`http://localhost:8080/ticket/${idTicket}`, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "status": stato_btn.value,
            "idCategoria": parseInt(idCategoria)
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Errore HTTP: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log("Stato aggiornato:", data);
        
    })
    .catch(error => {
        console.error("Errore aggiornamento stato:", error);
    });
}

// Conversione data in formato italiano
function convertToItalianDate(dataStringata) {
    if (!dataStringata) {
        return null;
    }
    const date = new Date(dataStringata);
    const formatter = date.toLocaleDateString('it-IT');
    return formatter;
}


    
   