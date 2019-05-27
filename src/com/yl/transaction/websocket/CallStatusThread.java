package com.yl.transaction.websocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import org.apache.ibatis.session.SqlSession;

import com.yl.common.dao.PublicDao;
import com.yl.common.util.DBUtil;
import com.yl.transaction.conver.dao.ConverDao;
import com.yl.transaction.conver.service.impl.ConverServiceImpl;

public class CallStatusThread implements Runnable {
	private boolean stopMe = true;
	
	private String seatID = "";
	
	private Session session = null;

	public void stopMe() {
		stopMe = false;
	}

	public void setSeatID(String seatID){
		this.seatID = seatID;
	}
	
	public void setSession(Session session){
		this.session=session;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		WebSocketServlet wbs = new WebSocketServlet();
		SqlSession sqlSession = null;
		while (stopMe) {
			try {
				sqlSession = DBUtil.getDefaultSqlSession();
				ConverDao converMapper = (ConverDao)sqlSession.getMapper(ConverDao.class);
				
				Map<String, String> param = new HashMap<String, String>();
				param.put("seatID", seatID);
				param.put("showFlag", "0");
				List<Map<String, String>> callList = converMapper.getCallStatus(param);
				if(callList.size()>0){
					wbs.onMessage(callList.get(0).get("PHONE"), session);
					
					//修改弹屏状态
					converMapper.updateShowStauts(seatID);
					sqlSession.commit();
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}