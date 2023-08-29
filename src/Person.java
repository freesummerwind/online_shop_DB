public abstract class Person {
    private String name;
    private String surname;
    private int age;

    abstract String display();

    public void setName(String name) throws Exception {
        if (name.isEmpty()) {
            throw new Exception("Name can't be empty");
        }
        this.name = name;
    }

    public void setSurname(String surname) throws Exception {
        if (surname.isEmpty()) {
            throw new Exception("Surname can't be empty");
        }
        this.surname = surname;
    }

    public void setAge(int age) throws Exception {
        if (age < 0 || age > 150) {
            throw new Exception("Age should be a positive number less than 150");
        }
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }
}
