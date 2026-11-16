# AI Chatbot Backend

Spring Boot 3.5.16 + Spring AI 1.0.9 + Google Gemini + PostgreSQL.

Import the `backend` directory into Eclipse as an Existing Maven Project.
Run `AiChatbotApplication.java` as a Spring Boot App.

Set the Windows environment variable before launching Eclipse:
setx GOOGLE_API_KEY "YOUR_GEMINI_API_KEY"

Expected PostgreSQL:
localhost:6969 / database KalI_byte / user postgres

Test:
http://localhost:7000/api/chat?message=Hello
