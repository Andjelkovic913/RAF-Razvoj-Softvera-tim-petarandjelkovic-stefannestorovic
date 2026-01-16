package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzba.dtos.PredmetDTO;
import org.raflab.studsluzba.dtos.StudProgramDTO;
import org.raflab.studsluzbadesktopclient.services.PredmetService;
import org.raflab.studsluzbadesktopclient.services.StudProgramService;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class PredmetViewController {

    private final PredmetService predmetService;
    private final StudProgramService studProgramService;
    private final MainView mainView;

    public PredmetViewController(PredmetService predmetService, StudProgramService studProgramService, MainView mainView) {
        this.predmetService = predmetService;
        this.studProgramService = studProgramService;
        this.mainView = mainView;
    }

    @FXML
    private ComboBox<StudProgramDTO> studProgramCb;

    @FXML
    private CheckBox obavezniCheckBox;

    @FXML
    private VBox predmetiContainer;

    @FXML
    private Button editBtn;

    @FXML
    private Button deleteBtn;

    private PredmetDTO selectedPredmet;

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
        } catch (Exception e) {
            System.err.println("Greška pri učitavanju studijskih programa: " + e.getMessage());
            showError("Greška", "Nije moguće učitati studijske programe. Proverite da li je server pokrenut.");
        }
    }

    @FXML
    public void handleLoadPredmeti(ActionEvent event) {
        StudProgramDTO selectedProgram = studProgramCb.getValue();
        
        if (selectedProgram == null) {
            showWarning("Upozorenje", "Molimo izaberite studijski program");
            return;
        }

        Boolean obavezniFilter = obavezniCheckBox.isSelected() ? true : null;

        CompletableFuture.supplyAsync(() -> 
            predmetService.getPredmetiByProgram(selectedProgram.getId(), obavezniFilter)
        ).thenAccept(predmeti -> 
            Platform.runLater(() -> displayPredmetiByYear(predmeti))
        ).exceptionally(ex -> {
            Platform.runLater(() -> 
                showError("Greška", "Greška pri učitavanju predmeta: " + ex.getMessage())
            );
            return null;
        });
    }

    private void displayPredmetiByYear(List<PredmetDTO> predmeti) {
        predmetiContainer.getChildren().clear();

        if (predmeti == null || predmeti.isEmpty()) {
            Label emptyLabel = new Label("Nema predmeta za izabrani studijski program");
            emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
            predmetiContainer.getChildren().add(emptyLabel);
            return;
        }

        Map<Integer, List<PredmetDTO>> predmetiPoGodinama = predmeti.stream()
                .collect(Collectors.groupingBy(p -> (p.getSemestar() - 1) / 2 + 1));

        predmetiPoGodinama.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    Integer godina = entry.getKey();
                    List<PredmetDTO> predmetiGodine = entry.getValue();

                    TitledPane godinaPane = createGodinaPane(godina, predmetiGodine);
                    predmetiContainer.getChildren().add(godinaPane);
                });
    }

    private TitledPane createGodinaPane(Integer godina, List<PredmetDTO> predmeti) {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        Map<Integer, List<PredmetDTO>> predmetiPoSemestrima = predmeti.stream()
                .collect(Collectors.groupingBy(PredmetDTO::getSemestar));

        predmetiPoSemestrima.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    Integer semestar = entry.getKey();
                    List<PredmetDTO> predmetiSemestra = entry.getValue();

                    Label semestarLabel = new Label(getRomanNumeral(semestar) + " semestar");
                    semestarLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

                    TableView<PredmetDTO> table = createPredmetTable(predmetiSemestra);

                    content.getChildren().addAll(semestarLabel, table);
                });

        TitledPane pane = new TitledPane();
        pane.setText(getRomanNumeral(godina) + " godina");
        pane.setContent(content);
        pane.setExpanded(godina == 1);

        return pane;
    }

    private TableView<PredmetDTO> createPredmetTable(List<PredmetDTO> predmeti) {
        TableView<PredmetDTO> table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(predmeti));

        TableColumn<PredmetDTO, String> nazivCol = new TableColumn<>("Naziv predmeta");
        nazivCol.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        nazivCol.setPrefWidth(300);

        TableColumn<PredmetDTO, String> sifraCol = new TableColumn<>("Šifra");
        sifraCol.setCellValueFactory(new PropertyValueFactory<>("sifra"));
        sifraCol.setPrefWidth(80);

        TableColumn<PredmetDTO, String> casovaCol = new TableColumn<>("Časova");
        casovaCol.setCellValueFactory(cellData -> {
            PredmetDTO p = cellData.getValue();
            String casova = (p.getFondCasovaPredavanja() != null ? p.getFondCasovaPredavanja() : 0) + "+" +
                           (p.getFondCasovaVezbe() != null ? p.getFondCasovaVezbe() : 0);
            return new javafx.beans.property.SimpleStringProperty(casova);
        });
        casovaCol.setPrefWidth(80);

        TableColumn<PredmetDTO, Integer> espbCol = new TableColumn<>("ESPB");
        espbCol.setCellValueFactory(new PropertyValueFactory<>("espb"));
        espbCol.setPrefWidth(60);

        TableColumn<PredmetDTO, String> obavezanCol = new TableColumn<>("Obavezan");
        obavezanCol.setCellValueFactory(cellData -> {
            String value = cellData.getValue().isObavezan() ? "✓" : "";
            return new javafx.beans.property.SimpleStringProperty(value);
        });
        obavezanCol.setPrefWidth(80);

        table.getColumns().addAll(nazivCol, sifraCol, casovaCol, espbCol, obavezanCol);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedPredmet = newSelection;
            editBtn.setDisable(newSelection == null);
            deleteBtn.setDisable(newSelection == null);
        });

        table.setPrefHeight(150);
        return table;
    }

    @FXML
    public void handleAddPredmet(ActionEvent event) {
        mainView.openModal("addPredmet", "Dodavanje novog predmeta", 550, 550);
        handleRefresh(null);
    }

    @FXML
    public void handleEditPredmet(ActionEvent event) {
        if (selectedPredmet == null) {
            showWarning("Upozorenje", "Molimo izaberite predmet za izmenu");
            return;
        }
        showInfo("Izmena predmeta", "Funkcionalnost izmene predmeta biće implementirana");
    }

    @FXML
    public void handleDeletePredmet(ActionEvent event) {
        if (selectedPredmet == null) {
            showWarning("Upozorenje", "Molimo izaberite predmet za brisanje");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Potvrda brisanja");
        confirm.setHeaderText("Brisanje predmeta");
        confirm.setContentText("Da li ste sigurni da želite da obrišete predmet: " + selectedPredmet.getNaziv() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                CompletableFuture.supplyAsync(() -> 
                    predmetService.deletePredmet(selectedPredmet.getId())
                ).thenAccept(success -> 
                    Platform.runLater(() -> {
                        if (success) {
                            showInfo("Uspešno", "Predmet je uspešno obrisan");
                            handleRefresh(null);
                        } else {
                            showError("Greška", "Greška pri brisanju predmeta");
                        }
                    })
                );
            }
        });
    }

    @FXML
    public void handleGoBack(ActionEvent event) {
        mainView.changeRoot("main");
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        if (studProgramCb.getValue() != null) {
            handleLoadPredmeti(event);
        }
    }

    @FXML
    public void handlePrintReport(ActionEvent event) {
        StudProgramDTO selectedProgram = studProgramCb.getValue();
        
        if (selectedProgram == null) {
            showWarning("Upozorenje", "Molimo izaberite studijski program");
            return;
        }

        Boolean obavezniFilter = obavezniCheckBox.isSelected() ? true : null;

        CompletableFuture.supplyAsync(() -> 
            predmetService.getPredmetiByProgram(selectedProgram.getId(), obavezniFilter)
        ).thenAccept(predmeti -> {
            if (predmeti == null || predmeti.isEmpty()) {
                Platform.runLater(() -> 
                    showWarning("Upozorenje", "Nema predmeta za izabrani studijski program")
                );
                return;
            }
            
            try {
                // Load Jasper report template
                InputStream reportStream = getClass().getResourceAsStream("/reports/sviPredmeti.jrxml");
                if (reportStream == null) {
                    Platform.runLater(() -> 
                        showError("Greška", "Nije moguće pronaći šablon izveštaja")
                    );
                    return;
                }
                
                // Compile report
                JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
                
                // Prepare parameters (empty - template doesn't use any)
                Map<String, Object> parameters = new HashMap<>();
                
                // Create data source
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(predmeti);
                
                // Fill report
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                
                // Export to PDF
                String fileName = "Predmeti_" + selectedProgram.getOznaka() + "_" + System.currentTimeMillis() + ".pdf";
                String pdfPath = System.getProperty("user.dir") + "/" + fileName;
                
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfPath));
                exporter.exportReport();
                
                // Open PDF with default application
                Platform.runLater(() -> {
                    try {
                        java.awt.Desktop.getDesktop().open(new java.io.File(pdfPath));
                        showInfo("Uspešno", "Izveštaj je uspešno generisan");
                    } catch (Exception e) {
                        showInfo("Uspešno", "Izveštaj je uspešno generisan");
                    }
                });
                
            } catch (Exception e) {
                Platform.runLater(() -> 
                    showError("Greška", "Greška pri generisanju izveštaja: " + e.getMessage())
                );
                e.printStackTrace();
            }
        }).exceptionally(ex -> {
            Platform.runLater(() -> 
                showError("Greška", "Greška pri učitavanju predmeta: " + ex.getMessage())
            );
            return null;
        });
    }

    private String getRomanNumeral(int number) {
        String[] romanNumerals = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII"};
        return number > 0 && number < romanNumerals.length ? romanNumerals[number] : String.valueOf(number);
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
