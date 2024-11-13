import java.net.*;
import java.util.Scanner;

public class DNSServer {

    private static final int DNS_PORT = 50;

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(DNS_PORT);
        System.out.println("DNS Server is running on port " + DNS_PORT);

        byte[] receiveData = new byte[1024];
        
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a domain name or IP address (or type 'exit' to quit):");
            String request = sc.nextLine();

            if (request.equalsIgnoreCase("exit")) {
                break;
            }

            String response = handleRequest(request);
            System.out.println("Server response: " + response);
        }

        socket.close();
        sc.close();
    }

    private static String handleRequest(String request) {
        String response = "Invalid request";
        
        try {
            InetAddress address = InetAddress.getByName(request);
            response = "Domain: " + request + " resolves to IP: " + address.getHostAddress();
        } catch (UnknownHostException e1) {
            try {
                InetAddress address = InetAddress.getByAddress(request.getBytes());
                String hostName = address.getHostName();
                response = "IP: " + request + " resolves to Domain: " + hostName;
            } catch (Exception e2) {
                response = "Unable to resolve: " + request;
            }
        }

        return response;
    }
}
