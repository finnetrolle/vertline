package vertline;

/**
 * Билдер для построения @Pipeline
 * @param <TO> выход текущего элемента
 * @param <PI> вход содержащего пайплайна
 * @param <PO> выход содержащего пайплайна
 */
public class PipelineBuilder<TO, PI, PO> {

    private final Pipeline<PI,PO> pipeline;

    PipelineBuilder(Pipeline<PI, PO> pipeline) {
        this.pipeline = pipeline;
    }

    /**
     * Добавление действия к пайплайну
     * @param action - действие @Action
     * @param <NEW_TO> - тип объекта, который возвращает @Action
     * @return @PipelineBuilder
     */
    public <NEW_TO> PipelineBuilder<NEW_TO, PI, PO> emit(Action<TO, NEW_TO> action) {
        this.pipeline.addAction(action);
        return new PipelineBuilder<>(this.pipeline);
    }

    /**
     * Добавление завершающего действия к пайплайну. Отличается от добавления следующего действия тем,
     * что финальное действие должно всегда возвращать тип, который возвращает сам @Pipeline
     * @param action - добавляемое действие @Action
     * @return Сформированный @Pipeline
     */
    public Pipeline<PI, PO> finish(Action<TO, PO> action) {
        this.pipeline.addAction(action);
        return pipeline;
    }

}
