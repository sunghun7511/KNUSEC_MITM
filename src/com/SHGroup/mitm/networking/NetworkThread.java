package com.SHGroup.mitm.networking;

import java.util.LinkedList;

public class NetworkThread extends Thread{
	private final LinkedList<Runnable> works = new LinkedList<>();
	private boolean exit = false;
	
	public void appendWork(Runnable work) {
		synchronized (works) {
			this.works.add(work);
		}
	}
	
	public void exit() {
		exit = true;
	}
	
	@Override
	public void run() {
		while(!exit) {
			synchronized (works) {
				while(works.size() > 0) {
					works.removeFirst().run();
				}
			}
		}
	}
}
