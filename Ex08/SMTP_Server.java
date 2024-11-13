import java.io.*;
import java.net.*;
import java.util.*;

public class SMTP_Server {

    public static void main(String[] args) throws Exception {
        int port = 25;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("SMTP Server started on port " + port);
        
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New connection from " + clientSocket.getInetAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            out.println("220 Welcome to SMTP server");

            String clientMessage;
            String sender = null;
            String recipient = null;
            StringBuilder emailBody = new StringBuilder();

            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Received: " + clientMessage);

                if (clientMessage.startsWith("HELO")) {
                    out.println("250 Hello " + clientSocket.getInetAddress().getHostName());
                } else if (clientMessage.startsWith("MAIL FROM:")) {
                    sender = clientMessage.substring(10);
                    out.println("250 OK");
                } else if (clientMessage.startsWith("RCPT TO:")) {
                    recipient = clientMessage.substring(8);
                    out.println("250 OK");
                } else if (clientMessage.equals("DATA")) {
                    out.println("354 End data with <CR><LF>.<CR><LF>");
                    while ((clientMessage = in.readLine()) != null) {
                        if (clientMessage.equals(".")) {
                            break;
                        }
                        emailBody.append(clientMessage).append("\n");
                    }
                    out.println("250 OK: Message accepted for delivery");
                    System.out.println("Email sent from " + sender + " to " + recipient);
                    System.out.println("Email Body:\n" + emailBody);
                } else if (clientMessage.equals("QUIT")) {
                    out.println("221 Bye");
                    break;
                } else {
                    out.println("500 Syntax error, command unrecognized");
                }
            }

            in.close();
            out.close();
            clientSocket.close();
        }
    }
}
