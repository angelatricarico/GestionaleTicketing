const token =localStorage.getItem("authToken");

// Form login e reindirizzamento a seconda del ruolo
if(token != null){
    window.location.href="home.html";
    }
    document.getElementById("loginForm").addEventListener("submit", (event) =>{
        event.preventDefault();
        const email=document.getElementById("email").value.trim();
        const password=document.getElementById("password").value.trim();                                                      
    if (!email || !password ){
        window.alert("Credenziali non valide");
        return;                                                
    }

        fetch("http://localhost:8080/login",{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
            "email": email,
            "password": password
        }) 
    })

    .then(response =>{
        if(!response.ok){                                 
            throw new Error("Errore nel login");

        }  return response.json();
    })                                      
        .then(data => {                                     
        if(data.token){                                     
            localStorage.setItem("authToken", data.token);
            localStorage.setItem("userRole", data.role); 
        if(data.role === "Admin"){                        
            window.location.href="Admin.html";
        }else if (data.role === "Utente"){
            window.location.href="Profile.html";
        } else {
            window.location.href = "Profile.html";
        }

        }else{
            window.alert("Login fallito");
        }
        
    })
    .catch(error => {
        console.log("Errore:", error);
    });
});