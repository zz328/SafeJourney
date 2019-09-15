package graphFiles;

import java.util.List;
import exceptions.PositionException;
import exceptions.LoopException;
import exceptions.RemovalException;

/**
* This a general interface that maps out a directed graph.
* This interface assumes that each vertex and edge carries one uniform type of data.
*
* @param <V> Vertex element type
* @param <E> Edge element type
*/
public interface Graph<V, E> {

    /**
    * Insert new vertex
    * @param v Element to Insert
    * @return Vertex position created
    */
    Vertex<V> insert(V vname, V longitued, V latitude);

    /**
    * Insert new Edge
    * @param from Vertex position from where the edge begins
    * @param to Vertex position where the edge goes
    * @param e Element to Insert
    * @return Edge position created
    * @throws PositionException thrown if either position is invalid
    * @throws LoopException thrown if edge creates a self-loop
    */
    Edge<E> insert(Vertex<V> from, Vertex<V> to, E e) throws PositionException, LoopException;


    /**
    * Removes the passed in Veretex
    * @param v Vertex that will be removed
    * @return Vertex that was removed, return null if not successful
    * @throws PositionException if vertex is invalid
    * @throws RemovalException if Vertex still has incident edges
    */
    Vertex<V> remove(Vertex<V> v) throws PositionException, RemovalException;

    /**
    * Removes the passed in Edge
    * @param e Edge that will be removed
    * @return retuns the edge removed if successful, null if not
    * @throws PositionException if Edge is invalid
    */
    Edge<E> remove(Edge<E> e) throws PositionException;


    /**
    * Checks to see if edge is incident to vertex
    * @param v the Vertex passed in
    * @param e the Edge that will be checked agains the Vertex
    * @return true if Edge is incident to Vertex
    * @throws PositionException if either the vertex or edge are invalid
    */
    boolean incident(Vertex<V> v, Edge<E> e) throws PositionException;

    /**
    * Graph Edges
    * @return returns an interable object over the edges in the graph in no specific order
    */
    Iterable<Edge<E>> edges();

    /**
    * Graph Verticies
    * @return returns an interable object over the verticies in no specific order
    */
    Iterable<Vertex<V>> vertices();

    /**
    * Outgoing edges of vertex.
    * @param v Vertex position to explore.
    * @return Iterable over all outgoing edges of the given vertex, no specific order
    */
    Iterable<Edge<E>> outgoing(Vertex<V> v) throws PositionException;

    /**
    * Incoming edges of vertex.
    * @param v Vertex position to explore.
    * @return Iterable over all outgoing edges of the given vertex, no specific order
    */
    Iterable<Edge<E>> incoming(Vertex<V> v) throws PositionException;

    /**
    * End vertex position
    * @return Vertex position edge ends from
    * @throws PositionException If edge position is invalid.
    */
    Vertex<V> from(Edge<E> e) throws PositionException;

    /**
    * To vertex position
    * @return Vertex position edge goes to
    * @throws PositionException If edge position is invalid.
    */
    Vertex<V> to(Edge<E> e) throws PositionException;

    /**
    * Give vertex the path information with object
    * @param v Vertex position to pathInfo
    * @param info information object
    * @throws PositionException if Vertex is invalid
    */
    void pathInfo(Vertex<V> v, Object info) throws PositionException;

    /**
    * Give Edge the path information from the object
    * @param e Edge position to pathInfo
    * @param info information object
    * @throws PositionException if Vertex is invalid
    */
    void pathInfo(Edge<E> e, Object info) throws PositionException;

    /**
    * Returns the path information associated with the vertex
    * @param v Vertext position that is passed in
    * @return info Object or null if nothing
    * @throws PositionException if vertex is invalid
    */
    Object pathInfo(Vertex<V> v) throws PositionException;

    /**
    * Returns the path information associated with the edge
    * @param e Edge position that is passed in
    * @return info Object or null if nothing
    * @throws PositionException if edge is invalid
    */
    Object pathInfo(Edge<E> e) throws PositionException;

    /**
    * Clear all pathInformation
    * All labes from accessed Edges and Verticies are null
    */
    void clearPathInfo();
}
