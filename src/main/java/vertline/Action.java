package vertline;

/**
 * Интерфейс ключевого элемента - действие. Читается как преобразовать I в O
 * @param <I> - входящие данные
 * @param <O> - выходящие данные
 */
@FunctionalInterface
public interface Action<I,O> {

    /**
     * Обработка входящих данных
     * @param in - входные данные
     * @param context - объект контекста
     * @return выходные данные
     */
    O execute(I in, Context context);

}
