package com.phrase.book;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Client {

    String username;
    String password;
    boolean logged_in = false;

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void connect(int port){
        try{
            String serverIP = "127.0.0.1";
            clientSocket = new Socket(serverIP, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (!logged_in) {
                System.out.println("Enter 1 to login into your account!");
                System.out.println("Enter 2 to create your account!");
                System.out.println("Enter 3 if your forgot your account password!");
                try {
                    Scanner scanner = new Scanner(System.in);
                    String msg;
                    int input = Integer.parseInt(scanner.nextLine());
                    switch (input) {
                        case 1:
                            System.out.println("Enter username!");
                            username = scanner.nextLine();
                            System.out.println("Enter password!");
                            password = scanner.nextLine();
                            msg = DOM.login_user(username, password);
                            if (msg.equals("Logged in!"))
                                logged_in = true;
                            System.out.println(msg);
                            break;
                        case 2:
                            System.out.println("Enter username!");
                            username = scanner.nextLine();
                            System.out.println("Enter password!");
                            password = scanner.nextLine();
                            msg = DOM.register_user(username, password);
                            System.out.println(msg);
                            break;
                        case 3:
                            System.out.println("Enter username!");
                            username = scanner.nextLine();
                            msg = DOM.forgot_password(username);
                            System.out.println(msg);
                            break;
                        default:
                            System.out.println("Enter correct number from 1 to 3");
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Enter a valid number not a string!");
                }

            }
            while (true) {
                try {
                    System.out.println("Enter phrase to search for! Enter quit to end the program!");
                    Scanner scanner = new Scanner(System.in);
                    String phrase = scanner.nextLine();
                    out.println(phrase);
                    if ("quit".equals(phrase))
                        break;
                    String line;
                    sleep(1000);
                    while (in.ready()) {
                        line = in.readLine();
                        System.out.println(line);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args){

        Client client = new Client();
        client.connect(6666);
    }

}
