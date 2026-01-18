package org.raflab.studsluzbadesktopclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.raflab.studsluzba.dtos.VisokoskolskaUstanovaDTO;
import org.raflab.studsluzbadesktopclient.services.VisokoskolskaUstanovaService;
import org.springframework.stereotype.Component;

@Component
public class AddVisokoskolskaUstanovaModalController {

    private final VisokoskolskaUstanovaService visokoskolskaUstanovaService;
    private VisokoskolskaUstanovaDTO createdUstanova;

    @FXML
    private TextField nazivTf;
    @FXML
    private TextField tipSkoleTf;

    public AddVisokoskolskaUstanovaModalController(VisokoskolskaUstanovaService visokoskolskaUstanovaService) {
        this.visokoskolskaUstanovaService = visokoskolskaUstanovaService;
    }

    @FXML
    public void handleDodaj() {
        String naziv = nazivTf.getText();
        String tipSkole = tipSkoleTf.getText();

        if (naziv == null || naziv.trim().isEmpty()) {
            showAlert("Validacija", "Naziv ustanove je obavezan");
            return;
        }

        if (tipSkole == null || tipSkole.trim().isEmpty()) {
            showAlert("Validacija", "Tip škole je obavezan");
            return;
        }

        try {
            // Kreiramo request objekat sa potrebnim poljima
            java.util.Map<String, String> requestBody = new java.util.HashMap<>();
            requestBody.put("naziv", naziv.trim());
            requestBody.put("tipSkole", tipSkole.trim());

            createdUstanova = visokoskolskaUstanovaService.createVisokoskolskaUstanova(requestBody);
            
            if (createdUstanova != null) {
                showInfo("Uspeh", "Visokoškolska ustanova je uspešno dodata");
                closeModal();
            } else {
                showAlert("Greška", "Nije moguće dodati visokoškolsku ustanovu");
            }
        } catch (Exception e) {
            showAlert("Greška", "Greška pri dodavanju visokoškolske ustanove: " + e.getMessage());
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

    public VisokoskolskaUstanovaDTO getCreatedUstanova() {
        return createdUstanova;
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
