async function loadNotes(){

    const response = await fetch(
        "http://localhost:8080/api/materials/all"
    );

    const notes = await response.json();

    const select =
        document.getElementById("noteSelect");

    select.innerHTML = "";

    notes.forEach(note=>{

        select.innerHTML += `

            <option value="${note.id}">
                ${note.fileName}
            </option>

        `;

    });

}

loadNotes();

document
.getElementById("generateBtn")
.addEventListener("click",generateResources);

async function generateResources(){

    const id =
        document.getElementById("noteSelect").value;

    const result =
        document.getElementById("resourcesResult");

    result.innerHTML = `

        <div class="loader"></div>

        <div class="loading-text">

            Finding the best learning resources...

        </div>

    `;

    try{

        const response = await fetch(

            `http://localhost:8080/api/ai/resources/${id}`,

            {
                method:"POST"
            }

        );

        const resources =
            await response.text();

        result.innerHTML =
            marked.parse(resources);

    }

    catch(error){

        console.error(error);

        result.innerHTML =

        "<p style='color:red;'>Failed to generate resources.</p>";

    }

}