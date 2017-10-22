package com.SHGroup.mitm.gui;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import com.SHGroup.mitm.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class SceneController implements Initializable {
	@FXML
	private JFXListView<String> listview;

	@FXML
	private JFXTextArea consolePane;

	@FXML
	private JFXTextField consoleInput;

	@FXML
	public SwingNode graphPane;

	@FXML
	private JFXButton network_devices;

	@FXML
	private JFXButton arp_spoofing;

	private ObservableList<String> listItems = FXCollections.observableArrayList();

	private boolean applyListItems() {
		if (listItems == null) {
			return false;
		}
		listview.setItems(listItems);
		return true;
	}

	private final SimpleDateFormat sdf = new SimpleDateFormat("[yyyyMMdd-hhmmss] ");

	public void appendLog(Object o) {
		consolePane.appendText(sdf.format(new Date()) + o.toString() + "\n");
		consolePane.setScrollTop(consolePane.getScrollTop());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.gui.setController(this);

		listview.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				if (click.getClickCount() == 2 && click.getButton() == MouseButton.PRIMARY) {
					ObservableList<Integer> select = listview.getSelectionModel().getSelectedIndices();
					if (select.size() != 1) {
						Main.gui.getController().appendLog("Select only one item..");
						return;
					}
					listview.setDisable(true);
					Main.runOnNetworkingThread(new Runnable() {
						@Override
						public void run() {
							if (mode == 0) {
								Main.network.selectNetworkCard(select.get(0));
								Main.gui.getController().loadARPTargetDevices();
							} else if (mode == 1) {

							}
							listview.setDisable(false);
						}
					});
				}
			}
		});

		consoleInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					Main.command.dispatchCommand(consoleInput.getText());
					consoleInput.setText("");
				}
			}
		});

		Task<Void> task = new Task<Void>() {
			private boolean b = true;
			@Override
			protected Void call() throws Exception {
				while(b) {
					
				}
				return null;
			}
		};
		loadNetworkDevices();
		new Thread(task).start();
	}

	private int mode = 0;

	public int getMode() {
		return mode;
	}

	public void loadNetworkDevices() {

		network_devices.setDisable(true);
		arp_spoofing.setDisable(true);
		listview.setDisable(true);

		Main.runOnNetworkingThread(new Runnable() {
			@Override
			public void run() {
				ArrayList<String> list = Main.network.getNetworkDevices();
				listItems.clear();

				for (String n : list) {
					listItems.add(n);
				}

				applyListItems();
				mode = 0;
				network_devices.setDisable(false);
				arp_spoofing.setDisable(false);
				listview.setDisable(false);
			}
		});
	}

	public void loadARPTargetDevices() {

		network_devices.setDisable(true);
		arp_spoofing.setDisable(true);
		listview.setDisable(true);

		Main.runOnNetworkingThread(new Runnable() {
			@Override
			public void run() {
				if (Main.network.getPcap() != null) {
					ArrayList<String> list = Main.network.getARPTargetDevices();
					listItems.clear();

					for (String n : list) {
						listItems.add(n);
					}

					applyListItems();
					mode = 1;
				} else {
					Main.gui.appendLog("Please select network device first..");
				}

				network_devices.setDisable(false);
				arp_spoofing.setDisable(false);
				listview.setDisable(false);
			}
		});
	}
}