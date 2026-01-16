package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.coder.CoderFactory;
import org.raflab.studsluzbadesktopclient.coder.CoderType;
import org.raflab.studsluzbadesktopclient.coder.SimpleCode;
import org.raflab.studsluzbadesktopclient.coder.SimpleCode;
import org.raflab.studsluzba.dtos.StudentDTO;
import org.raflab.studsluzba.dtos.StudentIndeksRequest;
import org.raflab.studsluzba.dtos.StudentKontaktPodaciRequest;
import org.raflab.studsluzba.dtos.StudentReportDTO;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;
import javafx.application.Platform;

import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
public class StudentController {

    private final StudentService studentService;
    private final CoderFactory coderFactory;

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
    private DatePicker datumAktivacijeDp;
    @FXML
    ComboBox<SimpleCode> mestoRodjenjaCb;
    @FXML
    private TextField emailPrivatniTf;
    @FXML
    private TextField emailFakultetTf;
    @FXML
    TextField brojTelefonaTf;
    @FXML
    TextField adresaTf;
    @FXML
    ComboBox<SimpleCode> mestoStanovanjaCb;
    @FXML
    ComboBox<SimpleCode> drzavaRodjenjaCb;
    @FXML
    ComboBox<SimpleCode> drzavljanstvoCb;
    @FXML
    TextField nacionalnostTf;
    @FXML
    TextField brojLicneKarteTf;
    @FXML
    TextField godinaUpisaTf;
    @FXML
    TextField brojIndeksaTf;
    @FXML
    TextField godinaIndeksaTf;
    @FXML
    TextField uspehSrednjaSkolaTf;
    @FXML
    TextField uspehPrijemniTf;
    @FXML
    TextField studentIdTf;
    @FXML
    TextField studentIdIndeksTf;
    @FXML
    ComboBox<SimpleCode> studProgramCb;
    @FXML
    ComboBox<String> nacinFinansiranjaIndeksCb;
    @FXML
    TabPane studentTabPane;
    @FXML
    Button saveOsnovniPodaciBtn;
    @FXML
    Button saveKontaktPodaciBtn;
    @FXML
    Button saveIndeksBtn;


    public StudentController(StudentService studentService, CoderFactory coderFactory) {
        this.studentService = studentService;
        this.coderFactory = coderFactory;
    }

    @FXML
    public void initialize(){
        drzavaRodjenjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.DRZAVA).getCodes()));
        drzavaRodjenjaCb.setValue(new SimpleCode("Serbia"));

        drzavljanstvoCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.DRZAVA).getCodes()));
        drzavljanstvoCb.setValue(new SimpleCode("Serbia"));

        mestoRodjenjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.MESTO).getCodes()));
        mestoRodjenjaCb.setValue(new SimpleCode("Beograd"));

        mestoStanovanjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.MESTO).getCodes()));
        mestoStanovanjaCb.setValue(new SimpleCode("Beograd"));

        studProgramCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.OZNAKA_PLANA).getCodes()));

        nacinFinansiranjaIndeksCb.setItems(FXCollections.observableArrayList("Budžet", "Samofinansiranje"));

        if (studentTabPane != null) {
            studentTabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
                updateButtonVisibility(newVal.intValue());
            });
            updateButtonVisibility(0);
        }
    }

    private void updateButtonVisibility(int tabIndex) {
        if (saveOsnovniPodaciBtn == null || saveKontaktPodaciBtn == null || saveIndeksBtn == null) {
            return;
        }
        
        saveOsnovniPodaciBtn.setVisible(false);
        saveOsnovniPodaciBtn.setManaged(false);
        saveKontaktPodaciBtn.setVisible(false);
        saveKontaktPodaciBtn.setManaged(false);
        saveIndeksBtn.setVisible(false);
        saveIndeksBtn.setManaged(false);

        switch (tabIndex) {
            case 0:
                saveOsnovniPodaciBtn.setVisible(true);
                saveOsnovniPodaciBtn.setManaged(true);
                break;
            case 1:
                saveIndeksBtn.setVisible(true);
                saveIndeksBtn.setManaged(true);
                break;
            case 2:
                saveKontaktPodaciBtn.setVisible(true);
                saveKontaktPodaciBtn.setManaged(true);
                break;
        }
    }

    public void handleOpenModalSrednjeSkole(ActionEvent ae) {
        //mainViewManager.openModal("addSrednjaSkola");
    }

    public void handleOpenModalVisokoskolskeUstanove(ActionEvent ae) {
        //mainViewManager.openModal("addVisaUstanovaForStudent");
    }

    public void handleSaveOsnovniPodaci(ActionEvent event) {
        // Validate required fields
        if (imeTf.getText() == null || imeTf.getText().trim().isEmpty()) {
            showAlert("Validacija", "Ime je obavezno polje.");
            return;
        }
        if (!imeTf.getText().matches("[a-zA-ZčćžšđČĆŽŠĐ\\s]+")) {
            showAlert("Validacija", "Ime može sadržati samo slova.");
            return;
        }
        
        if (prezimeTf.getText() == null || prezimeTf.getText().trim().isEmpty()) {
            showAlert("Validacija", "Prezime je obavezno polje.");
            return;
        }
        if (!prezimeTf.getText().matches("[a-zA-ZčćžšđČĆŽŠĐ\\s]+")) {
            showAlert("Validacija", "Prezime može sadržati samo slova.");
            return;
        }
        
        if (srednjeImeTf.getText() != null && !srednjeImeTf.getText().trim().isEmpty()) {
            if (!srednjeImeTf.getText().matches("[a-zA-ZčćžšđČĆŽŠĐ\\s]+")) {
                showAlert("Validacija", "Srednje ime može sadržati samo slova.");
                return;
            }
        }
        
        if (!muski.isSelected() && !zenski.isSelected()) {
            showAlert("Validacija", "Pol je obavezno polje.");
            return;
        }
        
        if (jmbgTf.getText() == null || jmbgTf.getText().trim().isEmpty()) {
            showAlert("Validacija", "JMBG je obavezno polje.");
            return;
        }
        if (!jmbgTf.getText().matches("\\d{13}")) {
            showAlert("Validacija", "JMBG mora sadržati tačno 13 cifara.");
            return;
        }
        
        if (datumRodjenjaDp.getValue() == null) {
            showAlert("Validacija", "Datum rođenja je obavezno polje.");
            return;
        }
        
        if (mestoRodjenjaCb.getValue() == null) {
            showAlert("Validacija", "Mesto rođenja je obavezno polje.");
            return;
        }
        
        if (drzavaRodjenjaCb.getValue() == null) {
            showAlert("Validacija", "Država rođenja je obavezno polje.");
            return;
        }
        
        if (drzavljanstvoCb.getValue() == null) {
            showAlert("Validacija", "Državljanstvo je obavezno polje.");
            return;
        }
        
        if (nacionalnostTf.getText() != null && !nacionalnostTf.getText().trim().isEmpty()) {
            if (!nacionalnostTf.getText().matches("[a-zA-ZčćžšđČĆŽŠĐ\\s]+")) {
                showAlert("Validacija", "Nacionalnost može sadržati samo slova.");
                return;
            }
        }
        
        if (brojLicneKarteTf.getText() == null || brojLicneKarteTf.getText().trim().isEmpty()) {
            showAlert("Validacija", "Broj lične karte je obavezno polje.");
            return;
        }
        if (!brojLicneKarteTf.getText().matches("\\d{9}")) {
            showAlert("Validacija", "Broj lične karte mora sadržati tačno 9 cifara.");
            return;
        }
        
        if (godinaUpisaTf.getText() != null && !godinaUpisaTf.getText().trim().isEmpty()) {
            if (!godinaUpisaTf.getText().matches("\\d{4}")) {
                showAlert("Validacija", "Godina upisa mora biti četvorocifreni broj.");
                return;
            }
            int godina = Integer.parseInt(godinaUpisaTf.getText());
            if (godina < 1900 || godina > 2010) {
                showAlert("Validacija", "Godina upisa mora biti između 1900 i 2010.");
                return;
            }
        }
        
        StudentDTO studentDTO = new StudentDTO();

        studentDTO.setIme(imeTf.getText().trim());
        studentDTO.setPrezime(prezimeTf.getText().trim());
        studentDTO.setSrednjeIme(srednjeImeTf.getText() != null ? srednjeImeTf.getText().trim() : null);
        studentDTO.setPol(muski.isSelected() ? "M" : "Z");
        studentDTO.setJmbg(jmbgTf.getText().trim());
        
        studentDTO.setDatumRodjenja(
                Date.from(datumRodjenjaDp.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        
        studentDTO.setMestoRodjenja(mestoRodjenjaCb.getValue().getCode());
        studentDTO.setDrzavaRodjenja(drzavaRodjenjaCb.getValue().getCode());
        studentDTO.setDrzavljanstvo(drzavljanstvoCb.getValue().getCode());
        
        studentDTO.setNacionalnost(nacionalnostTf.getText() != null ? nacionalnostTf.getText().trim() : null);
        studentDTO.setLicnaKarta(brojLicneKarteTf.getText().trim());

        try {
            Integer result = studentService.saveStudent(studentDTO);
            
            if (result != null) {
                System.out.println("Osnovni podaci studenta uspešno sačuvani sa ID: " + result);
                studentIdTf.setText(result.toString());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Uspešno");
                alert.setHeaderText("Student je kreiran");
                alert.setContentText("Student je kreiran sa ID: " + result + "\nID je automatski unet u Kontakt podaci tab.");
                alert.showAndWait();
                resetForm();
            } else {
                System.err.println("Greška pri dodavanju studenta");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greška");
                alert.setHeaderText("Greška pri kreiranju studenta");
                alert.setContentText("Došlo je do greške pri kreiranju studenta. Pokušajte ponovo.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.err.println("Greška pri dodavanju studenta: " + e.getMessage());
            String errorMessage = e.getMessage();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            
            if (errorMessage != null && errorMessage.contains("JMBG")) {
                alert.setHeaderText("Duplikat JMBG");
                alert.setContentText("Student sa unetim JMBG-om već postoji u sistemu.\nMolimo proverite unete podatke.");
            } else if (errorMessage != null && errorMessage.contains("lične karte")) {
                alert.setHeaderText("Duplikat broja lične karte");
                alert.setContentText("Student sa unetim brojem lične karte već postoji u sistemu.\nMolimo proverite unete podatke.");
            } else if (errorMessage != null && errorMessage.contains("email")) {
                alert.setHeaderText("Duplikat email adrese");
                alert.setContentText("Student sa unetom email adresom već postoji u sistemu.\nMolimo proverite unete podatke.");
            } else {
                alert.setHeaderText("Greška pri kreiranju studenta");
                alert.setContentText("Došlo je do greške: " + (errorMessage != null ? errorMessage : "Nepoznata greška"));
            }
            
            alert.showAndWait();
        }
    }

    public void handleSaveKontaktPodaci(ActionEvent event) {
        if (studentIdTf.getText() == null || studentIdTf.getText().trim().isEmpty()) {
            showAlert("Validacija", "ID studenta je obavezno polje.");
            return;
        }

        Long studentId;
        try {
            studentId = Long.parseLong(studentIdTf.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Validacija", "ID studenta mora biti broj.");
            return;
        }
        
        // Validate email addresses
        if (emailPrivatniTf.getText() != null && !emailPrivatniTf.getText().trim().isEmpty()) {
            if (!emailPrivatniTf.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                showAlert("Validacija", "Privatni email nije u ispravnom formatu.");
                return;
            }
        }
        
        if (emailFakultetTf.getText() != null && !emailFakultetTf.getText().trim().isEmpty()) {
            if (!emailFakultetTf.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                showAlert("Validacija", "Fakultetski email nije u ispravnom formatu.");
                return;
            }
        }
        
        // Validate phone number
        if (brojTelefonaTf.getText() != null && !brojTelefonaTf.getText().trim().isEmpty()) {
            if (!brojTelefonaTf.getText().matches("^[0-9+\\-\\s()]+$")) {
                showAlert("Validacija", "Broj telefona može sadržati samo cifre, +, -, razmake i zagrade.");
                return;
            }
        }
        
        // Validate address - must contain both letters and numbers
        if (adresaTf.getText() != null && !adresaTf.getText().trim().isEmpty()) {
            String adresa = adresaTf.getText().trim();
            if (!adresa.matches(".*[a-zA-ZčćžšđČĆŽŠĐ].*")) {
                showAlert("Validacija", "Adresa mora sadržati slova.");
                return;
            }
        }

        StudentKontaktPodaciRequest kontaktPodaci = new StudentKontaktPodaciRequest();
        kontaktPodaci.setStudentId(studentId);
        kontaktPodaci.setEmailPrivatni(emailPrivatniTf.getText() != null ? emailPrivatniTf.getText().trim() : null);
        kontaktPodaci.setEmailFakultetski(emailFakultetTf.getText() != null ? emailFakultetTf.getText().trim() : null);
        kontaktPodaci.setBrojTelefonaMobilni(brojTelefonaTf.getText() != null ? brojTelefonaTf.getText().trim() : null);
        kontaktPodaci.setAdresa(adresaTf.getText() != null ? adresaTf.getText().trim() : null);

        if (mestoStanovanjaCb.getValue() != null) {
            kontaktPodaci.setMestoStanovanja(mestoStanovanjaCb.getValue().getCode());
        }

        Boolean result = studentService.updateStudentKontaktPodaci(kontaktPodaci);

        if (result) {
            System.out.println("Kontakt podaci studenta uspešno ažurirani");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspešno");
            alert.setHeaderText("Kontakt podaci sačuvani");
            alert.setContentText("Kontakt podaci su uspešno sačuvani za studenta sa ID: " + studentId);
            alert.showAndWait();
            studentIdTf.clear();
            emailPrivatniTf.clear();
            emailFakultetTf.clear();
            brojTelefonaTf.clear();
            adresaTf.clear();
            mestoStanovanjaCb.setValue(null);
        } else {
            System.err.println("Greška pri ažuriranju kontakt podataka");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Greška pri čuvanju kontakt podataka");
            alert.setContentText("Došlo je do greške pri čuvanju kontakt podataka. Proverite da li student sa ID " + studentId + " postoji.");
            alert.showAndWait();
        }
    }

    public void handleSaveIndeks(ActionEvent event) {
        if (studentIdIndeksTf.getText() == null || studentIdIndeksTf.getText().trim().isEmpty()) {
            showAlert("Validacija", "ID studenta je obavezno polje.");
            return;
        }

        Long studentId;
        try {
            studentId = Long.parseLong(studentIdIndeksTf.getText().trim());
            if (studentId <= 0) {
                showAlert("Validacija", "ID studenta mora biti pozitivan broj.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Validacija", "ID studenta mora biti broj.");
            return;
        }

        if (godinaIndeksaTf.getText() == null || godinaIndeksaTf.getText().trim().isEmpty()) {
            showAlert("Validacija", "Godina indeksa je obavezno polje.");
            return;
        }
        
        if (!godinaIndeksaTf.getText().matches("\\d{4}")) {
            showAlert("Validacija", "Godina indeksa mora biti četvorocifreni broj.");
            return;
        }

        int godina;
        try {
            godina = Integer.parseInt(godinaIndeksaTf.getText().trim());
            if (godina < 2000 || godina > 2010) {
                showAlert("Validacija", "Godina indeksa mora biti između 2000 i 2010.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Validacija", "Godina indeksa mora biti broj.");
            return;
        }

        if (studProgramCb.getValue() == null) {
            showAlert("Validacija", "Studijski program je obavezno polje.");
            return;
        }

        if (nacinFinansiranjaIndeksCb.getValue() == null) {
            showAlert("Validacija", "Način finansiranja je obavezno polje.");
            return;
        }

        if (datumAktivacijeDp.getValue() == null) {
            showAlert("Validacija", "Datum aktivacije je obavezno polje.");
            return;
        }

        StudentIndeksRequest indeksRequest = new StudentIndeksRequest();
        indeksRequest.setStudentId(studentId);
        indeksRequest.setGodina(godina);
        indeksRequest.setStudProgramOznaka(studProgramCb.getValue().getCode());
        indeksRequest.setNacinFinansiranja(nacinFinansiranjaIndeksCb.getValue());
        indeksRequest.setVaziOd(datumAktivacijeDp.getValue());
        indeksRequest.setAktivan(false);

        try {
            Long indeksId = studentService.saveStudentIndeks(indeksRequest);

            if (indeksId != null) {
                Boolean aktiviranResult = studentService.aktivirajIndeks(indeksId);
                
                if (aktiviranResult) {
                    System.out.println("Indeks uspešno kreiran i aktiviran sa ID: " + indeksId);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Uspešno");
                    alert.setHeaderText("Indeks kreiran i aktiviran");
                    alert.setContentText("Indeks je uspešno kreiran sa ID: " + indeksId + 
                            "\nIndeks je postavljen kao jedini aktivan za studenta sa ID: " + studentId);
                    alert.showAndWait();
                    
                    studentIdIndeksTf.clear();
                    godinaIndeksaTf.clear();
                    studProgramCb.setValue(null);
                    nacinFinansiranjaIndeksCb.setValue(null);
                    datumAktivacijeDp.setValue(null);
                } else {
                    System.err.println("Indeks kreiran ali greška pri aktivaciji");
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Upozorenje");
                    alert.setHeaderText("Indeks kreiran ali nije aktiviran");
                    alert.setContentText("Indeks je kreiran sa ID: " + indeksId + " ali došlo je do greške pri aktivaciji.");
                    alert.showAndWait();
                }
            } else {
                System.err.println("Greška pri kreiranju indeksa");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greška");
                alert.setHeaderText("Greška pri kreiranju indeksa");
                alert.setContentText("Došlo je do greške pri kreiranju indeksa. Proverite da li student sa ID " + studentId + " postoji.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.err.println("Greška pri kreiranju indeksa: " + e.getMessage());
            String errorMessage = e.getMessage();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Greška pri kreiranju indeksa");
            
            if (errorMessage != null && errorMessage.contains("vec ima indeks")) {
                alert.setContentText("Student već ima indeks u ovoj godini ili na ovom studijskom programu.\n" + errorMessage);
            } else {
                alert.setContentText("Došlo je do greške: " + (errorMessage != null ? errorMessage : "Nepoznata greška"));
            }
            
            alert.showAndWait();
        }
    }

    public void handleIzvestaj() {
        // Reactive async pattern using Mono.subscribe() like in SearchStudentController
        studentService.getStudentsForReportAsync()
                .subscribe(
                        studenti -> {
                            // This executes when data arrives
                            try {
                                JasperReport report = JasperCompileManager.compileReport(
                                        MainView.class.getResourceAsStream("/reports/sviStudenti.jrxml")
                                );

                                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(studenti);
                                JasperPrint jp = JasperFillManager.fillReport(report, new HashMap<>(), dataSource);

                                JasperExportManager.exportReportToPdfFile(jp, "sviStudenti.pdf");

                                Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Izveštaj");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Izveštaj je uspešno generisan: sviStudenti.pdf");
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
                        error -> {
                            // This executes on error
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Greška");
                                alert.setHeaderText(null);
                                alert.setContentText("Greška pri preuzimanju podataka: " + error.getMessage());
                                alert.showAndWait();
                            });
                        }
                );
        System.out.println("Izveštaj se generiše u pozadini...");
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void resetForm() {
        imeTf.clear();
        prezimeTf.clear();
        srednjeImeTf.clear();

        muski.setSelected(false);
        zenski.setSelected(false);

        jmbgTf.clear();
        datumRodjenjaDp.setValue(null);
        datumAktivacijeDp.setValue(null);

        mestoRodjenjaCb.setValue(null);
        emailPrivatniTf.clear();
        emailFakultetTf.clear();
        brojTelefonaTf.clear();
        adresaTf.clear();

        mestoStanovanjaCb.setValue(null);
        drzavaRodjenjaCb.setValue(null);
        drzavljanstvoCb.setValue(null);

        nacionalnostTf.clear();
        brojLicneKarteTf.clear();
        godinaUpisaTf.clear();
        if (brojIndeksaTf != null) brojIndeksaTf.clear();
        godinaIndeksaTf.clear();
        uspehSrednjaSkolaTf.clear();
        uspehPrijemniTf.clear();
    }


}
