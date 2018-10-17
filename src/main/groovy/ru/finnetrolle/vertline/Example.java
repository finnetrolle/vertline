package ru.finnetrolle.vertline;

import org.springframework.stereotype.Component;
import ru.finnetrolle.vertline.pipeline.*;


import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Example {

    @PostConstruct
    public void init() {

//        Pipeline<String, String> pipe = Pipeline.define(String.class, String.class)
//                .emit(new DateParser())
//                .splitTo((a,b) -> {int v = a.getDayOfMonth(); b.set("day", v); return v;})
//                .andTo((a,b) -> {int v = a.getMonthValue(); b.set("month", v); return v;})
//                .andTo((a,b) -> {int v = a.getYear(); b.set("year", v); return v;})
//                .joinWith(new SuperJoiner())
//                .finish(new Skobochker());

//        Pipeline<String, String> pipe = Pipeline.define(String.class, String.class)
//                .emit(new DateParser())
//                .split(SplitBuilder
//                        .to((a,b) -> {int v = a.getDayOfMonth(); b.set("day", v); return v;})
//                        .to((a,b) -> {int v = a.getMonthValue(); b.set("month", v); return v;})
//                        .to((a,b) -> {int v = a.getYear(); b.set("year", v); return v;})
//                ).joinWith(new SuperJoiner())
//                .finish(new Skobochker());

//        SplitBuilder.to((a,b) -> {int v = a.getDayOfMonth(); b.set("day", v); return v;})
//                .to((a,b) -> {int v = a.getDayOfMonth(); b.set("day", v); return v;})
//
//
//        Pipeline<String, String> pipe = Pipeline.define(String.class, String.class)
//                .emit(new DateParser())
//                .split(
//                        new ArrayOfActionsBuilder<LocalDate, Integer>((a,b) -> {int v = a.getDayOfMonth(); b.set("day", v); return v;})
//
//                ).joinWith(new SuperJoiner())
//                .finish(new Skobochker());

        Pipeline<String, String> pipeline = Pipeline.define(String.class, String.class)
                .emit(new DateParser())
                .emit(new DayExtractor())
                .emit(new Writer())
                .finish(Pipeline.define(String.class, Integer.class)
                        .finish((s, z) -> {
                            String ss = "-="+s+"=-";
                            z.set("lambda", ss);
                            return ss.length();
                        }));

//        Pipeline.define(String.class, String.class)
//                .emit(SplitJoin.define(String.class, String.class)
//                        .split(
//                            new DateParser(),
//                            new DateParser(),
//                            new DateParser()
//                ).joinWith(joiner));

        Context ctx = new Context();
        System.out.println(pipe.execute("2018.12.05", ctx));

        System.out.println(ctx);
    }

    static class SuperJoiner implements Joiner<Integer, String> {

        @Override
        public String join(List<Integer> results, Context context) {
            context.set("joiner", results);
            return results.stream().map(String::valueOf).collect(Collectors.joining(","));
        }
    }

    static class Skobochker implements Action<String, String> {

        @Override
        public String execute(String in, Context context) {
            return "-= " + in + " =-";
        }
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
