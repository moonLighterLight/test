package server_.service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 该类用于管理和客户端通信的线程
 */
public class ManageClientThreads {
    private static HashMap<String, ServerConnectClientThread> hashMap = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getHashMap() {
        return hashMap;
    }

    public static void addClientThread(String id, ServerConnectClientThread serverConnectClientThread){
        hashMap.put(id, serverConnectClientThread);
    }

    public static ServerConnectClientThread getClientThread(String id){
        return hashMap.get(id);
    }

    public static void removeServerConnectClientThread(String id){
        hashMap.remove(id);
    }

    public static String getOnlineUser(){
        //遍历集合，遍历hashmap的key
        String onLineUserList = "";
        Iterator<String> iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()){
            onLineUserList += iterator.next().toString() + " ";
        }
        return onLineUserList;
    }
}
