const signupForm = document.getElementById("signupForm");

if (signupForm) {

    signupForm.addEventListener("submit", function (e) {

        e.preventDefault();

        document.getElementById("nameError").textContent = "";
        document.getElementById("emailError").textContent = "";
        document.getElementById("passwordError").textContent = "";
        document.getElementById("confirmPasswordError").textContent = "";

        const name = document.getElementById("name").value.trim();
        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value;
        const confirmPassword = document.getElementById("confirmPassword").value;

        let valid = true;

        if (name.length < 3) {

            document.getElementById("nameError").textContent =
                "Name must contain at least 3 characters.";

            valid = false;
        }

        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailPattern.test(email)) {

            document.getElementById("emailError").textContent =
                "Please enter a valid email.";

            valid = false;
        }

        if (password.length < 8) {

            document.getElementById("passwordError").textContent =
                "Password must contain at least 8 characters.";

            valid = false;
        }

        if (password !== confirmPassword) {

            document.getElementById("confirmPasswordError").textContent =
                "Passwords do not match.";

            valid = false;
        }

        if (valid) {

    const button = signupForm.querySelector("button");

    button.disabled = true;
    button.textContent = "Creating Account...";

    fetch("http://localhost:8080/api/auth/signup", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({

            name: name,

            email: email,

            password: password

        })

    })

    .then(response => response.text())

    .then(data => {

        const message = document.getElementById("message");

message.textContent = data;

if(data === "Registration Successful"){

    message.style.color = "#16A34A";

    signupForm.reset();

    setTimeout(()=>{

        window.location.href="login.html";

    },1500);

}
else{

    message.style.color="#EF4444";

}

        if(data === "Registration Successful"){

            window.location.href = "login.html";

        }

    })

    .catch(error => {

        alert("Unable to connect to the server.");

        console.error(error);

    })

    .finally(() => {

        button.disabled = false;

        button.textContent = "Create Account";

    });

}

    });

}
const loginForm = document.getElementById("loginForm");

if (loginForm) {

    loginForm.addEventListener("submit", function (e) {

        e.preventDefault();

        document.getElementById("loginEmailError").textContent = "";
        document.getElementById("loginPasswordError").textContent = "";
        document.getElementById("loginMessage").textContent = "";

        const email = document.getElementById("loginEmail").value.trim();
        const password = document.getElementById("loginPassword").value;

        let valid = true;

        if (email === "") {

            document.getElementById("loginEmailError").textContent =
                "Email is required.";

            valid = false;
        }

        if (password === "") {

            document.getElementById("loginPasswordError").textContent =
                "Password is required.";

            valid = false;
        }

        if (!valid) return;

        const button = loginForm.querySelector("button");

        button.disabled = true;
        button.textContent = "Logging in...";

        fetch("http://localhost:8080/api/auth/login", {

            method: "POST",

            headers: {

                "Content-Type": "application/json"

            },

            body: JSON.stringify({

                email: email,
                password: password

            })

        })

        .then(response => response.json())

        .then(data => {

            const message = document.getElementById("loginMessage");

            if (data.message === "Login Successful") {

                message.style.color = "#16A34A";

                message.textContent = data.message;

                localStorage.setItem("user", JSON.stringify(data));

                setTimeout(() => {

                    window.location.href = "dashboard.html";

                }, 1500);

            }

            else {

                message.style.color = "#EF4444";

                message.textContent = data.message;

            }

        })

        .catch(error => {

            console.error(error);

            document.getElementById("loginMessage").textContent =
                "Unable to connect to server.";

        })

        .finally(() => {

            button.disabled = false;

            button.textContent = "Login";

        });

    });

}