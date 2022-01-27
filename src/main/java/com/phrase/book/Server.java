package com.phrase.book;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<Socket> clientSockets;
    private ElasticsearchClient elasticClient;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clientSockets = new ArrayList<>();
            elasticClient = RestClientElastic.clientConfig();
            RestClientElastic.createIndex(elasticClient);
            RestClientElastic.createDatabase();
            while (true){
                Socket clientSocket = serverSocket.accept();
                clientSockets.add(clientSocket);
                new ClientHandler(clientSocket, elasticClient).start();
                System.out.println("New client connected at :" + clientSocket.toString());
            }
        } catch (java.io.IOException E) {
            System.out.println(E.getMessage());
        }finally {
            stop();
        }
    }

    public void stop() {
        try {
            for(Socket iter : clientSockets)
                iter.close();
            clientSockets.clear();
            serverSocket.close();
        } catch (java.io.IOException E) {
            System.out.println(E.getMessage());
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(6666);
    }

}
