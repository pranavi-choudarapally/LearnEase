let quizData = [];

async function loadNotes() {

    const response = await fetch(
        "http://localhost:8080/api/materials/all"
    );

    const notes = await response.json();

    const select = document.getElementById("noteSelect");

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
    .addEventListener("click", generateQuiz);

async function generateQuiz() {

    const id = document.getElementById("noteSelect").value;

    if (!id) {
        alert("Select a PDF");
        return;
    }

    const loading = document.getElementById("loading");
    const quizContainer = document.getElementById("quizContainer");

    loading.innerHTML = "<h3>Generating Quiz...</h3>";
    quizContainer.innerHTML = "";

    try {

        const response = await fetch(
            `http://localhost:8080/api/ai/quiz/${id}`,
            {
                method: "POST"
            }
        );

        const text = await response.text();

        const quiz = JSON.parse(text);

        quizData = quiz.questions;

        loading.innerHTML = "";

        let html = "";

        quiz.questions.forEach((q, index) => {

            html += `
                <div class="question-card">

                    <h3>Question ${index + 1}</h3>

                    <p class="question-text">${q.question}</p>

                    ${q.options.map((option, i) => `

                        <label class="option">

                            <input
                                type="radio"
                                name="q${index}"
                                value="${i}">

                            ${option}

                        </label>

                    `).join("")}

                </div>
            `;

        });

        html += `
            <button id="submitQuiz">
                Submit Quiz
            </button>

            <div id="scoreCard"></div>
        `;

        quizContainer.innerHTML = html;

        document
            .getElementById("submitQuiz")
            .addEventListener("click", submitQuiz);

    }

    catch (e) {

        console.log(e);

        loading.innerHTML = "";

        quizContainer.innerHTML =
            "<h3>Failed to generate quiz.</h3>";

    }

}

function submitQuiz() {

    let score = 0;

    quizData.forEach((q, index) => {

        const selected = document.querySelector(
            `input[name="q${index}"]:checked`
        );

        const card =
            document.querySelectorAll(".question-card")[index];

        card.querySelectorAll("input").forEach(input => {
            input.disabled = true;
        });

        if (!selected) {

            card.innerHTML += `

                <p class="wrong">
                    ❌ Not Answered
                </p>

                <p class="correctAnswer">

                    Correct Answer :
                    <b>${q.options[q.answer]}</b>

                </p>

            `;

            return;
        }

        if (parseInt(selected.value) === q.answer) {

            score++;

            card.innerHTML += `
                <p class="correct">
                    ✅ Correct
                </p>
            `;

        }

        else {

            card.innerHTML += `

                <p class="wrong">
                    ❌ Wrong
                </p>

                <p class="correctAnswer">

                    Correct Answer :
                    <b>${q.options[q.answer]}</b>

                </p>

            `;

        }

    });

    const percentage =
        Math.round(score * 100 / quizData.length);

    let performance = "";

    if (percentage >= 90)
        performance = "🏆 Outstanding";

    else if (percentage >= 75)
        performance = "🌟 Excellent";

    else if (percentage >= 60)
        performance = "👍 Good";

    else if (percentage >= 40)
        performance = "🙂 Average";

    else
        performance = "📚 Keep Practicing";

    document.getElementById("scoreCard").innerHTML = `

        <div class="score-card">

            <h2>🎉 Quiz Completed</h2>

            <h3>
                Score : ${score} / ${quizData.length}
            </h3>

            <h3>
                Percentage : ${percentage}%
            </h3>

            <h3>
                ${performance}
            </h3>

        </div>

    `;

    document
        .getElementById("submitQuiz")
        .disabled = true;

}