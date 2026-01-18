# UI Implementation Status - Klijentska Aplikacija
**Generated:** January 2, 2026  
**Last Updated:** January 2, 2026

## Overview
This document tracks the implementation status of UI specifications based on the project requirements document.

---

## 🚨 CRITICAL: MANDATORY FEATURE NOT IMPLEMENTED

### History Navigation (OBAVEZAN FEATURE - BEZ OVOG SE PROJEKAT NEĆE OCENJIVATI)
- **Status:** ❌ **NOT IMPLEMENTED**
- **Required Features:**
  - ❌ Mouse button 4/5 navigation (back/forward)
  - ❌ Touchpad gesture alternative (swipe left/right)
  - ❌ Keyboard shortcuts (Ctrl + [ / Ctrl + ])
  - ❌ Configurable history depth in `application.properties`
  - ❌ Forward history clearing on new navigation
  - ❌ Silent error handling for edge cases
- **Current State:** `MainView.java` only has scene caching, NO history stack implementation
- **Missing Files:**
  - `application.properties` (not found - required for history depth config)
  - History navigation manager/service

---

## 1. Student Data Management (13 points) - PARTIALLY IMPLEMENTED (~70%)

### ✅ COMPLETED Features

#### Student Search Functionality
- **Files:** `searchStudent.fxml` + `SearchStudentController.java`
- **Status:** ✅ FULLY IMPLEMENTED
- **Features:**
  - ✅ Search by name and surname (with table results)
  - ✅ Search by index number (broj indeksa + godina)
  - ✅ Search by high school (završena srednja škola)
  - ✅ Error handling and validation
  - ✅ "Prikaži profil" buttons in all search result tables
  - ✅ Navigation to student profile with state preservation

#### Student Profile - Basic Data Entry
- **Files:** `studentPodaciTabPane.fxml` + `StudentController.java`
- **Status:** ✅ IMPLEMENTED
- **Completed Tabs:**
  - ✅ Osnovni podaci - Personal data
  - ✅ Indeks - Index data
  - ✅ Kontakt podaci - Contact information
  - ✅ Prvi upis - First enrollment
- **Features:**
  - ✅ Full form validation
  - ✅ Save functionality for all tabs
  - ✅ Error messages for duplicate data

#### Student Creation
- **File:** `newStudent.fxml`
- **Status:** ✅ FULLY IMPLEMENTED

#### Student Profile - Complete View
- **Files:** `studentProfile.fxml` + `StudentProfileController.java`
- **Status:** ✅ FULLY IMPLEMENTED
- **Features:**
  - ✅ Full student profile page with TabPane layout
  - ✅ Tab: Osnovni podaci - Personal and contact information
  - ✅ Tab: Ispiti - Exam history (passed and failed)
  - ✅ Tab: Uplate - Payment history
  - ✅ Tab: Upisi - Enrollment and year renewal history
  - ✅ Tab: Statistika - Statistics (total ECTS, GPA, exam counts)
  - ✅ Navigation from search results
  - ✅ Back button with state preservation

### ❌ MISSING Features

#### Student Operations (Buttons exist as stubs in StudentProfileController)
- **Status:** ❌ NOT IMPLEMENTED (handlers show "Info" alert only)
- **Missing:**
  - ❌ **Upis (Enrollment)** - `handleEnrollYear()` - stub only
  - ❌ **Obnova godine (Year renewal)** - `handleRenewYear()` - stub only
  - ❌ **Unos uplate (Payment entry)** - `handleAddPayment()` - stub only
  - ❌ **Uverenje o studiranju** - Certificate of enrollment report
  - ❌ **Uverenje o položenim ispitima** - Transcript report (grouped by year)

---

## 2. Study Programs and Subjects (4 points) - PARTIALLY IMPLEMENTED (~60%)

### ✅ COMPLETED Features

#### Subject Management
- **Files:** `predmeti.fxml` + `PredmetViewController.java`, `addPredmet.fxml` + `AddPredmetController.java`
- **Features:**
  - ✅ Display study programs
  - ✅ Display subjects by study program
  - ✅ Add new subject to study program
  - ✅ Filter by mandatory subjects (obavezni predmeti)
  - ✅ Edit and delete subject buttons (UI present)

### ❌ MISSING Features

#### Subject Statistics and Reports
- **Status:** ❌ NOT IMPLEMENTED
- **Missing:**
  - ❌ Average grade by subject for year range (TABLE VIEW in app)
  - ❌ Printable report for subject average grades

---

## 3. Exam Management (5 points) - NOT IMPLEMENTED (0%)

### Menu Items Exist But NOT Functional
- **File:** `menuBar.fxml` has menu items:
  - "Ispitni rokovi" - NO handler
  - "Ispiti - unos" - NO handler
  - "Ispiti - prijave" - NO handler
  - "Ispiti - rezultati" - NO handler

### ❌ ALL Features Missing

#### Exam Period Management
- ❌ UI for creating exam periods (ispitni rokovi)
- ❌ UI for creating exams within exam periods
- ❌ Select exam period and exam functionality
- ❌ View students registered for exam

#### Student Exam Registration
- ❌ Register student for exam (with eligibility check)
- ❌ Check if student is taking the subject
- ❌ Check if student hasn't passed the exam yet

#### Exam Results
- ❌ Display exam results in sorted list
- ❌ Sort by: study program → enrollment year → index number
- ❌ Show total points (pre-exam obligations + exam points)
- ❌ Print exam results as exam protocol (zapisnik sa ispita)

---

## 4. Reports (8 points) - MINIMALLY IMPLEMENTED (~12%)

### ✅ COMPLETED Features

#### Basic Student Report
- **File:** `StudentController.java` (handleIzvestaj method)
- **Template:** `/reports/sviStudenti.jrxml`
- **Features:**
  - ✅ Generate PDF report of all students
  - ✅ Uses JasperReports

### ❌ MISSING Features (Per Spec - Added 22.12.2025)

- ❌ **Uverenje o studiranju** - Certificate of enrollment
- ❌ **Uverenje o položenim ispitima** - Transcript (exams grouped by year of study)
- ❌ **Zapisnik sa ispita** - Exam protocol report
- ❌ Subject average grade report (by year range)

---

## 5. Technical Requirements - PARTIALLY IMPLEMENTED

### ✅ COMPLETED

| Requirement | Status | Files |
|-------------|--------|-------|
| Spring Boot | ✅ | `pom.xml` |
| JavaFX with FXML | ✅ | 11 FXML files |
| Layered architecture | ✅ | `services/`, `controllers/`, `dtos/` |
| Error messages | ✅ | Alert dialogs in controllers |
| DTO classes | ✅ | 14 DTOs in `dtos/` package |
| JasperReports | ✅ | `pom.xml` + 1 report template |

### ❌ NOT IMPLEMENTED

| Requirement | Status | Notes |
|-------------|--------|-------|
| **History Navigation** | ❌ | **MANDATORY - PROJECT WON'T BE GRADED** |
| Async communication (WebClient) | ❌ | Only RestTemplate used in `StudentService.java` |
| Client-side caching component | ❌ | Only scene caching in MainView |
| Shared DTO library | ❌ | DTOs are local, not in separate Maven module |
| `application.properties` | ❌ | File not found |

---

## 6. Project Structure Summary

### Current Files

```
src/main/java/org/raflab/studsluzbadesktopclient/
├── controllers/ (9 files)
│   ├── AddPredmetController.java
│   ├── MainWindowController.java
│   ├── MenuBarController.java
│   ├── NastavnikController.java
│   ├── PredmetViewController.java
│   ├── SearchNastavnikController.java
│   ├── SearchStudentController.java
│   ├── StudentController.java
│   └── StudentProfileController.java
├── dtos/ (14 files)
├── services/ (5 files)
│   ├── NastavnikService.java
│   ├── PredmetService.java
│   ├── SrednjaSkolaService.java
│   ├── StudProgramService.java
│   └── StudentService.java
├── utils/ (2 files)
├── coder/ (6 files)
├── MainView.java
├── ContextFXMLLoader.java
├── ClientAppConfig.java
└── StudsluzbaFxClientApp.java

src/main/resources/
├── fxml/ (11 files)
├── reports/ (1 file - sviStudenti.jrxml)
├── css/ (1 file)
└── coders/ (5 files)
```

### Missing Services
- ❌ `ExamService.java` / `IspitService.java`
- ❌ `ExamPeriodService.java` / `IspitniRokService.java`
- ❌ `UplataService.java`
- ❌ `UpisService.java`
- ❌ `NavigationHistoryService.java`
- ❌ `CacheService.java`

---

## Summary of Implementation Status

| Category | Points | Status | Completion % |
|----------|--------|--------|--------------|
| **History Navigation** | **MANDATORY** | **❌ NOT DONE** | **0%** |
| Student Data Management | 13 | Partial | ~70% |
| Study Programs & Subjects | 4 | Partial | ~60% |
| Exam Management | 5 | Not Started | 0% |
| Reports | 8 | Minimal | ~12% |
| **TOTAL** | **30** | **In Progress** | **~40%** |

---

## 🎯 TOP PRIORITY IMPLEMENTATIONS

### 🔴 CRITICAL (Project Won't Be Graded Without This)

#### 1. History Navigation System
**Priority:** 🔴 CRITICAL - MANDATORY  
**Estimated Effort:** HIGH

**Files to Create:**
- `src/main/resources/application.properties` - Config for history depth
- `NavigationHistoryService.java` - History stack management
- Update `MainView.java` - Integrate history tracking
- Update `StudsluzbaFxClientApp.java` - Add mouse/keyboard listeners

**Requirements:**
- Mouse button 4/5 for back/forward
- Ctrl + [ / Ctrl + ] keyboard shortcuts
- Touchpad swipe alternative
- Configurable max history depth
- Silent handling of edge cases (empty history)

---

### 🟠 HIGH PRIORITY (Core Functionality for Points)

#### 2. Student Operations (Part of 13 points)
**Priority:** 🟠 HIGH  
**Files to Create:**
- `addPayment.fxml` + `AddPaymentController.java`
- `enrollYear.fxml` + `EnrollYearController.java`
- `renewYear.fxml` + `RenewYearController.java`
- `UplataService.java`
- `UpisService.java`

#### 3. Required Reports (8 points)
**Priority:** 🟠 HIGH  
**Files to Create:**
- `/reports/uverenjeStudiranje.jrxml` - Certificate of enrollment
- `/reports/uverenjePolozeniIspiti.jrxml` - Transcript (grouped by year)
- `/reports/zapisnikIspita.jrxml` - Exam protocol
- Update `StudentProfileController.java` handlers

#### 4. Exam Management System (5 points)
**Priority:** 🟠 HIGH  
**Files to Create:**
- `examPeriods.fxml` + `ExamPeriodController.java`
- `examRegistration.fxml` + `ExamRegistrationController.java`
- `examResults.fxml` + `ExamResultsController.java`
- `IspitService.java`
- `IspitniRokService.java`
- Update `MenuBarController.java` with handlers

---

### 🟡 MEDIUM PRIORITY (Technical & Quality)

#### 5. Subject Statistics (Part of 4 points)
- Average grade table view for year range
- Printable subject statistics report

#### 6. Async Communication
- Add `spring-boot-starter-webflux` to `pom.xml`
- Implement WebClient calls in services
- Prevent UI blocking

---

### 🟢 LOW PRIORITY (Polish)

#### 7. Client-side Caching
- Add caching component for frequently accessed data

#### 8. DTO Library Extraction
- Extract DTOs to separate Maven module
- Configure as local repository dependency

---

**Note:** This status document should be updated as features are implemented.
