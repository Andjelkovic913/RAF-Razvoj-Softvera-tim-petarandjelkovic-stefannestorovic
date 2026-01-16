package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzba.dtos.IspitDTO;
import org.raflab.studsluzba.dtos.IspitniRokDTO;
import org.raflab.studsluzbadesktopclient.services.IspitService;
import org.raflab.studsluzbadesktopclient.services.PredmetService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@Component
public class IspitiController {

    private final IspitService ispitService;
    private final PredmetService predmetService;
    private final MainView mainView;

    private static IspitniRokDTO selectedIspitniRok;

    @FXML private Label selectedRokLabel;
    @FXML private ComboBox<DrziPredmetItem> drziPredmetCb;
    @FXML private DatePicker datumOdrzavanjaDp;
    @FXML private TextField vremePocetkaTf;
    @FXML private TextField vremeZavrsetkaTf;
    @FXML private TableView<IspitDTO> ispitiTable;
    @FXML private TableColumn<IspitDTO, Void> actionColumn;

    public IspitiController(IspitService ispitService, PredmetService predmetService, MainView mainView) {
        this.ispitService = ispitService;
        this.predmetService = predmetService;
        this.mainView = mainView;
    }

    public static void setSelectedIspitniRok(IspitniRokDTO rok) {
        selectedIspitniRok = rok;
    }

    @FXML
    public void initialize() {
        if (selectedIspitniRok != null) {
            selectedRokLabel.setText(selectedIspitniRok.getTipRoka() + 
                    " (" + selectedIspitniRok.getDatumPocetka() + " - " + selectedIspitniRok.getDatumZavrsetka() + ")");
        } else {
            selectedRokLabel.setText("Nije izabran rok");
        }
        
        loadDrziPredmeti();
        setupActionColumn();
        loadIspiti();
    }

    private void loadDrziPredmeti() {
        try {
            List<Map<String, Object>> drziPredmeti = predmetService.getAllDrziPredmet();
            drziPredmetCb.setItems(FXCollections.observableArrayList(
                    drziPredmeti.stream()
                            .map(dp -> {
                                Long id = dp.get("id") instanceof Number ? ((Number) dp.get("id")).longValue() : null;
                                Long predmetId = dp.get("predmetId") instanceof Number ? ((Number) dp.get("predmetId")).longValue() : null;
                                String predmetNaziv = (String) dp.get("predmetNaziv");
                                String nastavnikIme = (String) dp.get("nastavnikIme");
                                String nastavnikPrezime = (String) dp.get("nastavnikPrezime");
                                String fullNastavnik = (nastavnikIme != null ? nastavnikIme : "") + 
                                        (nastavnikPrezime != null ? " " + nastavnikPrezime : "");
                                return new DrziPredmetItem(id, predmetId, predmetNaziv, fullNastavnik.trim());
                            })
                            .toList()
            ));
        } catch (Exception e) {
            System.err.println("Error loading drzi predmet: " + e.getMessage());
        }
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<IspitDTO, Void> call(TableColumn<IspitDTO, Void> param) {
                return new TableCell<>() {
                    private final Button prijaveBtn = new Button("Prijave");
                    private final Button rezultatiBtn = new Button("Rezultati");
                    private final Button deleteBtn = new Button("Obriši");
                    private final HBox hbox = new HBox(5, prijaveBtn, rezultatiBtn, deleteBtn);

                    {
                        prijaveBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 11px;");
                        rezultatiBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 11px;");
                        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 11px;");

                        prijaveBtn.setOnAction(e -> {
                            IspitDTO ispit = getTableView().getItems().get(getIndex());
                            openPrijaveForIspit(ispit);
                        });

                        rezultatiBtn.setOnAction(e -> {
                            IspitDTO ispit = getTableView().getItems().get(getIndex());
                            openRezultatiForIspit(ispit);
                        });

                        deleteBtn.setOnAction(e -> {
                            IspitDTO ispit = getTableView().getItems().get(getIndex());
                            handleDeleteIspit(ispit);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : hbox);
                    }
                };
            }
        });
    }

    private void loadIspiti() {
        try {
            List<IspitDTO> ispiti;
            if (selectedIspitniRok != null) {
                ispiti = ispitService.getByIspitniRok(selectedIspitniRok.getId());
            } else {
                ispiti = ispitService.getAll();
            }
            ispitiTable.setItems(FXCollections.observableArrayList(ispiti));
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju ispita: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddIspit(ActionEvent event) {
        if (!validateForm()) return;

        try {
            IspitDTO dto = new IspitDTO();
            DrziPredmetItem selectedDrziPredmet = drziPredmetCb.getValue();
            
            dto.setDrziPredmetId(selectedDrziPredmet.getId());
            dto.setPredmetId(selectedDrziPredmet.getPredmetId());  // Server requires this!
            dto.setDatumOdrzavanja(datumOdrzavanjaDp.getValue());
            
            // Parse time fields - server requires both
            dto.setVremePocetka(LocalTime.parse(vremePocetkaTf.getText().trim()));
            dto.setVremeZavrsetka(LocalTime.parse(vremeZavrsetkaTf.getText().trim()));
            
            if (selectedIspitniRok != null) {
                dto.setIspitniRokId(selectedIspitniRok.getId());
                dto.setSkolskaGodinaId(selectedIspitniRok.getSkolskaGodinaId());  // Server requires this!
            }

            Long id = ispitService.create(dto);
            if (id != null) {
                showAlert("Uspeh", "Ispit je uspešno kreiran sa ID: " + id);
                handleClearForm(null);
                loadIspiti();
            }
        } catch (DateTimeParseException e) {
            showAlert("Greška", "Neispravan format vremena. Koristite HH:mm (npr. 09:00)");
        } catch (Exception e) {
            showAlert("Greška", e.getMessage());
        }
    }

    private void handleDeleteIspit(IspitDTO ispit) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Potvrda brisanja");
        confirm.setHeaderText("Da li ste sigurni?");
        confirm.setContentText("Brisanje ispita: " + ispit.getPredmetNaziv() + " - " + ispit.getDatumOdrzavanja());

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                boolean success = ispitService.delete(ispit.getId());
                if (success) {
                    showAlert("Uspeh", "Ispit je uspešno obrisan.");
                    loadIspiti();
                } else {
                    showAlert("Greška", "Neuspešno brisanje ispita.");
                }
            } catch (Exception e) {
                showAlert("Greška", e.getMessage());
            }
        }
    }

    private void openPrijaveForIspit(IspitDTO ispit) {
        PrijavaIspitaController.setSelectedIspit(ispit);
        mainView.changeRoot("prijavaIspita");
    }

    private void openRezultatiForIspit(IspitDTO ispit) {
        RezultatiIspitaController.setSelectedIspit(ispit);
        mainView.changeRoot("rezultatiIspita");
    }

    @FXML
    public void handleBackToRokovi(ActionEvent event) {
        mainView.changeRoot("ispitniRokovi");
    }

    @FXML
    public void handleClearForm(ActionEvent event) {
        drziPredmetCb.setValue(null);
        datumOdrzavanjaDp.setValue(null);
        vremePocetkaTf.clear();
        vremeZavrsetkaTf.clear();
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        loadIspiti();
    }

    private boolean validateForm() {
        if (drziPredmetCb.getValue() == null) {
            showAlert("Greška", "Izaberite predmet.");
            return false;
        }
        if (datumOdrzavanjaDp.getValue() == null) {
            showAlert("Greška", "Unesite datum održavanja.");
            return false;
        }
        if (vremePocetkaTf.getText().trim().isEmpty()) {
            showAlert("Greška", "Unesite vreme početka (npr. 09:00).");
            return false;
        }
        if (vremeZavrsetkaTf.getText().trim().isEmpty()) {
            showAlert("Greška", "Unesite vreme završetka (npr. 12:00).");
            return false;
        }
        if (selectedIspitniRok == null) {
            showAlert("Greška", "Nije izabran ispitni rok.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper class for DrziPredmet combo box
    public static class DrziPredmetItem {
        private final Long id;
        private final Long predmetId;
        private final String predmetNaziv;
        private final String nastavnikIme;

        public DrziPredmetItem(Long id, Long predmetId, String predmetNaziv, String nastavnikIme) {
            this.id = id;
            this.predmetId = predmetId;
            this.predmetNaziv = predmetNaziv;
            this.nastavnikIme = nastavnikIme;
        }

        public Long getId() { return id; }
        public Long getPredmetId() { return predmetId; }

        @Override
        public String toString() {
            return predmetNaziv + " (" + (nastavnikIme != null ? nastavnikIme : "N/A") + ")";
        }
    }
}
