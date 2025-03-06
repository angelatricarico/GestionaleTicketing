
const token = localStorage.getItem("authToken");
const ruolo = localStorage.getItem("ruolo");
// Funzione per caricare e mostrare i ticket
async function createTicket() {
    const URL = `http://localhost:8080/ticket`;
    const token = localStorage.getItem("authToken");

    try {
        const response = await fetch(URL, {
            headers: {
                "Authorization": token
            }
        });
        if (!response.ok) {
            throw new Error("Errore nel recupero dei ticket");
        }
        const dati = await response.json();

        var num = 1;
        dati.forEach(ticket => {
            const tr = document.createElement("tr");
            tr.id = "riga" + ticket.id;
            tr.classList.add("ticket-row");
            tr.addEventListener("click", () => apriPopup(ticket.id, ticket.idCategoria, ticket.oggetto, ticket.messaggio.corpoUtente, ticket.messaggio.corpoOperatore));

            const numero = document.createElement("th");
            numero.id = "id_ticket" + ticket.id;
            numero.textContent = num;
            num += 1;

            const data_ap = document.createElement("td");
            data_ap.id = "data_ticket_ap" + ticket.id;
            data_ap.textContent = convertToItalianDate(ticket.dataApertura);
            data_ap.classList.add("data-tb");

            const data_chi = document.createElement("td");
            data_chi.id = "data_ticket_chi" + ticket.id;
            data_chi.textContent = convertToItalianDate(ticket.dataChiusura);
            data_chi.classList.add("data-tb");

            const oggetto = document.createElement("td");
            oggetto.id = "oggetto_ticket" + ticket.id;
            oggetto.textContent = ticket.oggetto;

            console.log(ticket.oggetto);
            console.log(ticket);

            tr.appendChild(numero);
            tr.appendChild(data_ap);
            tr.appendChild(data_chi);
            tr.appendChild(oggetto);

            // Aggiunta bottone
            const stato = document.createElement("td");
            const stato_btn = document.createElement("button");
            stato.id = "stato_ticket" + ticket.id;
            stato_btn.classList.add("btn", "btn-primary", "stato");
            stato_btn.textContent = ticket.status;
            stato.appendChild(stato_btn);
            tr.appendChild(stato);
            aggiornaColoreBottone(stato_btn);

            const tbody = document.getElementById("tbody");
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error("Errore:", error);
    }
}

// Funzione per aprire pop up e mostrare scambio messaggi utente-operatore
function apriPopup(ticketId, idCategoria, oggetto, messaggioUtente, messaggioOperatore) {
    console.log("Apri popup per il ticket:", ticketId);
    
    const esistente = document.getElementById("popup-messaggio");
    if (esistente) esistente.remove();

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

    const popupBody = document.createElement("div");
    popupBody.style.background = "#ffaa00";
    popupBody.style.padding = "20px";
    popupBody.style.borderRadius = "10px";
    popupBody.style.width = "600px";
    popupBody.style.textAlign = "center";
    popupBody.style.boxShadow = "0px 4px 10px rgba(0, 0, 0, 0.3)";
    popupBody.style.position = "relative";

    popupBody.innerHTML = `
        <button class="popup-close" style="position: absolute; top: 10px; right: 10px; border: none; background: none; font-size: 20px; cursor: pointer;">×</button>
        <h2>Dettagli Ticket</h2>
        <div style="text-align: left; background: white; padding: 10px; border-radius: 5px; margin-bottom: 10px; color: black; word-wrap: break-word; overflow-wrap: break-word;">
            <strong>Oggetto:</strong> ${oggetto || "N/A"} <br>
            <strong>Messaggio Utente:</strong> ${messaggioUtente} <br>
            <strong>Messaggio Operatore:</strong> ${messaggioOperatore || "Nessuna risposta operatore"}
        </div>
    `;

    overlay.appendChild(popupBody);
    document.body.appendChild(overlay);

    document.querySelector(".popup-close").onclick = () => overlay.remove();
}

createTicket();



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
                    const statoTicket = statoBtn.innerHTML.trim();

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

// Conversione data in formato italiano
function convertToItalianDate(dataStringata) {
    if (!dataStringata) {
        return null;
    }
    const date = new Date(dataStringata);
    const formatter = date.toLocaleDateString('it-IT');
    return formatter;
}