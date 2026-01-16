package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzba.dtos.NastavnikProfileDTO;
import org.raflab.studsluzba.dtos.NastavnikZvanjeDTO;
import org.raflab.studsluzbadesktopclient.services.NavigationHistoryService;
import org.raflab.studsluzbadesktopclient.services.NastavnikService;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class NastavnikProfileController {

    private static final String VIEW_NAME = "nastavnikProfile";
    
    private final MainView mainView;
    private final NastavnikService nastavnikService;
    private final NavigationHistoryService navigationHistoryService;
    
    private static Long currentNastavnikId;
    private NastavnikProfileDTO currentProfile;

    @FXML private TabPane profileTabPane;
    @FXML private Label nastavnikNameLabel;
    @FXML private Label nastavnikZvanjeLabel;
    
    @FXML private Label imeLabel;
    @FXML private Label prezimeLabel;
    @FXML private Label srednjeImeLabel;
    @FXML private Label jmbgLabel;
    @FXML private Label datumRodjenjaLabel;
    @FXML private Label polLabel;
    
    @FXML private Label emailLabel;
    @FXML private Label telefonLabel;
    @FXML private Label adresaLabel;
    
    @FXML private Label trenutnoZvanjeLabel;
    
    @FXML private TableView<NastavnikZvanjeDTO> zvanjaTable;

    public NastavnikProfileController(MainView mainView, NastavnikService nastavnikService,
                                      NavigationHistoryService navigationHistoryService) {
        this.mainView = mainView;
        this.nastavnikService = nastavnikService;
        this.navigationHistoryService = navigationHistoryService;
    }

    public static void setNastavnikId(Long nastavnikId) {
        currentNastavnikId = nastavnikId;
    }

    @FXML
    public void initialize() {
        setupTabChangeTracking();
        
        if (currentNastavnikId != null) {
            loadNastavnikProfile();
        } else {
            showAlert("Greška", "Nije izabran nastavnik za prikaz profila.");
        }
    }
    
    private void setupTabChangeTracking() {
        if (profileTabPane != null) {
            // Register initial tab state (tab 0) so we can go back to it
            int initialTab = profileTabPane.getSelectionModel().getSelectedIndex();
            if (initialTab >= 0) {
                navigationHistoryService.registerTabChange(VIEW_NAME, initialTab);
            }
            
            // Track future tab changes
            profileTabPane.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldIndex, newIndex) -> {
                    if (!navigationHistoryService.isNavigating()) {
                        navigationHistoryService.registerTabChange(VIEW_NAME, newIndex.intValue());
                    }
                }
            );
        }
    }

    private void loadNastavnikProfile() {
        try {
            currentProfile = nastavnikService.getNastavnikProfile(currentNastavnikId);
            
            if (currentProfile != null) {
                populateBasicInfo();
                populateZvanja();
            } else {
                showAlert("Greška", "Nije moguće učitati profil nastavnika.");
            }
        } catch (Exception e) {
            System.err.println("Greška pri učitavanju profila: " + e.getMessage());
            e.printStackTrace();
            showAlert("Greška", "Došlo je do greške pri učitavanju profila: " + e.getMessage());
        }
    }

    private void populateBasicInfo() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        
        nastavnikNameLabel.setText(currentProfile.getFullName());
        nastavnikZvanjeLabel.setText("Zvanje: " + safeString(currentProfile.getTrenutnoZvanje()));
        
        imeLabel.setText(safeString(currentProfile.getIme()));
        prezimeLabel.setText(safeString(currentProfile.getPrezime()));
        srednjeImeLabel.setText(safeString(currentProfile.getSrednjeIme()));
        jmbgLabel.setText(safeString(currentProfile.getJmbg()));
        datumRodjenjaLabel.setText(currentProfile.getDatumRodjenja() != null ? 
                currentProfile.getDatumRodjenja().format(dateFormatter) : "-");
        polLabel.setText(currentProfile.getPol() != null ? 
                (currentProfile.getPol().equals("M") ? "Muški" : "Ženski") : "-");
        
        emailLabel.setText(safeString(currentProfile.getEmail()));
        telefonLabel.setText(safeString(currentProfile.getBrojTelefona()));
        adresaLabel.setText(safeString(currentProfile.getAdresa()));
        
        trenutnoZvanjeLabel.setText(safeString(currentProfile.getTrenutnoZvanje()));
    }

    private void populateZvanja() {
        if (currentProfile.getZvanja() != null && zvanjaTable != null) {
            zvanjaTable.setItems(FXCollections.observableArrayList(currentProfile.getZvanja()));
        }
    }

    @FXML
    public void handleGoBack(ActionEvent event) {
        currentNastavnikId = null;
        mainView.changeRoot("searchNastavnik", true);
    }

    private String safeString(String value) {
        return value != null && !value.isEmpty() ? value : "-";
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
