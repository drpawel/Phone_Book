package phonebook;

public class Person {
    private String number,name,surname;

    public Person(String number, String name, String surname) {
        this.number = number;
        this.name = name;
        this.surname = surname;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
