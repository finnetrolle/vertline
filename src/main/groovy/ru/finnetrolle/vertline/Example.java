package ru.finnetrolle.vertline;

import org.springframework.stereotype.Component;
import ru.finnetrolle.vertline.pipeline.Context;
import ru.finnetrolle.vertline.pipeline.Action;
import ru.finnetrolle.vertline.pipeline.Pipeline;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class Example {

    @PostConstruct
    public void init() {

        Pipeline<String, String> pipeline = Pipeline.define(String.class, String.class)
                .emit(new DateParser())
                .emit(new DayExtractor())
                .emit(new Writer())
                .finish(Pipeline.define(String.class, String.class)
                        .finish((s, z) -> {
                            String ss = "-="+s+"=-";
                            z.set("lambda", ss);
                            return ss;
                        }));

        Context ctx = new Context();
        System.out.println(pipeline.execute("2018.12.05", ctx));

        System.out.println(ctx);
    }

    static class Writer implements Action<Integer, String> {

        @Override
        public String execute(Integer in, Context ctx) {
            String a = "";
            for (Integer i = 0; i < in; i++) {
                a += "Ke";
            }
            ctx.set("writer", a);
            return a;
        }
    }

    static class DayExtractor implements Action<LocalDate, Integer> {

        @Override
        public Integer execute(LocalDate in, Context ctx) {
            int v = in.getDayOfMonth();
            ctx.set("DE", v);
            return v;
        }
    }

    static class DateParser implements Action<String, LocalDate> {

        @Override
        public LocalDate execute(String in, Context ctx) {
            LocalDate localDate = LocalDate.parse(in, DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            ctx.set("DP", localDate);
            return localDate;
        }
    }

}
