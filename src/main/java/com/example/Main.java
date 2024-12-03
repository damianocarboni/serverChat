package com.example;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 3000)) {
    Toolkit.getDefaultToolkit().beep();
    System.out.println("Connesso al server.");
    Scanner scan = new Scanner(System.in);
    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Per leggere risposte dal server
    
    String username;
    boolean usernameAccettato = false;
    
    // Loop finché non si ottiene uno username valido
    while (!usernameAccettato) {
        System.out.print("Inserisci il tuo username: ");
        username = scan.nextLine();
        out.writeBytes(username + "\n"); // Invia lo username al server

        // Attende la risposta del server
        String risposta = in.readLine();
        if (!risposta.startsWith("!")) { 
            usernameAccettato = true;
            System.out.println("Username accettato!");
        } else  {
            System.out.println("Errore: " + risposta.substring(1)); // Mostra il messaggio di errore senza "!"
        } 
    }

    ThreadAscolto ascolto = new ThreadAscolto(socket);
    ascolto.start();
    ThreadMessaggio messaggio = new ThreadMessaggio(socket);
    messaggio.start();
    
    Thread.sleep(1000);
    System.out.println("\nMenu:");
    System.out.println("1) Chat globale");
    System.out.println("2) Messaggio privato");
    System.out.println("3) Disconnetti");
    
    ascolto.join();
    messaggio.join();
            
            

        } catch (IOException | InterruptedException e) {
            System.err.println("!Errore: " + e.getMessage());
        }
    }
}