package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.raflab.studsluzba.dtos.PredmetRequest;
import org.raflab.studsluzba.dtos.StudProgramDTO;
import org.raflab.studsluzbadesktopclient.services.PredmetService;
import org.raflab.studsluzbadesktopclient.services.StudProgramService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddPredmetController {

    private final PredmetService predmetService;
    private final StudProgramService studProgramService;

    public AddPredmetController(PredmetService predmetService, StudProgramService studProgramService) {
        this.predmetService = predmetService;
        this.studProgramService = studProgramService;
    }

    @FXML
    private ComboBox<StudProgramDTO> studProgramCb;

    @FXML
    private TextField sifraTf;

    @FXML
    private TextField nazivTf;

    @FXML
    private TextField espbTf;

    @FXML
    private ComboBox<Integer> semestarCb;

    @FXML
    private TextField fondPredavanjaTf;

    @FXML
    private TextField fondVezbeTf;

    @FXML
    private CheckBox obavezanCheckBox;

    @FXML
    private TextArea opisTa;

    private Runnable onSaveCallback;

    @FXML
    public void initialize() {
        try {
            List<StudProgramDTO> programi = studProgramService.getSudijskiProgramiSorted();
            if (programi != null) {
                studProgramCb.setItems(FXCollections.observableArrayList(programi));
                studProgramCb.setCellFactory(param -> new ListCell<StudProgramDTO>() {
                    @Override
                    protected void updateItem(StudProgramDTO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getOznaka() != null ? 
                                    item.getOznaka() + " - " + item.getNaziv() : 
                                    item.getNaziv());
                        }
                    }
                });
                studProgramCb.setButtonCell(new ListCell<StudProgramDTO>() {
                    @Override
                    protected void updateItem(StudProgramDTO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getOznaka() != null ? 
                                    item.getOznaka() + " - " + item.getNaziv() : 
                                    item.getNaziv());
                        }
                    }
                });
            }

            semestarCb.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8));
        } catch (Exception e) {
            showError("Greška", "Greška pri inicijalizaciji: " + e.getMessage());
        }
    }

    @FXML
    public void handleSave(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        try {
            PredmetRequest request = new PredmetRequest();
            request.setSifra(sifraTf.getText().trim());
            request.setNaziv(nazivTf.getText().trim());
            request.setEspb(Integer.parseInt(espbTf.getText().trim()));
            request.setSemestar(semestarCb.getValue());
            
            if (!fondPredavanjaTf.getText().trim().isEmpty()) {
                request.setFondCasovaPredavanja(Integer.parseInt(fondPredavanjaTf.getText().trim()));
            }
            
            if (!fondVezbeTf.getText().trim().isEmpty()) {
                request.setFondCasovaVezbe(Integer.parseInt(fondVezbeTf.getText().trim()));
            }
            
            request.setObavezan(obavezanCheckBox.isSelected());
            request.setOpis(opisTa.getText().trim());
            request.setStudProgramId(studProgramCb.getValue().getId());

            Long predmetId = predmetService.addPredmet(request);

            if (predmetId != null) {
                showInfo("Uspešno", "Predmet je uspešno dodat sa ID: " + predmetId);
                
                if (onSaveCallback != null) {
                    onSaveCallback.run();
                }
                
                closeWindow();
            } else {
                showError("Greška", "Greška pri dodavanju predmeta");
            }
        } catch (NumberFormatException e) {
            showError("Greška", "Neispravan format broja. Proverite unete vrednosti.");
        } catch (Exception e) {
            showError("Greška", "Greška pri dodavanju predmeta: " + e.getMessage());
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private boolean validateInput() {
        if (studProgramCb.getValue() == null) {
            showWarning("Upozorenje", "Molimo izaberite studijski program");
            return false;
        }

        if (sifraTf.getText() == null || sifraTf.getText().trim().isEmpty()) {
            showWarning("Upozorenje", "Molimo unesite šifru predmeta");
            return false;
        }

        if (nazivTf.getText() == null || nazivTf.getText().trim().isEmpty()) {
            showWarning("Upozorenje", "Molimo unesite naziv predmeta");
            return false;
        }

        if (espbTf.getText() == null || espbTf.getText().trim().isEmpty()) {
            showWarning("Upozorenje", "Molimo unesite broj ESPB");
            return false;
        }

        try {
            Integer.parseInt(espbTf.getText().trim());
        } catch (NumberFormatException e) {
            showWarning("Upozorenje", "ESPB mora biti broj");
            return false;
        }

        if (semestarCb.getValue() == null) {
            showWarning("Upozorenje", "Molimo izaberite semestar");
            return false;
        }

        if (!fondPredavanjaTf.getText().trim().isEmpty()) {
            try {
                Integer.parseInt(fondPredavanjaTf.getText().trim());
            } catch (NumberFormatException e) {
                showWarning("Upozorenje", "Fond časova predavanja mora biti broj");
                return false;
            }
        }

        if (!fondVezbeTf.getText().trim().isEmpty()) {
            try {
                Integer.parseInt(fondVezbeTf.getText().trim());
            } catch (NumberFormatException e) {
                showWarning("Upozorenje", "Fond časova vežbi mora biti broj");
                return false;
            }
        }

        return true;
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    private void closeWindow() {
        Stage stage = (Stage) sifraTf.getScene().getWindow();
        stage.close();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
