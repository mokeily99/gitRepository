package com;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.yl.common.util.DBUtil;
import com.yl.transaction.seat.dao.SeatDao;

public class Test {
	public static void main(String[] args) throws IOException {
		System.out.println("========================");
		
		SqlSession session = DBUtil.getDefaultSqlSession();
		SeatDao mapper = session.getMapper(SeatDao.class);
		Map<String, String> param = new HashMap<String, String>();
		param.put("seatID", "1000000002");
		List<Map<String, String>> seatInfo = mapper.getSeatFreeBusyInfo(param);
		System.out.println(seatInfo);
	}
}
