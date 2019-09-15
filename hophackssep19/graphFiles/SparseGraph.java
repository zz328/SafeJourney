package graphFiles;

import exceptions.LoopException;
import exceptions.PositionException;
import exceptions.RemovalException;

import java.util.ArrayList;
import java.util.List;

/**
* This is an implementation of a directed graph using incidence lists for sparse graphs.
* @param <V> Vertext element type.
* @param <E> Edge element type.
*/
public class SparseGraph<V, E> implements Graph<V, E> {

    /**
    * Concrete implementation of the Vertex interface.
    */
    private final class VertexNode<V> implements Vertex<V> {
        V data;
        V lon;
        V lat;
        Graph<V, E> owner;
        List<Edge<E>> out;
        List<Edge<E>> in;
        Object pathInfo;

        VertexNode(V v, V lo, V la) {
            this.data = v;
            this.lon = lo;
            this.lat = la;
            this.out = new ArrayList<>();
            this.in = new ArrayList<>();
            this.pathInfo = null;
        }

        @Override
        public V get() {
            return this.data;
        }

        @Override
        public void put(V v) {
            this.data = v;
        }

        /*
        * @return the longitude value
        */
        public V lon() {
            return this.lon;
        }

        /**
        * @return the latittude value
        */
        public V lat() {
            return this.lat;
        }
    }

    /**
    * Concrete implementation of Edge interface
    */
    private final class EdgeNode<E> implements Edge<E> {
        E data;
        Graph<V, E> owner;
        Vertex<V> from;
        Vertex<V> to;
        Object pathInfo;
        int[] crimeFreq;
        int crimeIncidents;

        EdgeNode(E e, Vertex<V> f, Vertex<V> t) {
            this.data = e;
            this.from = f;
            this.to = t;
            this.pathInfo = null;
            this.crimeIncidents = 0;
            this.crimeFreq = new int[9];
        }

        /**
        * Increments the count of crimes associated with this edge.
        * @param index identifies the significance of the crime
        *       the values of signficance associated with each crime was determined
        *       based on the hierarchy rules of crime from the U.S. Department of Justice.
        */
        public void incrementCrime(int index) {
            this.crimeFreq[index]++;
            this.crimeIncidents++;
        }

        /**
        * Access the crime inident variable.
        * @return the number of crimes that occured associated with the edge.
        */
        public int getCrimeIncidents() {
            return crimeIncidents;
        }

        @Override
        public E get() {
            return this.data;
        }

        @Override
        public void put(E e) {
            this.data = e;
        }

        /**
        * This function computes the addition edge weights based on the frequency
        * of each category of crime.
        * @return a double array containing the additionl weights.
        */
        public double[] addedWeight() {
            double[] moreWeight = new double[8];
            for (int i = 0; i < 8; i++) {
                moreWeight[i] = Math.pow(crimeFreq[i] + 20, 2);
            }
            return moreWeight;
        }

    }

    private List<Vertex<V>> vertices;
    private List<Edge<E>> edges;

    /**
    * Default Constructor
    */
    public SparseGraph() {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    /**
     * Gets the list of all of the vertices in the graph.
     * @return vertex list of the graph
     */
    public List<Vertex<V>> getVertices() {
        return this.vertices;
    }

    /**
     * Gets the longitude value of the vertex.
     * @param v the vertex in the graph.
     * @return the longitude value of the vertex.
     */
    public V getLon(Vertex<V> v) {
        VertexNode<V> ver = this.convert(v);
        return ver.lon();
    }

    /**
     * Gets the latittude value of the vertex.
     * @param v the vertex in the graph.
     * @return the latitude value of the vertex.
     */
    public V getLat(Vertex<V> v) {
        VertexNode<V> ver = this.convert(v);
        return ver.lat();
    }

    /**
     * Increases the frequency of the crime within a specifc category
     * @param e the edge in the graph.
     * @param index the identifier of the category of the crime.
     */
    public void incrementCrime(Edge<E> e, int index) {
        EdgeNode<E> edge = this.convert(e);
        edge.incrementCrime(index);
    }

    /**
    * Access the crimeIncident value of an edge.
    * @param e the edge in the graph.
    * @return the number of incidents associated with the given edge.
    */
    public int getCrimeIncidents(Edge<E> e) {
        EdgeNode<E> edge = this.convert(e);
        return edge.getCrimeIncidents();
    }

    /**
     * Gets the list of all of the edges in the graph.
     * @return edge list of the graph
     */
    public List<Edge<E>> getEdges() {
        return this.edges;
    }

    /**
    * Ensures the owner field of the VertexNode matches the graph that calls the function.
    * @param v the VertexNode in the graph.
    */
    private void checkOwner(VertexNode<V> v) {
        if (v.owner != this) {
            throw new PositionException();
        }
    }

    /**
    * Ensures the owner field of the EdgeNode matches the graph that calls the function.
    * @param e the EdgeNode in the graph.
    */
    private void checkOwner(EdgeNode<E> e) {
        if (e.owner != this) {
            throw new PositionException();
        }
    }

    /**
    * Converts a Vertex into a VertexNode
    * @param v the vetex in the graph
    * @return the VertexNode that contains Vertex v
    * @throws PositionException when v is not in the graph.
    */
    private VertexNode<V> convert(Vertex<V> v) throws PositionException {
        try {
            VertexNode<V> node = (VertexNode<V>) v;
            this.checkOwner(node);
            return node;
        } catch (ClassCastException ex) {
            throw new PositionException();
        }
    }

    /**
    * Converts a Edge into a EdgeNode
    * @param e the edge in the graph
    * @return the EdgeNode that contains Edge e
    * @throws PositionException when e is not in the graph.
    */
    // converts Edge into EdgeNode for implementation
    private EdgeNode<E> convert(Edge<E> e) throws PositionException {
        try {
            EdgeNode<E> node = (EdgeNode<E>) e;
            this.checkOwner(node);
            return node;
        } catch (ClassCastException ex) {
            throw new PositionException();
        }
    }

    @Override
    public Vertex<V> insert(V v, V lo, V la) {
        VertexNode<V> node = new VertexNode(v, lo, la);
        node.owner = this;
        this.vertices.add(node);
        return node;
    }

    @Override
    public Edge<E> insert(Vertex<V> from, Vertex<V> to, E e) throws PositionException, LoopException {


        //throws PositionException if verticies are null
        if (from == null || to == null) {
            throw new PositionException();
        }

        //throws LoopException if verticies are the same
        if (from == to) {
            throw new LoopException();
        }

        VertexNode<V> f = this.convert(from);
        VertexNode<V> t = this.convert(to);

        //creates edge
        EdgeNode<E> node = new EdgeNode(e, f, t);
        node.owner = this;

        //checks for duplicate edges
        List<Edge<E>> check;
        if (f.out.size() > t.in.size()) {
            check = t.in;
        } else {
            check = f.out;
        }

        //checks the list of edges associated with one of the given vertices for potential duplicate value.
        for (int i = 0; i < check.size(); i++) {
            VertexNode<V> f1 = this.convert(this.from(check.get(i)));
            VertexNode<V> t1 = this.convert(this.to(check.get(i)));

            if (f1 == f && t1 == t) {
                throw new LoopException();
            }
        }

        //adds the edge if unique
        f.out.add(node);
        t.in.add(node);
        this.edges.add(node);
        return node;
    }

    @Override
    public Vertex<V> remove(Vertex<V> v) throws PositionException, RemovalException {
        if (v == null) {
            throw new PositionException();
        }

        VertexNode<V> data = this.convert(v);

        //can't remove vertices that have incident edges
        if (data.in.size() > 0 || data.out.size() > 0) {
            throw new RemovalException();
        }

        boolean removed = this.vertices.remove(data);
        if (!removed) {
            throw new PositionException();
        }
        data.owner = null;
        return data;
    }

    @Override
    public Edge<E> remove(Edge<E> e) throws PositionException {
        if (e == null) {
            throw new PositionException();
        }

        EdgeNode<E> edge = this.convert(e);
        VertexNode<V> f = this.convert(edge.from);
        VertexNode<V> t = this.convert(edge.to);

        // removes edges from vertice incident lists
        f.out.remove(edge);
        t.in.remove(edge);

        boolean removed = this.edges.remove(edge);
        if (!removed) {
            throw new PositionException();
        }
        edge.owner = null;
        return edge;
    }

    @Override
    public Iterable<Vertex<V>> vertices() {
        List<Vertex<V>> iterate = new ArrayList<>();
        for (int i = 0; i < this.vertices.size(); i++) {
            Vertex<V> v = this.vertices.get(i);
            iterate.add(v);
        }
        return iterate;
    }

    @Override
    public Iterable<Edge<E>> edges() {
        List<Edge<E>> iterate = new ArrayList<>();
        for (int i = 0; i < this.edges.size(); i++) {
            Edge<E> e = this.edges.get(i);
            iterate.add(e);
        }
        return iterate;
    }

    @Override
    public Iterable<Edge<E>> outgoing(Vertex<V> v) throws PositionException {
        List<Edge<E>> iterate = new ArrayList<>();
        VertexNode<V> vertex = this.convert(v);
        for (int i = 0; i < vertex.out.size(); i++) {
            Edge<E> e = vertex.out.get(i);
            iterate.add(e);
        }
        return iterate;
    }

    @Override
    public Iterable<Edge<E>> incoming(Vertex<V> v) throws PositionException {
        List<Edge<E>> iterate = new ArrayList<>();
        VertexNode<V> vertex = this.convert(v);
        for (int i = 0; i < vertex.in.size(); i++) {
            Edge<E> e = vertex.in.get(i);
            iterate.add(e);
        }
        return iterate;
    }

    @Override
    public Vertex<V> from(Edge<E> e) throws PositionException {
        EdgeNode<E> node = this.convert(e);
        return node.from;
    }

    @Override
    public Vertex<V> to(Edge<E> e) throws PositionException {
        EdgeNode<E> node = this.convert(e);
        return node.to;
    }

    @Override
    public void pathInfo(Vertex<V> v, Object info) throws PositionException {
        if (v == null) {
            throw new PositionException();
        }
        VertexNode<V> vertex = this.convert(v);
        this.checkOwner(vertex);
        vertex.pathInfo = info;
    }

    @Override
    public void pathInfo(Edge<E> e, Object info) throws PositionException {

        if(e == null) {
            throw new PositionException();
        }
        EdgeNode<E> edge = this.convert(e);
        this.checkOwner(edge);
        edge.pathInfo = info;
    }

    @Override
    public Object pathInfo(Vertex<V> v) throws PositionException {
        if (v == null) {
            throw new PositionException();
        }

        VertexNode<V> vertex = this.convert(v);
        this.checkOwner(vertex);
        return vertex.pathInfo;
    }

    @Override
    public Object pathInfo(Edge<E> e) throws PositionException {
        if (e == null) {
            throw new PositionException();
        }

        EdgeNode<E> edge = this.convert(e);
        this.checkOwner(edge);
        return edge.pathInfo;
    }

    @Override
    public boolean incident(Vertex<V> v, Edge<E> e) throws PositionException {
        if (v == null || e == null) {
            throw new PositionException();
        }
        VertexNode<V> vertex = this.convert(v);
        EdgeNode<E> edge = this.convert(e);

        boolean check1 = vertex.in.contains(edge);
        boolean check2 = vertex.out.contains(edge);
        if (check1 || check2) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clearPathInfo() {
        //goes through all of the vertices
        for (int i = 0; i < vertices.size(); i++) {
            this.pathInfo(vertices.get(i), null);
        }

        //goes through all of the edges
        for (int i = 0; i < edges.size(); i++) {
            this.pathInfo(edges.get(i), null);
        }
    }

    public double[] getEdgeCrimeWeights(Edge<E> e) {
        EdgeNode<E> edge = this.convert(e);
        return edge.addedWeight();
    }

}
