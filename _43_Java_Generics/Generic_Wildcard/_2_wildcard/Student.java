package _43_Java_Generics.Generic_Wildcard._2_wildcard;

public class Student extends Person {
    public Student(String name, int age, String address) {
        super(name, age, address);
    }

    @Override
    public void say() {
        System.out.println("I am Student !!!");
    }
}
