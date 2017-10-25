package com.SHGroup.mitm.gui;

import com.SHGroup.mitm.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGUIApplication extends Application {
	public MainGUIApplication() {
	}

	private boolean isInit = false;

	public void initialize() {
		if (isInit)
			return;
		isInit = true;
		try {
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						Main.gui.launchGUI();
					}catch(Exception ex) {
						Main.exit();
					}
				}
			};
			t.setDaemon(true);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
			Main.exit();
		}
	}
	
	private void launchGUI() {
		Application.launch();
	}

	public void onExit() {

	}

	private SceneController controller = null;
	private boolean load = false;

	public SceneController getController() {
		return controller;
	}

	public void setController(SceneController controller) {
		this.controller = controller;
	}

	public boolean isLoaded() {
		return load;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();

			Parent root = loader.load(getClass().getResourceAsStream("MainScene.fxml"));
			root.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

			primaryStage.setTitle("MITM Attack");
			primaryStage.setScene(new Scene(root));
			
			primaryStage.setOnCloseRequest(e -> System.exit(0));
			
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateGraph() {
		while(getController() == null) {}
		getController().getGraphPanel().update();
	}

	public void appendLog(Object o) {
		while(getController() == null) {}
		getController().appendLog(o);
	}
}
