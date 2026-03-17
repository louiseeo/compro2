package com.louiseeo.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkService {
    private String host;
    private int port;

    public NetworkService(String host, int port){
        this.host = host;
        this.port = port;
    }
    public String fetchData(String host, int port, String path) {
        StringBuilder response = new StringBuilder();
        // socket
        try (Socket socket = new Socket(host, port);
                PrintWriter requestWriter = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader responsReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ){
                System.out.print("Connected to server...");
                //send an HTTP request Method, Path, Protocol version, Host header, blank line
                requestWriter.println("GET " + path + " HTTP/1.1");
                requestWriter.println("Host: " + host);
                requestWriter.println("User-agent: Java/SocketDemo");
                requestWriter.println("Accept: application/json");
                requestWriter.println("Connection: close");
                requestWriter.println("\r\n"); //ends request header
                System.out.println("\n--- HTTP Response Headers ---");

                String line;
                boolean isBody = false;
                while ((line = responsReader.readLine()) != null){
                    if (line.isEmpty() && !isBody) {
                        System.out.println("[Header] " + line);
                        isBody = true;
                        continue;
                    }

                    if(isBody)
                    response.append(line);
                }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    public void sendData(String host, )
}
