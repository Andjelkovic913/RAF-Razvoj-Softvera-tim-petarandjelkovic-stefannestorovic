package org.raflab.studsluzbadesktopclient.controllers;

import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;

@Component
public class MenuBarController { 

	final MainView mainView;
	final StudentService studentService;

	@FXML
	private MenuBar menuBar;

    public MenuBarController(StudentService studentService, MainView mainView){
        this.studentService = studentService;
        this.mainView = mainView;
    }

	public void openSearchStudent() {
		mainView.changeRoot("searchStudent");
	}

	public void openNewStudent() {
		mainView.changeRoot("newStudent");
	}

	public void openPredmeti() {
		mainView.changeRoot("predmeti");
	}

	public void openPrijavaStudenta() {
		mainView.changeRoot("prijavaStudenta");
	}

	public void openListaPrijavljenih() {
		mainView.changeRoot("listaPrijavljenih");
	}

	public void openSearchNastavnik() {
		mainView.changeRoot("searchNastavnik");
	}

	public void openNewNastavnik() {
		mainView.changeRoot("newNastavnik");
	}

	public void openIspitniRokovi() {
		mainView.changeRoot("ispitniRokovi");
	}

	public void openIspiti() {
		mainView.changeRoot("ispiti");
	}

	public void openPrijaveIspita() {
		mainView.changeRoot("prijavaIspita");
	}

	public void openRezultatiIspita() {
		mainView.changeRoot("rezultatiIspita");
	}

	public void openProsecnaOcena() {
		mainView.changeRoot("prosecnaOcena");
	}

	@FXML
    public void initialize() {		

    }
}
