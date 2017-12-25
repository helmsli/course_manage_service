package com.company.websocket.service;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.company.websocket.domain.ClientSession;

@ServerEndpoint(value = "/webSocket/{userId}")
@Component
public class WebSocketService {
	 //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private  int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private  Map<String,ClientSession> webSocketMap = new java.util.concurrent.ConcurrentHashMap<String,ClientSession>();
    private TreeMap<String,ClientSession> webSocketTreeMap = new TreeMap<String,ClientSession>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    
    
    protected String getSessionKey(Session session)
    {
    	return "s:" + session.getId();
    }

    protected String getUserSessionKey(String userId)
    {
    	return "u:" + userId;
    }
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(@PathParam("userId")String userId,Session session) {
        ClientSession clientSession= new ClientSession();
        clientSession.setSession(session);
        webSocketMap.put(getSessionKey(session),clientSession);     //加入set中
        webSocketMap.put(getUserSessionKey(userId),clientSession);     //加入set中
        
        addOnlineCount();           //在线数加1
        System.out.println("有新连接:"+userId +  " 加入！当前在线人数为" + getOnlineCount());
        try {
            sendMessage(clientSession,"welcome ");
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId")String userId,Session session) {
    	webSocketMap.remove(getSessionKey(session));     //加入set中
        subOnlineCount();           //在线数减1
        System.out.println("有一连接"+userId +"关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);

        //群发消息
        
        for (String keys : webSocketMap.keySet()){
            try {
            	ClientSession item = webSocketMap.get(keys);
                sendMessage(item,message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(ClientSession clientSession,String message) throws IOException {
    	clientSession.getSession().getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public void sendMessage(String userId,String message) throws IOException {
    	ClientSession clientSession = this.webSocketMap.get(getUserSessionKey(userId));
    	clientSession.getSession().getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     * */
    public  void sendToAll(String message) throws IOException {
    	  for (String keys : webSocketMap.keySet()){
              try {
              	ClientSession item = webSocketMap.get(keys);
                  sendMessage(item,message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public  synchronized int getOnlineCount() {
        return onlineCount;
    }

    public  synchronized void addOnlineCount() {
    	onlineCount++;
    }

    public  synchronized void subOnlineCount() {
    	onlineCount--;
    }
}
