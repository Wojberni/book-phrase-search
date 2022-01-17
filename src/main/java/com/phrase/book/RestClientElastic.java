package com.phrase.book;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class RestClientElastic {

    static ElasticsearchClient clientConfig() {
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200)).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    static void createIndex(ElasticsearchClient client){
        try{
            CreateIndexResponse createResponse = client.indices()
                    .create(c -> c
                            .index("books")
                            .aliases("booksLibrary", a -> a
                                    .isWriteIndex(true)
                            )
                    );
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    static void createDatabase(){

        System.out.println("Started creating database!");
        File folder = new File("./books/");
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            String filename = listOfFile.getName();
            String[] result = filename.split(" - ");
            String author = result[0];
            String book = result[1].substring(0, result[1].lastIndexOf('.'));
            try (BufferedReader br = new BufferedReader(new FileReader("./books/" + filename))) {
                String line;
                int lineNumber = 0;
                HttpClient client = HttpClient.newHttpClient();
                while ((line = br.readLine()) != null) {
                    if (!line.isBlank()){
                        HttpRequest request = postJSON(book, author, line, lineNumber);
                        client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
                    }
                    lineNumber++;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Ended creating database!");

    }

    static HttpRequest postJSON(String book, String author, String verse, int line) throws IOException
    {
        try{
            URI uri = new URI("http://localhost:9200/books/_doc");
            Map<String,String> map = new HashMap<String, String>();
            map.put("book", book);
            map.put("author", author);
            map.put("verse", verse);
            map.put("line_nr", Integer.toString(line));
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(map);

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(requestBody))
                    .build();
            return request;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
