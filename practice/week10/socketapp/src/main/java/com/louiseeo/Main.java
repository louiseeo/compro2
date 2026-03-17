package com.louiseeo;

import com.google.gson.Gson;
import com.louiseeo.model.Task;
import com.louiseeo.services.NetworkService;

public class Main {
    public static void main(String[] args) {
        NetworkService ns = new NetworkService();
        String host = "jsonplaceholder.typicode.com";
        int port = 80;
        String path = "/todos/1";

        String response = ns.fetchData(host, port, path);
        Gson gson = new Gson();
        Task task = gson.fromJson(response, Task.class);
        
        System.out.println(task);
    }
}