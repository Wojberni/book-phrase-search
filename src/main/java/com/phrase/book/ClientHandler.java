package com.phrase.book;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread{
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private ElasticsearchClient elasticClient;

    public ClientHandler(Socket socket, ElasticsearchClient elasticClient) {
        this.clientSocket = socket;
        this.elasticClient = elasticClient;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while(true){
                String inputLine;
                inputLine = in.readLine();
                if("quit".equals(inputLine))
                    break;
                if(!inputLine.isBlank())
                    findPhrase(inputLine);
            }
            in.close();
            out.close();
            clientSocket.close();
        } catch (java.io.IOException E) {
            System.out.println(E.getMessage());
        }
    }

    public void findPhrase(String phrase){
        try{
            SearchResponse<BookProperties> search = elasticClient.search(s -> s
                            .index("books")
                            .query(q -> q
                                    .term(t -> t
                                            .field("verse")
                                            .value(v -> v.stringValue(phrase))
                                    )),
                    BookProperties.class);
            if(!search.hits().hits().isEmpty()){
                for (Hit<BookProperties> hit: search.hits().hits()) {
                    out.println("----------SEARCH-RESULT-----------");
                    out.println("Author: " + hit.source().author);
                    out.println("Book: " + hit.source().book);
                    out.println("Verse: " + hit.source().verse);
                    out.println("Line number: " + hit.source().line_nr + "\n");
                }
            }
            else{
                out.println("No matches!\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
