package com;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import com.yl.common.util.DBUtil;
import com.yl.transaction.smsManager.dao.SMSDao;

public class Test {
	public static void main(String[] args) {
		SqlSession session = DBUtil.getDefaultSqlSession();
		SMSDao mapper = session.getMapper(SMSDao.class);
		List<Map<String, String>> list = mapper.getSmsSyncList(new HashMap<String, Object>(), new RowBounds(0, 1));
		System.out.println(list);
	}
}
