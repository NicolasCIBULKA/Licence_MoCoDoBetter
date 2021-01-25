package data;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class MCD {

	/*
	 * This class contains the graph that represent the MDC Scheme
	 * 
	 * The Graph used to display it comes from the library is JGraphT
	 */

	// Data

	private UndirectedGraph<Entity, DefaultEdge> MCDGraph;

	// Methods

	public MCD(UndirectedGraph<Entity, DefaultEdge> mCDGraph) {
		super();
		MCDGraph = mCDGraph;
	}

	public UndirectedGraph<Entity, DefaultEdge> getMCDGraph() {
		return MCDGraph;
	}

	public void setMCDGraph(UndirectedGraph<Entity, DefaultEdge> mCDGraph) {
		MCDGraph = mCDGraph;
	}

}
