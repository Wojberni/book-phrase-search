package com.phrase.book;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class DOM
{

    public static String login_user(String username, String password){
        try{
            File file = new File("database.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("user");
            for (int itr = 0; itr < nodeList.getLength(); itr++)
            {
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    if(username.equals(eElement.getElementsByTagName("username").item(0).getTextContent())){
                        if(password.equals(eElement.getElementsByTagName("password").item(0).getTextContent())){
                            return "Logged in!";
                        }
                        else
                            return "Wrong password! Try again!";
                    }
                }
            }
            return "Wrong login! Try again!";
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return "";
    }

    public static String forgot_password(String username){
        try{
            File file = new File("database.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("user");
            for (int itr = 0; itr < nodeList.getLength(); itr++)
            {
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    if(username.equals(eElement.getElementsByTagName("username").item(0).getTextContent())){
                        return "Your password is: " + eElement.getElementsByTagName("password").item(0).getTextContent();
                    }
                }
            }
            return "Wrong login! Try again!";
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return "";
    }

    public static String register_user(String username, String password){
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse("database.xml");
            Element root = document.getDocumentElement();
            Element rootElement = document.getDocumentElement();

            Element server = document.createElement("user");
            rootElement.appendChild(server);

            Element name = document.createElement("username");
            name.appendChild(document.createTextNode(username));
            server.appendChild(name);

            Element pass = document.createElement("password");
            pass.appendChild(document.createTextNode(password));
            server.appendChild(pass);

            root.appendChild(server);

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult("database.xml");
            transformer.transform(source, result);
            return "Registered successfully!";
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return "";
    }

}
