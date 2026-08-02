async function loadNotes() {

    const response = await fetch(
        "http://localhost:8080/api/materials/all"
    );

    const notes = await response.json();

    const select =
        document.getElementById("noteSelect");

    select.innerHTML = "";

    notes.forEach(note => {

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
    .addEventListener("click", generateSummary);



async function generateSummary() {

    const id =
        document.getElementById("noteSelect").value;

    const result =
        document.getElementById("summaryResult");

    result.innerHTML = `

        <div class="loader"></div>

        <div class="loading-text">

            Generating summary...

        </div>

    `;

    try {

        const response = await fetch(

            `http://localhost:8080/api/ai/summarize/${id}`,

            {
                method: "POST"
            }

        );

        const summary = await response.text();

        result.innerHTML = marked.parse(summary);

    }

    catch (error) {

        console.error(error);

        result.innerHTML =
            "<p style='color:red;'>Failed to generate summary.</p>";

    }

}