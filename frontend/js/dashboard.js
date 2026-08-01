const user = JSON.parse(localStorage.getItem("user"));

if(user){

    document.getElementById("welcomeText").textContent =
        `Welcome, ${user.name} 👋`;

}

document.getElementById("logout").addEventListener("click",()=>{

    localStorage.removeItem("user");

    window.location.href="login.html";

});