package org.raflab.studsluzbadesktopclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.raflab.studsluzba.dtos.SrednjaSkolaDTO;
import org.raflab.studsluzbadesktopclient.services.SrednjaSkolaService;
import org.springframework.stereotype.Component;

@Component
public class AddSrednjaSkolaModalController {

    private final SrednjaSkolaService srednjaSkolaService;
    private SrednjaSkolaDTO createdSkola;

    @FXML
    private TextField nazivTf;
    @FXML
    private TextField vrstaTf;
    @FXML
    private TextField mestoTf;
    @FXML
    private TextField napomenaTf;

    public AddSrednjaSkolaModalController(SrednjaSkolaService srednjaSkolaService) {
        this.srednjaSkolaService = srednjaSkolaService;
    }

    @FXML
    public void handleDodaj() {
        String naziv = nazivTf.getText();
        String vrsta = vrstaTf.getText();
        String mesto = mestoTf.getText();
        String napomena = napomenaTf.getText();

        if (naziv == null || naziv.trim().isEmpty()) {
            showAlert("Validacija", "Naziv škole je obavezan");
            return;
        }

        if (vrsta == null || vrsta.trim().isEmpty()) {
            showAlert("Validacija", "Vrsta škole je obavezna");
            return;
        }

        if (mesto == null || mesto.trim().isEmpty()) {
            showAlert("Validacija", "Mesto škole je obavezno");
            return;
        }

        try {
            // Kreiramo request objekat sa potrebnim poljima
            java.util.Map<String, String> requestBody = new java.util.HashMap<>();
            requestBody.put("naziv", naziv.trim());
            requestBody.put("vrsta", vrsta.trim());
            requestBody.put("mesto", mesto.trim());
            if (napomena != null && !napomena.trim().isEmpty()) {
                requestBody.put("napomena", napomena.trim());
            }

            createdSkola = srednjaSkolaService.createSrednjaSkola(requestBody);
            
            if (createdSkola != null) {
                showInfo("Uspeh", "Srednja škola je uspešno dodata");
                closeModal();
            } else {
                showAlert("Greška", "Nije moguće dodati srednju školu");
            }
        } catch (Exception e) {
            showAlert("Greška", "Greška pri dodavanju srednje škole: " + e.getMessage());
        }
    }

    @FXML
    public void handleOtkazi() {
        closeModal();
    }

    private void closeModal() {
        Stage stage = (Stage) nazivTf.getScene().getWindow();
        stage.close();
    }

    public SrednjaSkolaDTO getCreatedSkola() {
        return createdSkola;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
