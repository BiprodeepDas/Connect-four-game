package com.example.connectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


	private Controller controller;


	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));

		GridPane rootid = loader.load();

		controller=loader.getController();
		controller.playGround();

		MenuBar menuBar=CreateMenu();
		menuBar.prefWidthProperty().bind(stage.widthProperty());

		Pane menu= (Pane) rootid.getChildren().get(0);

		menu.getChildren().add(menuBar);

		Scene scene = new Scene(rootid);
		stage.setTitle("Connect Four");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

	public MenuBar CreateMenu(){
		Menu fileMenu=new Menu("File");

		MenuItem ng=new MenuItem("New Game");
		MenuItem rg=new MenuItem("Reset Game");
		MenuItem eg=new MenuItem("Exit Game");
		SeparatorMenuItem s=new SeparatorMenuItem();

		eg.setOnAction(event -> {
			Platform.exit();
			System.exit(0);});

		ng.setOnAction(event ->{
			controller.resetGame();
		});

		rg.setOnAction(event -> {
			controller.resetGame();
		});





		fileMenu.getItems().addAll(ng,rg,s,eg);

		Menu helpMenu=new Menu("Help");

		MenuItem ag=new MenuItem("About Game");
		MenuItem ad=new MenuItem("About Devs");
		SeparatorMenuItem se=new SeparatorMenuItem();

		ag.setOnAction(event -> {
			aboutgame();
		});

		ad.setOnAction(event -> {
			aboutdev();
		});


		helpMenu.getItems().addAll(ag,se,ad);


		MenuBar menu=new MenuBar();
		menu.getMenus().addAll(fileMenu,helpMenu);

		return menu;

	}

	private void aboutdev() {
		Alert a=new Alert(Alert.AlertType.INFORMATION);
		a.setTitle("About the Developer");
		a.setHeaderText("Biprodeep Das");
		a.setContentText("I am learning Java and looking " +
				"forward to make awesome apps and this game is my first java game");
		a.show();
	}

	private void aboutgame() {
		Alert a=new Alert(Alert.AlertType.INFORMATION);
		a.setTitle("About Connect four game.");
		a.setHeaderText("How to play ?");
		a.setContentText("Connect Four is a two-player connection game in which the players " +
				"first choose a color and then take turns dropping colored discs from the top " +
				"into a seven-column, six-row vertically suspended grid. The pieces fall straight" +
				" down, occupying the next available space within the column. The objective of the" +
				" game is to be the first to form a horizontal, vertical, or diagonal line of four" +
				" of one's own discs. Connect Four is a solved game. The first player can always" +
				" win by playing the right moves.");
		a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		a.show();
	}



}