package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzba.dtos.IspitniRokDTO;
import org.raflab.studsluzbadesktopclient.services.IspitniRokService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class IspitniRokoviController {

    private final IspitniRokService ispitniRokService;
    private final MainView mainView;
    
    private IspitniRokDTO selectedRok = null;

    @FXML private ComboBox<String> tipRokaCb;
    @FXML private DatePicker datumPocetkaDp;
    @FXML private DatePicker datumZavrsetkaDp;
    @FXML private ComboBox<SkolskaGodinaItem> skolskaGodinaCb;
    @FXML private TableView<IspitniRokDTO> ispitniRokoviTable;
    @FXML private TableColumn<IspitniRokDTO, Void> actionColumn;

    public IspitniRokoviController(IspitniRokService ispitniRokService, MainView mainView) {
        this.ispitniRokService = ispitniRokService;
        this.mainView = mainView;
    }

    @FXML
    public void initialize() {
        setupTipRokaComboBox();
        setupSkolskaGodinaComboBox();
        setupActionColumn();
        loadIspitniRokovi();
        
        // Add selection listener to populate form
        ispitniRokoviTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateForm(newVal);
            }
        });
    }

    private void setupTipRokaComboBox() {
        tipRokaCb.setItems(FXCollections.observableArrayList(
                "JANUAR", "FEBRUAR", "JUN", "JUL", "AVGUST", "SEPTEMBAR"
        ));
    }

    private void setupSkolskaGodinaComboBox() {
        try {
            List<Map<String, Object>> skolskeGodine = ispitniRokService.getAllSkolskeGodine();
            List<SkolskaGodinaItem> items = new ArrayList<>();
            for (Map<String, Object> sg : skolskeGodine) {
                Long id = sg.get("id") instanceof Number ? ((Number) sg.get("id")).longValue() : null;
                String naziv = (String) sg.get("naziv");
                if (id != null && naziv != null) {
                    items.add(new SkolskaGodinaItem(id, naziv));
                }
            }
            skolskaGodinaCb.setItems(FXCollections.observableArrayList(items));
        } catch (Exception e) {
            System.err.println("Error loading skolske godine: " + e.getMessage());
            // Fallback to hardcoded values if server fails
            int currentYear = LocalDate.now().getYear();
            skolskaGodinaCb.setItems(FXCollections.observableArrayList(
                    new SkolskaGodinaItem(1L, (currentYear - 1) + "/" + currentYear),
                    new SkolskaGodinaItem(2L, currentYear + "/" + (currentYear + 1))
            ));
        }
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<IspitniRokDTO, Void> call(TableColumn<IspitniRokDTO, Void> param) {
                return new TableCell<>() {
                    private final Button editBtn = new Button("Izmeni");
                    private final Button deleteBtn = new Button("Obriši");
                    private final Button ispitiBtn = new Button("Ispiti");
                    private final HBox hbox = new HBox(5, editBtn, deleteBtn, ispitiBtn);

                    {
                        editBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 11px;");
                        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 11px;");
                        ispitiBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 11px;");

                        editBtn.setOnAction(e -> {
                            IspitniRokDTO rok = getTableView().getItems().get(getIndex());
                            populateForm(rok);
                        });

                        deleteBtn.setOnAction(e -> {
                            IspitniRokDTO rok = getTableView().getItems().get(getIndex());
                            handleDeleteRok(rok);
                        });

                        ispitiBtn.setOnAction(e -> {
                            IspitniRokDTO rok = getTableView().getItems().get(getIndex());
                            openIspitiForRok(rok);
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

    private void loadIspitniRokovi() {
        try {
            List<IspitniRokDTO> rokovi = ispitniRokService.getAll();
            ispitniRokoviTable.setItems(FXCollections.observableArrayList(rokovi));
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju ispitnih rokova: " + e.getMessage());
        }
    }

    private void populateForm(IspitniRokDTO rok) {
        selectedRok = rok;
        tipRokaCb.setValue(rok.getTipRoka());
        datumPocetkaDp.setValue(rok.getDatumPocetka());
        datumZavrsetkaDp.setValue(rok.getDatumZavrsetka());
        
        // Try to select matching skolska godina
        for (SkolskaGodinaItem item : skolskaGodinaCb.getItems()) {
            if (item.getId().equals(rok.getSkolskaGodinaId())) {
                skolskaGodinaCb.setValue(item);
                break;
            }
        }
    }

    @FXML
    public void handleAddIspitniRok(ActionEvent event) {
        if (!validateForm()) return;

        try {
            IspitniRokDTO dto = new IspitniRokDTO();
            dto.setTipRoka(tipRokaCb.getValue());
            dto.setDatumPocetka(datumPocetkaDp.getValue());
            dto.setDatumZavrsetka(datumZavrsetkaDp.getValue());
            dto.setSkolskaGodinaId(skolskaGodinaCb.getValue().getId());

            Long id = ispitniRokService.create(dto);
            if (id != null) {
                showAlert("Uspeh", "Ispitni rok je uspešno kreiran sa ID: " + id);
                handleClearForm(null);
                loadIspitniRokovi();
            }
        } catch (Exception e) {
            showAlert("Greška", e.getMessage());
        }
    }

    @FXML
    public void handleUpdateIspitniRok(ActionEvent event) {
        if (selectedRok == null) {
            showAlert("Upozorenje", "Izaberite ispitni rok iz tabele za ažuriranje.");
            return;
        }
        if (!validateForm()) return;

        try {
            IspitniRokDTO dto = new IspitniRokDTO();
            dto.setTipRoka(tipRokaCb.getValue());
            dto.setDatumPocetka(datumPocetkaDp.getValue());
            dto.setDatumZavrsetka(datumZavrsetkaDp.getValue());
            dto.setSkolskaGodinaId(skolskaGodinaCb.getValue().getId());

            boolean success = ispitniRokService.update(selectedRok.getId(), dto);
            if (success) {
                showAlert("Uspeh", "Ispitni rok je uspešno ažuriran.");
                handleClearForm(null);
                loadIspitniRokovi();
            } else {
                showAlert("Greška", "Neuspešno ažuriranje ispitnog roka.");
            }
        } catch (Exception e) {
            showAlert("Greška", e.getMessage());
        }
    }

    private void handleDeleteRok(IspitniRokDTO rok) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Potvrda brisanja");
        confirm.setHeaderText("Da li ste sigurni?");
        confirm.setContentText("Brisanje ispitnog roka: " + rok.getTipRoka());

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                boolean success = ispitniRokService.delete(rok.getId());
                if (success) {
                    showAlert("Uspeh", "Ispitni rok je uspešno obrisan.");
                    loadIspitniRokovi();
                } else {
                    showAlert("Greška", "Neuspešno brisanje ispitnog roka.");
                }
            } catch (Exception e) {
                showAlert("Greška", e.getMessage());
            }
        }
    }

    private void openIspitiForRok(IspitniRokDTO rok) {
        IspitiController.setSelectedIspitniRok(rok);
        mainView.changeRoot("ispiti");
    }

    @FXML
    public void handleClearForm(ActionEvent event) {
        selectedRok = null;
        tipRokaCb.setValue(null);
        datumPocetkaDp.setValue(null);
        datumZavrsetkaDp.setValue(null);
        skolskaGodinaCb.setValue(null);
        ispitniRokoviTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        loadIspitniRokovi();
    }

    private boolean validateForm() {
        if (tipRokaCb.getValue() == null) {
            showAlert("Greška", "Izaberite tip roka.");
            return false;
        }
        if (datumPocetkaDp.getValue() == null) {
            showAlert("Greška", "Unesite datum početka.");
            return false;
        }
        if (datumZavrsetkaDp.getValue() == null) {
            showAlert("Greška", "Unesite datum završetka.");
            return false;
        }
        if (datumPocetkaDp.getValue().isAfter(datumZavrsetkaDp.getValue())) {
            showAlert("Greška", "Datum početka mora biti pre datuma završetka.");
            return false;
        }
        if (skolskaGodinaCb.getValue() == null) {
            showAlert("Greška", "Izaberite školsku godinu.");
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

    // Helper class for school year combo box
    public static class SkolskaGodinaItem {
        private final Long id;
        private final String naziv;

        public SkolskaGodinaItem(Long id, String naziv) {
            this.id = id;
            this.naziv = naziv;
        }

        public Long getId() { return id; }
        public String getNaziv() { return naziv; }

        @Override
        public String toString() { return naziv; }
    }
}
