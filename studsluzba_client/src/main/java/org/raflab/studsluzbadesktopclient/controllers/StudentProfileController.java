package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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
    
    @FXML private Label adresaLabel;
    @FXML private Label mestoStanovanjaLabel;
    @FXML private Label emailPrivatniLabel;
    @FXML private Label emailFakultetLabel;
    @FXML private Label telefonLabel;
    
    @FXML private Label studentIdLabel;
    @FXML private Label brojIndeksaLabel;
    @FXML private Label studProgramLabel;
    @FXML private Label godinaUpisaLabel;
    @FXML private Label finansiranjeLabel;
    @FXML private Label statusIndeksaLabel;
    
    @FXML private Label srednjaSkolaNazivLabel;
    @FXML private Label srednjaSkolaMestoLabel;
    @FXML private Label uspehSrednjaLabel;
    @FXML private Label uspehPrijemniLabel;
    
    @FXML private TableView<StudentExamDTO> polozeniIspitiTable;
    @FXML private TableView<StudentExamDTO> nepolozeniIspitiTable;
    
    @FXML private TableView<StudentPaymentDTO> uplateTable;
    @FXML private Label ukupnoUplataLabel;
    
    @FXML private TableView<StudentEnrollmentDTO> upisiTable;
    
    // statistika
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
        setupTabChangeTracking(); // pamti na kom tabu smo bili
        
        if (currentStudentId != null) {
            loadStudentProfile();
        } else {
            showAlert("Greška", "Nije izabran student za prikaz profila.");
        }
    }
    

    private void setupTabChangeTracking() {
        if (profileTabPane != null) {
            int initialTab = profileTabPane.getSelectionModel().getSelectedIndex();
            if (initialTab >= 0) {
                navigationHistoryService.registerTabChange(VIEW_NAME, initialTab);
            }
            
            // prati promene tabova
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

        studentNameLabel.setText(currentProfile.getFullName());
        studentIndexLabel.setText("Indeks: " + currentProfile.getBrojIndeksaFormatted());
        
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
        
        adresaLabel.setText(safeString(currentProfile.getAdresa()));
        mestoStanovanjaLabel.setText(safeString(currentProfile.getMestoStanovanja()));
        emailPrivatniLabel.setText(safeString(currentProfile.getEmailPrivatni()));
        emailFakultetLabel.setText(safeString(currentProfile.getEmailFakultetski()));
        telefonLabel.setText(safeString(currentProfile.getBrojTelefona()));
        
        studentIdLabel.setText(currentProfile.getIdStudentPodaci() != null ?
                currentProfile.getIdStudentPodaci().toString() : "-");
        brojIndeksaLabel.setText(currentProfile.getBrojIndeksaFormatted());
        studProgramLabel.setText(safeString(currentProfile.getStudijskiProgram()));
        godinaUpisaLabel.setText(currentProfile.getGodinaUpisa() != null ? 
                currentProfile.getGodinaUpisa().toString() : "-");
        finansiranjeLabel.setText(safeString(currentProfile.getNacinFinansiranja()));
        statusIndeksaLabel.setText(currentProfile.isAktivanIndeks() ? "Aktivan" : "Neaktivan");
        
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
            
            loadStudentProfile(); // osvezi nakon zatvaranja
        } catch (Exception e) {
            showAlert("Greška", "Greška pri otvaranju prijave ispita: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddPayment(ActionEvent event) {
        if (currentIndeksId == null) {
            showAlert("Greška", "Nije moguće dodati uplatu - nedostaje ID indeksa.");
            return;
        }

        // koliko je ostalo za platiti
        Map<String, Double> preostaliIznos = studentService.getPreostaliIznos(currentIndeksId);
        double preostaloRSD = preostaliIznos != null ? preostaliIznos.getOrDefault("preostaloRSD", 0.0) : 0.0;
        double ukupnoUplaceno = preostaliIznos != null ? preostaliIznos.getOrDefault("ukupnoUplacenoRSD", 0.0) : 0.0;

        Dialog<Map<String, Object>> dialog = new Dialog<>();
        dialog.setTitle("Dodaj uplatu školarine");
        dialog.setHeaderText("Evidentiranje uplate za studenta");

        ButtonType uplataDugme = new ButtonType("Uplati", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(uplataDugme, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        // School year combo
        ComboBox<Map<String, Object>> skolskaGodinaCb = new ComboBox<>();
        List<Map<String, Object>> skolskeGodine = studentService.getSkolskeGodine();
        
        skolskaGodinaCb.setCellFactory(param -> new ListCell<Map<String, Object>>() {
            @Override
            protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String naziv = (String) item.get("naziv");
                    Boolean aktivna = (Boolean) item.get("aktivna");
                    setText(naziv + (aktivna != null && aktivna ? " (aktivna)" : ""));
                }
            }
        });
        
        skolskaGodinaCb.setButtonCell(new ListCell<Map<String, Object>>() {
            @Override
            protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String naziv = (String) item.get("naziv");
                    Boolean aktivna = (Boolean) item.get("aktivna");
                    setText(naziv + (aktivna != null && aktivna ? " (aktivna)" : ""));
                }
            }
        });
        
        skolskaGodinaCb.getItems().addAll(skolskeGodine);
        
        if (!skolskaGodinaCb.getItems().isEmpty()) {
            // bira aktivnu godinu
            for (Map<String, Object> sg : skolskeGodine) {
                Boolean aktivna = (Boolean) sg.get("aktivna");
                if (aktivna != null && aktivna) {
                    skolskaGodinaCb.getSelectionModel().select(sg);
                    break;
                }
            }
        }

        ComboBox<String> razlogCb = new ComboBox<>();
        razlogCb.getItems().addAll(
            "Cela školarina",
            "1. rata",
            "2. rata", 
            "3. rata",
            "4. rata",
            "Dopuna"
        );
        razlogCb.getSelectionModel().selectFirst();

        TextField iznosField = new TextField();
        iznosField.setPromptText("Iznos u RSD");

        Label preostaloLabel = new Label(String.format("Preostalo za uplatu: %.2f RSD", preostaloRSD));
        preostaloLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1976D2;");
        Label uplacenoLabel = new Label(String.format("Do sada uplaćeno: %.2f RSD", ukupnoUplaceno));

        grid.add(new Label("Školska godina:"), 0, 0);
        grid.add(skolskaGodinaCb, 1, 0);
        grid.add(new Label("Razlog uplate:"), 0, 1);
        grid.add(razlogCb, 1, 1);
        grid.add(new Label("Iznos (RSD):"), 0, 2);
        grid.add(iznosField, 1, 2);
        grid.add(uplacenoLabel, 0, 3, 2, 1);
        grid.add(preostaloLabel, 0, 4, 2, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == uplataDugme) {
                Map<String, Object> result = new HashMap<>();
                result.put("skolskaGodina", skolskaGodinaCb.getValue());
                result.put("razlog", razlogCb.getValue());
                result.put("iznos", iznosField.getText());
                return result;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            try {
                String iznosStr = (String) result.get("iznos");
                String razlog = (String) result.get("razlog");
                @SuppressWarnings("unchecked")
                Map<String, Object> skolskaGodinaMap = (Map<String, Object>) result.get("skolskaGodina");
                
                if (skolskaGodinaMap == null) {
                    showAlert("Greška", "Izaberite školsku godinu.");
                    return;
                }
                
                if (iznosStr == null || iznosStr.trim().isEmpty()) {
                    showAlert("Greška", "Unesite iznos uplate.");
                    return;
                }

                double iznos = Double.parseDouble(iznosStr);
                if (iznos <= 0) {
                    showAlert("Greška", "Iznos mora biti pozitivan broj.");
                    return;
                }

                // provera prekoracenja
                if (preostaloRSD > 0 && iznos > preostaloRSD) {
                    double prekoracenje = iznos - preostaloRSD;
                    Alert confirm = new Alert(Alert.AlertType.WARNING);
                    confirm.setTitle("Upozorenje - Prekoračenje");
                    confirm.setHeaderText("Iznos uplate prelazi preostali dug!");
                    confirm.setContentText(String.format(
                        "Preostalo za uplatu: %.2f RSD\n" +
                        "Uneti iznos: %.2f RSD\n" +
                        "Prekoračenje: %.2f RSD\n\n" +
                        "Da li želite da nastavite sa uplatom?",
                        preostaloRSD, iznos, prekoracenje
                    ));
                    confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    
                    if (confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
                        return;
                    }
                }

                Long skolskaGodinaId = ((Number) skolskaGodinaMap.get("id")).longValue();
                
                boolean success = studentService.dodajUplatu(currentIndeksId, skolskaGodinaId, iznos, razlog);
                if (success) {
                    showAlert("Uspeh", "Uplata je uspešno evidentirana.\nRazlog: " + razlog);
                    loadStudentProfile();
                } else {
                    showAlert("Greška", "Došlo je do greške pri evidentiranju uplate.");
                }
            } catch (NumberFormatException e) {
                showAlert("Greška", "Unesite validan broj za iznos.");
            } catch (Exception e) {
                showAlert("Greška", "Greška pri dodavanju uplate: " + e.getMessage());
            }
        });
    }

    @FXML
    public void handleEnrollYear(ActionEvent event) {
        if (currentProfile == null || currentIndeksId == null) {
            showAlert("Greška", "Nije učitan profil studenta.");
            return;
        }

        if (!currentProfile.isAktivanIndeks()) {
            showAlert("Greška", "Indeks nije aktivan. Nije moguće upisati godinu.");
            return;
        }

        // uzmi trenutnu godinu i izracunaj sledecu
        Integer currentYear = studentService.getCurrentStudentYear(currentIndeksId, currentProfile.getBrojIndeksa());
        Integer nextYear = currentYear + 1;

        if (nextYear > 4) {
            showAlert("Info", "Student je već upisao maksimalnu godinu studija (4. godina).");
            return;
        }

        Dialog<Map<String, Object>> dialog = new Dialog<>();
        dialog.setTitle("Upis nove godine");
        dialog.setHeaderText("Upis " + nextYear + ". godine studija za studenta " + currentProfile.getFullName());

        ButtonType upisDugme = new ButtonType("Upiši", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(upisDugme, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        // School year combo
        ComboBox<Map<String, Object>> skolskaGodinaCb = new ComboBox<>();
        List<Map<String, Object>> skolskeGodine = studentService.getSkolskeGodine();

        skolskaGodinaCb.setCellFactory(param -> new ListCell<Map<String, Object>>() {
            @Override
            protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String naziv = (String) item.get("naziv");
                    Boolean aktivna = (Boolean) item.get("aktivna");
                    setText(naziv + (aktivna != null && aktivna ? " (aktivna)" : ""));
                }
            }
        });

        skolskaGodinaCb.setButtonCell(new ListCell<Map<String, Object>>() {
            @Override
            protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String naziv = (String) item.get("naziv");
                    Boolean aktivna = (Boolean) item.get("aktivna");
                    setText(naziv + (aktivna != null && aktivna ? " (aktivna)" : ""));
                }
            }
        });

        skolskaGodinaCb.getItems().addAll(skolskeGodine);

        for (Map<String, Object> sg : skolskeGodine) {
            Boolean aktivna = (Boolean) sg.get("aktivna");
            if (aktivna != null && aktivna) {
                skolskaGodinaCb.getSelectionModel().select(sg);
                break;
            }
        }

        TextField espbField = new TextField("0");
        espbField.setPromptText("Preneti ESPB bodovi");

        Label infoLabel = new Label("Godina za upis: " + nextYear + ". godina");
        infoLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1976D2;");

        grid.add(new Label("Školska godina:"), 0, 0);
        grid.add(skolskaGodinaCb, 1, 0);
        grid.add(new Label("Preneti ESPB:"), 0, 1);
        grid.add(espbField, 1, 1);
        grid.add(infoLabel, 0, 2, 2, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == upisDugme) {
                Map<String, Object> result = new HashMap<>();
                result.put("skolskaGodina", skolskaGodinaCb.getValue());
                result.put("espb", espbField.getText());
                result.put("godina", nextYear);
                return result;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> skolskaGodinaMap = (Map<String, Object>) result.get("skolskaGodina");
                String espbStr = (String) result.get("espb");
                Integer godina = (Integer) result.get("godina");

                if (skolskaGodinaMap == null) {
                    showAlert("Greška", "Izaberite školsku godinu.");
                    return;
                }

                Integer prenetiESPB = 0;
                if (espbStr != null && !espbStr.trim().isEmpty()) {
                    try {
                        prenetiESPB = Integer.parseInt(espbStr.trim());
                    } catch (NumberFormatException e) {
                        showAlert("Greška", "Unesite validan broj za ESPB bodove.");
                        return;
                    }
                }

                Long skolskaGodinaId = ((Number) skolskaGodinaMap.get("id")).longValue();

                Long upisId = studentService.createUpisGodine(currentIndeksId, skolskaGodinaId, godina, prenetiESPB);

                if (upisId != null) {
                    showAlert("Uspeh", "Student je uspešno upisan na " + godina + ". godinu studija.");
                    loadStudentProfile();
                } else {
                    showAlert("Greška", "Došlo je do greške pri upisu godine.");
                }
            } catch (RuntimeException e) {
                showAlert("Greška", e.getMessage());
            } catch (Exception e) {
                showAlert("Greška", "Greška pri upisu godine: " + e.getMessage());
            }
        });
    }

    @FXML
    public void handleRenewYear(ActionEvent event) {
        if (currentProfile == null || currentIndeksId == null) {
            showAlert("Greška", "Nije učitan profil studenta.");
            return;
        }

        if (!currentProfile.isAktivanIndeks()) {
            showAlert("Greška", "Indeks nije aktivan. Nije moguće obnoviti godinu.");
            return;
        }

        Integer currentYear = studentService.getCurrentStudentYear(currentIndeksId, currentProfile.getBrojIndeksa());

        if (currentYear < 1) {
            showAlert("Greška", "Student nema upisanih godina. Obnova nije moguća.");
            return;
        }

        // nepolozeni predmeti
        Map<String, Object> failedSubjectsInfo = studentService.getFailedSubjects(currentIndeksId);
        
        int totalFailedESPB = 0;
        String failedSubjectsList = "";
        
        if (failedSubjectsInfo != null) {
            if (failedSubjectsInfo.get("totalFailedESPB") instanceof Number) {
                totalFailedESPB = ((Number) failedSubjectsInfo.get("totalFailedESPB")).intValue();
            }
            
            Object failedSubjects = failedSubjectsInfo.get("failedSubjects");
            if (failedSubjects instanceof List) {
                StringBuilder sb = new StringBuilder();
                for (Object subj : (List<?>) failedSubjects) {
                    if (subj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> subjMap = (Map<String, Object>) subj;
                        String naziv = (String) subjMap.get("predmetNaziv");
                        Integer espb = subjMap.get("espb") instanceof Number ? 
                            ((Number) subjMap.get("espb")).intValue() : 0;
                        if (sb.length() > 0) sb.append("\n");
                        sb.append("• ").append(naziv).append(" (").append(espb).append(" ESPB)");
                    }
                }
                failedSubjectsList = sb.toString();
            }
        }

        if (totalFailedESPB == 0) {
            showAlert("Info", "Student nema nepoloženih predmeta. Obnova godine nije potrebna - možete upisati novu godinu.");
            return;
        }
        
        int prenetiESPB = studentService.getPrenetiESPBForYear(currentProfile.getBrojIndeksa(), currentYear);

        Dialog<Map<String, Object>> dialog = new Dialog<>();
        dialog.setTitle("Obnova godine");
        dialog.setHeaderText("Obnova " + currentYear + ". godine studija za studenta " + currentProfile.getFullName());

        ButtonType obnovaDugme = new ButtonType("Obnovi", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(obnovaDugme, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        ComboBox<Map<String, Object>> skolskaGodinaCb = new ComboBox<>();
        List<Map<String, Object>> skolskeGodine = studentService.getSkolskeGodine();

        skolskaGodinaCb.setCellFactory(param -> new ListCell<Map<String, Object>>() {
            @Override
            protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String naziv = (String) item.get("naziv");
                    Boolean aktivna = (Boolean) item.get("aktivna");
                    setText(naziv + (aktivna != null && aktivna ? " (aktivna)" : ""));
                }
            }
        });

        skolskaGodinaCb.setButtonCell(new ListCell<Map<String, Object>>() {
            @Override
            protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String naziv = (String) item.get("naziv");
                    Boolean aktivna = (Boolean) item.get("aktivna");
                    setText(naziv + (aktivna != null && aktivna ? " (aktivna)" : ""));
                }
            }
        });

        skolskaGodinaCb.getItems().addAll(skolskeGodine);

        for (Map<String, Object> sg : skolskeGodine) {
            Boolean aktivna = (Boolean) sg.get("aktivna");
            if (aktivna != null && aktivna) {
                skolskaGodinaCb.getSelectionModel().select(sg);
                break;
            }
        }

        // Reason field
        TextField razlogField = new TextField();
        razlogField.setPromptText("Razlog obnove (opciono)");

        // ESPB field - read-only, automatically calculated
        TextField espbField = new TextField(String.valueOf(prenetiESPB));
        espbField.setEditable(false);
        espbField.setStyle("-fx-opacity: 1.0; -fx-background-color: #f0f0f0;");

        // Info labels
        Label godinaLabel = new Label("Godina za obnovu: " + currentYear + ". godina");
        godinaLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FF9800;");

        Label failedEspbLabel = new Label("Nepoloženi predmeti: " + totalFailedESPB + " ESPB");
        failedEspbLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #D32F2F;");

        // Failed subjects list
        TextArea failedSubjectsArea = new TextArea(failedSubjectsList);
        failedSubjectsArea.setEditable(false);
        failedSubjectsArea.setPrefRowCount(4);
        failedSubjectsArea.setWrapText(true);

        grid.add(new Label("Školska godina:"), 0, 0);
        grid.add(skolskaGodinaCb, 1, 0);
        grid.add(new Label("Razlog obnove:"), 0, 1);
        grid.add(razlogField, 1, 1);
        grid.add(new Label("Preneti ESPB (ostvareni):"), 0, 2);
        grid.add(espbField, 1, 2);
        grid.add(godinaLabel, 0, 3, 2, 1);
        grid.add(failedEspbLabel, 0, 4, 2, 1);
        grid.add(new Label("Nepoloženi predmeti:"), 0, 5);
        grid.add(failedSubjectsArea, 0, 6, 2, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setPrefWidth(500);

        final int finalCurrentYear = currentYear;
        final int finalPrenetiESPB = prenetiESPB;
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == obnovaDugme) {
                Map<String, Object> result = new HashMap<>();
                result.put("skolskaGodina", skolskaGodinaCb.getValue());
                result.put("razlog", razlogField.getText());
                result.put("godina", finalCurrentYear);
                result.put("prenetiESPB", finalPrenetiESPB);
                return result;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> skolskaGodinaMap = (Map<String, Object>) result.get("skolskaGodina");
                String razlog = (String) result.get("razlog");
                Integer godina = (Integer) result.get("godina");
                Integer prenetiESPBValue = (Integer) result.get("prenetiESPB");

                if (skolskaGodinaMap == null) {
                    showAlert("Greška", "Izaberite školsku godinu.");
                    return;
                }

                Long skolskaGodinaId = ((Number) skolskaGodinaMap.get("id")).longValue();

                Long obnovaId = studentService.createObnovaGodine(
                    currentIndeksId, 
                    skolskaGodinaId, 
                    godina, 
                    null,  // prethodniUpisId - let server determine
                    prenetiESPBValue, 
                    razlog,
                    null   // nextYearDrziPredmetIds - can be extended later
                );

                if (obnovaId != null) {
                    showAlert("Uspeh", "Student je uspešno obnovio " + godina + ". godinu studija.");
                    loadStudentProfile();
                } else {
                    showAlert("Greška", "Došlo je do greške pri obnovi godine.");
                }
            } catch (RuntimeException e) {
                showAlert("Greška", e.getMessage());
            } catch (Exception e) {
                showAlert("Greška", "Greška pri obnovi godine: " + e.getMessage());
            }
        });
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

                // tekst koji se ne ponavlja - ime, indeks, program
                Map<String, Object> parameters = new HashMap<>();

                // iterativni deo - lista predmeta i ocene
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
