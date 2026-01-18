package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzba.dtos.AverageGradeDTO;
import org.raflab.studsluzba.dtos.PredmetDTO;
import org.raflab.studsluzba.dtos.YearlyGradeStatsDTO;
import org.raflab.studsluzba.dtos.PredmetGradeStatsDTO;
import org.raflab.studsluzbadesktopclient.services.PredmetService;
import org.raflab.studsluzbadesktopclient.services.ReportService;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ProsecnaOcenaController {

    private final PredmetService predmetService;
    private final MainView mainView;
    private final ReportService reportService;

    private List<YearlyGradeStatsDTO> currentYearlyStats;
    private AverageGradeDTO currentResult;
    private List<PredmetGradeStatsDTO> currentAllPredmetiStats;
    private boolean isAllPredmetiMode = false;

    public ProsecnaOcenaController(PredmetService predmetService, MainView mainView, ReportService reportService) {
        this.predmetService = predmetService;
        this.mainView = mainView;
        this.reportService = reportService;
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
    private TableView<YearlyGradeStatsDTO> statsTable;

    @FXML
    private TableColumn<YearlyGradeStatsDTO, Integer> godinaColumn;

    @FXML
    private TableColumn<YearlyGradeStatsDTO, Integer> brStudenataColumn;

    @FXML
    private TableColumn<YearlyGradeStatsDTO, Integer> poloziloColumn;

    @FXML
    private TableColumn<YearlyGradeStatsDTO, String> prosecnaOcenaColumn;

    @FXML
    private TableColumn<YearlyGradeStatsDTO, String> procenatColumn;

    @FXML
    public void initialize() {
        int currentYear = Year.now().getValue();
        List<Integer> years = IntStream.rangeClosed(2010, currentYear)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        godinaOdCb.setItems(FXCollections.observableArrayList(years));
        godinaDoCb.setItems(FXCollections.observableArrayList(years));

        // podesavanje kolona tabele
        godinaColumn.setCellValueFactory(data -> {
            Integer godina = data.getValue().getGodina();
            return new SimpleIntegerProperty(godina != null ? godina : 0).asObject();
        });
        brStudenataColumn.setCellValueFactory(data -> {
            Integer brStudenata = data.getValue().getBrStudenata();
            return new SimpleIntegerProperty(brStudenata != null ? brStudenata : 0).asObject();
        });
        poloziloColumn.setCellValueFactory(data -> {
            Integer polozilo = data.getValue().getPolozilo();
            return new SimpleIntegerProperty(polozilo != null ? polozilo : 0).asObject();
        });
        prosecnaOcenaColumn.setCellValueFactory(data -> {
            Double avg = data.getValue().getProsecnaOcena();
            return new SimpleStringProperty(avg != null ? String.format("%.2f", avg) : "-");
        });
        procenatColumn.setCellValueFactory(data -> {
            Double pct = data.getValue().getProcenatProlaz();
            return new SimpleStringProperty(pct != null ? String.format("%.1f%%", pct) : "-");
        });

        predmetService.getAllPredmetiAsync()
            .subscribe(
                predmeti -> Platform.runLater(() -> {
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
                }),
                error -> Platform.runLater(() ->
                    showWarning("Upozorenje", "Greška pri učitavanju predmeta: " + error.getMessage())
                )
            );
    }

    @FXML
    public void handleGoBack(ActionEvent event) {
        mainView.changeRoot("main");
    }

    @FXML
    public void handleClearSubject(ActionEvent event) {
        predmetCb.setValue(null);
    }

    @FXML
    public void handleCalculateAverageGrade(ActionEvent event) {
        PredmetDTO selectedPredmet = predmetCb.getValue();
        Integer startYear = godinaOdCb.getValue();
        Integer endYear = godinaDoCb.getValue();

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

        if (selectedPredmet == null) {
            // svi predmeti
            isAllPredmetiMode = true;
            predmetService.getAllPredmetiGradeStatsAsync(startYear, endYear)
                .subscribe(
                    allStats -> Platform.runLater(() -> {
                        if (allStats != null && !allStats.isEmpty()) {
                            currentAllPredmetiStats = allStats;
                            currentResult = null;
                            currentYearlyStats = null;
                            displayAllPredmetiResult(allStats, startYear, endYear);
                        } else {
                            showWarning("Upozorenje", "Nema podataka za izabrani period");
                        }
                    }),
                    error -> Platform.runLater(() ->
                        showError("Greška", "Greška pri učitavanju statistike: " + error.getMessage())
                    )
                );
        } else {
            // jedan predmet - paralelno oba poziva
            isAllPredmetiMode = false;
            final Long predmetId = selectedPredmet.getId();
            
            reactor.core.publisher.Mono.zip(
                predmetService.getAverageGradeByYearRangeAsync(predmetId, startYear, endYear),
                predmetService.getYearlyGradeStatsAsync(predmetId, startYear, endYear)
            )
            .subscribe(
                tuple2 -> Platform.runLater(() -> {
                    AverageGradeDTO result = tuple2.getT1();
                    List<YearlyGradeStatsDTO> yearlyStats = tuple2.getT2();
                    
                    if (result != null) {
                        currentResult = result;
                        currentYearlyStats = yearlyStats;
                        currentAllPredmetiStats = null;
                        displayResult(result, yearlyStats);
                    } else {
                        showError("Greška", "Nije moguće izračunati prosečnu ocenu");
                    }
                }),
                error -> Platform.runLater(() ->
                    showError("Greška", "Greška pri izračunavanju prosečne ocene: " + error.getMessage())
                )
            );
        }
    }

    private void displayResult(AverageGradeDTO result, List<YearlyGradeStatsDTO> yearlyStats) {
        resultContainer.setVisible(true);
        resultContainer.setManaged(true);

        configureTableForSingleSubject();

        resultPredmetLabel.setText(result.getPredmetSifra() + " - " + result.getPredmetNaziv());
        resultPeriodLabel.setText(result.getStartYear() + " - " + result.getEndYear());

        if (result.getAverageGrade() != null) {
            resultAverageLabel.setText(String.format("%.2f", result.getAverageGrade()));
        } else {
            resultAverageLabel.setText("N/A");
        }

        resultCountLabel.setText(result.getTotalPassedExams() != null ?
                result.getTotalPassedExams().toString() : "0");

        if (yearlyStats != null && !yearlyStats.isEmpty()) {
            statsTable.setItems(FXCollections.observableArrayList(yearlyStats));
        } else {
            statsTable.getItems().clear();
        }
    }

    private void displayAllPredmetiResult(List<PredmetGradeStatsDTO> allStats, Integer startYear, Integer endYear) {
        resultContainer.setVisible(true);
        resultContainer.setManaged(true);

        resultPredmetLabel.setText("Svi predmeti (" + allStats.size() + ")");
        resultPeriodLabel.setText(startYear + " - " + endYear);

        int totalPolozilo = allStats.stream().mapToInt(PredmetGradeStatsDTO::getPolozilo).sum();
        double avgGrade = allStats.stream()
                .filter(s -> s.getProsecnaOcena() != null && s.getPolozilo() > 0)
                .mapToDouble(s -> s.getProsecnaOcena() * s.getPolozilo())
                .sum() / totalPolozilo;

        resultAverageLabel.setText(String.format("%.2f", avgGrade));
        resultCountLabel.setText(String.valueOf(totalPolozilo));

        configureTableForAllSubjects();
        
        // konvertuj u DTO za tabelu
        List<YearlyGradeStatsDTO> tableData = allStats.stream()
                .map(s -> {
                    YearlyGradeStatsDTO dto = new YearlyGradeStatsDTO();
                    dto.setGodina(null);
                    dto.setBrStudenata(s.getBrStudenata());
                    dto.setPolozilo(s.getPolozilo());
                    dto.setProsecnaOcena(s.getProsecnaOcena());
                    dto.setProcenatProlaz(s.getProcenatProlaz());
                    return dto;
                })
                .collect(Collectors.toList());
        
        currentAllPredmetiStats = allStats;
        statsTable.setItems(FXCollections.observableArrayList(tableData));
    }
    
    private void configureTableForAllSubjects() {
        godinaColumn.setText("Predmet");
        godinaColumn.setCellValueFactory(data -> new SimpleIntegerProperty(0).asObject());
        godinaColumn.setCellFactory(col -> new TableCell<YearlyGradeStatsDTO, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || currentAllPredmetiStats == null || getIndex() < 0 || getIndex() >= currentAllPredmetiStats.size()) {
                    setText(null);
                } else {
                    PredmetGradeStatsDTO predmet = currentAllPredmetiStats.get(getIndex());
                    setText(predmet.getPredmetSifra() + " - " + predmet.getPredmetNaziv());
                }
            }
        });
        
        brStudenataColumn.setText("Br. studenata");
        poloziloColumn.setText("Položilo");
        prosecnaOcenaColumn.setText("Prosečna ocena");
        procenatColumn.setText("Prolaz (%)");
    }
    
    private void configureTableForSingleSubject() {
        godinaColumn.setText("Godina");
        godinaColumn.setCellValueFactory(data -> {
            Integer godina = data.getValue().getGodina();
            return new SimpleIntegerProperty(godina != null ? godina : 0).asObject();
        });
        godinaColumn.setCellFactory(col -> new TableCell<YearlyGradeStatsDTO, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item == 0) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });
    }

    @FXML
    public void handlePrintReport(ActionEvent event) {
        if (isAllPredmetiMode) {
            if (currentAllPredmetiStats == null || currentAllPredmetiStats.isEmpty()) {
                showWarning("Upozorenje", "Nema podataka za štampu. Prvo izračunajte prosečnu ocenu.");
                return;
            }
            
            try {
                Integer startYear = godinaOdCb.getValue();
                Integer endYear = godinaDoCb.getValue();
                
                Map<String, Object> params = new HashMap<>();
                params.put("predmetNaziv", "Svi predmeti");
                params.put("odGodine", startYear);
                params.put("doGodine", endYear);
                
                int ukupnoPolozilo = currentAllPredmetiStats.stream().mapToInt(PredmetGradeStatsDTO::getPolozilo).sum();
                double avgGrade = currentAllPredmetiStats.stream()
                        .filter(s -> s.getProsecnaOcena() != null)
                        .mapToDouble(s -> s.getProsecnaOcena() * s.getPolozilo())
                        .sum() / ukupnoPolozilo;
                
                params.put("ukupnoBrStudenata", ukupnoPolozilo);
                params.put("ukupnoPolozilo", ukupnoPolozilo);
                params.put("ukupnoProsecnaOcena", avgGrade);
                params.put("ukupnoProcenatProlaz", 100.0);

                List<YearlyGradeStatsDTO> reportData = currentAllPredmetiStats.stream()
                        .map(s -> new YearlyGradeStatsDTO(
                                null,
                                s.getBrStudenata(),
                                s.getPolozilo(),
                                s.getProsecnaOcena(),
                                s.getProcenatProlaz()
                        ))
                        .collect(java.util.stream.Collectors.toList());

                String fileName = "prosecnaOcena_" + System.currentTimeMillis() + ".pdf";
                reportService.exportToPdf("prosecnaOcenaNaPredmetu", params, reportData, fileName);
                showSuccess("Uspeh", "Izveštaj je uspešno generisan i sačuvan kao: " + fileName);
            } catch (ReportService.ReportException e) {
                showError("Greška", "Greška pri generisanju izveštaja: " + e.getMessage());
            }
        } else {
            if (currentResult == null || currentYearlyStats == null) {
                showWarning("Upozorenje", "Nema podataka za štampu. Prvo izračunajte prosečnu ocenu.");
                return;
            }

            try {
                Map<String, Object> params = new HashMap<>();
                params.put("predmetNaziv", currentResult.getPredmetSifra() + " - " + currentResult.getPredmetNaziv());
                params.put("odGodine", currentResult.getStartYear());
                params.put("doGodine", currentResult.getEndYear());
                
                int ukupnoBrStudenata = currentYearlyStats.stream().mapToInt(YearlyGradeStatsDTO::getBrStudenata).sum();
                int ukupnoPolozilo = currentYearlyStats.stream().mapToInt(YearlyGradeStatsDTO::getPolozilo).sum();
                
                params.put("ukupnoBrStudenata", ukupnoBrStudenata);
                params.put("ukupnoPolozilo", ukupnoPolozilo);
                params.put("ukupnoProsecnaOcena", currentResult.getAverageGrade());
                params.put("ukupnoProcenatProlaz", ukupnoBrStudenata > 0 ? (ukupnoPolozilo * 100.0 / ukupnoBrStudenata) : null);

                String fileName = "prosecnaOcena_" + currentResult.getPredmetSifra() + "_" + System.currentTimeMillis() + ".pdf";
                reportService.exportToPdf("prosecnaOcenaNaPredmetu", params, currentYearlyStats, fileName);
                showSuccess("Uspeh", "Izveštaj je uspešno generisan i sačuvan kao: " + fileName);
            } catch (ReportService.ReportException e) {
                showError("Greška", "Greška pri generisanju izveštaja: " + e.getMessage());
            }
        }
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

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
