package com.yl.daemon.sendSMS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import com.yl.common.util.DBUtil;
import com.yl.common.util.LogUtil;
import com.yl.transaction.smsManager.dao.SMSDao;

public class SendsmsDaemon {
	private static final String SERVICE_NO = "200064";
	private static final String BIP_CODE = "BIP5A131";
	private static final String TRANS_CODE = "T5101015";
	
	private static final int MAX_TRY_TIMES = 5;
	/** 和生活业务 **/
	private static final String BIZ_TYPE = "78";
	/** 线程池中所保存的线程数 **/
	private static int CORE_POOL_SIZE = 1;
	/** 线程池中允许的最大线程数 **/
	private static int MAX_POOL_SIZE = 10;
	/** 线程池保持任务的队列的容量 **/
	private static int CAPACITY = 100;
	/** 线程池中空闲线程存活时间 **/
	private static long ALIVE_TIME = 60000L;
	/** 同步报文中最大记录条数 **/
	private static int MAX_REC_NUM = 300;
	/** 休眠时间（毫秒） **/
	private static long SLEEP_TIME = 15000L;
	/** 每次从数据库取数据的最大条数 **/
	private static int MAX_ROWS = MAX_REC_NUM * 10;

	public SendsmsDaemon() {
	}


	public SendsmsDaemon(int corePoolSize, int maxPoolSize, int capacity,int recNum, long sleepTime) {
		CORE_POOL_SIZE = corePoolSize;
		MAX_POOL_SIZE = maxPoolSize;
		CAPACITY = capacity;
		MAX_REC_NUM = recNum;
		SLEEP_TIME = sleepTime;
	}

	/**
	 * 同步用户订购关系
	 */
	public void syncOrderMsg() {
		LogUtil.infoLog("开始同步订购关系...");
		LogUtil.infoLog("启动同步线程...");
		LogUtil.infoLog("核心线程数:" + CORE_POOL_SIZE);
		LogUtil.infoLog("最大线程数:" + MAX_POOL_SIZE);
		LogUtil.infoLog("队列长度:" + CAPACITY);
		LogUtil.infoLog("同步报文最大记录条数:" + MAX_REC_NUM);
		LogUtil.infoLog("无数据程序休眠时间（毫秒）:" + SLEEP_TIME);

		// 将所有处于正在处理状态中的数据，修改状态为待处理
		LogUtil.infoLog("将所有处于正在处理状态中的数据，修改状态为待处理");
		updateSendStatusTo0();

		/** 创建线程池 **/
		ThreadPoolExecutor pool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE, ALIVE_TIME, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(CAPACITY, true));
		pool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); // 线程池拒绝处理任务时，execute 方法抛出  RejectedExecutionException
		LogUtil.infoLog("同步线程启动完毕！");

		/** 向线程池添加需要同步的数据 **/
		while (true) {
			SqlSession session = null;
			try {
				session = DBUtil.getDefaultSqlSession();

				/** 查询待同步数据 **/
				List<Map<String, String>> syncList = readSmsSync(session);
				int rowNum = syncList != null ? syncList.size() : 0;
				if (rowNum > 0) { // 有待同步数据
					updateMakeStatus(session, syncList);
					session.commit();
					int listlen = MAX_REC_NUM;
					/** 将待同步数据按照最大记录数分片，提交数据至线程池发送 **/
					while (syncList.size() > 0) {
						if (syncList.size() < MAX_REC_NUM)
							listlen = syncList.size();
						List<Map<String, String>> subList = new ArrayList<Map<String, String>>(listlen);
						for (int ix = 0; ix < listlen; ix++) {
							subList.add((Map<String, String>) syncList.remove(0));
						}

						/** 提交至线程池，等待发送 **/
						while (true) {
							try {
								pool.execute(new SmsSync(subList));
								break;
							} catch (RejectedExecutionException e) {
								LogUtil.infoLog("线程池已满，暂停提交新的任务，程序休眠！");
								try { Thread.sleep(5000L);} catch (InterruptedException localInterruptedException) {} // 队列满，程序休眠5秒
							}
						}
					}
				}

				/** 如果记录数为零或小于最大记录数，则认为已无数据待同步，程序休眠。以减少数据库访问 **/
				if ((rowNum == 0) || (rowNum < MAX_ROWS)) {
					LogUtil.infoLog("无订购关系变更信息");

					if (session != null) { session.close();session = null;}
					try { Thread.sleep(SLEEP_TIME);} catch (InterruptedException localInterruptedException1) {}
					LogUtil.debugLog("线程池主动执行任务的线程数" + pool.getActiveCount());
				}
			} finally {
				if (session != null)
					session.close();
			}
		}
	}

	/**
	 * 取用待发送短信
	 * 
	 * @return
	 */
	private List<Map<String, String>> readSmsSync(SqlSession session) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("makeFlag", "10702");
		SMSDao mapper = session.getMapper(SMSDao.class);
		return mapper.getSmsSyncList(map, new RowBounds(0, MAX_ROWS));
	}

	/**
	 * 将所有处于正在处理状态中的数据，修改状态为待处理
	 * 
	 * @return
	 */
	private int updateSendStatusTo0() {
		SqlSession session = null;
		try {
			session = DBUtil.getDefaultSqlSession();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("makeStatus", "10702");
			map.put("oldMakeStatus", "10701");
			SMSDao mapper = (SMSDao) session.getMapper(SMSDao.class);
			int count = mapper.updateMakeStatus(map);
			session.commit();
			int i = count;
			return i;
		} finally {
			if (session != null)
				session.close();
		}
	}

	/**
	 * 修改发送状态
	 * 
	 * @param session
	 * @param syncList
	 */
	private void updateMakeStatus(SqlSession session,List<Map<String, String>> syncList) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("makeStatus", "10701");
		SMSDao mapper = (SMSDao) session.getMapper(SMSDao.class);
		for (Map<String, String> sync : syncList) {
			map.put("maxaccept", sync.get("MAXACCEPT"));
			mapper.updateMakeStatus(map);
		}
	}

	/**
	 * 用户订购关系变更同步程序
	 * 
	 */
	public static class SmsSync implements Runnable {
		private List<Map<String, String>> syncList;

		public SmsSync(List<Map<String, String>> syncList) {
			this.syncList = syncList;
		}

		public void run() {
			if ((this.syncList == null) || (this.syncList.size() == 0))
				return;

			while (true) {
				SqlSession session = null;
				try {
					session = DBUtil.getDefaultSqlSession();
					int recNum = this.syncList.size();
					for (int ix = 0; ix < recNum; ix++) {
						String errCode = "0000";
						String errMsg = "success";
						
						Map<String, String> sync = syncList.get(ix);
						String phone = sync.get("PHONE");
						if(isBlackList(session, phone)){
							errCode = "0001";
							errMsg = "黑名单用户不能发送短信！";
						}else{
							/**发送短信**/
							
						}
						/** 根据发送结果处理数据 **/
						moveToHisTable(session, sync, errCode, errMsg);
						session.commit();
					}

					if (this.syncList.size() == 0) // 如果本次发送同步成功或列表已空，停止发送
						return;
				} catch (Exception e) {
					LogUtil.errorLog("发送订购关系同步失败,程序休眠5秒后重新发送！", e);
				} finally {
					if (session != null)
						session.close();
				} // end try
				try {Thread.sleep(5000L);} catch (InterruptedException localInterruptedException) {}
			}// end while
		}// end run method

		private boolean isBlackList(SqlSession session, String phone) {
			boolean blackFlag = false;
			try {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("blackPhone", phone);
				map.put("ableFlag", "10101");
				SMSDao mapper = (SMSDao) session.getMapper(SMSDao.class);
				List<Map<String, String>> blackList = mapper.getBlackList(map);
				if(blackList.size()>0){
					blackFlag = true;
				}
				
			} catch (Exception e) {
				LogUtil.errorLog("校验黑名单失败！", e);
			}
			return blackFlag;
		}

		/**
		 * 备份数据
		 * 
		 * @param session
		 * @param chgList
		 * @param errCode
		 * @param errMsg
		 */
		private void moveToHisTable(SqlSession session,Map<String, String> sync, String errCode, String errMsg) {
			SMSDao mapper = (SMSDao) session.getMapper(SMSDao.class);
			LogUtil.infoLog("短信记录同步[" + errCode + "][" + errMsg + "]");
			if ("0000".equals(errCode) || "0001".equals(errCode)) { // 发送成功、黑名单数据移送历史记录表
				// 删除数据
				mapper.deleteSmsSync(sync.get("MAXACCEPT"));
				// 记录历史表
				sync.put("tryTimes", Integer.parseInt(sync.get("TRY_TIMES")) + 1 + "");
				if("0000".equals(errCode)){
					sync.put("sendFlag", "10601");
				}else{
					sync.put("sendFlag", "10602");
				}
				sync.put("errMsg", errMsg);
				mapper.insertSmsSynchis(sync);
			} else { //发送同步请求不成功
				int tryTimes = Integer.parseInt(sync.get("TRY_TIMES")) + 1;
				if (tryTimes >= MAX_TRY_TIMES) { // 已达最大发送次数
					// 删除数据
					mapper.deleteSmsSync(sync.get("MAXACCEPT"));
					// 记录历史表
					sync.put("tryTimes",String.valueOf(tryTimes));
					sync.put("sendFlag", "10602");
					sync.put("errMsg", errMsg);
					mapper.insertSmsSynchis(sync);
				} else {// 未达到最大发送次数，修改发送次数
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("tryTimes", String.valueOf(tryTimes));
					map.put("maxaccept", sync.get("MAXACCEPT"));
					mapper.updateTryTimes(map);
					sync.put("TRY_TIMES", String.valueOf(tryTimes));
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SendsmsDaemon f = null;
		if (args.length >= 5) {
			int corePoolSize = 1;
			int maxPoolSize = 10;
			int capacity = 100;
			int recNum = 0;
			long sleepTime = 0L;
			try {
				corePoolSize = Integer.parseInt(args[0]);
				maxPoolSize = Integer.parseInt(args[1]);
				capacity = Integer.parseInt(args[2]);
				recNum = Integer.parseInt(args[3]);
				sleepTime = Long.parseLong(args[4]);
				if ((recNum <= 0) || (sleepTime <= 0)) {
					f = new SendsmsDaemon();
				}
				f = new SendsmsDaemon(corePoolSize, maxPoolSize, capacity,recNum, sleepTime);
			} catch (Exception e) {
				LogUtil.errorLog("程序发生异常！退出", e);
				System.out
						.println("Usage:F5A131aDaemon <核心线程数> <最大线程数> <等待队列长度> <交易包最大数> <休眠时间>");
				System.exit(1);
			}
		} else
			f = new SendsmsDaemon();
		try {
			LogUtil.infoLog("程序开始运行...");
			f.syncOrderMsg();
		} catch (Exception e) {
			LogUtil.errorLog("程序运行异常，退出！", e);
			System.exit(1);
		}
	}
}
