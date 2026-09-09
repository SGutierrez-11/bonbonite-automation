package com.bonbonite.qa.api.context;

import com.bonbonite.qa.api.exceptions.CustomException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Almacén de datos clave-valor que comparten los pasos de un escenario.
 *
 * <p>Reemplaza el uso de campos estáticos mutables para pasar información entre
 * step definitions: un paso guarda el usuario que registró y otro, varios pasos
 * después, lo recupera para validar la interfaz. Al no haber estado estático, la
 * ejecución en paralelo es segura.</p>
 *
 * <p>Las llaves deben provenir siempre de una enum, nunca de cadenas sueltas, para
 * que el compilador detecte los errores de escritura.</p>
 */
public class Context {

  private final Map<String, Object> data;

  private Context(Map<String, Object> backingMap) {
    this.data = backingMap;
  }

  /**
   * Crea un contexto apto para ser leído y escrito desde varios hilos.
   *
   * @return contexto respaldado por un mapa concurrente
   */
  public static Context threadSafeContext() {
    return new Context(new ConcurrentHashMap<>());
  }

  /**
   * Crea un contexto para uso dentro de un solo hilo.
   *
   * @return contexto respaldado por un mapa simple
   */
  public static Context nonThreadSafeContext() {
    return new Context(new HashMap<>());
  }

  /**
   * Almacena un valor bajo la llave indicada.
   *
   * @param key   llave con la que se recuperará el valor
   * @param value valor a almacenar
   * @throws CustomException si el valor es nulo
   */
  public void set(String key, Object value) {
    if (value == null) {
      throw new CustomException("No se puede almacenar un valor nulo con la llave '" + key + "'");
    }
    data.put(key, value);
  }

  /**
   * Recupera el valor asociado a una llave.
   *
   * @param key llave a consultar
   * @param <T> tipo esperado del valor almacenado
   * @return valor asociado a la llave
   * @throws CustomException si la llave no existe en el contexto
   */
  @SuppressWarnings("unchecked")
  public <T> T get(String key) {
    if (!data.containsKey(key)) {
      throw new CustomException("La llave '" + key + "' no existe en el contexto del escenario");
    }
    return (T) data.get(key);
  }

  /**
   * Recupera el valor asociado a una llave, o un valor por defecto si no existe.
   *
   * @param key          llave a consultar
   * @param defaultValue valor a devolver cuando la llave no está presente
   * @param <T>          tipo esperado del valor almacenado
   * @return valor asociado a la llave, o {@code defaultValue}
   */
  @SuppressWarnings("unchecked")
  public <T> T getOrDefault(String key, T defaultValue) {
    return (T) data.getOrDefault(key, defaultValue);
  }

  /**
   * Indica si el contexto contiene un valor para la llave indicada.
   *
   * @param key llave a verificar
   * @return {@code true} si la llave está presente
   */
  public boolean containsKey(String key) {
    return data.containsKey(key);
  }

  /**
   * Elimina el valor asociado a una llave.
   *
   * @param key llave a eliminar
   * @throws CustomException si la llave no existe en el contexto
   */
  public void remove(String key) {
    if (!data.containsKey(key)) {
      throw new CustomException("La llave '" + key + "' no existe en el contexto del escenario");
    }
    data.remove(key);
  }
}
