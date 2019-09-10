package Client;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientMain {
    public static void main(String[] args) {
        try {
            InetAddress address = InetAddress.getByName("192.168.1.67");
//            Client client = new Client(address, "Hayden");
//            client.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
