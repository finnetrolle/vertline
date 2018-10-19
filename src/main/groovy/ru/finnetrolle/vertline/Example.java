package ru.finnetrolle.vertline;

import org.springframework.stereotype.Component;
import ru.finnetrolle.vertline.pipeline.*;

import javax.annotation.PostConstruct;
import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.finnetrolle.vertline.pipeline.Action.pipeline;
import static ru.finnetrolle.vertline.pipeline.Action.split;

@Component
public class Example {

    @PostConstruct
    public void init() {

        UserList list = new UserList();
        list.users = Arrays.asList(
                new User("Ivan", "Petrov"),
                new User("Nick", "Pavlov"));

        Pipeline<Request, byte[]> documentPipe = pipeline(Request.class, byte[].class)
                .emit(enricher)
                .emit(split(EnrichedSubscription.class, byte[].class)
                        .flow(pipeline(EnrichedSubscription.class, byte[].class)
                                .emit(documentAFactory)
                                .finish(csvRenderer))
                        .flow(pipeline(EnrichedSubscription.class, byte[].class)
                                .emit(documentBFactory)
                                .emit(split(DocumentBContent.class, byte[].class)
                                        .flow(new JasperRenderer("XLS"))
                                        .flow(new JasperRenderer("XLSX"))
                                        .flow(new JasperRenderer("PDF"))
                                        .thenJoin(new Zipper("b.zip")))
                                .finish())
                        .thenJoin(new Zipper("out.zip")))
                .finish();
        byte[] out = documentPipe.emit(request, new Context());


        Pipeline<String, String> pipe = pipeline(String.class, String.class)
                .emit((s,ctx) -> LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .emit(split(LocalDate.class, String.class)
                        .flow((s,ctx) -> s.getDayOfMonth())
                        .flow((s,ctx) -> s.getMonthValue())
                        .flow((s,ctx) -> s.getYear())
                        .thenJoin((lst,ctx) -> lst.stream().map(String::valueOf).collect(Collectors.joining("."))))
                .finish((s,ctx) -> "'" + s + "'");

        String result = pipe.execute("2018.01.01", new Context());
        System.out.println(result);


        Pipeline<String, String> pipeline = pipeline(String.class, String.class)
                .emit((s,ctx) -> LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .emit(split(LocalDate.class, String.class)
                        .flow((s, ctx) -> s.getDayOfMonth())
                        .flow((s, ctx) -> s.getMonthValue())
                        .flow(split(LocalDate.class, Integer.class)
                                .flow((s, ctx) -> s.getDayOfWeek().getValue())
                                .flow((s, ctx) -> s.getMonthValue() - 1)
                                .thenJoin((in, ctx) -> in.stream().reduce((a, b) -> a + b).get()))
                        .flow((s, ctx) -> s.getYear())
                        .flow(pipeline(LocalDate.class, Integer.class)
                                .emit((s, ctx) -> s.getYear() * 365 + s.getMonthValue() * 30 + s.getDayOfMonth())
                                .finish((s, ctx) -> s += s))
                        .thenJoin(new SuperJoiner()))
                .finish(new Skobochker());

        Context ctx = new Context();
        System.out.println(pipeline.execute("2018.12.05", ctx));

        System.out.println(ctx);
    }

    public static class UserList {
        List<User> users;
    }

    public static class User {
        public String name;
        public String surname;

        public User(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }
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
            String out = "-= " + in + " =-";
            context.set("skobochker", out);
            return out;
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
