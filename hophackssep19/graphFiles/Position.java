package graphFiles;

/**
 * Generic position abstraction.
 * @param <T> Element type.
 */
public interface Position<T> {

  /**
   * @return Element at position
   */
  T get();

  /**
   * @param t Element to store at position
   */
  void put(T t);
}
