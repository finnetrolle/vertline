package vertline;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Объект контекста - обычный враппер для Map, который добавляет и возвращает элементы с определенными условиями
 */
public class Context {

    private final Map<String, Object> map = new LinkedHashMap<>();

    /**
     * Метод возвращает объект по ключу
     * @param name - ключ объекта
     * @return объект или @RuntimeException если объект отсутствует
     */
    public Object get(String name) {
        Object object = map.get(name);
        if (object == null) {
            throw new RuntimeException("No such element");
        }
        return object;
    }

    /**
     * Метод устанавливает значение по ключу. В случае если ключ уже занят - выбрасывает @RuntimeException
     * @param name ключ
     * @param value значение
     */
    public void set(String name, Object value) {
        if (map.containsKey(name)) {
            throw new RuntimeException("Already has such element");
        }
        map.put(name, value);
    }

    @Override
    public String toString() {
        return "Context{" +
                "map=" + map +
                '}';
    }
}
