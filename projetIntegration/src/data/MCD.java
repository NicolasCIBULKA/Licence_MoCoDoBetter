package data;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.SimpleGraph;

public class MCD implements Serializable{

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
	
    public MCD deepClone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(this);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        return (MCD) in.readObject();
    }

}
