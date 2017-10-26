package com.SHGroup.mitm.gui;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import com.SHGroup.mitm.Main;
import com.SHGroup.mitm.networking.Device;

public class GraphManager {
	public Graph mainGraph = new SingleGraph("MainGraph");
	public ViewPanel vp;

	private final String router = "게이트웨이";
	private final String me = "나";

	public GraphManager() {
		Viewer viewer = new Viewer(mainGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		
		vp = viewer.addDefaultView(true);

		mainGraph.addNode(router);
		
		mainGraph.addNode(me);
		mainGraph.addEdge(router + me, router, me);
		
	}

	public void update() {
		for(Device d : Main.network.getDevices()) {
			if(!containsEdge(d.getNickName())) {
				addEdge(d.getNickName());
				System.out.println(d.getNickName());
			}
		}
		for (Node node : mainGraph) {
			if(!node.hasAttribute("ui.label"))
				node.addAttribute("ui.label", node.getId());
	    }
	}

	public boolean containsEdge(String nickname) {
		return mainGraph.getNode(nickname) != null;
	}

	public void removeEdge(String nickname) {
		if (mainGraph.getNode(nickname) == null) {
			return;
		}
		mainGraph.removeEdge(router + nickname);
		mainGraph.removeNode(nickname);
	}

	public void addEdge(String nickname) {
		if (mainGraph.getNode(nickname) != null) {
			return;
		}
		mainGraph.addNode(nickname);
		mainGraph.addEdge(router + nickname, router, nickname);
	}
}
