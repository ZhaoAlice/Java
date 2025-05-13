package lang.java8.streams;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈〉<br>
 *
 * @author 0027009101
 * @create 2024/7/3
 * @since 1.0.0
 */
public class SortStream {
    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 35)
        );

        // 根据 age 字段进行排序
        List<Person> sortedByAge = people.stream()
            .sorted(Comparator.comparingInt(Person::getAge))
            .toList();

        // 输出排序后的列表
        sortedByAge.forEach(System.out::println);
    }

    public static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + '}';
        }
    }

}