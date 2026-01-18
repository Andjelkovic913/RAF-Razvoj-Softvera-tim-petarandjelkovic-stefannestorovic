package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.raflab.studsluzba.dtos.IspitDTO;
import org.raflab.studsluzba.dtos.IspitniRokDTO;
import org.raflab.studsluzbadesktopclient.services.IspitService;
import org.raflab.studsluzbadesktopclient.services.IspitniRokService;
import org.raflab.studsluzbadesktopclient.services.PrijavaIspitaService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PrijavaIspitaPopupController {

    private final IspitService ispitService;
    private final IspitniRokService ispitniRokService;
    private final PrijavaIspitaService prijavaIspitaService;

    private static Long studentIndeksId;
    private static String studentName;
    private static String studentIndex;

    @FXML private Label studentNameLabel;
    @FXML private Label studentIndexLabel;
    @FXML private ComboBox<IspitniRokDTO> ispitniRokComboBox;
    @FXML private ComboBox<IspitDTO> ispitComboBox;

    public PrijavaIspitaPopupController(IspitService ispitService, 
                                       IspitniRokService ispitniRokService,
                                       PrijavaIspitaService prijavaIspitaService) {
        this.ispitService = ispitService;
        this.ispitniRokService = ispitniRokService;
        this.prijavaIspitaService = prijavaIspitaService;
    }

    public static void setStudentData(Long indeksId, String name, String index) {
        studentIndeksId = indeksId;
        studentName = name;
        studentIndex = index;
    }

    @FXML
    public void initialize() {
        // Set student info
        studentNameLabel.setText(studentName != null ? studentName : "-");
        studentIndexLabel.setText(studentIndex != null ? studentIndex : "-");

        // Setup combo boxes
        setupIspitniRokComboBox();
        setupIspitComboBox();

        // Load data
        loadIspitniRokovi();
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
                return ispit.getPredmetNaziv() + " - " + ispit.getDatumOdrzavanja() + 
                       " (" + ispit.getVremePocetka() + ")";
            }

            @Override
            public IspitDTO fromString(String s) {
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

    @FXML
    public void handleIspitniRokSelected(ActionEvent event) {
        IspitniRokDTO selectedRok = ispitniRokComboBox.getValue();
        if (selectedRok == null) {
            ispitComboBox.setItems(FXCollections.observableArrayList());
            return;
        }

        try {
            List<IspitDTO> ispiti = ispitService.getByIspitniRok(selectedRok.getId());
            ispitComboBox.setItems(FXCollections.observableArrayList(ispiti));
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju ispita: " + e.getMessage());
        }
    }

    @FXML
    public void handlePrijavi(ActionEvent event) {
        IspitDTO selectedIspit = ispitComboBox.getValue();
        
        if (selectedIspit == null) {
            showAlert("Greška", "Morate izabrati ispit.");
            return;
        }

        if (studentIndeksId == null) {
            showAlert("Greška", "Podaci o studentu nisu dostupni.");
            return;
        }

        try {
            Long prijavaId = prijavaIspitaService.prijaviIspit(studentIndeksId, selectedIspit.getId());
            if (prijavaId != null) {
                showAlert("Uspeh", "Student je uspešno prijavljen na ispit:\n" + 
                         selectedIspit.getPredmetNaziv() + " - " + selectedIspit.getDatumOdrzavanja());
                closeWindow();
            }
        } catch (Exception e) {
            showAlert("Greška", "Greška pri prijavi ispita:\n" + e.getMessage());
        }
    }

    @FXML
    public void handleOtkazi(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) studentNameLabel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
