package com.bonbonite.qa.api.context;

import com.bonbonite.qa.api.exceptions.CustomException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Key value store shared by the steps of a scenario.
 *
 * <p>It replaces mutable static fields as the way to pass information between step
 * definitions: one step stores the customer it registered and another one, several
 * steps later, reads it back to validate the interface. With no static state,
 * parallel execution is safe.</p>
 *
 * <p>Keys must always come from an enum, never from loose strings, so that the
 * compiler catches typos.</p>
 */
public class Context {

  private final Map<String, Object> data;

  private Context(Map<String, Object> backingMap) {
    this.data = backingMap;
  }

  /**
   * Creates a context that can be read and written from several threads.
   *
   * @return a context backed by a concurrent map
   */
  public static Context threadSafeContext() {
    return new Context(new ConcurrentHashMap<>());
  }

  /**
   * Creates a context meant to be used within a single thread.
   *
   * @return a context backed by a plain map
   */
  public static Context nonThreadSafeContext() {
    return new Context(new HashMap<>());
  }

  /**
   * Stores a value under the given key.
   *
   * @param key   key the value will be retrieved with
   * @param value value to store
   * @throws CustomException if the value is null
   */
  public void set(String key, Object value) {
    if (value == null) {
      throw new CustomException("No se puede almacenar un valor nulo con la llave '" + key + "'");
    }
    data.put(key, value);
  }

  /**
   * Retrieves the value bound to a key.
   *
   * @param key key to look up
   * @param <T> expected type of the stored value
   * @return the value bound to the key
   * @throws CustomException if the key is not present in the context
   */
  @SuppressWarnings("unchecked")
  public <T> T get(String key) {
    if (!data.containsKey(key)) {
      throw new CustomException("La llave '" + key + "' no existe en el contexto del escenario");
    }
    return (T) data.get(key);
  }

  /**
   * Retrieves the value bound to a key, or a default value when it is absent.
   *
   * @param key          key to look up
   * @param defaultValue value returned when the key is not present
   * @param <T>          expected type of the stored value
   * @return the value bound to the key, or {@code defaultValue}
   */
  @SuppressWarnings("unchecked")
  public <T> T getOrDefault(String key, T defaultValue) {
    return (T) data.getOrDefault(key, defaultValue);
  }

  /**
   * Tells whether the context holds a value for the given key.
   *
   * @param key key to check
   * @return {@code true} when the key is present
   */
  public boolean containsKey(String key) {
    return data.containsKey(key);
  }

  /**
   * Removes the value bound to a key.
   *
   * @param key key to remove
   * @throws CustomException if the key is not present in the context
   */
  public void remove(String key) {
    if (!data.containsKey(key)) {
      throw new CustomException("La llave '" + key + "' no existe en el contexto del escenario");
    }
    data.remove(key);
  }
}
