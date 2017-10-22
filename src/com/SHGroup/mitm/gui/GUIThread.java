package com.SHGroup.mitm.gui;

import java.util.LinkedList;

import javafx.concurrent.Task;

public class GUIThread extends Task<Void>{
	private final LinkedList<Runnable> works = new LinkedList<>();
	private boolean exit = false;
	
	public void appendWork(Runnable work) {
		synchronized (works) {
			this.works.add(work);
		}
	}
	
	@Override
	protected Void call() throws Exception {
		while(!exit) {
			synchronized (works) {
				while(works.size() > 0) {
					works.remove(0).run();
				}
			}
		}
		return null;
	}

}
