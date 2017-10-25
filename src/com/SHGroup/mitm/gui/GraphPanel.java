package com.SHGroup.mitm.gui;

import javax.swing.JPanel;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

public class GraphPanel extends JPanel {
	private static final long serialVersionUID = -6548579033901759417L;

	private Graph mainGraph = new SingleGraph("MainGraph");

	public GraphPanel() {
		mainGraph.addNode("blabla");
		mainGraph.addNode("blabla2");
		mainGraph.addEdge("blablaa", "blabla", "blabla2");
		Viewer viewer = new Viewer(mainGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		ViewPanel vp = viewer.addDefaultView(false);
		this.add(vp);
	}

	public void update() {
	}
}
