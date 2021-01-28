package data;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class MCD {

	/*
	 * This class contains the graph that represent the MDC Scheme
	 * 
	 * The Graph used to display it comes from the library is JGraphT
	 */

	// Data

	private UndirectedGraph<Node, DefaultEdge> MCDGraph;

	// Methods

	public MCD(UndirectedGraph<Node, DefaultEdge> mCDGraph) {
		MCDGraph = mCDGraph;
	}
	
	public MCD() {
		this.MCDGraph = new SimpleGraph<Node, DefaultEdge>(DefaultEdge.class);
	}

	public UndirectedGraph<Node, DefaultEdge> getMCDGraph() {
		return MCDGraph;
	}

	public void setMCDGraph(UndirectedGraph<Node, DefaultEdge> mCDGraph) {
		MCDGraph = mCDGraph;
	}

}
