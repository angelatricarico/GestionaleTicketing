// Registrazione operatore per admin
document.getElementById("Registrazione-form").addEventListener("submit", (event) => {
    event.preventDefault();

    const token = localStorage.getItem("authToken");
    const nome = document.getElementById("nome").value.trim();
    const cognome = document.getElementById("Cognome").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const confermaPassword = document.getElementById("confermaPassword").value.trim();
    const categoria = document.getElementById("categoria").value; 
    const privacyChecked = document.getElementById("privacy").checked;
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;

    if (!nome || !cognome || !email || !password || !confermaPassword || !categoria) {
        alert("Compila tutti i campi, inclusa la categoria.");
        return;
    }

    if (confermaPassword !== password) {
        alert("Conferma password errata!");
        return;
    }

    if (!privacyChecked) {
        alert("Bisogna accettare la Privacy Policy per continuare.");
        return;
    }

    if (!emailRegex.test(email)) {
        alert("Inserisci un'email valida.");
        return;
    }

    fetch("http://localhost:8080/utenti/operatore", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization" : token
        },
        body: JSON.stringify({
            nome: nome,
            cognome: cognome,
            email: email,
            password: password,
            categoriaTicket: {
                id : parseInt(categoria)
            } 
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Errore di registrazione");
        }
        return response.json();
    })
    .then(data => {
        window.alert("Creazione account avvenuta con successo!");
        window.location.href = "home.html";
    })
    .catch(error => {
        alert("Si è verificato un errore durante la registrazione.");
    });
});