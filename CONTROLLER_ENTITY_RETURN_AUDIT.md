# Controller Entity Return Audit
**Date**: 2025-11-21  
**Status**: VIOLATIONS FOUND ⚠️

---

## Executive Summary

After comprehensive audit of all 10 controllers, **3 CRITICAL VIOLATIONS** found where controllers return entities or legacy DTOs:

1. ❌ `StudentController.search()` - Returns `Page<StudentDTO>` (legacy DTO, not Response DTO)
2. ❌ `StudentController.getStudentWebProfileForEmail()` - Returns `null` instead of throwing exception
3. ❌ `RaspodelaNastaveController` - Returns `List<StudentIndeksDTO>` (legacy DTO, not Response DTO)
4. ⚠️ `NastavnikResponse` - Contains `Set<NastavnikZvanje>` entity field (should be DTO)

---

## 🚨 Detailed Violations

### VIOLATION #1: StudentController.search()
**File**: `controllers/StudentController.java` (line 88-97)  
**Severity**: CRITICAL

```java
@GetMapping(path="/search")
public Page<StudentDTO> search(@RequestParam (required = false) String ime,
                               @RequestParam (required = false) String prezime,
                               @RequestParam (required = false) String studProgram,
                               @RequestParam (required = false) Integer godina,
                               @RequestParam (required = false) Integer broj,
                               @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "10") Integer size) {
    return studentService.search(ime, prezime, studProgram, godina, broj, page, size);
}
```

**Issue**: Returns `Page<StudentDTO>` which is a legacy DTO from `model/dtos/`, not a Response DTO from `controllers/response/`.

**Rule Violated**: Controllers must return Response DTOs (from `controllers/response/`), not legacy DTOs.

**Impact**: Inconsistent DTO usage - violates standardized response format.

**Fix Required**:
- Create `StudentSearchResponse` DTO or use `StudentPodaciResponse`/`StudentIndeksResponse`
- Update controller return type to use Response DTO
- Update service to return Response DTO

---

### VIOLATION #2: StudentController.getStudentWebProfileForEmail()
**File**: `controllers/StudentController.java` (line 111-118)  
**Severity**: CRITICAL

```java
@GetMapping(path="/webprofile/email")
public StudentWebProfileDTO getStudentWebProfileForEmail(@RequestParam String studEmail) {
    StudentIndeksResponse si = studentService.emailSearch(studEmail);
    if(si != null) {
        return studentProfileService.getStudentWebProfile(si.getId());
    }
    return null;  // ❌ Returns null
}
```

**Issue**: Returns `null` instead of throwing exception when student not found.

**Rule Violated**: Services/Controllers should throw exceptions for not found, not return null.

**Impact**: Callers must check for null, inconsistent error handling.

**Fix Required**:
- Remove null check
- Let service throw exception if not found
- Or throw exception explicitly

---

### VIOLATION #3: RaspodelaNastaveController
**File**: `controllers/RaspodelaNastaveController.java` (line 28-40)  
**Severity**: CRITICAL

```java
@GetMapping(path = "/slusapredmetaktivna/{idPredmeta}/{idNastavnika}")
public List<StudentIndeksDTO> getSlusaPredmetUAktivnojSkolskojGodini(@PathVariable Long idPredmeta, @PathVariable Long idNastavnika) {
    return raspodelaNastaveService.getStudentiSlusaPredmetAktivnaGodina(idPredmeta, idNastavnika);
}

@GetMapping(path = "/slusapredmetaktivna/{idDrziPredmet}")
public List<StudentIndeksDTO> getSlusaPredmetUAktivnojSkolskojGodiniForDrziPredmet(@PathVariable Long idDrziPredmet) {
    return raspodelaNastaveService.getStudentiSlusaPredmetZaDrziPredmet(idDrziPredmet);
}

@GetMapping(path = "/neslusapredmetaktivna/{idDrziPredmet}")
public List<StudentIndeksDTO> getNeSlusaPredmetUAktivnojSkolskojGodiniForDrziPredmet(@PathVariable Long idDrziPredmet) {
    return raspodelaNastaveService.getStudentiNeSlusajuDrziPredmet(idDrziPredmet);
}
```

**Issue**: Returns `List<StudentIndeksDTO>` which is a legacy DTO from `model/dtos/`, not a Response DTO.

**Rule Violated**: Controllers must return Response DTOs, not legacy DTOs.

**Impact**: Inconsistent DTO usage across API.

**Fix Required**:
- Create `StudentIndeksResponse` if not exists (or use existing one)
- Update controller return type to `List<StudentIndeksResponse>`
- Update service to return Response DTO

---

### VIOLATION #4: NastavnikResponse - Contains Entity Field
**File**: `controllers/response/NastavnikResponse.java` (line 24)  
**Severity**: MEDIUM

```java
@Data
@NoArgsConstructor
public class NastavnikResponse {
    // ... other fields ...
    private Set<NastavnikZvanje> zvanja;  // ❌ Entity, not DTO
    // ...
}
```

**Issue**: Response DTO contains `Set<NastavnikZvanje>` which is an entity, not a DTO.

**Rule Violated**: DTOs must contain only primitives or other DTOs, never entities.

**Impact**: Exposes entity structure to API clients, violates DTO boundary.

**Fix Required**:
- Create `NastavnikZvanjeDTO` or `NastavnikZvanjeResponse`
- Replace `Set<NastavnikZvanje>` with `Set<NastavnikZvanjeDTO>`
- Update `EntityMappers` to map entity to DTO

---

## ✅ Compliant Controllers

The following controllers are **COMPLIANT** (return Response DTOs only):

- ✅ `PredmetController` - All methods return `PredmetResponse` or `Long`
- ✅ `NastavnikController` - All methods return `NastavnikResponse` or `Long`
- ✅ `SlusaPredmetController` - All methods return `SlusaPredmetResponse` or `Long`
- ✅ `DrziPredmetController` - Returns `DrziPredmetResponse`
- ✅ `IspitController` - Returns `Long`
- ✅ `StudijskiProgramController` - Returns `StudijskiProgramResponse` or `Long`
- ✅ `UpisGodineController` - Returns Response DTOs or `Long`

---

## 📊 Summary

| Controller | Issue | Severity | Status |
|------------|-------|----------|--------|
| `StudentController` | Returns legacy DTO + null | CRITICAL | ❌ NEEDS FIX |
| `RaspodelaNastaveController` | Returns legacy DTO | CRITICAL | ❌ NEEDS FIX |
| `NastavnikResponse` | Contains entity field | MEDIUM | ⚠️ NEEDS FIX |
| Other 7 controllers | Compliant | - | ✅ OK |

---

## 🔧 Fix Plan

### Priority 1: Critical (Must Fix)
1. **StudentController.search()**
   - Create `StudentSearchResponse` DTO or use `StudentPodaciResponse`
   - Update return type to Response DTO
   - Update service to return Response DTO

2. **StudentController.getStudentWebProfileForEmail()**
   - Remove null check
   - Let service throw exception
   - Or throw `EntityNotFoundException` explicitly

3. **RaspodelaNastaveController**
   - Replace `StudentIndeksDTO` with `StudentIndeksResponse`
   - Update all 3 methods to return Response DTO
   - Update service to return Response DTO

### Priority 2: Important (Should Fix)
4. **NastavnikResponse**
   - Create `NastavnikZvanjeDTO` or `NastavnikZvanjeResponse`
   - Replace `Set<NastavnikZvanje>` with `Set<NastavnikZvanjeDTO>`
   - Update `EntityMappers` to map entity to DTO

---

## 🎯 Recommended Actions

1. **Immediate**: Fix violations #1, #2, #3 (critical DTO inconsistencies)
2. **Short-term**: Fix violation #4 (entity in DTO)
3. **Verification**: Re-run audit after fixes to confirm 100% compliance

---

**Report Generated**: 2025-11-21  
**Violations Found**: 4  
**Compliant Controllers**: 7/10 (70%)  
**Action Required**: YES
