package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzba.dtos.SrednjaSkolaDTO;
import org.raflab.studsluzba.dtos.StudentBySrednjaSkolaDTO;
import org.raflab.studsluzba.dtos.StudentDTO;
import org.raflab.studsluzbadesktopclient.services.NavigationHistoryService;
import org.raflab.studsluzbadesktopclient.services.SrednjaSkolaService;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchStudentController {

    private static final String VIEW_NAME = "searchStudent";
    
    private final StudentService studentService;
    private final SrednjaSkolaService srednjaSkolaService;
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
    private TableView<StudentBySrednjaSkolaDTO> tabelaStudentiPoSrednjojSkoli;

    @FXML
    private TableColumn<StudentDTO, Void> actionColumnName;

    @FXML
    private TableColumn<StudentDTO, Void> actionColumnIndex;

    @FXML
    private TableColumn<StudentBySrednjaSkolaDTO, Void> actionColumnSchool;

    public SearchStudentController(StudentService studentService, SrednjaSkolaService srednjaSkolaService, 
                                   MainView mainView, NavigationHistoryService navigationHistoryService) {
        this.studentService = studentService;
        this.srednjaSkolaService = srednjaSkolaService;
        this.mainView = mainView;
        this.navigationHistoryService = navigationHistoryService;
    }

    @FXML
    public void initialize() {
        setupTabChangeTracking();
        loadSrednjeSkole();
        setupActionColumns();
    }
    
    private void setupTabChangeTracking() {
        if (mainTabPane != null) {
            // Register initial tab state (tab 0) so we can go back to it
            int initialTab = mainTabPane.getSelectionModel().getSelectedIndex();
            if (initialTab >= 0) {
                navigationHistoryService.registerTabChange(VIEW_NAME, initialTab);
            }
            
            // Track future tab changes
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
        // Setup action column for name search table
        if (actionColumnName != null) {
            actionColumnName.setCellFactory(createButtonCellFactory());
        }
        
        // Setup action column for index search table
        if (actionColumnIndex != null) {
            actionColumnIndex.setCellFactory(createButtonCellFactory());
        }
        
        // Setup action column for high school search table
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
        
        // Cache current search state before navigating
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

    public void handleSearchByName(ActionEvent actionEvent) {
        try {
            String ime = imeStudentaTf.getText();
            String prezime = prezimeStudentaTf.getText();

            List<StudentDTO> studenti = studentService.searchByNameAndSurname(ime, prezime);

            if (studenti != null) {
                tabelaStudentiPoImenu.setItems(FXCollections.observableArrayList(studenti));
            } else {
                showAlert("Greska", "Doslo je do greske pri pretrazi studenata.");
            }
        } catch (Exception e) {
            showAlert("Greska", "Doslo je do greske: " + e.getMessage());
        }
    }

    public void handleSearchByIndex(ActionEvent actionEvent) {
        try {
            String brojText = brojIndeksaTf.getText();
            String godinaText = godinaIndeksaTf.getText();

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

            List<StudentDTO> studenti = studentService.searchByIndex(broj, godina);

            if (studenti != null) {
                tabelaStudentiPoIndeksu.setItems(FXCollections.observableArrayList(studenti));
            } else {
                showAlert("Greska", "Doslo je do greske pri pretrazi studenata.");
            }
        } catch (Exception e) {
            showAlert("Greska", "Doslo je do greske: " + e.getMessage());
        }
    }

    public void handleSearchByHighSchool(ActionEvent actionEvent) {
        try {
            SrednjaSkolaDTO selectedSkola = srednjaSkolaCb.getValue();

            if (selectedSkola == null) {
                showAlert("Upozorenje", "Izaberite srednju skolu.");
                return;
            }

            List<StudentBySrednjaSkolaDTO> studenti = studentService.searchByHighSchool(selectedSkola.getId());

            if (studenti != null) {
                tabelaStudentiPoSrednjojSkoli.setItems(FXCollections.observableArrayList(studenti));
            } else {
                showAlert("Greska", "Doslo je do greske pri pretrazi studenata.");
            }
        } catch (Exception e) {
            showAlert("Greska", "Doslo je do greske: " + e.getMessage());
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
