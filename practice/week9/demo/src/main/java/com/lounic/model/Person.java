package com.lounic.model;

public class Person {
    private String firstName;
    private String lastName;
    private int age;
    private String emailAddress;
    private String phoneNumber;
    private String dateOfBirth;
    private String homeAddress;
    private boolean isEmployed;
    private String nationality;
    private String gender;

    public Person(){}

    public Person(String firstName, String lastName, int age, String emailAddress, String phoneNumber,
        String dateOfBirth, String homeAddress, boolean isEmployed, String gender, String nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.homeAddress = homeAddress;
        this.isEmployed = isEmployed;
        this.nationality = nationality;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public boolean isEmployed() {
        return isEmployed;
    }

    public void setEmployed(boolean isEmployed) {
        this.isEmployed = isEmployed;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return String.format("""
                First Name: %s
                Last Name: %s
                Age: %d
                Email Address: %s
                Phone Number: %s
                Date of Birth: %s
                Home Address: %s
                Employed: %s
                Nationality: %s
                Gender: %s
                """, firstName, lastName, age, emailAddress, phoneNumber, dateOfBirth, homeAddress, isEmployed, nationality, gender);
    }

}
