# Service Entity Return Audit
**Date**: 2025-11-21  
**Status**: VIOLATIONS FOUND ⚠️

---

## Executive Summary

After comprehensive audit, **4 CRITICAL VIOLATIONS** found where services return entities instead of DTOs:

1. ❌ `StudentIndeksService.findByStudentIdAndAktivan()` - Returns `StudentIndeks` entity
2. ❌ `StudijskiProgramService.findByOznaka()` - Returns `List<StudijskiProgram>` entities
3. ❌ `StudentService.search()` - Returns `Page<StudentDTO>` (legacy DTO, not Response DTO)
4. ❌ `StudentService.getStudentIndeksById()` - Returns `StudentIndeksResponse` but can return `null`

---

## 🚨 Detailed Violations

### VIOLATION #1: StudentIndeksService.findByStudentIdAndAktivan()
**File**: `services/StudentIndeksService.java` (line 44-45)  
**Severity**: CRITICAL

```java
public StudentIndeks findByStudentIdAndAktivan(Long studentPodaciId) {
    return studentIndeksRepository.findAktivanStudentIndeksiByStudentPodaciId(studentPodaciId);
}
```

**Issue**: Returns `StudentIndeks` entity directly to caller.

**Rule Violated**: Services must return DTOs, never entities.

**Impact**: Any controller or service calling this method receives an entity, breaking layering.

**Fix Required**: 
- Inject `EntityMappers`
- Return `StudentIndeksResponse` DTO instead
- Map entity to DTO before returning

---

### VIOLATION #2: StudijskiProgramService.findByOznaka()
**File**: `services/StudijskiProgramService.java` (line 57-59)  
**Severity**: CRITICAL

```java
public List<StudijskiProgram> findByOznaka(String oznaka) {
    return studijskiProgramRepository.findByOznaka(oznaka);
}
```

**Issue**: Returns `List<StudijskiProgram>` entities directly.

**Rule Violated**: Services must return DTOs, never entities.

**Impact**: Exposes entity objects to callers, violates DTO boundary.

**Fix Required**:
- Inject `EntityMappers`
- Return `List<StudijskiProgramResponse>` instead
- Map entities to DTOs using stream

---

### VIOLATION #3: StudentService.search()
**File**: `services/StudentService.java` (line 227-234)  
**Severity**: MEDIUM

```java
public Page<StudentDTO> search(String ime, String prezime, String studProgram, Integer godina, Integer broj, Integer page, Integer size) {
    if (studProgram == null && godina == null && broj == null) {
        Page<StudentPodaci> spList = studentPodaciRepository.findStudent(ime, prezime, PageRequest.of(page, size, Sort.by("id").descending()));
        return spList.map(EntityMappers::fromStudentPodaciToDTO);
    }
    Page<StudentIndeks> siList = studentIndeksRepository.findStudentIndeks(ime, prezime, studProgram, godina, broj, PageRequest.of(page, size, Sort.by("id").descending()));
    return siList.map(EntityMappers::fromStudentIndeksToDTO);
}
```

**Issue**: Returns `Page<StudentDTO>` which is a legacy DTO, not a Response DTO.

**Rule Violated**: Services should return Response DTOs (from `controllers/response/`), not legacy DTOs (from `model/dtos/`).

**Impact**: Inconsistent DTO usage - some endpoints use Response DTOs, others use legacy DTOs.

**Fix Required**:
- Create `StudentSearchResponse` DTO if needed
- Or return `Page<StudentPodaciResponse>` or `Page<StudentIndeksResponse>`
- Update to use proper Response DTOs

---

### VIOLATION #4: StudentService.getStudentIndeksById()
**File**: `services/StudentService.java` (line 191-195)  
**Severity**: LOW (but still violation)

```java
public StudentIndeksResponse getStudentIndeksById(Long id) {
    return studentIndeksRepository.findById(id)
            .map(entityMappers::fromStudentIndexToResponse)
            .orElse(null);  // ❌ Returns null instead of throwing exception
}
```

**Issue**: Returns `null` instead of throwing exception. While it does return a DTO, the null handling violates error handling best practices.

**Rule Violated**: Services should throw exceptions for not found, not return null.

**Impact**: Callers must check for null, error handling is inconsistent.

**Fix Required**:
- Replace `.orElse(null)` with `.orElseThrow(...)`
- Throw `EntityNotFoundException` with descriptive message

---

## ✅ Compliant Services

The following services are **COMPLIANT** (return DTOs only):

- ✅ `PredmetService` - All methods return `PredmetResponse` or `Long`
- ✅ `NastavnikService` - All methods return `NastavnikResponse` or `Long`
- ✅ `SlusaPredmetService` - All methods return `SlusaPredmetResponse` or `Long`
- ✅ `DrziPredmetService` - Returns `DrziPredmetResponse` or `void`
- ✅ `IspitService` - Returns `Long` (ID only)
- ✅ `PrijavaIspitaService` - Returns `Long` or `List<PrijavljeniStudentDTO>`
- ✅ `RaspodelaNastaveService` - Returns DTOs or `Long`
- ✅ `StudentProfileService` - Returns `StudentProfileDTO` or `StudentWebProfileDTO`
- ✅ `UpisGodineService` - Returns `Long` or Response DTOs

---

## 📊 Summary

| Service | Issue | Severity | Status |
|---------|-------|----------|--------|
| `StudentIndeksService` | Returns entity | CRITICAL | ❌ NEEDS FIX |
| `StudijskiProgramService` | Returns entities | CRITICAL | ❌ NEEDS FIX |
| `StudentService.search()` | Returns legacy DTO | MEDIUM | ⚠️ NEEDS FIX |
| `StudentService.getStudentIndeksById()` | Returns null | LOW | ⚠️ NEEDS FIX |
| Other 9 services | Compliant | - | ✅ OK |

---

## 🔧 Fix Plan

### Priority 1: Critical (Must Fix)
1. **StudentIndeksService.findByStudentIdAndAktivan()**
   - Inject `EntityMappers`
   - Return `StudentIndeksResponse` DTO
   - Map entity before returning

2. **StudijskiProgramService.findByOznaka()**
   - Inject `EntityMappers`
   - Return `List<StudijskiProgramResponse>`
   - Use stream to map entities

### Priority 2: Important (Should Fix)
3. **StudentService.search()**
   - Return `Page<StudentPodaciResponse>` or create unified response DTO
   - Use Response DTOs consistently

4. **StudentService.getStudentIndeksById()**
   - Replace `.orElse(null)` with `.orElseThrow(EntityNotFoundException::new)`
   - Consistent error handling

---

## 🎯 Recommended Actions

1. **Immediate**: Fix violations #1 and #2 (critical entity returns)
2. **Short-term**: Fix violations #3 and #4 (DTO consistency and null handling)
3. **Verification**: Re-run audit after fixes to confirm 100% compliance

---

**Report Generated**: 2025-11-21  
**Violations Found**: 4  
**Compliant Services**: 9/13 (69%)  
**Action Required**: YES
