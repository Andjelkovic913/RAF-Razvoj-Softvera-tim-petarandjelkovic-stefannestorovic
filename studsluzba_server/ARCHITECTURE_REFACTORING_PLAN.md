# Architecture Refactoring Plan

## 🧩 Controllers

**Current Issues:**
- Controllers directly inject and use repositories (`StudentPodaciRepository`, `StudijskiProgramRepository`, `SlusaPredmetRepository`, etc.) instead of services
- Controllers return raw entities (`List<Predmet>`, `StudentIndeks`, `SlusaPredmet`) instead of Response DTOs
- Controllers contain business logic (e.g., `StudentController.updateStudentPodaci`, `ParseUtils` usage, entity mutation)
- Controllers manually call mappers/converters instead of delegating to services

**Required Changes:**
- **Controllers must ONLY inject their corresponding Service** (e.g., `StudentController` → `StudentService`)
- Controllers accept **Request DTOs** via `@RequestBody`, `@PathVariable`, `@RequestParam` (Spring auto-converts JSON)
- Controllers call service methods with Request DTOs: `return studentService.createStudent(request)`
- Controllers return **Response DTOs** directly from service calls
- **Remove ALL repository injections** from controllers
- **Remove ALL business logic** from controllers (validation, parsing, entity manipulation)
- **Remove ALL mapper/converter calls** from controllers - services handle this
- Controllers should be simple pass-through: HTTP → Service → Response DTO

**Affected Files:**
- `StudentController.java` - Remove repository injections, move all logic to `StudentService`
- `NastavnikController.java` - Remove `NastavnikRepository` injection, use `NastavnikService` only
- `StudijskiProgramController.java` - Remove repository injections, create `StudijskiProgramService`
- `SlusaPredmetController.java` - Remove repository injection, create `SlusaPredmetService`
- `RaspodelaNastaveController.java` - Remove repository injections, create `RaspodelaNastaveService`
- `PredmetController.java` - Fix endpoint returning `List<Predmet>` to return `List<PredmetResponse>`
- `IspitController.java` - Verify it follows the pattern
- `DrziPredmetController.java` - Verify it follows the pattern

---

## ⚙️ Services

**Current Issues:**
- Services use field injection (`@Autowired` on mutable fields) instead of constructor injection
- Services return entities instead of Response DTOs
- Services accept entities instead of Request DTOs
- Missing services: `StudentService`, `StudijskiProgramService`, `SlusaPredmetService`, `RaspodelaNastaveService`
- Services don't use mappers/converters for DTO conversion

**Required Changes:**
- **All services must use constructor injection** with `@RequiredArgsConstructor` and `final` fields
- **Services accept Request DTOs** as parameters (not entities)
- **Services return Response DTOs** (never entities)
- **Services inject Repository, Converters, and EntityMappers** (not controllers)
- Services contain ALL business logic:
  - Validation (e.g., duplicate JMBG, duplicate indices)
  - Business rules (e.g., compute next index number)
  - Error handling (e.g., entity not found, duplicate entries)
  - Parsing logic (e.g., `ParseUtils` calls should be in services)
- **Service flow:** Request DTO → Mapper (to Entity) → Repository → Converter (to Response DTO) → return Response DTO
- Mark all services with `@Service` annotation

**Service Pattern:**
```java
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final Converters converters;  // Request DTO → Entity (all methods in one class)
    private final EntityMappers entityMappers;  // Entity → Response DTO (all methods in one class)
    
    public StudentResponse createStudent(StudentRequest dto) {
        Student entity = converters.toStudentPodaci(dto);
        // Business logic here
        if (studentRepository.existsByJmbg(entity.getJmbg()))
            throw new IllegalArgumentException("Duplicate JMBG");
        Student saved = studentRepository.save(entity);
        return entityMappers.fromStudentPodaciToResponse(saved);
    }
}
```

**Affected Files:**
- `StudentIndeksService.java` - Convert to constructor injection, expand to handle all student operations
- `StudentProfileService.java` - Convert to constructor injection, ensure returns DTOs
- `NastavnikService.java` - Convert to constructor injection, accept Request DTOs, return Response DTOs
- `PredmetService.java` - Convert to constructor injection, accept Request DTOs, return Response DTOs
- `IspitService.java` - Convert to constructor injection, accept Request DTOs, return Response DTOs
- `DrziPredmetService.java` - Convert to constructor injection, accept Request DTOs, return Response DTOs
- **Create new services:**
  - `StudentService.java` - Main service for student operations (consolidate `StudentIndeksService` logic)
  - `StudijskiProgramService.java` - Handle all studijski program operations
  - `SlusaPredmetService.java` - Handle all slusa predmet operations
  - `RaspodelaNastaveService.java` - Handle raspodela nastave operations

---

## 🧮 Mappers and Converters

**Current Issues:**
- Static utility methods in `Converters.java` and `EntityMappers.java` - should be Spring components
- Mixed responsibilities: `Converters` has both Request→Entity and Entity→Response methods
- DTOs contain entities (e.g., `StudentIndeksResponse` has `StudentPodaci` and `StudijskiProgram` entities)
- Mappers/Converters are called from controllers instead of services

**Required Changes:**
- **Split responsibilities:**
  - **Mapper (in `Converters.java`):** Converts **Request DTO → Entity** (e.g., `StudentRequest` → `Student`)
  - **Converter (in `EntityMappers.java`):** Converts **Entity → Response DTO** (e.g., `Student` → `StudentResponse`)
- **Keep all mapping methods in single classes:**
  - **ALL Request → Entity methods** stay in `Converters.java` (convert to `@Component`)
  - **ALL Entity → Response methods** stay in `EntityMappers.java` (already `@Component`)
  - **DO NOT create separate mapper classes** for each entity - keep everything centralized
- **Convert static methods to Spring `@Component` beans** so they can be injected
- **Mappers/Converters are ONLY used by Services** (never by controllers)
- **Fix DTO definitions:**
  - `StudentIndeksResponse` - Replace `StudentPodaci` entity with nested DTO or scalar fields
  - `StudentIndeksResponse` - Replace `StudijskiProgram` entity with nested DTO or scalar fields
  - `StudentProfileDTO` - Replace `StudentIndeks`, `SlusaPredmet`, `Predmet` entities with DTOs
  - `StudentWebProfileDTO` - Replace `StudentIndeks`, `SlusaPredmet` entities with DTOs
- **One-way references are safe:**
  - Vezivni entiteti (kao `DrziPredmetDTO`) mogu koristiti druge DTO klase (kao `TeacherDTO`, `SubjectDTO`)
  - Ovo je **bezbedno** jer ti DTO-ovi sadrže samo proste tipove podataka (String, Long, Integer, etc.)
  - Nema rizika od beskonačnih petlji jer su reference jednosmerne i DTO-ovi ne sadrže entitete

**Affected Files:**
- `Converters.java` - Convert to `@Component`, keep all Request → Entity methods here
- `EntityMappers.java` - Already `@Component`, keep all Entity → Response methods here
- `StudentIndeksResponse.java` - Remove entity fields, use DTOs or scalar fields
- `StudentProfileDTO.java` - Replace entities with DTOs
- `StudentWebProfileDTO.java` - Replace entities with DTOs

---

## 🗄️ Repositories

**Current Issues:**
- Repositories are injected in controllers (should only be in services)
- Some repositories may need additional query methods to support service logic

**Required Changes:**
- **Repositories are ONLY used by Services** (never by controllers or other layers)
- **Repositories work with Entities only** (never DTOs)
- Remove all repository injections from controllers
- Add custom query methods to repositories if needed for service business logic

**Affected Files:**
- All controller files - Remove repository injections
- Repository interfaces - Add query methods if needed for service operations

---

## 📦 Package Structure

**Current Issues:**
- Mixed naming (plural vs singular)
- Mappers/Converters in `utils` package

**Recommended Structure:**
```
org.raflab.studsluzba/
├── controllers/
│   ├── request/  (Request DTOs)
│   └── response/ (Response DTOs)
├── services/
├── repositories/
├── utils/
│   ├── Converters.java      (ALL Request DTO → Entity methods)
│   └── EntityMappers.java   (ALL Entity → Response DTO methods)
└── model/        (Entities)
```

**Important Notes:**
- **Keep all mapping methods centralized** - do NOT create separate mapper classes for each entity
- `Converters.java` contains ALL Request → Entity conversion methods
- `EntityMappers.java` contains ALL Entity → Response conversion methods
- Both classes should be `@Component` beans for dependency injection

---

## 🧹 Code Quality

- Remove `System.out.println` debugging statements (e.g., in `DrziPredmetService`)
- Replace with proper logging (SLF4J/Logback)
- Remove unused imports
- Replace `null` returns with proper exception handling (`ResponseStatusException`, custom exceptions)
- Add proper validation annotations on Request DTOs

---

## ✅ Summary of Golden Rules

1. **Controller** → talks to **Service** using **Request DTOs**, returns **Response DTOs**
2. **Service** → talks to **Repository** using **Entities**, uses **Converters** (Request→Entity) and **EntityMappers** (Entity→Response)
3. **Converters.java** → contains ALL **Request DTO → Entity** methods (used by Service)
4. **EntityMappers.java** → contains ALL **Entity → Response DTO** methods (used by Service)
5. **Repository** → talks to **DB** using **Entities only** (used by Service only)

**No exceptions:**
- ❌ Controllers never inject repositories
- ❌ Controllers never contain business logic
- ❌ Controllers never call mappers/converters directly
- ❌ Services never return entities to controllers
- ❌ DTOs never contain entities (but can contain other DTOs with simple types)
- ❌ Repositories never work with DTOs
- ❌ Do NOT create separate mapper classes - keep all methods in `Converters.java` and `EntityMappers.java`

**Safe DTO References:**
- ✅ Vezivni entiteti (kao `DrziPredmetDTO`) mogu koristiti druge DTO klase (kao `TeacherDTO`, `SubjectDTO`)
- ✅ Ovo je bezbedno jer DTO-ovi sadrže samo proste tipove podataka (String, Long, Integer, etc.)
- ✅ Jednosmerne reference su uvek bezbedne - nema rizika od beskonačnih petlji

---

## ✅ Phase 2: Data Exposure - COMPLETED

**Summary:** All DTOs now use DTOs instead of entities, and all controllers return DTOs instead of entities.

### Completed Tasks:

1. **Created new DTOs for nested objects:**
   - ✅ `StudijskiProgramBasicDTO` - Basic DTO for nested use in other DTOs
   - ✅ `StudentIndeksDTO` - DTO for nested use in profile DTOs
   - ✅ `SlusaPredmetDTO` - DTO for nested use in profile DTOs
   - ✅ `SkolskaGodinaDTO` - DTO for nested use in `StudijskiProgramResponse`
   - ✅ `SlusaPredmetResponse` - Response DTO for `SlusaPredmet`

2. **Fixed DTOs that contained entities:**
   - ✅ `StudentIndeksResponse` - Replaced `StudentPodaci` and `StudijskiProgram` entities with `StudentPodaciResponse` and `StudijskiProgramBasicDTO`
   - ✅ `StudentProfileDTO` - Replaced entities with DTOs (`StudentIndeksDTO`, `SlusaPredmetDTO`, `PredmetDTO`)
   - ✅ `StudentWebProfileDTO` - Replaced entities with DTOs (`StudentIndeksDTO`, `SlusaPredmetDTO`)
   - ✅ `StudijskiProgramResponse` - Replaced `SkolskaGodina` entity with `SkolskaGodinaDTO`

3. **Fixed controllers returning entities:**
   - ✅ `PredmetController.getPredmetiByProgram` - Now returns `List<PredmetResponse>` instead of `List<Predmet>`
   - ✅ `RaspodelaNastaveController` - Now returns DTOs (`List<PredmetResponse>`, `List<StudentIndeksDTO>`) instead of entities
   - ✅ `StudijskiProgramController.getAllStudProgramiSortedDesc` - Now returns `List<StudijskiProgramResponse>` instead of `List<StudijskiProgram>`
   - ✅ `SlusaPredmetController` - Now returns `SlusaPredmetResponse` instead of `SlusaPredmet` entity

4. **Updated services to return DTOs:**
   - ✅ `RaspodelaNastaveService` - Now returns DTOs instead of entities
   - ✅ `StudijskiProgramService.getAllSortedDesc` - Now returns `List<StudijskiProgramResponse>`
   - ✅ `SlusaPredmetService` - Now returns `SlusaPredmetResponse` instead of entities
   - ✅ `StudentProfileService` - Now properly maps entities to DTOs using `EntityMappers`

5. **Updated `EntityMappers`:**
   - ✅ Added `fromStudentIndeksToIndeksDTO` method (renamed to avoid conflict with static `fromStudentIndeksToDTO`)
   - ✅ Added `fromStudijskiProgramToBasicDTO` method
   - ✅ Added `fromSkolskaGodinaToDTO` method
   - ✅ Added `fromSlusaPredmetToDTO` and `fromSlusaPredmetiToDTO` methods
   - ✅ Updated `fromStudentIndexToResponse` to use DTOs instead of entities
   - ✅ Updated `fromStudijskiProgramToResponse` to use `SkolskaGodinaDTO` instead of entity
   - ✅ Injected `Converters` into `EntityMappers` for `toDrziPredmetResponse` method

6. **Fixed compilation issues:**
   - ✅ Fixed duplicate method name conflict in `EntityMappers`
   - ✅ Fixed `NastavnikController` to inject `Converters` and use instance methods instead of static methods

**Result:** All controllers now return DTOs, and all DTOs use DTOs instead of entities. No entities are exposed outside the service layer.

---

## ✅ Phase 3: Refinement - COMPLETED

**Summary:** All services now use constructor injection, business logic has been moved from controllers to services, and all services accept Request DTOs and return Response DTOs with proper exception handling.

### Completed Tasks:

1. **Converted existing services to constructor injection:**
   - ✅ `StudentIndeksService` - Converted from `@Autowired` to `@RequiredArgsConstructor` with `final` fields
   - ✅ `NastavnikService` - Converted from `@Autowired` to `@RequiredArgsConstructor` with `final` fields

2. **Moved business logic from controllers to services:**
   - ✅ `NastavnikController` - Removed repository injection, removed converter calls, moved all business logic to `NastavnikService`
   - ✅ `NastavnikController.updateNastavnik` - Now delegates to `NastavnikService.updateNastavnik`
   - ✅ `NastavnikController.deleteNastavnik` - Now delegates to `NastavnikService.deleteNastavnik`
   - ✅ `SlusaPredmetController.updateSlusaPredmet` - Now accepts `SlusaPredmetRequest` instead of entity

3. **Ensured all services accept Request DTOs and return Response DTOs:**
   - ✅ `NastavnikService.save` - Now accepts `NastavnikRequest` and returns `Long` (ID)
   - ✅ `NastavnikService.findAll` - Now returns `List<NastavnikResponse>` instead of `Iterable<Nastavnik>`
   - ✅ `NastavnikService.findById` - Now returns `NastavnikResponse` instead of `Optional<Nastavnik>`
   - ✅ `NastavnikService.findByImeAndPrezime` - Now returns `List<NastavnikResponse>` instead of `List<Nastavnik>`
   - ✅ `NastavnikService.updateNastavnik` - Now accepts `NastavnikRequest` and returns `NastavnikResponse`
   - ✅ `SlusaPredmetService.updateSlusaPredmet` - Now accepts `SlusaPredmetRequest` instead of entity
   - ✅ `DrziPredmetService.findById` - Now returns `DrziPredmetResponse` instead of `Optional<DrziPredmetResponse>`

4. **Fixed null returns with proper exception handling:**
   - ✅ `StudentService.getStudentPodaciById` - Now throws `EntityNotFoundException` instead of returning null
   - ✅ `StudentService.fastSearch` - Now throws `IllegalArgumentException` or `EntityNotFoundException` instead of returning null
   - ✅ `StudentService.emailSearch` - Now throws `IllegalArgumentException` or `EntityNotFoundException` instead of returning null
   - ✅ `SlusaPredmetService.updateSlusaPredmet` - Now throws `EntityExistsException` instead of returning null
   - ✅ `SlusaPredmetService.fromSlusaPredmetToResponse` - Now throws `IllegalArgumentException` instead of returning null
   - ✅ `NastavnikController.getNastavnikById` - Now throws exception via service instead of returning null
   - ✅ `DrziPredmetController.getById` - Now throws exception via service instead of returning null

5. **Code quality improvements:**
   - ✅ Removed `System.out.println` from `DrziPredmetService` (business logic)
   - ✅ All services now properly inject `Converters` and `EntityMappers` for DTO conversion
   - ✅ All controllers are now simple pass-through layers with no business logic

**Result:** The application now fully adheres to the layered architecture. All services use constructor injection, accept Request DTOs, return Response DTOs, and handle errors with proper exceptions. Controllers are thin layers that only handle HTTP concerns.
