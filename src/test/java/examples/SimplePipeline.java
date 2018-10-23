package examples;

import org.junit.Test;
import vertline.Context;
import vertline.Pipeline;

public class SimplePipeline {

    @Test
    public void howToDefineAndExecuteSimplePipeline() {
        // Вывести длину строки, умноженную на 10
        assert 120 == Pipeline.define(String.class, Integer.class)
                .emit((e, ctx) -> e.length())
                .finish((e, ctx) -> e * 10)
                .execute("Abracadabrah", new Context());
    }

    @Test
    public void howToUseContextInsidePipeline() {
        // Вывести длину строки, умноженную на 10, сохранив в контексте значение строки
        Pipeline<String, Integer> pipeline = Pipeline.define(String.class, Integer.class)
                .emit((e, ctx) -> {
                    int length = e.length();
                    ctx.set("input", e);
                    return length;
                })
                .finish((e, ctx) -> e * 10);
        Context context = new Context();
        Integer result = pipeline.execute("Hello", context);
        assert result == 50;
        assert context.get("input").equals("Hello");
    }


}
