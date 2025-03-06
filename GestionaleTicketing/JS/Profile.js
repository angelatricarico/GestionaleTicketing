// Funzione per caricare il profilo
function loadProfile(){
    const token = localStorage.getItem("authToken");

    const headers=token? {authorization: "Bearer " + token }: {};

    fetch("http://localhost:8080/utenti", {
        method:"GET",
        headers: headers

    })
    .then((response)=> {
        if(!response.ok) {
            throw new Error("Errore 404!")
        }
        return response.json();
    })
    .then((data) => {
        console.log(data);
        document.getElementById("nome-edit").value = data.nome;
        document.getElementById("cognome-edit").value = data.cognome;
        document.getElementById("email-edit").value = data.email;

    localStorage.setItem("userRole", data.ruolo);  

    updateRole(data.ruolo);
})
    .catch((error) => {
       window.location.href = "Login.html";
        console.error("Errore durante il caricamento del profilo:", error);
    });
}


// Mostra bottoni a seconda del ruolo
function updateRole(ruolo){
    if(ruolo === "Operatore"){
        document.getElementById("ticket-btn").style.display = "inline";
        document.getElementById("btn-logout").style.display = "inline";
        document.getElementById("edit-btn").style.display = "none";
        document.getElementById("delete-btn").style.display = "none";
        
    }else if (ruolo === "Admin"){
        document.getElementById("ticket-btn").style.display = "inline";
        document.getElementById("delete-btn").style.display = "none";
        document.getElementById("btn-logout").style.display = "inline";
        document.getElementById("edit-btn").style.display = "none";

    }
}

// Bottone per mostrare i tickets
function goToTickets() {

    const ruolo = localStorage.getItem("userRole");

    if(ruolo === "Operatore"){
        window.location.href = "../HTML/TicketOperatore.html"; 
    } else if (ruolo === "Admin"){
        window.location.href = "../HTML/Admin.html";
    } else {
        window.location.href = "../HTML/Ticket.html"; 
    }
}

loadProfile();


// Funzione 'modifica' profilo per utente base
function editProfile(){
    document.getElementById("nome-edit").style.display="inline";
    document.getElementById("cognome-edit").style.display="inline";
    document.getElementById("email-edit").style.display="inline";
    document.getElementById("password-edit").style.display="inline";
    document.getElementById("password").style.display="inline";

    document.getElementById("save-btn").style.display="inline";
    document.getElementById("ticket-btn").style.display="none";
    document.getElementById("edit-btn").style.display="none";
    document.getElementById("delete-btn").style.display="none";
    document.getElementById("btn-logout").style.display = "none";


    document.getElementById("nome-edit").disabled=false;
    document.getElementById("cognome-edit").disabled=false;
    document.getElementById("email-edit").disabled=false;
    document.getElementById("password-edit").disabled=false;

}
let updateUtente={};
// Salvare le modifiche 
    function saveProfile(){
        const token = localStorage.getItem("authToken");
        if(!token){
             window.location.href="Login.html";
            return;
        }

        const nome=document.getElementById("nome-edit").value.trim();
        const cognome=document.getElementById("cognome-edit").value.trim();
        const email=document.getElementById("email-edit").value.trim();
        const password=document.getElementById("password-edit").value.trim();

         updateUtente={nome, cognome, email};

        if(password) updateUtente.password=password;
fetch("http://localhost:8080/utenti", {
        method: "PUT",
        headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
        },
         body: JSON.stringify(updateUtente),
    })
    .then((response) => {
    if (!response.ok) {
        throw new Error("Errore nel salvataggio del profilo");
    }
    return response.json();

    })

    .then((data) => {
    console.log("Profilo aggiornato con successo:", data);
    window.alert("Profilo aggiornato con successo!");



        document.getElementById("nome-edit").textContent = data.nome;
        document.getElementById("cognome-edit").textContent = data.cognome;
        document.getElementById("email-edit").textContent = data.email;


        document.getElementById("nome-edit").style.display = "inline";
        document.getElementById("cognome-edit").style.display = "inline";
        document.getElementById("email-edit").style.display = "inline";
        document.getElementById("ticket-btn").style.display="inline";
        document.getElementById("edit-btn").style.display="inline";
        document.getElementById("delete-btn").style.display="inline";
        document.getElementById("btn-logout").style.display="inline";
        document.getElementById("password-edit").style.display = "none";
        document.getElementById("password").style.display="none";
        document.getElementById("save-btn").style.display = "none";

        document.getElementById("nome-edit").disabled = true;
        document.getElementById("cognome-edit").disabled = true;
        document.getElementById("email-edit").disabled = true;
        document.getElementById("password-edit").disabled = true;


})
.catch((error) => {
    console.error("Errore durante l'aggiornamento del profilo:", error);
    });
 }


 
// Eliminare l'account per utente base
function deleteAccount(ruolo) {
    const token = localStorage.getItem("authToken");
   
    console.log(token + ruolo);
    if (!confirm("Vuoi davvero eliminare l'account?"))
         return;

    if(ruolo==="Operatore"){
        alert("Non puoi eliminare l'account di un operatore!")
        return;
    }

   
    fetch("http://localhost:8080/utenti", {
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${token}`,
        },
    })
    .then((response) => {
        if (!response.ok) {
            throw new Error("Errore durante l'eliminazione dell'account");
        }
        alert("Account eliminato con successo!");
        localStorage.removeItem("authToken");
        localStorage.removeItem("userRole")
        window.location.href = "Login.html";
    })
    .catch((error) => {
        console.error("Errore durante l'eliminazione dell'account:", error);
    });
}


// Funzione log out
function logout()
{
     const token = localStorage.getItem("authToken");

    fetch("http://localhost:8080/logout", {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
        },
    })
    .then(response => {
        if(!response.ok){
            throw new Error("Errore durante il logout!")
        }else{
            localStorage.removeItem("authToken");
            localStorage.removeItem("userRole")
            window.location.href="home.html"
        }
    })
    .catch(error => {
        console.error(error);
    });


}
document.getElementById("btn-logout").addEventListener("click", function(event){
    event.preventDefault();
    logout();
});