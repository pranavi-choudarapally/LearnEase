async function loadFiles() {

    const response = await fetch(
        "http://localhost:8080/api/materials/all"
    );

    const files = await response.json();

    const table = document.getElementById("tableBody");

    // Clear existing rows
    table.innerHTML = "";

    files.forEach(file => {

        table.innerHTML += `

<tr>

<td>${file.fileName}</td>

<td>${file.fileType}</td>

<td>${Math.round(file.fileSize / 1024)} KB</td>

<td>${file.uploadedBy}</td>

<td>

<button onclick="downloadFile(${file.id})">
    Download
</button>

<button onclick="deleteFile(${file.id})">
    Delete
</button>

</td>

</tr>

`;

    });

}

loadFiles();

function downloadFile(id) {

    window.open(
        `http://localhost:8080/api/materials/download/${id}`,
        "_blank"
    );

}

async function deleteFile(id) {

    const confirmDelete = confirm(
        "Are you sure you want to delete this file?"
    );

    if (!confirmDelete) {
        return;
    }

    const response = await fetch(
        `http://localhost:8080/api/materials/${id}`,
        {
            method: "DELETE"
        }
    );

    const message = await response.text();

    alert(message);

    // Reload the table
    loadFiles();

}
async function searchFiles(){

    const keyword =
        document.getElementById("search").value;

    const response = await fetch(
        `http://localhost:8080/api/materials/search?keyword=${keyword}`
    );

    const files = await response.json();

    const table = document.getElementById("tableBody");

    table.innerHTML = "";

    files.forEach(file=>{

        table.innerHTML += `

<tr>

<td>${file.fileName}</td>

<td>${file.fileType}</td>

<td>${Math.round(file.fileSize/1024)} KB</td>

<td>${file.uploadedBy}</td>

<td>

<button onclick="downloadFile(${file.id})">
Download
</button>

<button onclick="deleteFile(${file.id})">
Delete
</button>

</td>

</tr>

`;

    });

}