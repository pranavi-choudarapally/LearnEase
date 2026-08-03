# LearnEase

LearnEase is a study-support web application that lets users upload PDF study materials, generate AI-powered summaries, quizzes, and learning resources, and manage uploaded notes.

## Project Structure

- `backend/` - Spring Boot backend API
  - `src/main/java/com/learnease/backend/` - application source code
  - `src/main/resources/application.properties` - environment-driven configuration
  - `Dockerfile` - containerized build/runtime setup
- `frontend/` - static client application HTML/CSS/JavaScript
  - `index.html`, `login.html`, `signup.html`, `dashboard.html`, `upload.html`, `quiz.html`, `resources.html`, `my-notes.html`, `ai-summary.html`
  - `js/` - client-side logic
  - `css/` - styling

## Features

- User sign-up and login
- PDF upload for study materials
- Download, delete, and search uploaded files
- AI summary generation from PDF text
- AI-generated quiz creation from PDF content
- AI-generated study resources suggestions
- Cloudinary support for handling media credentials
- OpenRouter integration for AI responses

## Technology Stack

- Java 17
- Spring Boot 3.5.x
- Spring Web, Spring Data JPA, Spring Security, Validation
- MySQL Connector/J
- Apache PDFBox for PDF text extraction
- Cloudinary HTTP SDK
- Plain HTML/CSS/JavaScript frontend

## Requirements

- JDK 17
- Maven
- MySQL or another compatible database
- OpenRouter API key
- Cloudinary credentials

## Backend Configuration

The backend reads database and API credentials from environment variables. Set the following before running:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://<HOST>:<PORT>/<DATABASE>
SPRING_DATASOURCE_USERNAME=<DB_USERNAME>
SPRING_DATASOURCE_PASSWORD=<DB_PASSWORD>
OPENROUTER_API_KEY=<YOUR_OPENROUTER_API_KEY>
CLOUDINARY_CLOUD_NAME=<CLOUDINARY_CLOUD_NAME>
CLOUDINARY_API_KEY=<CLOUDINARY_API_KEY>
CLOUDINARY_API_SECRET=<CLOUDINARY_API_SECRET>
```

The default server port is `8080`.

### Example `application.properties`

```properties
spring.application.name=backend
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
server.port=8080
openrouter.api.key=${OPENROUTER_API_KEY}
openrouter.api.url=https://openrouter.ai/api/v1/chat/completions
openrouter.model=meta-llama/llama-3.1-8b-instruct
cloudinary.cloud-name=${CLOUDINARY_CLOUD_NAME}
cloudinary.api-key=${CLOUDINARY_API_KEY}
cloudinary.api-secret=${CLOUDINARY_API_SECRET}
```

## Running the Backend

From the `backend/` directory:

```bash
# using Maven wrapper
./mvnw spring-boot:run
```

or build a jar and run it:

```bash
./mvnw clean package -DskipTests
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Docker

From `backend/`:

```bash
docker build -t learnease-backend .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=... \
  -e SPRING_DATASOURCE_USERNAME=... \
  -e SPRING_DATASOURCE_PASSWORD=... \
  -e OPENROUTER_API_KEY=... \
  -e CLOUDINARY_CLOUD_NAME=... \
  -e CLOUDINARY_API_KEY=... \
  -e CLOUDINARY_API_SECRET=... \
  learnease-backend
```

## Frontend Usage

The frontend is static and can be served from any static host, local file server, or Netlify-like deployment.

The client uses the backend API base URL currently hard-coded in the JS files as:

- `https://learnease-backend-mc5m.onrender.com`

For local development, update the fetch URLs in `frontend/js/*.js` to point to `http://localhost:8080` or your backend host.

### Local test flow

1. Open `frontend/signup.html` in a browser
2. Sign up and log in
3. Upload a PDF via `upload.html`
4. View uploaded files in `my-notes.html`
5. Generate summaries, quizzes, or resources from the dashboard pages

## API Endpoints

### Authentication

- `POST /api/auth/signup`
- `POST /api/auth/login`

### Study Material Management

- `POST /api/materials/upload`
- `GET /api/materials/all`
- `GET /api/materials/download/{id}`
- `DELETE /api/materials/{id}`
- `GET /api/materials/search?keyword=<term>`

### AI Services

- `POST /api/ai/summarize/{id}`
- `POST /api/ai/quiz/{id}`
- `POST /api/ai/resources/{id}`

## Important Notes

- The backend currently allows CORS from all origins for most APIs and a limited CORS allowlist on uploads.
- Uploaded files are stored and managed through the backend `uploads/` directory.
- AI features depend on OpenRouter. Ensure the `OPENROUTER_API_KEY` is valid and has access to the configured model.
- The frontend does not currently perform token-based auth; it stores login results in `localStorage` and uses them for session state.

## Contribution

To extend LearnEase:

- add stronger authentication/token handling
- improve frontend routing and form validation
- add role-based access or permissions
- support additional file types or PDF previewing

## License

This project is developed for educational purposes and portfolio demonstration.

