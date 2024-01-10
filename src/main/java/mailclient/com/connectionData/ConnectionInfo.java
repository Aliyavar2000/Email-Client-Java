package mailclient.com.connectionData;

public class ConnectionInfo {
    private static String incomingHost;
    private static String outgoingHost;
    private static int incomingPort;
    private static int outgoingPort;
    private static String incomingprotocol;

    public static void initialize(String incomingHost, int incomingPort, String incomingProtocol, String outgoingHost,
            int outgoingPort) {
        ConnectionInfo.incomingHost = incomingHost;
        ConnectionInfo.outgoingHost = outgoingHost;
        ConnectionInfo.incomingPort = incomingPort;
        ConnectionInfo.outgoingPort = outgoingPort;
        ConnectionInfo.incomingprotocol = incomingProtocol;
    }

    public static String getincomingHost() {
        return incomingHost;
    }

    public static String getoutgoingHost() {
        return outgoingHost;
    }

    public static int getincomingPort() {
        return incomingPort;
    }

    public static int getoutgoingPort() {
        return outgoingPort;
    }

    public static String getincomingProtocol() {
        return incomingprotocol;
    }
}
