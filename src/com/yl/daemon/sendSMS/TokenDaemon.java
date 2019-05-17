package com.yl.daemon.sendSMS;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import com.yl.common.dao.PublicDao;
import com.yl.common.util.ConfigUtil;
import com.yl.common.util.DBUtil;
import com.yl.common.util.HttpUtil;
import com.yl.transaction.seat.dao.SeatDao;
import net.sf.json.JSONObject;

public class TokenDaemon {
	
	public void loadToken() throws UnsupportedEncodingException{
		SqlSession session = null;
		try {
			session = DBUtil.getDefaultSqlSession();
			PublicDao publicMapper = (PublicDao)session.getMapper(PublicDao.class);
			String token = publicMapper.getToken();
			JSONObject hearResp = HttpUtil.heartbeat(token, ConfigUtil.getConfigKey("API_ACCEPT_URL"));
			if("fail".equals(hearResp.optString("status"))){
				JSONObject loginResp = HttpUtil.login(ConfigUtil.getConfigKey("API_USER_NAME"), ConfigUtil.getConfigKey("API_PWD"), ConfigUtil.getConfigKey("API_ACCEPT_URL"));
				if("success".equals(loginResp.optString("status"))){
					token = loginResp.optString("token");
				}
			}
				
			Map<String, String> param = new HashMap<String, String>();
			param.put("token", token);
			SeatDao seatMapper = (SeatDao)session.getMapper(SeatDao.class);
			seatMapper.updateSeatStatus(param);
			
			session.commit();
		} finally {
			if (session != null)
				session.close();
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		TokenDaemon token = new TokenDaemon();
		token.loadToken();
	}
}
