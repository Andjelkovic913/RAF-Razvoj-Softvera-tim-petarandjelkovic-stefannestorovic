package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzba.dtos.IspitDTO;
import org.raflab.studsluzba.dtos.IspitniRokDTO;
import org.raflab.studsluzba.dtos.PrijavljeniStudentDTO;
import org.raflab.studsluzba.dtos.StudentDTO;
import org.raflab.studsluzba.dtos.StudProgramDTO;
import org.raflab.studsluzbadesktopclient.services.IspitService;
import org.raflab.studsluzbadesktopclient.services.IspitniRokService;
import org.raflab.studsluzbadesktopclient.services.PrijavaIspitaService;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.raflab.studsluzbadesktopclient.services.StudProgramService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PrijavaIspitaController {

    private final IspitService ispitService;
    private final IspitniRokService ispitniRokService;
    private final PrijavaIspitaService prijavaIspitaService;
    private final StudentService studentService;
    private final StudProgramService studProgramService;
    private final MainView mainView;

    private static IspitDTO preSelectedIspit;
    private IspitDTO selectedIspit = null;
    private IspitniRokDTO selectedIspitniRok = null;
    private Long selectedStudentIndeksId = null;
    private List<StudentDTO> foundStudents = null;

    @FXML private ComboBox<IspitniRokDTO> ispitniRokComboBox;
    @FXML private ComboBox<IspitDTO> ispitComboBox;
    @FXML private ComboBox<StudProgramDTO> smerComboBox;
    @FXML private TextField brojIndeksaTf;
    @FXML private TextField godinaIndeksaTf;
    @FXML private HBox studentSelectionBox;
    @FXML private ComboBox<StudentDTO> studentComboBox;
    @FXML private Label studentInfoLabel;
    @FXML private TableView<PrijavljeniStudentDTO> prijavljeniTable;

    public PrijavaIspitaController(IspitService ispitService, IspitniRokService ispitniRokService,
                                   PrijavaIspitaService prijavaIspitaService,
                                   StudentService studentService, StudProgramService studProgramService,
                                   MainView mainView) {
        this.ispitService = ispitService;
        this.ispitniRokService = ispitniRokService;
        this.prijavaIspitaService = prijavaIspitaService;
        this.studentService = studentService;
        this.studProgramService = studProgramService;
        this.mainView = mainView;
    }

    public static void setSelectedIspit(IspitDTO ispit) {
        preSelectedIspit = ispit;
    }

    @FXML
    public void initialize() {
        setupIspitniRokComboBox();
        setupIspitComboBox();
        setupSmerComboBox();
        setupStudentComboBox();
        loadIspitniRokovi();
        loadStudijskiProgrami();
        
        studentInfoLabel.setText("");
        studentSelectionBox.setVisible(false);
        studentSelectionBox.setManaged(false);
        
        // Auto-select preselected ispit if it exists
        if (preSelectedIspit != null && preSelectedIspit.getIspitniRokId() != null) {
            autoSelectPreSelectedIspit();
        }
    }

    private void setupIspitniRokComboBox() {
        ispitniRokComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(IspitniRokDTO rok) {
                if (rok == null) return "";
                return rok.getTipRoka() + " (" + rok.getDatumPocetka() + " - " + rok.getDatumZavrsetka() + ")";
            }

            @Override
            public IspitniRokDTO fromString(String s) {
                return null;
            }
        });
    }

    private void setupIspitComboBox() {
        ispitComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(IspitDTO ispit) {
                if (ispit == null) return "";
                return ispit.getPredmetNaziv() + " - " + ispit.getDatumOdrzavanja();
            }

            @Override
            public IspitDTO fromString(String s) {
                return null;
            }
        });
    }

    private void setupSmerComboBox() {
        smerComboBox.setConverter(new StringConverter<>() {
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

    private void setupStudentComboBox() {
        studentComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(StudentDTO student) {
                if (student == null) return "";
                return student.getIme() + " " + student.getPrezime() + 
                       " (" + student.getStudProgramOznaka() + " " + student.getBroj() + "/" + student.getGodinaUpisa() + ")";
            }

            @Override
            public StudentDTO fromString(String s) {
                return null;
            }
        });
    }

    private void loadIspitniRokovi() {
        try {
            List<IspitniRokDTO> rokovi = ispitniRokService.getAll();
            ispitniRokComboBox.setItems(FXCollections.observableArrayList(rokovi));
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju ispitnih rokova: " + e.getMessage());
        }
    }

    private void loadIspiti() {
        if (selectedIspitniRok == null) {
            ispitComboBox.setItems(FXCollections.observableArrayList());
            return;
        }
        
        try {
            List<IspitDTO> ispiti = ispitService.getByIspitniRok(selectedIspitniRok.getId());
            ispitComboBox.setItems(FXCollections.observableArrayList(ispiti));
            
            if (preSelectedIspit != null) {
                for (IspitDTO ispit : ispiti) {
                    if (ispit.getId().equals(preSelectedIspit.getId())) {
                        ispitComboBox.setValue(ispit);
                        selectedIspit = ispit;
                        loadPrijavljeniStudenti();
                        preSelectedIspit = null;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju ispita: " + e.getMessage());
        }
    }

    private void loadStudijskiProgrami() {
        try {
            List<StudProgramDTO> programi = studProgramService.getSudijskiProgramiSorted();
            if (programi != null) {
                smerComboBox.setItems(FXCollections.observableArrayList(programi));
            }
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju studijskih programa: " + e.getMessage());
        }
    }

    private void autoSelectPreSelectedIspit() {
        // Find and select the ispitni rok for the preselected ispit
        List<IspitniRokDTO> rokovi = ispitniRokComboBox.getItems();
        for (IspitniRokDTO rok : rokovi) {
            if (rok.getId().equals(preSelectedIspit.getIspitniRokId())) {
                ispitniRokComboBox.setValue(rok);
                selectedIspitniRok = rok;
                loadIspiti(); // This will auto-select the ispit in loadIspiti method
                break;
            }
        }
    }

    @FXML
    public void handleIspitniRokSelected(ActionEvent event) {
        selectedIspitniRok = ispitniRokComboBox.getValue();
        ispitComboBox.setValue(null);
        selectedIspit = null;
        loadIspiti();
        prijavljeniTable.setItems(FXCollections.observableArrayList());
    }

    @FXML
    public void handleIspitSelected(ActionEvent event) {
        selectedIspit = ispitComboBox.getValue();
        loadPrijavljeniStudenti();
    }

    private void loadPrijavljeniStudenti() {
        if (selectedIspit == null) {
            prijavljeniTable.setItems(FXCollections.observableArrayList());
            return;
        }
        
        try {
            List<PrijavljeniStudentDTO> prijavljeni = ispitService.getPrijavljeniStudenti(selectedIspit.getId());
            prijavljeniTable.setItems(FXCollections.observableArrayList(prijavljeni));
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju prijavljenih studenata: " + e.getMessage());
        }
    }

    @FXML
    public void handleSearchStudent(ActionEvent event) {
        String brojText = brojIndeksaTf.getText().trim();
        String godinaText = godinaIndeksaTf.getText().trim();

        if (brojText.isEmpty() || godinaText.isEmpty()) {
            showAlert("Upozorenje", "Unesite broj i godinu indeksa.");
            return;
        }

        try {
            Integer broj = Integer.parseInt(brojText);
            Integer godina = Integer.parseInt(godinaText);

            foundStudents = studentService.searchByIndex(broj, godina);
            
            StudProgramDTO selectedSmer = smerComboBox.getValue();
            if (selectedSmer != null) {
                foundStudents = foundStudents.stream()
                    .filter(s -> selectedSmer.getOznaka().equals(s.getStudProgramOznaka()))
                    .toList();
            }
            
            if (foundStudents.isEmpty()) {
                studentInfoLabel.setText("Student nije pronađen");
                selectedStudentIndeksId = null;
                hideStudentSelection();
            } else if (foundStudents.size() == 1) {
                StudentDTO student = foundStudents.get(0);
                selectStudent(student);
                hideStudentSelection();
            } else {
                studentComboBox.setItems(FXCollections.observableArrayList(foundStudents));
                studentComboBox.setValue(null);
                studentSelectionBox.setVisible(true);
                studentSelectionBox.setManaged(true);
                studentInfoLabel.setText("Pronađeno " + foundStudents.size() + " studenata - izaberite iz liste");
                selectedStudentIndeksId = null;
            }
        } catch (NumberFormatException e) {
            showAlert("Greška", "Broj i godina moraju biti brojevi.");
        } catch (Exception e) {
            showAlert("Greška", "Greška pri pretrazi: " + e.getMessage());
        }
    }

    @FXML
    public void handleStudentSelected(ActionEvent event) {
        StudentDTO selected = studentComboBox.getValue();
        if (selected != null) {
            selectStudent(selected);
        }
    }

    private void selectStudent(StudentDTO student) {
        selectedStudentIndeksId = student.getIdIndeks();
        studentInfoLabel.setText(student.getIme() + " " + student.getPrezime() + 
                " (" + student.getStudProgramOznaka() + " " + student.getBroj() + "/" + student.getGodinaUpisa() + ")");
    }

    private void hideStudentSelection() {
        studentSelectionBox.setVisible(false);
        studentSelectionBox.setManaged(false);
    }

    @FXML
    public void handlePrijaviIspit(ActionEvent event) {
        if (selectedIspit == null) {
            showAlert("Greška", "Nije izabran ispit.");
            return;
        }
        if (selectedStudentIndeksId == null) {
            showAlert("Greška", "Prvo pretražite i izaberite studenta.");
            return;
        }

        try {
            Long prijavaId = prijavaIspitaService.prijaviIspit(selectedStudentIndeksId, selectedIspit.getId());
            if (prijavaId != null) {
                showAlert("Uspeh", "Student je uspešno prijavljen na ispit.");
                clearStudentSelection();
                loadPrijavljeniStudenti();
            }
        } catch (Exception e) {
            showAlert("Greška", e.getMessage());
        }
    }

    private void clearStudentSelection() {
        brojIndeksaTf.clear();
        godinaIndeksaTf.clear();
        studentInfoLabel.setText("");
        selectedStudentIndeksId = null;
        foundStudents = null;
        hideStudentSelection();
    }

    @FXML
    public void handleClearIspitniRok(ActionEvent event) {
        ispitniRokComboBox.setValue(null);
        selectedIspitniRok = null;
        ispitComboBox.setValue(null);
        ispitComboBox.setItems(FXCollections.observableArrayList());
        selectedIspit = null;
        prijavljeniTable.setItems(FXCollections.observableArrayList());
    }

    @FXML
    public void handleClearIspit(ActionEvent event) {
        ispitComboBox.setValue(null);
        selectedIspit = null;
        prijavljeniTable.setItems(FXCollections.observableArrayList());
    }

    @FXML
    public void handleClearSmer(ActionEvent event) {
        smerComboBox.setValue(null);
    }

    @FXML
    public void handleBackToIspiti(ActionEvent event) {
        mainView.changeRoot("ispiti");
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        loadPrijavljeniStudenti();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
