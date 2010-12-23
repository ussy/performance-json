package net.pshared.performance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.map.ObjectMapper;

public class Main {

    public static void main(String[] args) throws Exception {
        benchDecode();
    }

    static void benchEncode() throws Exception {
        Result result = createResult(1000);

        // すぶり
        long start = System.currentTimeMillis();
        ObjectMapper mapper = new ObjectMapper();
        mapper.getSerializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US));
        mapper.writeValueAsString(result);
        long end = System.currentTimeMillis();
        System.out.println("Jackson:" + (end - start));
        //

        final int count = 100;
        start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            mapper.writeValueAsString(result);
        }
        end = System.currentTimeMillis();

        System.out.println("Jackson:" + ((end - start) / count));
    }

    static void benchDecode() throws Exception {
        String json = readJson();
        // すぶり
        long start = System.currentTimeMillis();
        ObjectMapper mapper = new ObjectMapper();
        mapper.getSerializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US));
        mapper.readValue(json, Result.class);
        long end = System.currentTimeMillis();
        System.out.println("Jackson:" + (end - start));

        final int count = 100;
        start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            mapper.readValue(json, Result.class);
        }
        end = System.currentTimeMillis();

        System.out.println("Jackson:" + ((end - start) / count));
    }

    static String readJson() throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("../dump.json"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            return buffer.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    static Result createResult(long num) {
        Result result = new Result();
        for (long i = 0; i < num; i++) {
            Person person = new Person();
            person.id = i;
            person.age = (int) (i % 100);
            person.name = "たろう" + i;
            person.birthday = new Date(System.currentTimeMillis() + i);

            List<Person> children = new ArrayList<Person>();
            for (int j = 0; j < 5; j++) {
                Person child = new Person();
                child.id = j;
                child.age = (int) (i % 100);
                child.name = "たろう" + i;
                child.birthday = new Date(System.currentTimeMillis() + i);
                children.add(child);
            }

            person.children = children;
            result.persons.add(person);
        }

        return result;
    }

    static class Result {

        public List<Person> persons = new ArrayList<Person>();
    }

    static class Person {

        public long id;

        public int age;

        public String name;

        public Date birthday;

        public List<Person> children = new ArrayList<Person>();

    }

}
