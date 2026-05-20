# Copilot instructions for this repository

## Build, test, and lint commands

- Backend full test suite: `./mvnw test`
- Backend full verification/build: `./mvnw verify` or `./mvnw clean install`
- Run one backend test class: `./mvnw -Dtest=TicketManagerApplicationTests test`
- Run one backend test method: `./mvnw -Dtest=TicketManagerApplicationTests#createsTicket test`
- Run the app locally: `./mvnw spring-boot:run`
- Frontend dependency install: `cd frontend && npm install`
- Frontend dev server: `cd frontend && npm run dev`
- Frontend production build: `cd frontend && npm run build`
- Frontend unit tests: `cd frontend && npm run test:unit -- --run`
- Run one frontend test file: `cd frontend && npm run test:unit -- --run src/components/__tests__/HelloWorld.spec.js`
- Frontend lint without edits: `cd frontend && npm run lint:check`
- Frontend lint with auto-fixes: `cd frontend && npm run lint`

Backend tests use Testcontainers PostgreSQL, so Docker must be available. The Maven build also runs the Vue build through `frontend-maven-plugin` during `generate-resources`, writing production assets to `src/main/resources/static`.

## High-level architecture

This is a Dr JSkill full-stack Spring Boot 4 + Vue 3 ticket manager for curating GitHub good-first-issue tickets in Java open source projects.

- Backend source is under `src/main/java/com/example/ticketmanager`.
- `Ticket` is a JPA entity with Bean Validation constraints for `title`, `repository`, `link`, and `status`; `link` must match GitHub issue URLs.
- `TicketStatus` is persisted as a string enum. Keep the backend enum, frontend `statusOptions`/`statusLabels`, and status CSS classes in sync when adding or renaming statuses.
- `TicketRepository` is the only repository. There is no service layer because the CRUD logic is simple; `TicketController` calls the repository directly.
- `TicketController` exposes `/api/tickets` for list/get/create/update/delete. It returns tickets sorted by ascending `id` and uses `ResponseStatusException` for 404s.
- `TicketSeedConfiguration` seeds the database only when `ticketRepository.count() == 0`; changing seed data will not modify an already-populated local database.
- Runtime configuration comes from `application.properties`, with optional `.env` import. `.env.sample` documents local ports and database variables.
- Frontend source is under `frontend/src`. Vite proxies `/api` to Spring Boot in dev mode and writes built assets into `src/main/resources/static` for Spring Boot to serve.
- Vue state for tickets lives in `frontend/src/stores/tickets.js`; HTTP calls are isolated in `frontend/src/services/tickets.js`; the main UI is `frontend/src/views/TicketBoardView.vue`.

## Key conventions

- Prefer the Java LSP configured in `.github/lsp.json` for Java navigation and refactoring.
- Java package-private controllers/configuration classes are used where public visibility is unnecessary.
- Java tests use Spring Boot integration tests with `@SpringBootTest`, `@AutoConfigureMockMvc`, and `TestcontainersConfiguration`.
- Frontend imports use the Vite `@` alias for `frontend/src`.
- Vue uses Composition API with `<script setup>` and Pinia stores.
- Do not edit generated files in `src/main/resources/static` directly; update `frontend/src` and rebuild.
- Do not read or commit `.env`; use `.env.sample` for documented placeholders.
- `compose.yaml` is for Spring Boot Docker Compose development support, while `docker-compose.yml` builds and runs the full app with PostgreSQL.
