package com.lexi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static List<String> getListCSV(){
        return null;
    }
    // get file from classpath, resources folder
    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }

    private static void printFile(File file) throws IOException {

        if (file == null) return;

        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    public static List<List<String>> getListFromFile(File file) {

        List<List<String>> list =  new ArrayList<>();

        if (file == null) return null;

        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                list.add(Arrays.asList(values));
            }
        }catch(Exception e){

        }

        return list;
    }

    public void sendMail(String to, String from, String subject, String content){


        String host = "smtp.titan.email";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("noresponda@aynitech.online", "PGDerecho2024.");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(content);


            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....\n");
            System.out.println("**************************************");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        /*
        Sources:
        https://netcorecloud.com/tutorials/send-email-in-java-using-gmail-smtp/
        https://programmerclick.com/article/24622109471/
        */

        // Recipient's email ID needs to be mentioned.
        Main main = new Main();
        File file = main.getFileFromResources("send1email.csv");
        String subject = "CREDENCIALES DE ACCESO A TEAMS - POSGRADO FCJyP UMSS";
        String fromEmail = "noresponda@aynitech.online";
        List<List<String>> list = main.getListFromFile(file);
        Iterator<List<String>> it= list.iterator();
        while(it.hasNext()) {
            List<String> row = it.next();
            String name = row.get(1);
            String ci = row.get(2);
            String toEmail = row.get(5);
            String emailTeams = row.get(10);
            String passwordTeams = row.get(11);
            String footer = "\n Atentamente, \n IT POSGRADO FCJyP UMSS";
            String content = "Estimad@ "+name+":\n Con CI="+ci+", sus credenciales de Teams son los siguientes:\n"+
                    "Correo Teams: "+emailTeams+" \n"+
                    "Password Teams: "+passwordTeams+
                    footer;

            main.sendMail(toEmail, fromEmail, subject, content);
        }

    }

}
