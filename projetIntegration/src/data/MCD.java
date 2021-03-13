package data;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.SimpleGraph;

public class MCD {

	/*
	 * This class contains the graph that represent the MDC Scheme
	 * 
	 * The Graph used to display it comes from the library is JGraphT
	 */

	// Data

	private Pseudograph<Node, DefaultEdge> MCDGraph;

	// Methods

	public MCD(Pseudograph<Node, DefaultEdge> mCDGraph) {
		MCDGraph = mCDGraph;
	}
	
	public MCD() {
		this.MCDGraph = new Pseudograph<Node, DefaultEdge>(DefaultEdge.class);
	}

	public UndirectedGraph<Node, DefaultEdge> getMCDGraph() {
		return MCDGraph;
	}

	public void setMCDGraph(Pseudograph<Node, DefaultEdge> mCDGraph) {
		MCDGraph = mCDGraph;
	}

}
