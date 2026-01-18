package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzba.dtos.SrednjaSkolaDTO;
import org.raflab.studsluzba.dtos.StudentBySrednjaSkolaDTO;
import org.raflab.studsluzba.dtos.StudentDTO;
import org.raflab.studsluzba.dtos.StudProgramDTO;
import org.raflab.studsluzbadesktopclient.services.NavigationHistoryService;
import org.raflab.studsluzbadesktopclient.services.SrednjaSkolaService;
import org.raflab.studsluzbadesktopclient.services.StudProgramService;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchStudentController {

    private static final String VIEW_NAME = "searchStudent";
    
    private final StudentService studentService;
    private final SrednjaSkolaService srednjaSkolaService;
    private final StudProgramService studProgramService;
    private final MainView mainView;
    private final NavigationHistoryService navigationHistoryService;
    
    @FXML private TabPane mainTabPane;

    @FXML
    private TextField imeStudentaTf;

    @FXML
    private TextField prezimeStudentaTf;

    @FXML
    private TableView<StudentDTO> tabelaStudentiPoImenu;

    @FXML
    private TextField brojIndeksaTf;

    @FXML
    private TextField godinaIndeksaTf;

    @FXML
    private TableView<StudentDTO> tabelaStudentiPoIndeksu;

    @FXML
    private ComboBox<SrednjaSkolaDTO> srednjaSkolaCb;

    @FXML
    private ComboBox<StudProgramDTO> smerCb;

    @FXML
    private TableView<StudentBySrednjaSkolaDTO> tabelaStudentiPoSrednjojSkoli;

    @FXML
    private TableColumn<StudentDTO, Void> actionColumnName;

    @FXML
    private TableColumn<StudentDTO, Void> actionColumnIndex;

    @FXML
    private TableColumn<StudentBySrednjaSkolaDTO, Void> actionColumnSchool;

    @FXML private Button searchByNameBtn;
    @FXML private Button searchByIndexBtn;
    @FXML private Button searchByHighSchoolBtn;

    private static final String LOADING_TEXT = "Učitavanje...";
    private static final String SEARCH_TEXT = "Pretraga";

    public SearchStudentController(StudentService studentService, SrednjaSkolaService srednjaSkolaService,
                                   StudProgramService studProgramService, MainView mainView, 
                                   NavigationHistoryService navigationHistoryService) {
        this.studentService = studentService;
        this.srednjaSkolaService = srednjaSkolaService;
        this.studProgramService = studProgramService;
        this.mainView = mainView;
        this.navigationHistoryService = navigationHistoryService;
    }

    @FXML
    public void initialize() {
        setupTabChangeTracking();
        loadSrednjeSkole();
        setupSmerComboBox();
        loadStudijskiProgrami();
        setupActionColumns();
    }

    private void setupSmerComboBox() {
        smerCb.setConverter(new StringConverter<>() {
            @Override
            public String toString(StudProgramDTO smer) {
                if (smer == null) return "";
                return smer.getOznaka() + " - " + smer.getNaziv();
            }

            @Override
            public StudProgramDTO fromString(String s) {
                return null;
            }
        });
    }
    
    private void setupTabChangeTracking() {
        if (mainTabPane != null) {
            int initialTab = mainTabPane.getSelectionModel().getSelectedIndex();
            if (initialTab >= 0) {
                navigationHistoryService.registerTabChange(VIEW_NAME, initialTab);
            }
            
            // prati promene tabova
            mainTabPane.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldIndex, newIndex) -> {
                    if (!navigationHistoryService.isNavigating()) {
                        navigationHistoryService.registerTabChange(VIEW_NAME, newIndex.intValue());
                    }
                }
            );
        }
    }

    private void setupActionColumns() {
        if (actionColumnName != null) {
            actionColumnName.setCellFactory(createButtonCellFactory());
        }
        
        if (actionColumnIndex != null) {
            actionColumnIndex.setCellFactory(createButtonCellFactory());
        }
        
        if (actionColumnSchool != null) {
            actionColumnSchool.setCellFactory(createButtonCellFactoryForSchool());
        }
    }

    private Callback<TableColumn<StudentDTO, Void>, TableCell<StudentDTO, Void>> createButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<StudentDTO, Void> call(final TableColumn<StudentDTO, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Prikaži profil");
                    {
                        btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 11px;");
                        btn.setOnAction(event -> {
                            StudentDTO student = getTableView().getItems().get(getIndex());
                            openStudentProfile(student.getIdStudentPodaci(), student.getIdIndeks());
                        });
                    }
                    // prikazuje dugme ako red nije prazan
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
    }

    private Callback<TableColumn<StudentBySrednjaSkolaDTO, Void>, TableCell<StudentBySrednjaSkolaDTO, Void>> createButtonCellFactoryForSchool() {
        return new Callback<>() {
            @Override
            public TableCell<StudentBySrednjaSkolaDTO, Void> call(final TableColumn<StudentBySrednjaSkolaDTO, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Prikaži profil");
                    {
                        btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 11px;");
                        btn.setOnAction(event -> {
                            StudentBySrednjaSkolaDTO student = getTableView().getItems().get(getIndex());
                            openStudentProfile(student.getIdStudentPodaci(), student.getIdIndeks());
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
    }

    private void openStudentProfile(Long studentPodaciId, Long indeksId) {
        if (studentPodaciId == null) {
            showAlert("Greška", "Nije moguće otvoriti profil - nedostaje ID studenta.");
            return;
        }
        
        mainView.cacheCurrentRoot("searchStudent");
        
        StudentProfileController.setStudentIds(studentPodaciId, indeksId);
        mainView.changeRoot("studentProfile");
    }

    private void loadSrednjeSkole() {
        try {
            List<SrednjaSkolaDTO> srednjeSkole = srednjaSkolaService.getAllSrednjeSkole();
            if (srednjeSkole != null && !srednjeSkole.isEmpty()) {
                srednjaSkolaCb.setItems(FXCollections.observableArrayList(srednjeSkole));
            } else {
                showAlert("Upozorenje", "Nije moguce ucitati srednje skole sa servera.");
            }
        } catch (Exception e) {
            showAlert("Greska", "Greska pri ucitavanju srednjih skola: " + e.getMessage());
        }
    }

    private void loadStudijskiProgrami() {
        try {
            List<StudProgramDTO> programi = studProgramService.getSudijskiProgramiSorted();
            if (programi != null && !programi.isEmpty()) {
                smerCb.setItems(FXCollections.observableArrayList(programi));
            }
        } catch (Exception e) {
            showAlert("Greska", "Greska pri ucitavanju studijskih programa: " + e.getMessage());
        }
    }

    public void handleSearchByName(ActionEvent actionEvent) {
        String ime = imeStudentaTf.getText();
        String prezime = prezimeStudentaTf.getText();

        setButtonLoading(searchByNameBtn, true);

        studentService.searchByNameAndSurnameAsync(ime, prezime)
            .subscribe(
                studenti -> Platform.runLater(() -> {
                    setButtonLoading(searchByNameBtn, false);
                    if (studenti != null) {
                        tabelaStudentiPoImenu.setItems(FXCollections.observableArrayList(studenti));
                    } else {
                        showAlert("Greska", "Doslo je do greske pri pretrazi studenata.");
                    }
                }),
                error -> Platform.runLater(() -> {
                    setButtonLoading(searchByNameBtn, false);
                    showAlert("Greska", "Doslo je do greske: " + error.getMessage());
                })
            );
    }

    public void handleSearchByIndex(ActionEvent actionEvent) {
        String brojText = brojIndeksaTf.getText();
        String godinaText = godinaIndeksaTf.getText();
        StudProgramDTO selectedSmer = smerCb.getValue();
        String smer = selectedSmer != null ? selectedSmer.getOznaka() : null;

        Integer broj = null;
        Integer godina = null;

        if (brojText != null && !brojText.trim().isEmpty()) {
            try {
                broj = Integer.parseInt(brojText.trim());
            } catch (NumberFormatException e) {
                showAlert("Greska", "Broj indeksa mora biti broj.");
                return;
            }
        }

        if (godinaText != null && !godinaText.trim().isEmpty()) {
            try {
                godina = Integer.parseInt(godinaText.trim());
            } catch (NumberFormatException e) {
                showAlert("Greska", "Godina indeksa mora biti broj.");
                return;
            }
        }

        if (broj == null && godina == null) {
            showAlert("Upozorenje", "Unesite bar jedan parametar za pretragu.");
            return;
        }

        setButtonLoading(searchByIndexBtn, true);
        final Integer finalBroj = broj;
        final Integer finalGodina = godina;

        studentService.searchByIndexAsync(finalBroj, finalGodina, smer)
            .subscribe(
                studenti -> Platform.runLater(() -> {
                    setButtonLoading(searchByIndexBtn, false);
                    if (studenti != null) {
                        tabelaStudentiPoIndeksu.setItems(FXCollections.observableArrayList(studenti));
                    } else {
                        showAlert("Greska", "Doslo je do greske pri pretrazi studenata.");
                    }
                }),
                error -> Platform.runLater(() -> {
                    setButtonLoading(searchByIndexBtn, false);
                    showAlert("Greska", "Doslo je do greske: " + error.getMessage());
                })
            );
    }

    public void handleSearchByHighSchool(ActionEvent actionEvent) {
        SrednjaSkolaDTO selectedSkola = srednjaSkolaCb.getValue();

        if (selectedSkola == null) {
            showAlert("Upozorenje", "Izaberite srednju skolu.");
            return;
        }

        setButtonLoading(searchByHighSchoolBtn, true);

        studentService.searchByHighSchoolAsync(selectedSkola.getId())
            .subscribe(
                studenti -> Platform.runLater(() -> {
                    setButtonLoading(searchByHighSchoolBtn, false);
                    if (studenti != null) {
                        tabelaStudentiPoSrednjojSkoli.setItems(FXCollections.observableArrayList(studenti));
                    } else {
                        showAlert("Greska", "Doslo je do greske pri pretrazi studenata.");
                    }
                }),
                error -> Platform.runLater(() -> {
                    setButtonLoading(searchByHighSchoolBtn, false);
                    showAlert("Greska", "Doslo je do greske: " + error.getMessage());
                })
            );
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Helper metoda za postavljanje loading stanja dugmeta.
     */
    private void setButtonLoading(Button button, boolean loading) {
        if (button != null) {
            button.setDisable(loading);
            button.setText(loading ? LOADING_TEXT : SEARCH_TEXT);
        }
    }
}
