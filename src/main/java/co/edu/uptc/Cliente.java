package co.edu.uptc;

import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        String direccionServidor = "localhost"; // IP del servidor
        int puerto = 1234;

        try (Socket socket = new Socket(direccionServidor, puerto)) {
            System.out.println("Conectado al servidor en " + direccionServidor + ":" + puerto);

            // Hilo para recibir mensajes del servidor
            Thread recibirMensajes = new Thread(() -> {
                try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String mensaje;
                    while ((mensaje = entrada.readLine()) != null) {
                        System.out.println("Servidor: " + mensaje);
                    }
                } catch (IOException e) {
                    System.out.println("Conexión cerrada por el servidor.");
                }
            });

            // Hilo para enviar mensajes al servidor
            Thread enviarMensajes = new Thread(() -> {
                try (PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader consola = new BufferedReader(new InputStreamReader(System.in))) {
                    String mensaje;
                    while (true) {
                        mensaje = consola.readLine(); // Lee mensaje desde la consola del cliente
                        salida.println(mensaje); // Envía mensaje al servidor
                    }
                } catch (IOException e) {
                    System.out.println("Error al enviar mensaje.");
                }
            });

            // Iniciar los hilos
            recibirMensajes.start();
            enviarMensajes.start();

            // Espera a que terminen los hilos5j
            recibirMensajes.join();
            enviarMensajes.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
