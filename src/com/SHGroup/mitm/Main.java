package com.SHGroup.mitm;

import com.SHGroup.mitm.command.CommandHandler;
import com.SHGroup.mitm.gui.MainGUIApplication;
import com.SHGroup.mitm.networking.NetworkManager;

import javafx.application.Platform;

public class Main extends Thread{
	public static void main(String[] args) {
		new Main().start();
	}
	
	public final static NetworkManager network = new NetworkManager();
	public final static MainGUIApplication gui = new MainGUIApplication();
	public final static CommandHandler command = new CommandHandler();
	
	private static boolean isExit = false;
	
	@Override
	public void run() {
		network.initialize();
		gui.initialize();
		command.initialize();
		
		while(!isExit) {
		}
	}
	
	public static final void exit() {
		exit(0);
	}
	
	public static final void exit(int exit_code) {
		isExit = true;
		
		network.onExit();
		gui.onExit();
		command.onExit();
		
		System.exit(exit_code);
	}

	public static final void runOnNetworkingThread(Runnable runnable) {
		network.getThread().appendWork(runnable);
	}
	public static final void runOnGUIThread(Runnable runnable) {
		Platform.runLater(runnable);
	}
}
