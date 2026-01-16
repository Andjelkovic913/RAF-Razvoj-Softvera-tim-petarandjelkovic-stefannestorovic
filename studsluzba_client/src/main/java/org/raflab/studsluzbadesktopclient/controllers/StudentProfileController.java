package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzba.dtos.*;
import org.raflab.studsluzbadesktopclient.services.NavigationHistoryService;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class StudentProfileController {

    private static final String VIEW_NAME = "studentProfile";
    
    private final MainView mainView;
    private final StudentService studentService;
    private final NavigationHistoryService navigationHistoryService;
    
    private static Long currentStudentId;
    private static Long currentIndeksId;
    private StudentProfileDTO currentProfile;
    
    @FXML private TabPane profileTabPane;

    @FXML private Label studentNameLabel;
    @FXML private Label studentIndexLabel;
    
    // Tab 1: Basic info labels
    @FXML private Label imeLabel;
    @FXML private Label prezimeLabel;
    @FXML private Label srednjeImeLabel;
    @FXML private Label jmbgLabel;
    @FXML private Label datumRodjenjaLabel;
    @FXML private Label mestoRodjenjaLabel;
    @FXML private Label drzavaRodjenjaLabel;
    @FXML private Label polLabel;
    @FXML private Label drzavljanstvoLabel;
    @FXML private Label nacionalnostLabel;
    @FXML private Label licnaKartaLabel;
    
    // Contact info labels
    @FXML private Label adresaLabel;
    @FXML private Label mestoStanovanjaLabel;
    @FXML private Label emailPrivatniLabel;
    @FXML private Label emailFakultetLabel;
    @FXML private Label telefonLabel;
    
    // Index info labels
    @FXML private Label studentIdLabel;
    @FXML private Label brojIndeksaLabel;
    @FXML private Label studProgramLabel;
    @FXML private Label godinaUpisaLabel;
    @FXML private Label finansiranjeLabel;
    @FXML private Label statusIndeksaLabel;
    
    // High school labels
    @FXML private Label srednjaSkolaNazivLabel;
    @FXML private Label srednjaSkolaMestoLabel;
    @FXML private Label uspehSrednjaLabel;
    @FXML private Label uspehPrijemniLabel;
    
    // Tab 2: Exams tables
    @FXML private TableView<StudentExamDTO> polozeniIspitiTable;
    @FXML private TableView<StudentExamDTO> nepolozeniIspitiTable;
    
    // Tab 3: Payments
    @FXML private TableView<StudentPaymentDTO> uplateTable;
    @FXML private Label ukupnoUplataLabel;
    
    // Tab 4: Enrollments
    @FXML private TableView<StudentEnrollmentDTO> upisiTable;
    
    // Tab 5: Statistics
    @FXML private Label statistikaEspbLabel;
    @FXML private Label statistikaProsekLabel;
    @FXML private Label statistikaPolozeniLabel;
    @FXML private Label statistikaNepolozeniLabel;

    public StudentProfileController(MainView mainView, StudentService studentService, 
                                    NavigationHistoryService navigationHistoryService) {
        this.mainView = mainView;
        this.studentService = studentService;
        this.navigationHistoryService = navigationHistoryService;
    }

    public static void setStudentIds(Long studentPodaciId, Long indeksId) {
        currentStudentId = studentPodaciId;
        currentIndeksId = indeksId;
    }

    @FXML
    public void initialize() {
        // Setup tab change tracking for history navigation
        setupTabChangeTracking();
        
        if (currentStudentId != null) {
            loadStudentProfile();
        } else {
            showAlert("Greška", "Nije izabran student za prikaz profila.");
        }
    }
    
    /**
     * Sets up listener to track tab changes for navigation history.
     */
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

    private void loadStudentProfile() {
        try {
            currentProfile = studentService.getStudentProfile(currentStudentId, currentIndeksId);
            
            if (currentProfile != null) {
                populateBasicInfo();
                populateExams();
                populatePayments();
                populateEnrollments();
                populateStatistics();
            } else {
                showAlert("Greška", "Nije moguće učitati profil studenta.");
            }
        } catch (Exception e) {
            System.err.println("Greška pri učitavanju profila: " + e.getMessage());
            e.printStackTrace();
            showAlert("Greška", "Došlo je do greške pri učitavanju profila: " + e.getMessage());
        }
    }

    private void populateBasicInfo() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        
        // Header
        studentNameLabel.setText(currentProfile.getFullName());
        studentIndexLabel.setText("Indeks: " + currentProfile.getBrojIndeksaFormatted());
        
        // Personal data
        imeLabel.setText(safeString(currentProfile.getIme()));
        prezimeLabel.setText(safeString(currentProfile.getPrezime()));
        srednjeImeLabel.setText(safeString(currentProfile.getSrednjeIme()));
        jmbgLabel.setText(safeString(currentProfile.getJmbg()));
        datumRodjenjaLabel.setText(currentProfile.getDatumRodjenja() != null ? 
                currentProfile.getDatumRodjenja().format(dateFormatter) : "-");
        mestoRodjenjaLabel.setText(safeString(currentProfile.getMestoRodjenja()));
        drzavaRodjenjaLabel.setText(safeString(currentProfile.getDrzavaRodjenja()));
        polLabel.setText(currentProfile.getPol() != null ? 
                (currentProfile.getPol().equals("M") ? "Muški" : "Ženski") : "-");
        drzavljanstvoLabel.setText(safeString(currentProfile.getDrzavljanstvo()));
        nacionalnostLabel.setText(safeString(currentProfile.getNacionalnost()));
        licnaKartaLabel.setText(safeString(currentProfile.getBrojLicneKarte()));
        
        // Contact data
        adresaLabel.setText(safeString(currentProfile.getAdresa()));
        mestoStanovanjaLabel.setText(safeString(currentProfile.getMestoStanovanja()));
        emailPrivatniLabel.setText(safeString(currentProfile.getEmailPrivatni()));
        emailFakultetLabel.setText(safeString(currentProfile.getEmailFakultetski()));
        telefonLabel.setText(safeString(currentProfile.getBrojTelefona()));
        
        // Index data
        studentIdLabel.setText(currentProfile.getIdStudentPodaci() != null ? 
                currentProfile.getIdStudentPodaci().toString() : "-");
        brojIndeksaLabel.setText(currentProfile.getBrojIndeksaFormatted());
        studProgramLabel.setText(safeString(currentProfile.getStudijskiProgram()));
        godinaUpisaLabel.setText(currentProfile.getGodinaUpisa() != null ? 
                currentProfile.getGodinaUpisa().toString() : "-");
        finansiranjeLabel.setText(safeString(currentProfile.getNacinFinansiranja()));
        statusIndeksaLabel.setText(currentProfile.isAktivanIndeks() ? "Aktivan" : "Neaktivan");
        
        // High school data
        srednjaSkolaNazivLabel.setText(safeString(currentProfile.getSrednjaSkolaNaziv()));
        srednjaSkolaMestoLabel.setText(safeString(currentProfile.getSrednjaSkolaMesto()));
        uspehSrednjaLabel.setText(currentProfile.getUspehSrednjaSkola() != null ? 
                String.format("%.2f", currentProfile.getUspehSrednjaSkola()) : "-");
        uspehPrijemniLabel.setText(currentProfile.getUspehPrijemni() != null ? 
                String.format("%.2f", currentProfile.getUspehPrijemni()) : "-");
    }

    private void populateExams() {
        List<StudentExamDTO> polozeni = currentProfile.getPolozeniIspiti();
        List<StudentExamDTO> nepolozeni = currentProfile.getNepolozeniIspiti();
        
        if (polozeni != null) {
            polozeniIspitiTable.setItems(FXCollections.observableArrayList(polozeni));
        }
        
        if (nepolozeni != null) {
            nepolozeniIspitiTable.setItems(FXCollections.observableArrayList(nepolozeni));
        }
    }

    private void populatePayments() {
        List<StudentPaymentDTO> uplate = currentProfile.getUplate();
        
        if (uplate != null) {
            uplateTable.setItems(FXCollections.observableArrayList(uplate));
            
            // Calculate total
            double total = uplate.stream()
                    .filter(u -> u.getIznos() != null)
                    .mapToDouble(u -> u.getIznos().doubleValue())
                    .sum();
            ukupnoUplataLabel.setText(String.format("Ukupno uplaćeno: %.2f RSD", total));
        } else {
            ukupnoUplataLabel.setText("Ukupno uplaćeno: 0.00 RSD");
        }
    }

    private void populateEnrollments() {
        List<StudentEnrollmentDTO> upisi = currentProfile.getUpisi();
        
        if (upisi != null) {
            upisiTable.setItems(FXCollections.observableArrayList(upisi));
        }
    }

    private void populateStatistics() {
        int polozeniCount = currentProfile.getPolozeniIspiti() != null ? 
                currentProfile.getPolozeniIspiti().size() : 0;
        int nepolozeniCount = currentProfile.getNepolozeniIspiti() != null ? 
                currentProfile.getNepolozeniIspiti().size() : 0;
        
        statistikaEspbLabel.setText(currentProfile.getUkupnoEspb() != null ? 
                currentProfile.getUkupnoEspb().toString() : "0");
        statistikaProsekLabel.setText(currentProfile.getProsecnaOcena() != null ? 
                String.format("%.2f", currentProfile.getProsecnaOcena()) : "0.00");
        statistikaPolozeniLabel.setText(String.valueOf(polozeniCount));
        statistikaNepolozeniLabel.setText(String.valueOf(nepolozeniCount));
    }

    @FXML
    public void handleGoBack(ActionEvent event) {
        currentStudentId = null;
        currentIndeksId = null;
        mainView.changeRoot("searchStudent", true);
    }

    @FXML
    public void handlePrijavaIspita(ActionEvent event) {
        if (currentProfile == null || currentIndeksId == null) {
            showAlert("Greška", "Nije učitan profil studenta.");
            return;
        }
        
        try {
            PrijavaIspitaPopupController.setStudentData(currentIndeksId, 
                currentProfile.getFullName(), 
                currentProfile.getBrojIndeksaFormatted());
            mainView.openModal("prijavaIspitaPopup", "Prijava ispita", 600, 500);
            
            // Refresh profile after modal closes
            loadStudentProfile();
        } catch (Exception e) {
            showAlert("Greška", "Greška pri otvaranju prijave ispita: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddPayment(ActionEvent event) {
        showAlert("Info", "Funkcionalnost dodavanja uplate će biti implementirana.");
    }

    @FXML
    public void handleEnrollYear(ActionEvent event) {
        showAlert("Info", "Funkcionalnost upisa nove godine će biti implementirana.");
    }

    @FXML
    public void handleRenewYear(ActionEvent event) {
        showAlert("Info", "Funkcionalnost obnove godine će biti implementirana.");
    }

    @FXML
    public void handleGenerateReport(ActionEvent event) {
        if (currentProfile == null) {
            showAlert("Greška", "Nije učitan profil studenta.");
            return;
        }
        
        CompletableFuture.runAsync(() -> {
            try {
                InputStream reportStream = getClass().getResourceAsStream("/reports/uverenjeOStudiranju1.jrxml");
                if (reportStream == null) {
                    Platform.runLater(() -> showAlert("Greška", "Nije moguće pronaći šablon izveštaja."));
                    return;
                }
                
                JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
                
                Map<String, Object> parameters = new HashMap<>();
                
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                        Collections.singletonList(currentProfile));
                
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                
                String fileName = "Uverenje_" + currentProfile.getBrojIndeksaFormatted().replace("/", "_").replace(" ", "_") + ".pdf";
                String pdfPath = System.getProperty("user.dir") + "/" + fileName;
                
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfPath));
                exporter.exportReport();
                
                Platform.runLater(() -> {
                    try {
                        java.awt.Desktop.getDesktop().open(new java.io.File(pdfPath));
                        showAlert("Uspešno", "Uverenje o studiranju je uspešno generisano.");
                    } catch (Exception e) {
                        showAlert("Uspešno", "Uverenje o studiranju je uspešno generisano.");
                    }
                });
                
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Greška", "Greška pri generisanju uverenja: " + e.getMessage()));
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void handlePrintTranscript(ActionEvent event) {
        if (currentProfile == null) {
            showAlert("Greška", "Nije učitan profil studenta.");
            return;
        }
        
        List<StudentExamDTO> polozeniIspiti = currentProfile.getPolozeniIspiti();
        if (polozeniIspiti == null || polozeniIspiti.isEmpty()) {
            showAlert("Info", "Student nema položenih ispita.");
            return;
        }
        
        CompletableFuture.runAsync(() -> {
            try {
                InputStream reportStream = getClass().getResourceAsStream("/reports/uverenjeOPolozenimIspitimaPoGodini.jrxml");
                if (reportStream == null) {
                    Platform.runLater(() -> showAlert("Greška", "Nije moguće pronaći šablon izveštaja."));
                    return;
                }
                
                JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
                
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("studentIme", currentProfile.getIme());
                parameters.put("studentPrezime", currentProfile.getPrezime());
                parameters.put("brojIndeksa", currentProfile.getBrojIndeksaFormatted());
                parameters.put("studijskiProgram", currentProfile.getStudijskiProgram());
                parameters.put("ukupnoEspb", currentProfile.getUkupnoEspb());
                parameters.put("prosecnaOcena", currentProfile.getProsecnaOcena());
                
                // Sort exams by godinaStudija, then by predmetNaziv
                List<StudentExamDTO> sortedIspiti = polozeniIspiti.stream()
                    .sorted((e1, e2) -> {
                        int godinaCompare = Integer.compare(
                            e1.getGodinaStudija() != null ? e1.getGodinaStudija() : 0,
                            e2.getGodinaStudija() != null ? e2.getGodinaStudija() : 0
                        );
                        if (godinaCompare != 0) return godinaCompare;
                        return e1.getPredmetNaziv().compareTo(e2.getPredmetNaziv());
                    })
                    .collect(java.util.stream.Collectors.toList());
                
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(sortedIspiti);
                
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                
                String fileName = "Uverenje_Ispiti_" + currentProfile.getBrojIndeksaFormatted().replace("/", "_").replace(" ", "_") + ".pdf";
                String pdfPath = System.getProperty("user.dir") + "/" + fileName;
                
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfPath));
                exporter.exportReport();
                
                Platform.runLater(() -> {
                    try {
                        java.awt.Desktop.getDesktop().open(new java.io.File(pdfPath));
                        showAlert("Uspešno", "Uverenje o položenim ispitima je uspešno generisano.");
                    } catch (Exception e) {
                        showAlert("Uspešno", "Uverenje o položenim ispitima je uspešno generisano.");
                    }
                });
                
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Greška", "Greška pri generisanju uverenja: " + e.getMessage()));
                e.printStackTrace();
            }
        });
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
