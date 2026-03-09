package com.lounic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.lounic.model.Person;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String json = "";

        Scanner sc = new Scanner(new File("practice/week9/demo/data/person.json"));
        while (sc.hasNextLine()) {
            json += sc.nextLine();
        }

        Gson gson = new Gson();
        Person person = gson.fromJson(json, Person.class);

        person.setFirstName("gir");
        System.out.println(person.toString());

        sc.close();
    }
}