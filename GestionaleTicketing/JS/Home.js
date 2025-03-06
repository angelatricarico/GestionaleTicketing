// Recupero categorie ticket inserite in database
document.addEventListener("DOMContentLoaded", async () => {
    
    const categoryContainer = document.getElementById("categoryContainer");

    const descriptions = {
        "Ordine e spedizioni": "Hai bisogno di aiuto con un ordine o vuoi sapere dove si trova il tuo pacco? Siamo qui per darti tutte le risposte di cui hai bisogno!",
        "Account accesso": "Non riesci ad accedere al tuo account? Non preoccuparti, ti aiuteremo a rientrare velocemente e in sicurezza!",
        "Pagamento e fatturazione": "Dubbi sui pagamenti o sulle fatture? Ti forniremo tutte le informazioni necessarie per gestire al meglio le tue transazioni.",
        "Rimborso": "Hai bisogno di un rimborso? Ti guideremo passo dopo passo per assicurarti che la tua richiesta venga gestita al meglio."
    };
    
    const icons = {
        "Ordine e spedizioni": "fas fa-box",
        "Account accesso": "fas fa-user-lock",
        "Pagamento e fatturazione": "fas fa-credit-card",
        "Rimborso": "fas fa-hand-holding-usd"
    };

    try {
        const response = await fetch("http://localhost:8080/categorieTicket");
        if (!response.ok) {
            throw new Error(`Errore HTTP! Status: ${response.status}`);
        }
        const categories = await response.json();

        categories.forEach(category => {
            const categoryElement = document.createElement("div");
            categoryElement.classList.add("category");

            const description = descriptions[category.nomeCategoria];
            const iconClass = icons[category.nomeCategoria];

            categoryElement.innerHTML = `
                <i class="${iconClass}"></i>
                <h3>${category.nomeCategoria}</h3>
                <p>${description}</p>
            `;

            categoryContainer.appendChild(categoryElement);
        });
        
    } catch (error) {
        console.error("Errore nel recupero delle categorie:", error);
    }

   /* Gestione pulsante ticket */
   const ticketBtn = document.getElementById("ticket-btn");
   const token = localStorage.getItem("authToken");
   const ruolo = localStorage.getItem("userRole"); 
  

   ticketBtn.addEventListener("click", () => {
       if (!token) {
           window.location.href = "login.html";
           return;
       }
     
       Redirect(ruolo);
   });
});


function Redirect(ruolo) {
    
    console.log(`Ruolo ricevuto: '${ruolo}'`);
    if (ruolo === "Admin") {
        window.location.href = "Admin.html";
    } else if (ruolo === "Operatore") {
        window.location.href = "TicketOperatore.html";
    } else if (ruolo === "Utente") {
        window.location.href = "Ticket.html";
    } else {
        console.warn("⚠️ Ruolo non riconosciuto, reindirizzamento al login...");
        window.location.href = "login.html";
    }
}

/* Gestione click footer */
const ticketBtn = document.getElementById("footer-btn");
const token = localStorage.getItem("authToken");
   const ruolo = localStorage.getItem("userRole"); 
   ticketBtn.addEventListener("click", () => {
    if (!token) {
        window.location.href = "login.html";
        return;
    }
  
    RedirectFooter(ruolo);
});

function RedirectFooter(ruolo) {
    if (ruolo === "Admin") {
        window.location.href = "home.html";
    } else if (ruolo === "Operatore") {
        window.location.href = "home.html";
    } else if (ruolo === "Utente") {
        window.location.href = "InvioTicket.html";
    } else {
        console.warn("⚠️ Ruolo non riconosciuto, reindirizzamento al login...");
        window.location.href = "login.html";
    }
}