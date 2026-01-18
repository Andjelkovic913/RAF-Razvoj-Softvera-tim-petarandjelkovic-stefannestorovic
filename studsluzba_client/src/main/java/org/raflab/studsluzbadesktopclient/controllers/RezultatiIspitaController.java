package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.raflab.studsluzbadesktopclient.services.ReportService;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzba.dtos.IspitDTO;
import org.raflab.studsluzba.dtos.IspitniRokDTO;
import org.raflab.studsluzba.dtos.IzlazakNaIspitDTO;
import org.raflab.studsluzba.dtos.PrijavljeniStudentDTO;
import org.raflab.studsluzbadesktopclient.services.IspitService;
import org.raflab.studsluzbadesktopclient.services.IspitniRokService;
import org.raflab.studsluzbadesktopclient.services.PrijavaIspitaService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RezultatiIspitaController {

    private final IspitService ispitService;
    private final IspitniRokService ispitniRokService;
    private final PrijavaIspitaService prijavaIspitaService;
    private final ReportService reportService;
    private final MainView mainView;

    private static IspitDTO preSelectedIspit;
    private IspitDTO selectedIspit = null;
    private IspitniRokDTO selectedIspitniRok = null;

    @FXML private ComboBox<IspitniRokDTO> ispitniRokComboBox;
    @FXML private ComboBox<IspitDTO> ispitComboBox;
    @FXML private ComboBox<PrijavljeniStudentDTO> studentCb;
    @FXML private TextField predispitniPoeniTf;
    @FXML private TextField ispitniPoeniTf;
    @FXML private ComboBox<Integer> ocenaCb;
    @FXML private CheckBox ponistioCb;
    @FXML private TextField napomenaTf;
    @FXML private TableView<IzlazakNaIspitDTO> rezultatiTable;

    public RezultatiIspitaController(IspitService ispitService, IspitniRokService ispitniRokService,
                                      PrijavaIspitaService prijavaIspitaService, ReportService reportService,
                                      MainView mainView) {
        this.ispitService = ispitService;
        this.ispitniRokService = ispitniRokService;
        this.prijavaIspitaService = prijavaIspitaService;
        this.reportService = reportService;
        this.mainView = mainView;
    }

    public static void setSelectedIspit(IspitDTO ispit) {
        preSelectedIspit = ispit;
    }

    @FXML
    public void initialize() {
        setupIspitniRokComboBox();
        setupIspitComboBox();
        loadIspitniRokovi();
        
        ocenaCb.setItems(FXCollections.observableArrayList(5, 6, 7, 8, 9, 10));
        
        if (preSelectedIspit != null && preSelectedIspit.getIspitniRokId() != null) {
            autoSelectPreSelectedIspit();
        }
    }

    private void setupIspitniRokComboBox() {
        ispitniRokComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(IspitniRokDTO rok) {
                if (rok == null) return "";
                return rok.getTipRoka() + " (" + rok.getDatumPocetka() + " - " + rok.getDatumZavrsetka() + ")";
            }

            @Override
            public IspitniRokDTO fromString(String s) {
                return null;
            }
        });
    }

    private void setupIspitComboBox() {
        ispitComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(IspitDTO ispit) {
                if (ispit == null) return "";
                return ispit.getPredmetNaziv() + " - " + ispit.getDatumOdrzavanja();
            }

            @Override
            public IspitDTO fromString(String s) {
                return null;
            }
        });
    }

    private void loadIspitniRokovi() {
        try {
            List<IspitniRokDTO> rokovi = ispitniRokService.getAll();
            ispitniRokComboBox.setItems(FXCollections.observableArrayList(rokovi));
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju ispitnih rokova: " + e.getMessage());
        }
    }

    private void loadIspiti() {
        if (selectedIspitniRok == null) {
            ispitComboBox.setItems(FXCollections.observableArrayList());
            return;
        }
        
        try {
            List<IspitDTO> ispiti = ispitService.getByIspitniRok(selectedIspitniRok.getId());
            ispitComboBox.setItems(FXCollections.observableArrayList(ispiti));
            
            if (preSelectedIspit != null) {
                for (IspitDTO ispit : ispiti) {
                    if (ispit.getId().equals(preSelectedIspit.getId())) {
                        ispitComboBox.setValue(ispit);
                        selectedIspit = ispit;
                        loadPrijavljeniStudenti();
                        loadRezultati();
                        preSelectedIspit = null;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju ispita: " + e.getMessage());
        }
    }

    private void autoSelectPreSelectedIspit() {
        List<IspitniRokDTO> rokovi = ispitniRokComboBox.getItems();
        for (IspitniRokDTO rok : rokovi) {
            if (rok.getId().equals(preSelectedIspit.getIspitniRokId())) {
                ispitniRokComboBox.setValue(rok);
                selectedIspitniRok = rok;
                loadIspiti();
                break;
            }
        }
    }

    @FXML
    public void handleIspitniRokSelected(ActionEvent event) {
        selectedIspitniRok = ispitniRokComboBox.getValue();
        ispitComboBox.setValue(null);
        selectedIspit = null;
        loadIspiti();
        studentCb.setItems(FXCollections.observableArrayList());
        rezultatiTable.setItems(FXCollections.observableArrayList());
    }

    @FXML
    public void handleIspitSelected(ActionEvent event) {
        selectedIspit = ispitComboBox.getValue();
        if (selectedIspit != null) {
            loadPrijavljeniStudenti();
            loadRezultati();
        } else {
            studentCb.setItems(FXCollections.observableArrayList());
            rezultatiTable.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    public void handleClearIspitniRok(ActionEvent event) {
        ispitniRokComboBox.setValue(null);
        selectedIspitniRok = null;
        ispitComboBox.setValue(null);
        ispitComboBox.setItems(FXCollections.observableArrayList());
        selectedIspit = null;
        studentCb.setItems(FXCollections.observableArrayList());
        rezultatiTable.setItems(FXCollections.observableArrayList());
    }

    @FXML
    public void handleClearIspit(ActionEvent event) {
        ispitComboBox.setValue(null);
        selectedIspit = null;
        studentCb.setItems(FXCollections.observableArrayList());
        rezultatiTable.setItems(FXCollections.observableArrayList());
    }

    private void loadPrijavljeniStudenti() {
        if (selectedIspit == null) return;
        
        try {
            List<PrijavljeniStudentDTO> prijavljeni = ispitService.getPrijavljeniStudenti(selectedIspit.getId());
            studentCb.setItems(FXCollections.observableArrayList(prijavljeni));
            
            studentCb.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(PrijavljeniStudentDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : 
                            item.getFullName() + " (" + item.getFullIndexDisplay() + ") - Predispitne: " + 
                            (item.getPredispitneObaveze() != null ? item.getPredispitneObaveze() : 0));
                }
            });
            studentCb.setButtonCell(studentCb.getCellFactory().call(null));
            
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju prijavljenih studenata: " + e.getMessage());
        }
    }

    private void loadRezultati() {
        if (selectedIspit == null) return;
        
        try {
            List<IzlazakNaIspitDTO> rezultati = prijavaIspitaService.getRezultatiByIspit(selectedIspit.getId(), 0, 100);
            
            // sortiranje po programu, godini, broju
            rezultati.sort(Comparator
                    .comparing((IzlazakNaIspitDTO r) -> r.getStudProgramOznaka() != null ? r.getStudProgramOznaka() : "")
                    .thenComparing(r -> r.getGodinaIndeksa() != null ? r.getGodinaIndeksa() : 0)
                    .thenComparing(r -> r.getBrojIndeksa() != null ? r.getBrojIndeksa() : 0));
            
            rezultatiTable.setItems(FXCollections.observableArrayList(rezultati));
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju rezultata: " + e.getMessage());
        }
    }

    @FXML
    public void handleSaveResult(ActionEvent event) {
        if (!validateForm()) return;

        try {
            PrijavljeniStudentDTO student = studentCb.getValue();
            
            if (student.getPrijavaId() == null) {
                showAlert("Greška", "Nedostaje prijavaId za izabranog studenta. Student možda nije pravilno prijavljen na ispit.");
                return;
            }
            
            IzlazakNaIspitDTO dto = new IzlazakNaIspitDTO();
            dto.setPrijavaIspitaId(student.getPrijavaId());
            dto.setIspitId(selectedIspit.getId());
            dto.setSlusaPredmetId(student.getSlusaPredmetId());
            dto.setOsvojeniPredispitniPoeni(Integer.parseInt(predispitniPoeniTf.getText().trim()));
            dto.setOsvojeniIspitniPoeni(Integer.parseInt(ispitniPoeniTf.getText().trim()));
            dto.setOcena(ocenaCb.getValue());
            dto.setPonistio(ponistioCb.isSelected());
            dto.setNapomena(napomenaTf.getText().trim());

            Long id = prijavaIspitaService.unosRezultata(dto);
            if (id != null) {
                showAlert("Uspeh", "Rezultat je uspešno sačuvan.");
                handleClearForm(null);
                loadRezultati();
            }
        } catch (NumberFormatException e) {
            showAlert("Greška", "Poeni moraju biti brojevi.");
        } catch (Exception e) {
            showAlert("Greška", e.getMessage());
        }
    }

    @FXML
    public void handlePrintProtocol(ActionEvent event) {
        if (selectedIspit == null) {
            showAlert("Greška", "Nije izabran ispit.");
            return;
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("predmetNaziv", selectedIspit.getPredmetNaziv());
            parameters.put("datumIspita", selectedIspit.getDatumOdrzavanja() != null ? 
                    selectedIspit.getDatumOdrzavanja().toString() : "");
            parameters.put("ispitniRok", selectedIspitniRok != null ? selectedIspitniRok.getTipRoka() : "");

            List<IzlazakNaIspitDTO> rezultati = new ArrayList<>(rezultatiTable.getItems());
            reportService.showReport("zapisnikIspita", parameters, rezultati);

        } catch (Exception e) {
            showAlert("Greška", "Greška pri generisanju izveštaja: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClearForm(ActionEvent event) {
        studentCb.setValue(null);
        predispitniPoeniTf.clear();
        ispitniPoeniTf.clear();
        ocenaCb.setValue(null);
        ponistioCb.setSelected(false);
        napomenaTf.clear();
    }

    @FXML
    public void handleBackToIspiti(ActionEvent event) {
        mainView.changeRoot("ispiti");
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        loadPrijavljeniStudenti();
        loadRezultati();
    }

    private boolean validateForm() {
        if (selectedIspit == null) {
            showAlert("Greška", "Nije izabran ispit.");
            return false;
        }
        if (studentCb.getValue() == null) {
            showAlert("Greška", "Izaberite studenta.");
            return false;
        }
        if (predispitniPoeniTf.getText().trim().isEmpty()) {
            showAlert("Greška", "Unesite predispitne poene.");
            return false;
        }
        if (ispitniPoeniTf.getText().trim().isEmpty()) {
            showAlert("Greška", "Unesite ispitne poene.");
            return false;
        }
        if (ocenaCb.getValue() == null) {
            showAlert("Greška", "Izaberite ocenu.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
