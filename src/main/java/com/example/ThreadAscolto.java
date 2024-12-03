package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadAscolto extends Thread {
    private final Socket socket;

    public ThreadAscolto(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String messaggio;
            while ((messaggio = in.readLine()) != null) {
                if (messaggio.startsWith("Mess:")) {
                    // Messaggio pubblico
                    String contenuto = messaggio.substring(5);
                    System.out.println(contenuto);
                } else if (messaggio.startsWith("MessP:")) {
                    // Messaggio privato
                    String[] parti = messaggio.split(":", 3);
                    if (parti.length == 3) {
                        String destinatario = parti[1];
                        String contenuto = parti[2];
                        System.out.println("Messaggio privato da " + destinatario + ": " + contenuto);
                    } else {
                        System.out.println("Errore: Formato di MessP non valido.");
                    }
                } else if (messaggio.equals("Disc")) {
                    // Disconnessione
                    System.out.println("Comando di disconnessione ricevuto.");
                    break; // Uscita dal ciclo per chiudere la connessione
                } else if (messaggio.startsWith("!")) {
                    // Messaggio di errore
                    String errore = messaggio.substring(1); 
                    System.out.println("Errore: " + errore);
                } else if (messaggio.equals("?")) {
                    // Comando non valido
                    System.out.println("Errore: Comando non valido.");
                } else if (messaggio.startsWith("Join:")) {
                    // Notifica utente connesso
                    String nomeUtente = messaggio.substring(5);
                    System.out.println("Utente connesso: " + nomeUtente);
                } else if (messaggio.startsWith("Left:")) {
                    // Notifica utente disconnesso
                    String nomeUtente = messaggio.substring(5);
                    System.out.println("Utente disconnesso: " + nomeUtente);
                } else {
                    // Messaggio non riconosciuto
                    System.out.println("Messaggio sconosciuto: " + messaggio);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura del messaggio: " + e.getMessage());
        }
    }
}