package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.raflab.studsluzba.dtos.DrziPredmetDTO;
import org.raflab.studsluzba.dtos.PredmetDTO;
import org.raflab.studsluzba.dtos.StudentDTO;
import org.raflab.studsluzba.dtos.StudProgramDTO;
import org.raflab.studsluzbadesktopclient.services.DrziPredmetService;
import org.raflab.studsluzbadesktopclient.services.PredmetService;
import org.raflab.studsluzbadesktopclient.services.SlusaPredmetService;
import org.raflab.studsluzbadesktopclient.services.StudProgramService;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PrijavaStudentaController {

    private final StudentService studentService;
    private final PredmetService predmetService;
    private final SlusaPredmetService slusaPredmetService;
    private final DrziPredmetService drziPredmetService;
    private final StudProgramService studProgramService;

    @FXML private TextField imeTf;
    @FXML private TextField prezimeTf;
    @FXML private TextField brojIndeksaTf;
    @FXML private TextField godinaIndeksaTf;
    @FXML private ComboBox<StudProgramDTO> smerCb;
    @FXML private Label selectedStudentLabel;
    @FXML private TableView<StudentDTO> studentiTable;
    @FXML private ComboBox<PredmetDTO> predmetComboBox;
    @FXML private ComboBox<DrziPredmetDTO> nastavnikComboBox;

    private StudentDTO selectedStudent = null;

    public PrijavaStudentaController(StudentService studentService, 
                                    PredmetService predmetService,
                                    SlusaPredmetService slusaPredmetService,
                                    DrziPredmetService drziPredmetService,
                                    StudProgramService studProgramService) {
        this.studentService = studentService;
        this.predmetService = predmetService;
        this.slusaPredmetService = slusaPredmetService;
        this.drziPredmetService = drziPredmetService;
        this.studProgramService = studProgramService;
    }

    @FXML
    public void initialize() {
        setupPredmetComboBox();
        setupNastavnikComboBox();
        setupSmerComboBox();
        setupTableSelection();
        loadPredmeti();
        loadStudijskiProgrami();
        selectedStudentLabel.setText("-");
    }

    private void setupSmerComboBox() {
        smerCb.setConverter(new StringConverter<>() {
            @Override
            public String toString(StudProgramDTO smer) {
                if (smer == null) return "";
                return smer.getOznaka() + " - " + smer.getNaziv();
            }

            @Override
            public StudProgramDTO fromString(String s) {
                return null;
            }
        });
    }

    private void loadStudijskiProgrami() {
        try {
            List<StudProgramDTO> programi = studProgramService.getSudijskiProgramiSorted();
            if (programi != null && !programi.isEmpty()) {
                smerCb.setItems(FXCollections.observableArrayList(programi));
            }
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju studijskih programa: " + e.getMessage());
        }
    }

    private void setupPredmetComboBox() {
        predmetComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(PredmetDTO predmet) {
                if (predmet == null) return "";
                String oznaka = predmet.getSifra() != null ? predmet.getSifra() : "";
                return oznaka + " - " + predmet.getNaziv();
            }

            @Override
            public PredmetDTO fromString(String s) {
                return null;
            }
        });
        
        // Add listener to load nastavnici when predmet is selected
        predmetComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadNastavniciForPredmet(newVal.getId());
            } else {
                nastavnikComboBox.setItems(FXCollections.observableArrayList());
            }
        });
    }
    
    private void loadNastavniciForPredmet(Long predmetId) {
        try {
            List<DrziPredmetDTO> nastavnici = drziPredmetService.getDrziPredmetByPredmetId(predmetId);
            nastavnikComboBox.setItems(FXCollections.observableArrayList(nastavnici));
            
            if (nastavnici.size() == 1) {
                nastavnikComboBox.setValue(nastavnici.get(0));
            }
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju nastavnika: " + e.getMessage());
        }
    }

    private void setupNastavnikComboBox() {
        nastavnikComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(DrziPredmetDTO drzi) {
                if (drzi == null) return "";
                if (drzi.getTeacher() == null) return "Nepoznat nastavnik";
                return drzi.getTeacher().getFirstName() + " " + drzi.getTeacher().getLastName();
            }

            @Override
            public DrziPredmetDTO fromString(String s) {
                return null;
            }
        });
    }

    private void setupTableSelection() {
        studentiTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectStudent(newSelection);
            }
        });
    }

    private void loadPredmeti() {
        predmetService.getAllPredmetiAsync()
            .subscribe(
                predmeti -> Platform.runLater(() -> {
                    if (predmeti != null && !predmeti.isEmpty()) {
                        predmetComboBox.setItems(FXCollections.observableArrayList(predmeti));
                    }
                }),
                error -> Platform.runLater(() ->
                    showAlert("Greška", "Greška pri učitavanju predmeta: " + error.getMessage())
                )
            );
    }

    @FXML
    public void handleSearchStudent(ActionEvent event) {
        String ime = imeTf.getText().trim();
        String prezime = prezimeTf.getText().trim();

        if (ime.isEmpty() && prezime.isEmpty()) {
            showAlert("Upozorenje", "Unesite ime ili prezime za pretragu.");
            return;
        }

        try {
            List<StudentDTO> studenti = studentService.searchByNameAndSurname(ime, prezime);
            studentiTable.setItems(FXCollections.observableArrayList(studenti));
            
            if (studenti.isEmpty()) {
                showAlert("Info", "Nije pronađen nijedan student.");
            }
        } catch (Exception e) {
            showAlert("Greška", "Greška pri pretrazi: " + e.getMessage());
        }
    }

    @FXML
    public void handleSearchByIndex(ActionEvent event) {
        String brojText = brojIndeksaTf.getText().trim();
        String godinaText = godinaIndeksaTf.getText().trim();
        StudProgramDTO selectedSmer = smerCb.getValue();
        String smer = selectedSmer != null ? selectedSmer.getOznaka() : null;

        if (brojText.isEmpty() || godinaText.isEmpty()) {
            showAlert("Upozorenje", "Unesite broj i godinu indeksa.");
            return;
        }

        try {
            Integer broj = Integer.parseInt(brojText);
            Integer godina = Integer.parseInt(godinaText);

            List<StudentDTO> studenti = studentService.searchByIndex(broj, godina, smer);
            studentiTable.setItems(FXCollections.observableArrayList(studenti));
            
            if (studenti.isEmpty()) {
                showAlert("Info", "Nije pronađen nijedan student.");
            } else if (studenti.size() == 1) {
                selectStudent(studenti.get(0));
            }
        } catch (NumberFormatException e) {
            showAlert("Greška", "Broj i godina moraju biti brojevi.");
        } catch (Exception e) {
            showAlert("Greška", "Greška pri pretrazi: " + e.getMessage());
        }
    }

    private void selectStudent(StudentDTO student) {
        selectedStudent = student;
        selectedStudentLabel.setText(student.getIme() + " " + student.getPrezime() + 
                " (" + student.getStudProgramOznaka() + " " + student.getBroj() + "/" + student.getGodinaUpisa() + ")");
    }

    @FXML
    public void handlePrijaviStudenta(ActionEvent event) {
        if (selectedStudent == null) {
            showAlert("Greška", "Morate izabrati studenta.");
            return;
        }

        PredmetDTO selectedPredmet = predmetComboBox.getValue();
        if (selectedPredmet == null) {
            showAlert("Greška", "Morate izabrati predmet.");
            return;
        }

        DrziPredmetDTO selectedDrzi = nastavnikComboBox.getValue();
        if (selectedDrzi == null) {
            showAlert("Greška", "Morate izabrati nastavnika.");
            return;
        }

        try {
            Long slusaPredmetId = slusaPredmetService.prijaviStudentaNaPredmet(
                selectedStudent.getIdIndeks(), 
                (long) selectedDrzi.getId()
            );
            
            if (slusaPredmetId != null) {
                String teacherName = selectedDrzi.getTeacher() != null ? 
                    selectedDrzi.getTeacher().getFirstName() + " " + selectedDrzi.getTeacher().getLastName() : 
                    "Nepoznat nastavnik";
                showAlert("Uspeh", "Student " + selectedStudent.getIme() + " " + selectedStudent.getPrezime() + 
                         " je uspešno prijavljen na predmet:\n" + 
                         selectedPredmet.getNaziv() + " (" + teacherName + ")");
                handleClear(null);
            }
        } catch (Exception e) {
            showAlert("Greška", "Greška pri prijavi studenta:\n" + e.getMessage());
        }
    }

    @FXML
    public void handleClear(ActionEvent event) {
        imeTf.clear();
        prezimeTf.clear();
        brojIndeksaTf.clear();
        godinaIndeksaTf.clear();
        selectedStudentLabel.setText("-");
        selectedStudent = null;
        studentiTable.setItems(FXCollections.observableArrayList());
        predmetComboBox.setValue(null);
        nastavnikComboBox.setValue(null);
        nastavnikComboBox.setItems(FXCollections.observableArrayList());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
