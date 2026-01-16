package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.raflab.studsluzbadesktopclient.dtos.DrziPredmetDTO;
import org.raflab.studsluzba.dtos.PredmetDTO;
import org.raflab.studsluzba.dtos.StudentDTO;
import org.raflab.studsluzbadesktopclient.services.DrziPredmetService;
import org.raflab.studsluzbadesktopclient.services.PredmetService;
import org.raflab.studsluzbadesktopclient.services.SlusaPredmetService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListaPrijavljenihController {

    private final PredmetService predmetService;
    private final DrziPredmetService drziPredmetService;
    private final SlusaPredmetService slusaPredmetService;

    @FXML private TableView<PredmetDTO> predmetiTable;
    @FXML private TableColumn<PredmetDTO, String> predmetSifraColumn;
    @FXML private TableColumn<PredmetDTO, String> predmetNazivColumn;
    
    @FXML private TableView<StudentDTO> studentiTable;
    @FXML private TableColumn<StudentDTO, String> studentIndeksColumn;
    @FXML private TableColumn<StudentDTO, String> studentImeColumn;
    @FXML private TableColumn<StudentDTO, String> studentPrezimeColumn;
    @FXML private TableColumn<StudentDTO, String> studentSmerColumn;
    
    @FXML private Label predmetInfoLabel;
    @FXML private Label brojStudenataLabel;
    @FXML private ComboBox<DrziPredmetDTO> nastavnikComboBox;

    private PredmetDTO selectedPredmet = null;

    public ListaPrijavljenihController(PredmetService predmetService,
                                      DrziPredmetService drziPredmetService,
                                      SlusaPredmetService slusaPredmetService) {
        this.predmetService = predmetService;
        this.drziPredmetService = drziPredmetService;
        this.slusaPredmetService = slusaPredmetService;
    }

    @FXML
    public void initialize() {
        setupPredmetiTable();
        setupStudentiTable();
        setupNastavnikComboBox();
        loadPredmeti();
        predmetInfoLabel.setText("");
        brojStudenataLabel.setText("");
    }

    private void setupPredmetiTable() {
        predmetSifraColumn.setCellValueFactory(new PropertyValueFactory<>("sifra"));
        predmetNazivColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        
        predmetiTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectPredmet(newSelection);
            }
        });
    }

    private void setupStudentiTable() {
        studentIndeksColumn.setCellValueFactory(new PropertyValueFactory<>("fullIndexDisplay"));
        studentImeColumn.setCellValueFactory(new PropertyValueFactory<>("ime"));
        studentPrezimeColumn.setCellValueFactory(new PropertyValueFactory<>("prezime"));
        studentSmerColumn.setCellValueFactory(new PropertyValueFactory<>("studProgramOznaka"));
    }

    private void setupNastavnikComboBox() {
        nastavnikComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(DrziPredmetDTO drzi) {
                if (drzi == null) return "Svi nastavnici";
                return drzi.getNastavnikIme() + " " + drzi.getNastavnikPrezime();
            }

            @Override
            public DrziPredmetDTO fromString(String s) {
                return null;
            }
        });
    }

    private void loadPredmeti() {
        try {
            List<PredmetDTO> predmeti = predmetService.getAllPredmeti();
            predmetiTable.setItems(FXCollections.observableArrayList(predmeti));
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju predmeta: " + e.getMessage());
        }
    }

    private void selectPredmet(PredmetDTO predmet) {
        selectedPredmet = predmet;
        String sifra = predmet.getSifra() != null ? predmet.getSifra() : "";
        predmetInfoLabel.setText("(" + sifra + " - " + predmet.getNaziv() + ")");
        
        // Load nastavnici for this predmet
        loadNastavniciForPredmet(predmet.getId());
        
        // Clear students table
        studentiTable.setItems(FXCollections.observableArrayList());
        brojStudenataLabel.setText("");
    }

    private void loadNastavniciForPredmet(Long predmetId) {
        try {
            List<DrziPredmetDTO> nastavnici = drziPredmetService.getDrziPredmetByPredmetId(predmetId);
            nastavnikComboBox.setItems(FXCollections.observableArrayList(nastavnici));
            nastavnikComboBox.setValue(null);
            
            if (nastavnici.size() == 1) {
                nastavnikComboBox.setValue(nastavnici.get(0));
                handlePrikaziStudente(null);
            }
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju nastavnika: " + e.getMessage());
        }
    }

    @FXML
    public void handlePrikaziStudente(ActionEvent event) {
        if (selectedPredmet == null) {
            showAlert("Upozorenje", "Izaberite predmet.");
            return;
        }

        DrziPredmetDTO selectedDrzi = nastavnikComboBox.getValue();
        if (selectedDrzi == null) {
            showAlert("Upozorenje", "Izaberite nastavnika.");
            return;
        }

        try {
            List<StudentDTO> studenti = slusaPredmetService.getStudentiZaDrziPredmet(selectedDrzi.getId());
            studentiTable.setItems(FXCollections.observableArrayList(studenti));
            brojStudenataLabel.setText("Ukupno studenata: " + studenti.size());
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju studenata: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
