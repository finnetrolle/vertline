package vertline;

import java.util.List;

/**
 * Интерфейс, описывающий метод сворачивания списка элементов в один элемент
 * @param <LIST_T> - тип объектов во входящем списке
 * @param <OUT> - тип объекта на выходе
 */
@FunctionalInterface
public interface Joiner<LIST_T, OUT> {

    /**
     * Метод сворачивает список объектов на входе в выходной объект
     * @param results список объектов на входе
     * @param context контекст
     * @return выходной объект
     */
    OUT join(List<LIST_T> results, Context context);
}
