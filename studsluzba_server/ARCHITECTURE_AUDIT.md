# Student Service – Architecture Audit (Concise)

Spec: `projekatspecifikacija.txt` (Controller → Service → Repository, DTOs + Mappers only across API boundary).

## Findings
- **PredmetController (`controllers/PredmetController.java`)**
  - `getPredmetiByProgram(...)` returns `List<Predmet>` (entity). Must return DTO/Response.
  - Imports `Converters` (not used; controllers should not use converters).
- **PredmetService (`services/PredmetService.java`)**
  - Returns entities from `create(...)`, `getByStudProgram(...)`, `getAll()`. Controller then exposes them in `/by-program`.
  - Field injection via `@Autowired` on fields. Prefer constructor injection with `final` fields.
- **StudentController (`controllers/StudentController.java`)**
  - Injects repositories directly (`StudentPodaciRepository`, `StudentIndeksRepository`, `StudijskiProgramRepository`). Violates layering.
  - Contains business logic and persistence calls (builds entities, saves, queries). Must move to service(s).
  - Mixed mapper usage: instance `entityMappers` and static `EntityMappers::...`.
- **EntityMappers (`utils/EntityMappers.java`)**
  - `@Component` but many methods are `static`. Choose one style (prefer instance methods) and align call sites.
- **Converters (`utils/Converters.java`)**
  - OK for Request→Entity. Caution: `toIspit(IspitRequest)` indicates request may carry entities; requests should carry IDs; resolve in service.

## Required Changes
- **Controllers**
  - Do not inject repositories; inject services (+ mappers if needed).
  - Return only DTO/Response classes; accept only Request DTOs.
  - PredmetController: change `/by-program/{programId}` to return `List<PredmetDTO>` or `List<PredmetResponse>`; map via `EntityMappers`.
- **Services**
  - Introduce `StudentService` to hold all logic currently in `StudentController` (CRUD, search, profile orchestration). Controllers call service; service uses repositories and mappers.
  - `PredmetService`: return DTOs or map in controller; keep repository access internal. Use constructor injection with `final` fields; consider `public final class PredmetService`.
- **Mappers/Converters**
  - Make `EntityMappers` a pure component with instance methods (remove `static` or provide a separate static utility but be consistent). Update all call sites to use injected `entityMappers`.
  - Keep `Converters` for Request→Entity only. For associations, accept IDs in requests and resolve entities in service.

## Concrete Refactors (minimal)
- PredmetController
  - Change `public List<Predmet> getPredmetiByProgram(...)` → `public List<PredmetDTO> ...` and use `entityMappers.fromPredmetiToDTO(predmetService.getByStudProgram(...))`.
- PredmetService
  - Replace field injection with constructor injection. Mark repo fields `private final`.
  - Option A: keep returning entities and map in controller (preferred minimal change). Option B: have service return DTOs using `EntityMappers`.
- StudentController
  - Remove direct repository fields. Add `private final StudentService studentService;` and use it in all endpoints.
  - Move logic of: create/update/delete/list student podaci; indeks creation; searches; lookups by indeks/email to `StudentService`.
- EntityMappers
  - Remove `static` from methods used by DI or provide static-only class, not both. Adjust usages accordingly.
- DTO Contracts
  - Ensure `IspitRequest` and similar carry only IDs; adjust `Converters.toIspit` to not accept entities from requests; resolve in service.

## Optional Improvements
- Add `@Validated` on controllers/services where appropriate.
- Add `@AllArgsConstructor` (Lombok) on services/controllers to support constructor injection.
- Ensure all controller responses use consistent DTO type (`PredmetDTO` vs `PredmetResponse`) per API design.
