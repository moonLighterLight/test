package start;

import server_.service.QQServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Start {
    public static void main(String[] args) {
        new QQServer();
//        try {
//            System.out.println(InetAddress.getLocalHost());//192.168.56.1
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
    }
}
