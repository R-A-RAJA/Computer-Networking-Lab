import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleHttpServer {
    private static final int PORT = 3702;
    private static final String DOCUMENT_ROOT = "path/to/your/document/root";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleClient(clientSocket);
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            StringBuilder request = new StringBuilder();
            String line;
            while (!(line = in.readLine()).isEmpty()) {
                request.append(line).append("\n");
            }
            System.out.println("Request:\n" + request.toString());
            String requestLine = request.toString().split("\n")[0];
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];
            String responseBody;
            String responseStatus;

            if ("GET".equalsIgnoreCase(method)) {
                if ("/".equals(path)) {
                    responseBody = "<html><body><h1>Welcome to my website!</h1></body></html>\n";
                    responseStatus = "HTTP/1.1 200 OK";
                } else if ("/about".equals(path)) {
                    responseBody = "<html><body><h1>About Us</h1><p>This is the about page.</p></body></html>\n";
                    responseStatus = "HTTP/1.1 200 OK";
                } else {
                    Path filePath = Paths.get(DOCUMENT_ROOT, path);
                    if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                        byte[] fileBytes = Files.readAllBytes(filePath);
                        responseBody = new String(fileBytes);
                        responseStatus = "HTTP/1.1 200 OK";
                    } else {
                        responseBody = "<html><body><h1>404 Not Found</h1></body></html>\n";
                        responseStatus = "HTTP/1.1 404 Not Found";
                    }
                }
            } else {
                responseBody = "<html><body><h1>405 Method Not Allowed</h1></body></html>\n";
                responseStatus = "HTTP/1.1 405 Method Not Allowed";
            }
            String response = responseStatus + "\r\n" +
                              "Content-Type: text/html\r\n" +
                              "Content-Length: " + responseBody.length() + "\r\n" +
                              "Connection: close\r\n" +
                              "\r\n" + 
                              responseBody;

            out.print(response);
            out.flush();
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }
}