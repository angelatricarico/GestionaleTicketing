// Registrazione per utente base
document.getElementById("Registrazione-form").addEventListener("submit" , (event)  =>{
    event.preventDefault();
    
    const nome=document.getElementById("nome").value.trim();
    const cognome=document.getElementById("Cognome").value.trim();
    const email=document.getElementById("email").value.trim();
    const password=document.getElementById("password").value.trim();
    const confermaPassword= document.getElementById("confermaPassword").value.trim();
    const privacyChecked=document.getElementById("privacy").checked;
    const emailRegex= /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;

    if(!nome || !cognome || !email || !password || !confermaPassword){
        alert("compila tutti i campi")
        return;
    }
    
    if(confermaPassword !== password){
        alert("conferma password errata!")
        return;
    }

    if(password.lenght < 5 || password.length > 10){
        alert("La password deve avere minimo 5 caratteri e massimo 10 caratteri!")
        return;
    }

    if(!privacyChecked){
        alert("Bisogna accettare la Privacy Policy per continuare.");
        return;
    }

    if(!emailRegex.test(email)){
        alert("inserisci un email valida")
        return;
    }


    fetch("http://localhost:8080/utenti", {
        method:"POST",
        headers: {
            "Content-Type": "application/json"
        },

        body:JSON.stringify({
            nome:nome,
            cognome:cognome,
            email:email,
            password:password,
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

// Funzione mostra password
function showPassword() {
    let showPasswordCheckbox = document.getElementById("show-password");
    
    if (showPasswordCheckbox.checked) {
        password.type = "text";
        confermaPassword.type = "text";
    } else {
        password.type = "password";
        confermaPassword.type = "password";
    }
}