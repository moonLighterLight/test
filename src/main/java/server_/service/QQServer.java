package server_.service;

import QQ.common.Message;
import QQ.common.MessageType;
import QQ.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * 服务端，监听9999端口
 */
public class QQServer {

    private ServerSocket serverSocket = null;

    private static HashMap<String, User> validUser = new HashMap<>();

    static {
        validUser.put("100", new User("100", "123456"));
        validUser.put("200", new User("200", "123456"));
        validUser.put("300", new User("300", "123456"));
        validUser.put("张三", new User("张三", "123456"));
        validUser.put("李四", new User("李四", "123456"));

    }

    public boolean checkUser(String id, String pwd){
        User user = validUser.get(id);
        if (user == null){
            return false;
        }
        if (!user.getPassword().equals(pwd)){
            return false;
        }
        return true;
    }

    public QQServer(){
        try {
            System.out.println("服务端在9999号端口监听：");
            serverSocket = new ServerSocket(9999);
            while (true){//当和客户端建立连接时就会一直监听，所以使用while循环
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User)ois.readObject();

                Message message = new Message();//回复给客户端的对象
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                //验证用户名和密码
                if (checkUser(user.getId(), user.getPassword())) {//验证通过
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);
                    //创建一个线程和客户端保持通信，该线程需要持有socket对象
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, user.getId());
                    serverConnectClientThread.start();
                    System.out.println("用户ID:" + user.getId() + ",密码:" + user.getPassword() + "登录成功！");
                    //将线程对象放入集合中,进行管理
                    ManageClientThreads.addClientThread(user.getId(), serverConnectClientThread);

                }else {
                    System.out.println("用户ID:" + user.getId() + ",密码:" + user.getPassword() + "登录失败！");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
