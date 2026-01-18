package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.coder.CoderFactory;
import org.raflab.studsluzbadesktopclient.coder.SimpleCode;
import org.raflab.studsluzba.dtos.NastavnikRequest;
import org.raflab.studsluzbadesktopclient.services.NastavnikService;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class NastavnikController {

    private final CoderFactory coderFactory;
    private final NastavnikService nastavnikService;

    @FXML
    private TextField imeTf;
    @FXML
    private TextField prezimeTf;
    @FXML
    private TextField srednjeImeTf;
    @FXML
    private RadioButton muski;
    @FXML
    private RadioButton zenski;
    @FXML
    private TextField jmbgTf;
    @FXML
    private DatePicker datumRodjenjaDp;
    @FXML
    private TextField nastavnikIdKontaktTf;
    @FXML
    private TextField emailTf;
    @FXML
    private TextField brojTelefonaTf;
    @FXML
    private TextField adresaTf;
    @FXML
    private TextField nastavnikIdZvanjaTf;
    @FXML
    private ComboBox<String> nazivZvanjaCb;
    @FXML
    private DatePicker datumIzboraDp;
    @FXML
    private TextField uzaNaucnaOblastTf;
    @FXML
    private TextField nastavnikIdObrazovanjeTf;
    @FXML
    private ComboBox<SimpleCode> visokoskolskaUstanovaCb;
    @FXML
    private ComboBox<String> nivoStudijaCb;
    @FXML
    private TextField godinaZavrsetkaTf;
    @FXML
    private TabPane nastavnikTabPane;
    @FXML
    private Button saveOsnovniPodaciBtn;
    @FXML
    private Button saveKontaktPodaciBtn;
    @FXML
    private Button dodajZvanjeBtn;
    @FXML
    private Button dodajObrazovanjeBtn;


    public NastavnikController(CoderFactory coderFactory, NastavnikService nastavnikService) {
        this.coderFactory = coderFactory;
        this.nastavnikService = nastavnikService;
    }

    @FXML
    public void initialize() {
        if (nazivZvanjaCb != null) {
            nazivZvanjaCb.setItems(FXCollections.observableArrayList(
                "Asistent", "Docent", "Vanredni profesor", "Redovni profesor"
            ));
        }

        if (nivoStudijaCb != null) {
            nivoStudijaCb.setItems(FXCollections.observableArrayList(
                "Osnovne studije", "Master studije", "Doktorske studije"
            ));
        }

        if (nastavnikTabPane != null) {
            nastavnikTabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
                updateButtonVisibility(newVal.intValue());
            });
            updateButtonVisibility(0);
        }

    }

    private void updateButtonVisibility(int tabIndex) {
        if (saveOsnovniPodaciBtn == null || saveKontaktPodaciBtn == null || dodajZvanjeBtn == null || dodajObrazovanjeBtn == null) {
            return;
        }

        saveOsnovniPodaciBtn.setVisible(false);
        saveOsnovniPodaciBtn.setManaged(false);
        saveKontaktPodaciBtn.setVisible(false);
        saveKontaktPodaciBtn.setManaged(false);
        dodajZvanjeBtn.setVisible(false);
        dodajZvanjeBtn.setManaged(false);
        dodajObrazovanjeBtn.setVisible(false);
        dodajObrazovanjeBtn.setManaged(false);

        switch (tabIndex) {
            case 0:
                saveOsnovniPodaciBtn.setVisible(true);
                saveOsnovniPodaciBtn.setManaged(true);
                break;
            case 1:
                saveKontaktPodaciBtn.setVisible(true);
                saveKontaktPodaciBtn.setManaged(true);
                break;
            case 2:
                dodajZvanjeBtn.setVisible(true);
                dodajZvanjeBtn.setManaged(true);
                break;
            case 3:
                dodajObrazovanjeBtn.setVisible(true);
                dodajObrazovanjeBtn.setManaged(true);
                break;
        }
    }

    public void handleSaveOsnovniPodaci(ActionEvent event) {
        if (imeTf.getText() == null || imeTf.getText().trim().isEmpty()) {
            showAlert("Validacija", "Ime je obavezno polje.");
            return;
        }
        if (prezimeTf.getText() == null || prezimeTf.getText().trim().isEmpty()) {
            showAlert("Validacija", "Prezime je obavezno polje.");
            return;
        }
        if (srednjeImeTf.getText() == null || srednjeImeTf.getText().trim().isEmpty()) {
            showAlert("Validacija", "Srednje ime je obavezno polje.");
            return;
        }

        NastavnikRequest request = new NastavnikRequest();
        request.setIme(imeTf.getText().trim());
        request.setPrezime(prezimeTf.getText().trim());
        request.setSrednjeIme(srednjeImeTf.getText().trim());
        
        if (jmbgTf.getText() != null && !jmbgTf.getText().trim().isEmpty()) {
            request.setJmbg(jmbgTf.getText().trim());
        }
        if (datumRodjenjaDp.getValue() != null) {
            request.setDatumRodjenja(datumRodjenjaDp.getValue());
        }
        if (muski.isSelected()) {
            request.setPol('M');
        } else if (zenski.isSelected()) {
            request.setPol('Z');
        }

        try {
            Long nastavnikId = nastavnikService.addNastavnik(request);
            if (nastavnikId != null) {
                updateIdFieldsOnAllTabs(nastavnikId);
                showAlert("Uspeh", "Nastavnik je uspešno kreiran sa ID: " + nastavnikId + 
                        "\nID je automatski unet u ostale tabove.");
                resetOsnovniPodaciForm();
            } else {
                showAlert("Greška", "Došlo je do greške pri dodavanju nastavnika.");
            }
        } catch (Exception e) {
            showAlert("Greška", "Došlo je do greške: " + e.getMessage());
        }
    }

    private void updateIdFieldsOnAllTabs(Long nastavnikId) {
        String idStr = nastavnikId != null ? nastavnikId.toString() : "";
        if (nastavnikIdKontaktTf != null) {
            nastavnikIdKontaktTf.setText(idStr);
        }
        if (nastavnikIdZvanjaTf != null) {
            nastavnikIdZvanjaTf.setText(idStr);
        }
        if (nastavnikIdObrazovanjeTf != null) {
            nastavnikIdObrazovanjeTf.setText(idStr);
        }
    }

    private void resetOsnovniPodaciForm() {
        if (imeTf != null) imeTf.clear();
        if (prezimeTf != null) prezimeTf.clear();
        if (srednjeImeTf != null) srednjeImeTf.clear();
        if (jmbgTf != null) jmbgTf.clear();
        if (datumRodjenjaDp != null) datumRodjenjaDp.setValue(null);
        if (muski != null) muski.setSelected(false);
        if (zenski != null) zenski.setSelected(false);
    }

    public void handleSaveKontaktPodaci(ActionEvent event) {
        if (nastavnikIdKontaktTf.getText() == null || nastavnikIdKontaktTf.getText().trim().isEmpty()) {
            showAlert("Validacija", "ID nastavnika je obavezno polje.");
            return;
        }

        Long nastavnikId;
        try {
            nastavnikId = Long.parseLong(nastavnikIdKontaktTf.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Validacija", "ID nastavnika mora biti broj.");
            return;
        }

        NastavnikRequest request = new NastavnikRequest();
        if (emailTf.getText() != null && !emailTf.getText().trim().isEmpty()) {
            request.setEmail(emailTf.getText().trim());
        }
        if (brojTelefonaTf.getText() != null && !brojTelefonaTf.getText().trim().isEmpty()) {
            request.setBrojTelefona(brojTelefonaTf.getText().trim());
        }
        if (adresaTf.getText() != null && !adresaTf.getText().trim().isEmpty()) {
            request.setAdresa(adresaTf.getText().trim());
        }

        try {
            boolean success = nastavnikService.updateNastavnik(nastavnikId, request);
            if (success) {
                showAlert("Uspeh", "Kontakt podaci nastavnika sa ID: " + nastavnikId + " su uspešno ažurirani!");
                resetKontaktPodaciForm();
            } else {
                showAlert("Greška", "Došlo je do greške pri ažuriranju. Proverite da li nastavnik sa ID " + nastavnikId + " postoji.");
            }
        } catch (Exception e) {
            showAlert("Greška", "Došlo je do greške: " + e.getMessage());
        }
    }

    private void resetKontaktPodaciForm() {
        if (nastavnikIdKontaktTf != null) nastavnikIdKontaktTf.clear();
        if (emailTf != null) emailTf.clear();
        if (brojTelefonaTf != null) brojTelefonaTf.clear();
        if (adresaTf != null) adresaTf.clear();
    }

    public void handleDodajZvanje(ActionEvent event) {
        if (nastavnikIdZvanjaTf.getText() == null || nastavnikIdZvanjaTf.getText().trim().isEmpty()) {
            showAlert("Validacija", "ID nastavnika je obavezno polje.");
            return;
        }

        Long nastavnikId;
        try {
            nastavnikId = Long.parseLong(nastavnikIdZvanjaTf.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Validacija", "ID nastavnika mora biti broj.");
            return;
        }

        if (nazivZvanjaCb.getValue() == null) {
            showAlert("Validacija", "Naziv zvanja je obavezno polje.");
            return;
        }
        if (datumIzboraDp.getValue() == null) {
            showAlert("Validacija", "Datum izbora je obavezno polje.");
            return;
        }

        showAlert("Info", "Funkcionalnost dodavanja zvanja će biti implementirana.");
    }

    public void handleDodajObrazovanje(ActionEvent event) {
        if (nastavnikIdObrazovanjeTf.getText() == null || nastavnikIdObrazovanjeTf.getText().trim().isEmpty()) {
            showAlert("Validacija", "ID nastavnika je obavezno polje.");
            return;
        }

        Long nastavnikId;
        try {
            nastavnikId = Long.parseLong(nastavnikIdObrazovanjeTf.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Validacija", "ID nastavnika mora biti broj.");
            return;
        }

        if (visokoskolskaUstanovaCb.getValue() == null) {
            showAlert("Validacija", "Visokoškolska ustanova je obavezno polje.");
            return;
        }
        if (nivoStudijaCb.getValue() == null) {
            showAlert("Validacija", "Nivo studija je obavezno polje.");
            return;
        }

        showAlert("Info", "Funkcionalnost dodavanja obrazovanja će biti implementirana.");
    }

    public void handleOpenModalVisokoskolskeUstanove(ActionEvent event) {
    }

    public void handleIzvestaj(ActionEvent event) {
        nastavnikService.getNastavniciForReportAsync()
            .subscribe(
                nastavnici -> {
                    try {
                        JasperReport report = JasperCompileManager.compileReport(
                                MainView.class.getResourceAsStream("/reports/sviNastavnici.jrxml")
                        );

                        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(nastavnici);
                        JasperPrint jp = JasperFillManager.fillReport(report, new HashMap<>(), dataSource);

                        JasperExportManager.exportReportToPdfFile(jp, "sviNastavnici.pdf");

                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Izveštaj");
                            alert.setHeaderText(null);
                            alert.setContentText("Izveštaj je uspešno generisan: sviNastavnici.pdf");
                            alert.showAndWait();
                        });

                    } catch (JRException e) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Greška");
                            alert.setHeaderText(null);
                            alert.setContentText("Greška pri generisanju izveštaja: " + e.getMessage());
                            alert.showAndWait();
                        });
                    }
                },
                error -> Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Greška");
                    alert.setHeaderText(null);
                    alert.setContentText("Greška pri dohvatanju podataka: " + error.getMessage());
                    alert.showAndWait();
                })
            );
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
