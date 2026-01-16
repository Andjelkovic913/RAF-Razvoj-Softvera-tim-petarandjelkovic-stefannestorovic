package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzba.dtos.AverageGradeDTO;
import org.raflab.studsluzba.dtos.PredmetDTO;
import org.raflab.studsluzbadesktopclient.services.PredmetService;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ProsecnaOcenaController {

    private final PredmetService predmetService;
    private final MainView mainView;

    public ProsecnaOcenaController(PredmetService predmetService, MainView mainView) {
        this.predmetService = predmetService;
        this.mainView = mainView;
    }

    @FXML
    private ComboBox<PredmetDTO> predmetCb;

    @FXML
    private ComboBox<Integer> godinaOdCb;

    @FXML
    private ComboBox<Integer> godinaDoCb;

    @FXML
    private VBox resultContainer;

    @FXML
    private Label resultPredmetLabel;

    @FXML
    private Label resultPeriodLabel;

    @FXML
    private Label resultAverageLabel;

    @FXML
    private Label resultCountLabel;

    @FXML
    public void initialize() {
        int currentYear = Year.now().getValue();
        List<Integer> years = IntStream.rangeClosed(2010, currentYear)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        godinaOdCb.setItems(FXCollections.observableArrayList(years));
        godinaDoCb.setItems(FXCollections.observableArrayList(years));

        CompletableFuture.supplyAsync(() -> predmetService.getAllPredmeti())
            .thenAccept(predmeti -> Platform.runLater(() -> {
                if (predmeti != null && !predmeti.isEmpty()) {
                    predmetCb.setItems(FXCollections.observableArrayList(predmeti));
                    predmetCb.setCellFactory(param -> new ListCell<PredmetDTO>() {
                        @Override
                        protected void updateItem(PredmetDTO item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setText(null);
                            } else {
                                setText(item.getSifra() + " - " + item.getNaziv());
                            }
                        }
                    });
                    predmetCb.setButtonCell(new ListCell<PredmetDTO>() {
                        @Override
                        protected void updateItem(PredmetDTO item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setText(null);
                            } else {
                                setText(item.getSifra() + " - " + item.getNaziv());
                            }
                        }
                    });
                }
            }));
    }

    @FXML
    public void handleGoBack(ActionEvent event) {
        mainView.changeRoot("main");
    }

    @FXML
    public void handleCalculateAverageGrade(ActionEvent event) {
        PredmetDTO selectedPredmet = predmetCb.getValue();
        Integer startYear = godinaOdCb.getValue();
        Integer endYear = godinaDoCb.getValue();

        if (selectedPredmet == null) {
            showWarning("Upozorenje", "Molimo izaberite predmet");
            return;
        }
        if (startYear == null) {
            showWarning("Upozorenje", "Molimo izaberite početnu godinu");
            return;
        }
        if (endYear == null) {
            showWarning("Upozorenje", "Molimo izaberite krajnju godinu");
            return;
        }
        if (startYear > endYear) {
            showWarning("Upozorenje", "Početna godina mora biti manja ili jednaka krajnjoj godini");
            return;
        }

        CompletableFuture.supplyAsync(() ->
            predmetService.getAverageGradeByYearRange(selectedPredmet.getId(), startYear, endYear)
        ).thenAccept(result -> Platform.runLater(() -> {
            if (result != null) {
                displayResult(result);
            } else {
                showError("Greška", "Nije moguće izračunati prosečnu ocenu");
            }
        })).exceptionally(ex -> {
            Platform.runLater(() ->
                showError("Greška", "Greška pri izračunavanju prosečne ocene: " + ex.getMessage())
            );
            return null;
        });
    }

    private void displayResult(AverageGradeDTO result) {
        resultContainer.setVisible(true);
        resultContainer.setManaged(true);

        resultPredmetLabel.setText(result.getPredmetSifra() + " - " + result.getPredmetNaziv());
        resultPeriodLabel.setText(result.getStartYear() + " - " + result.getEndYear());

        if (result.getAverageGrade() != null) {
            resultAverageLabel.setText(String.format("%.2f", result.getAverageGrade()));
        } else {
            resultAverageLabel.setText("N/A");
        }

        resultCountLabel.setText(result.getTotalPassedExams() != null ?
                result.getTotalPassedExams().toString() : "0");
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
