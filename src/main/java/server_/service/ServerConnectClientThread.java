package server_.service;

import QQ.common.Message;
import QQ.common.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 该类的一个对象和客户端保持通讯
 */
public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String id;

    public ServerConnectClientThread(Socket socket, String id) {
        this.socket = socket;
        this.id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {//线程处于run状态，可以接收或者发送信息
        while (true){
            try {
                System.out.println("服务器和客户端" + id + "保持通讯，读取数据......");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FIREND)){
                    //客户端在线用户列表
                    System.out.println(message.getSender() + " 请求在线用户列表");
                    String onlineUser = ManageClientThreads.getOnlineUser();
                    Message message1 = new Message();
                    message1.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message1.setContent(onlineUser);
                    message1.setGetter(message.getSender());
                    //返回给客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message1);

                }else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    System.out.println(message.getSender() + " 退出...");
                    ManageClientThreads.removeServerConnectClientThread(message.getSender());
                    socket.close();
                    break;
                }else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    //获取接受者的线程
                    ServerConnectClientThread clientThread = ManageClientThreads.getClientThread(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(clientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                }else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)){
                    //遍历线程管理结合，将所有线程的socket得到，发送message
                    HashMap<String, ServerConnectClientThread> hashMap = ManageClientThreads.getHashMap();
                    Iterator<String> iterator = hashMap.keySet().iterator();
                    while (iterator.hasNext()){
                        String next = iterator.next();
                        if (!next.equals(message.getSender())){
                            ObjectOutputStream oos = new ObjectOutputStream(hashMap.get(next).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                }else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    ServerConnectClientThread clientThread = ManageClientThreads.getClientThread(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(clientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                }
                else {
                    System.out.println("别的类型，不做处理......");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
