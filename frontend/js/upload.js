const form = document.getElementById("uploadForm");

const message = document.getElementById("message");

form.addEventListener("submit", async (e) => {

    e.preventDefault();

    const file = document.getElementById("file").files[0];
    if (!file) {
    alert("Please select a PDF file.");
    return;
}

if (file.type !== "application/pdf") {
    alert("Only PDF files are allowed.");
    return;
}

    if (!file) {

        message.style.color = "red";

        message.textContent = "Please choose a file.";

        return;
    }

    const user = JSON.parse(localStorage.getItem("user"));

    const formData = new FormData();

    formData.append("file", file);

    formData.append("uploadedBy", user.name);

    try {

        const response = await fetch(
            "http://localhost:8080/api/materials/upload",
            {
                method: "POST",
                body: formData
            }
        );

        const text = await response.text();

        if(response.ok){

            message.style.color = "green";

            message.textContent = text;

            form.reset();

        }else{

            message.style.color = "red";

            message.textContent = text;

        }

    }catch(error){

        message.style.color = "red";

        message.textContent = "Server not running.";

    }

});