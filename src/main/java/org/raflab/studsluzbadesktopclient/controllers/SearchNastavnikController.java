package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.coder.CoderFactory;
import org.raflab.studsluzbadesktopclient.coder.CoderType;
import org.raflab.studsluzbadesktopclient.coder.SimpleCode;
import org.raflab.studsluzba.dtos.NastavnikDTO;
import org.raflab.studsluzbadesktopclient.services.NavigationHistoryService;
import org.raflab.studsluzbadesktopclient.services.NastavnikService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchNastavnikController {

    private static final String VIEW_NAME = "searchNastavnik";
    
    private final NastavnikService nastavnikService;
    private final MainView mainView;
    private final CoderFactory coderFactory;
    private final NavigationHistoryService navigationHistoryService;
    
    @FXML private TabPane mainTabPane;

    @FXML
    private TextField imeNastavnikaTf;

    @FXML
    private TextField prezimeNastavnikaTf;

    @FXML
    private TableView<NastavnikDTO> tabelaNastavniciPoImenu;

    @FXML
    private ComboBox<String> zvanjeCb;

    @FXML
    private TableView<NastavnikDTO> tabelaNastavniciPoZvanju;

    @FXML
    private TextField emailSearchTf;

    @FXML
    private TableView<NastavnikDTO> tabelaNastavniciPoEmailu;

    @FXML
    private TableColumn<NastavnikDTO, Void> actionColumnName;

    @FXML
    private TableColumn<NastavnikDTO, Void> actionColumnZvanje;

    @FXML
    private TableColumn<NastavnikDTO, Void> actionColumnEmail;

    public SearchNastavnikController(NastavnikService nastavnikService, MainView mainView, 
                                     CoderFactory coderFactory, NavigationHistoryService navigationHistoryService) {
        this.nastavnikService = nastavnikService;
        this.mainView = mainView;
        this.coderFactory = coderFactory;
        this.navigationHistoryService = navigationHistoryService;
    }

    @FXML
    public void initialize() {
        setupTabChangeTracking();
        loadZvanja();
        setupActionColumns();
    }
    
    private void setupTabChangeTracking() {
        if (mainTabPane != null) {
            // Register initial tab state (tab 0) so we can go back to it
            int initialTab = mainTabPane.getSelectionModel().getSelectedIndex();
            if (initialTab >= 0) {
                navigationHistoryService.registerTabChange(VIEW_NAME, initialTab);
            }
            
            // Track future tab changes
            mainTabPane.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldIndex, newIndex) -> {
                    if (!navigationHistoryService.isNavigating()) {
                        navigationHistoryService.registerTabChange(VIEW_NAME, newIndex.intValue());
                    }
                }
            );
        }
    }

    private void loadZvanja() {
        if (zvanjeCb != null) {
            List<String> zvanja = coderFactory.getSimpleCoder(CoderType.NASTAVNO_ZVANJE)
                    .getCodes()
                    .stream()
                    .map(SimpleCode::getCode)
                    .collect(Collectors.toList());
            zvanjeCb.setItems(FXCollections.observableArrayList(zvanja));
        }
    }

    private void setupActionColumns() {
        if (actionColumnName != null) {
            actionColumnName.setCellFactory(createButtonCellFactory());
        }
        
        if (actionColumnZvanje != null) {
            actionColumnZvanje.setCellFactory(createButtonCellFactory());
        }
        
        if (actionColumnEmail != null) {
            actionColumnEmail.setCellFactory(createButtonCellFactory());
        }
    }

    private Callback<TableColumn<NastavnikDTO, Void>, TableCell<NastavnikDTO, Void>> createButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<NastavnikDTO, Void> call(final TableColumn<NastavnikDTO, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Prikaži profil");
                    {
                        btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 11px;");
                        btn.setOnAction(event -> {
                            NastavnikDTO nastavnik = getTableView().getItems().get(getIndex());
                            openNastavnikProfile(nastavnik.getId());
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
    }

    private void openNastavnikProfile(Long nastavnikId) {
        if (nastavnikId == null) {
            showAlert("Greška", "Nije moguće otvoriti profil - nedostaje ID nastavnika.");
            return;
        }
        
        mainView.cacheCurrentRoot("searchNastavnik");
        NastavnikProfileController.setNastavnikId(nastavnikId);
        mainView.changeRoot("nastavnikProfile");
    }

    public void handleSearchByName(ActionEvent actionEvent) {
        try {
            String ime = imeNastavnikaTf.getText();
            String prezime = prezimeNastavnikaTf.getText();

            List<NastavnikDTO> results;
            if ((ime == null || ime.trim().isEmpty()) && (prezime == null || prezime.trim().isEmpty())) {
                results = nastavnikService.findAll();
            } else {
                results = nastavnikService.searchByNameAndSurname(ime, prezime);
            }

            if (results != null) {
                tabelaNastavniciPoImenu.setItems(FXCollections.observableArrayList(results));
            } else {
                showAlert("Greška", "Došlo je do greške pri pretrazi nastavnika.");
            }
        } catch (Exception e) {
            showAlert("Greška", "Došlo je do greške: " + e.getMessage());
        }
    }

    public void handleSearchByZvanje(ActionEvent actionEvent) {
        try {
            String zvanje = zvanjeCb.getValue();

            if (zvanje == null || zvanje.trim().isEmpty()) {
                showAlert("Upozorenje", "Molimo izaberite zvanje.");
                return;
            }

            List<NastavnikDTO> results = nastavnikService.searchByZvanje(zvanje);

            if (results != null) {
                tabelaNastavniciPoZvanju.setItems(FXCollections.observableArrayList(results));
            } else {
                showAlert("Greška", "Došlo je do greške pri pretrazi nastavnika.");
            }
        } catch (Exception e) {
            showAlert("Greška", "Došlo je do greške: " + e.getMessage());
        }
    }

    public void handleSearchByEmail(ActionEvent actionEvent) {
        try {
            String email = emailSearchTf.getText();

            if (email == null || email.trim().isEmpty()) {
                showAlert("Upozorenje", "Molimo unesite email za pretragu.");
                return;
            }

            if (!isValidEmail(email.trim())) {
                showAlert("Upozorenje", "Molimo unesite ispravan format email adrese (npr. ime@domen.rs).");
                return;
            }

            List<NastavnikDTO> results = nastavnikService.searchByEmail(email.trim());

            if (results != null) {
                tabelaNastavniciPoEmailu.setItems(FXCollections.observableArrayList(results));
            } else {
                showAlert("Greška", "Došlo je do greške pri pretrazi nastavnika.");
            }
        } catch (Exception e) {
            showAlert("Greška", "Došlo je do greške: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-zćčšžđĆČŠŽĐ0-9._%+-]+@[A-Za-zćčšžđĆČŠŽĐ0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
