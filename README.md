# WordyNerd

WordyNerd is a full-stack vocabulary app with an Angular frontend and a Spring
Boot REST API. The frontend includes sign-in and account creation, and checks
the backend health endpoint when it loads.

## Project structure

- `backend/` — Spring Boot API, JWT authentication, and MySQL persistence
- `frontend/wordynerd-ui/` — Angular application

## Run locally

1. Start MySQL and configure the database connection in
   `backend/src/main/resources/application.properties`.
2. In the backend terminal, set a Base64-encoded random key of at least 32
   bytes, then start the API from `backend/`:

   ```powershell
   $bytes = New-Object byte[] 32
   $rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
   $rng.GetBytes($bytes)
   $key = [Convert]::ToBase64String($bytes)
   [Environment]::SetEnvironmentVariable('WORDYNERD_JWT_SECRET', $key, 'User')
   $env:WORDYNERD_JWT_SECRET = $key
   $rng.Dispose()
   .\mvnw.cmd spring-boot:run
   ```

   Save the key once and reuse it; generating a new value invalidates current
   access tokens. The API listens on `http://localhost:8080`.
3. Use Node.js **20.19+ or 22.12+** for Angular 21. If you use nvm-windows
   and Node 24.14.0 is installed, switch to it in the frontend terminal:

   ```powershell
   nvm use 24.14.0
   ```

   In another terminal, start the frontend from `frontend/wordynerd-ui/`:

   ```powershell
   cd frontend\wordynerd-ui
   npm ci
   npm start
   ```

4. Open `http://localhost:4200`. The Angular development server proxies
   authentication and health requests to the API.

The login screen provides account creation for a new database user, sign-in,
and an authenticated profile check. It displays the API connection status in
the header.

The backend stores only hashes of refresh tokens, rotates them through
`POST /refresh`, and invalidates access tokens after logout. Signing out
invalidates all sessions for that user. Existing access tokens from before
this change and previously issued un-hashed refresh tokens must be replaced
by signing in again.
