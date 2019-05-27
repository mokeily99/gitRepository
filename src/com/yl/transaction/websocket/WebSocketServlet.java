package com.yl.transaction.websocket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

//在相对路径中发布端点websocket
@ServerEndpoint("/websocket/{seatID}")
public class WebSocketServlet {
	CallStatusThread thread1 = new CallStatusThread();
	Thread thread = new Thread(thread1);
	// 用来存放每个客户端对应的MyWebSocket对象。
	private static CopyOnWriteArraySet<WebSocketServlet> webSocketSet = new CopyOnWriteArraySet<WebSocketServlet>();
	private javax.websocket.Session session = null;

	/**
	 * @ClassName: onOpen
	 * @Description: 开启连接的操作
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("seatID") String seatID) throws IOException {
		this.session = session;
		webSocketSet.add(this);
		System.out.println(webSocketSet);
		// 开启一个线程对数据库中的数据进行轮询
		thread1.setSeatID(seatID);
		thread1.setSession(session);
		thread.start();

	}

	/**
	 * @ClassName: onClose
	 * @Description: 连接关闭的操作
	 */
	@OnClose
	public void onClose() {
		thread1.stopMe();
		webSocketSet.remove(this);
	}

	/**
	 * @ClassName: onMessage
	 * @Description: 给服务器发送消息告知数据库发生变化
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("发生变化" + message);
		try {
			sendMessage(message, session);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @ClassName: OnError
	 * @Description: 出错的操作
	 */
	@OnError
	public void onError(Throwable error) {
		System.out.println(error);
		error.printStackTrace();
	}

	/**
	 * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
	 * 
	 * @throws IOException
	 *             发送自定义信号，“1”表示告诉前台，数据库发生改变了，需要刷新
	 */
	public void sendMessage(String message, Session session) throws IOException {
		// 群发消息
		session.getBasicRemote().sendText(message);
	}
}