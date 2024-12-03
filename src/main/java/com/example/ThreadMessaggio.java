package com.example;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ThreadMessaggio extends Thread {
    private final Socket socket;

    public ThreadMessaggio(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scan = new Scanner(System.in)) {
            
            // Menu principale
            while (!socket.isClosed()) {
                
                String scelta = scan.nextLine();

                switch (scelta) {
                    case "1":
                        System.out.print("Messaggio globale: ");
                        String messaggioGlobale = scan.nextLine();
                        out.writeBytes("Mess:" + messaggioGlobale + "\n");
                        break;
                    case "2":
                        System.out.print("Destinatario: ");
                        String destinatario = scan.nextLine();
                        System.out.print("Messaggio: ");
                        String messaggioPrivato = scan.nextLine();
                        out.writeBytes("MessP:" + destinatario + ":" + messaggioPrivato + "\n");
                        break;
                    case "3":
                        out.writeBytes("Disc\n");
                        socket.close();
                        return;
                    default:
                        System.out.println("Error : Scelta non valida.");
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("!Connessione interrotta.");
        }
    }
}