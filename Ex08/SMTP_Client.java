import java.io.*;
import java.net.*;

public class SMTP_Client {

    public static void main(String[] args) throws Exception {
        String serverAddress = "localhost";
        int port = 25;
        Socket socket = new Socket(serverAddress, port);
        System.out.println("Connected to SMTP server");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println(in.readLine());

        out.println("HELO localhost");
        System.out.println(in.readLine());

        out.println("MAIL FROM:<sender@example.com>");
        System.out.println(in.readLine());

        out.println("RCPT TO:<recipient@example.com>");
        System.out.println(in.readLine());

        out.println("DATA");
        System.out.println(in.readLine());

        out.println("Subject: Test Email");
        out.println("Hello, this is a test email sent via a simple SMTP server.");
        out.println(".");
        System.out.println(in.readLine());

        out.println("QUIT");
        System.out.println(in.readLine());

        socket.close();
        System.out.println("Disconnected from SMTP server");
    }
}
