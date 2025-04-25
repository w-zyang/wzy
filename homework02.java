package wzy_homework01_;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author wzy
 * @version 1.0
 */
@SuppressWarnings({"all"})
public class homework02 {
static class Dog{
    String name;
    int age;

    public Dog() {
    }

    public Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
    public static void main(String[] args) {

Vector dogs = new Vector();
for (int i = 0; i < 12; i++) {
    dogs.add(new Dog("Dog" + i, i));
}
Iterator it = dogs.iterator();
        while (it.hasNext()) {
            Object next =  it.next();
            System.out.println(next);
        }
        for (Object o :dogs) {
            System.out.println(o);
        }

    }
}
