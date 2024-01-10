package mailclient.com.EmailAPI;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PingServer {
    private String host;
    private int port;

    public PingServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void setHost(String host) {

    }

    public boolean pingServer() {
        try (Socket socket = new Socket()) {
            InetSocketAddress socketAddress = new InetSocketAddress(host, port);
            socket.connect(socketAddress, 1000); // Timeout in milliseconds

            // The server is reachable
            return true;
        } catch (IOException e) {
            // Failed to connect to the server
            return false;
        }
    }
}
