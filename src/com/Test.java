package com;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.yl.common.util.DBUtil;
import com.yl.common.util.LogUtil;
import com.yl.transaction.seat.dao.SeatDao;

public class Test {
	private static final String SERVICE_NO = "200064";
	private static final String BIP_CODE = "BIP5A131";
	private static final String TRANS_CODE = "T5101015";
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

	public Test() {
	}


	public Test(int corePoolSize, int maxPoolSize, int capacity,int recNum, long sleepTime) {
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

		new Thread(new OrderCfm()).start();

		/** 创建线程池 **/
		ThreadPoolExecutor pool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE, ALIVE_TIME, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(CAPACITY, true));
		pool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); // 线程池拒绝处理任务时，execute 方法抛出  RejectedExecutionException
		LogUtil.infoLog("同步线程启动完毕！");

		/** 向线程池添加需要同步的数据 **/
		Long maxAccept = 0L;
		while (true) {
			SqlSession session = null;
			try {
				session = DBUtil.getDefaultSqlSession();

				/** 查询待同步数据 **/
				List<Map<String, String>> syncList = readOrderChgSync(session,maxAccept);
				int rowNum = syncList != null ? syncList.size() : 0;
				if (rowNum > 0) { // 有待同步数据
					updateSendStatus(session, syncList);
					session.commit();
					int listlen = MAX_REC_NUM;
					/** 将待同步数据按照最大记录数分片，提交数据至线程池发送 **/
					while (syncList.size() > 0) {
						if (syncList.size() < MAX_REC_NUM)
							listlen = syncList.size();
						List<OrderChgSync> subList = new ArrayList<OrderChgSync>(listlen);
						for (int ix = 0; ix < listlen; ix++) {
							subList.add((OrderChgSync) syncList.remove(0));
						}

						/** 提交至线程池，等待发送 **/
						while (true) {
							try {
								pool.execute(new OrderSync(subList));
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
				maxAccept = 0L;// 流水号清零
			} finally {
				if (session != null)
					session.close();
			}
		}
	}

	/**
	 * 取用户订购关系变更信息
	 * 
	 * @return
	 */
	private List<Map<String, String>> readOrderChgSync(SqlSession session,
			Long maxAccept) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bizType", BIZ_TYPE);
		map.put("maxAccept", maxAccept.toString());
		OrderChgMapper mapper = session.getMapper(OrderChgMapper.class);
		return mapper.getOrderChgSyncList(map, new RowBounds(0, MAX_ROWS));
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
			map.put("sendStatus", "0");
			map.put("oldSendStatus", "x");
			map.put("bizType", BIZ_TYPE);
			OrderChgMapper mapper = (OrderChgMapper) session.getMapper(OrderChgMapper.class);
			int count = mapper.updateSendStatus(map);
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
	private void updateSendStatus(SqlSession session,List<OrderChgSync> syncList) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sendStatus", "x");
		OrderChgMapper mapper = (OrderChgMapper) session.getMapper(OrderChgMapper.class);
		for (OrderChgSync sync : syncList) {
			map.put("maxAccept", sync.getMaxAccept());
			mapper.updateSendStatus(map);
		}
	}

	/**
	 * 用户订购关系变更同步程序
	 * 
	 */
	public static class OrderSync implements Runnable {
		private List<OrderChgSync> syncList;

		public OrderSync(List<OrderChgSync> syncList) {
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
					String[][] input = new String[24][];
					input[0] = new String[] { BIP_CODE };
					input[1] = new String[] { TRANS_CODE };
					input[2] = new String[] { "system" };
					input[3] = new String[] { "01001001" };
					input[4] = new String[] { "1" };
					input[5] = new String[] { SPUtil.getOprNumb(session,ConfigUtil.getConfig("HOME_PROV"), "", "yyyyMMdd","seq_ob_lcpt") };
					input[6] = new String[] { String.valueOf(recNum) };
					input[7] = new String[recNum];
					input[8] = new String[recNum];
					input[9] = new String[recNum];
					input[10] = new String[recNum];
					input[11] = new String[recNum];
					input[12] = new String[recNum];
					input[13] = new String[recNum];
					input[14] = new String[recNum];
					input[15] = new String[recNum];
					input[16] = new String[recNum];
					input[17] = new String[recNum];
					input[18] = new String[recNum];
					input[19] = new String[recNum];
					input[20] = new String[recNum];
					input[21] = new String[recNum];
					input[22] = new String[recNum];
					input[23] = new String[] {"和生活业务订购关系同步"};

					String provCode = ConfigUtil.getConfig("HOME_PROV");
					for (int ix = 0; ix < recNum; ix++) {
						OrderChgSync sync = (OrderChgSync) syncList.get(ix);
						String seq = SPUtil.getOprNumb(session,provCode,BIP_CODE, "yyyyMMddHHmmss");
						String oprCode = sync.getOprCode();
						input[7][ix] = sync.getIdValue();
						input[8][ix] = sync.getOldIDValue();
						input[9][ix] = SPUtil.convertDefaultUserBrand(session,sync.getBrand(), "09");
						input[10][ix] = oprCode;
						String spId = StringUtils.trimToEmpty(sync.getSpId());
						String bizCode = StringUtils.trimToEmpty(sync.getSpBizCode());
						input[11][ix] = (StringUtils.isBlank(sync.getBillFlg()) ? getBillFlg(session, spId, bizCode) : sync.getBillFlg());
						input[12][ix] = sync.getOprTime();
						input[13][ix] = sync.getBizType();

						// 如EffetiTime与操作流水号中的日期相同，表示立即生效；否则表示在指定日期（EffetiTime）生效。EffetiTime必须>=操作流水号中的日期。
						String new_efft = seq.substring(11, 25);
						if (sync.getEfftTime().compareTo(new_efft) < 0)
							input[14][ix] = new_efft;
						else
							input[14][ix] = sync.getEfftTime();
						input[15][ix] = sync.getOprSrc();
						input[16][ix] = spId;
						input[17][ix] = bizCode;
						input[18][ix] = sync.getInfoCode();
						input[19][ix] = sync.getInfoValue();
						input[20][ix] = seq;
						input[21][ix] = sync.getPseq();
						input[22][ix] = sync.getUpdateFlag();
					}
					
					/**发送同步请求**/
					TransCfg transCfg = TransUtil.getTransCfg(session,BIP_CODE, TRANS_CODE,ConfigUtil.getConfig("SERVICE_TYPE"));
					TransMsg transMsg = new TransMsg(DBUtil.getMaxAccept(session));
					transMsg.setTransCfg(transCfg);
					transMsg.setServiceNo(SERVICE_NO); // 设置应用编号
					TransReqProcess process = TransProcessFactory.getReqInstance(transCfg.getReqDealClass());
					TransResponse tResponse = process.doTransProcess(transMsg,input);
					int errCode = tResponse.getErrCode();
					String errMsg = tResponse.getErrMsg();

					/** 根据发送结果，处理用户订购关系变更信息 **/
					moveToHisTable(session, this.syncList, errCode, errMsg);
					session.commit();

					if ((errCode == 0) || (this.syncList.size() == 0)) // 如果本次发送同步成功或列表已空，停止发送
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

		/**
		 * 查询业务付费类型
		 * 
		 * @param session
		 * @param spid
		 * @param bizcode
		 * @return
		 */
		private String getBillFlg(SqlSession session, String spid,String bizcode) {
			try {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("spId", spid);
				map.put("bizCode", bizcode);
				DSMPSpBizInfoMapper mapper = (DSMPSpBizInfoMapper) session.getMapper(DSMPSpBizInfoMapper.class);
				String billFlag = mapper.getBillingType(map);
				return StringUtils.isNotBlank(billFlag) ? billFlag : "2";
			} catch (Exception e) {
				LogUtil.errorLog("查询付费类型失败，默认付费类型为包月", e);
				return "2";
			}
		}

		/**
		 * 备份数据
		 * 
		 * @param session
		 * @param chgList
		 * @param errCode
		 * @param errMsg
		 */
		private void moveToHisTable(SqlSession session,List<OrderChgSync> chgList, int errCode, String errMsg) {
			OrderChgMapper mapper = (OrderChgMapper) session.getMapper(OrderChgMapper.class);
			LogUtil.infoLog("订购关系同步[" + errCode + "][" + errMsg + "]");
			for (int ix = 0; ix < chgList.size();) {
				OrderChgSync sync = (OrderChgSync) chgList.get(ix);
				if (errCode == 0) { // 发送同步请求成功
					// 删除数据
					mapper.deleteOrderChgSync(sync.getMaxAccept());
					// 记录历史表
					sync.setTryTimes(Integer.valueOf(sync.getTryTimes().intValue() + 1));
					sync.setSendStatus("1");
					sync.setMonth(sync.getOpTime().substring(0, 6));
					mapper.insertOrderChgSynchis(sync);
				} 
				 else { //发送同步请求不成功
					int maxTimes = sync.getMaxTimes().intValue();
					int tryTimes = sync.getTryTimes().intValue() + 1;
					if (maxTimes > 0) { // 变更信息有最大发送次数限制
						if (tryTimes >= maxTimes) { // 已达最大发送次数
							// 删除数据
							mapper.deleteOrderChgSync(sync.getMaxAccept());
							// 记录历史表
							sync.setTryTimes(Integer.valueOf(tryTimes));
							sync.setSendStatus("0");
							sync.setMonth(sync.getOpTime().substring(0, 6));
							mapper.insertOrderChgSynchis(sync);
							chgList.remove(ix);
							continue;
						} else {// 未达到最大发送次数，修改发送次数
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("tryTimes", String.valueOf(tryTimes));
							map.put("maxAccept", sync.getMaxAccept().toString());
							mapper.updateTryTimes(map);
							sync.setTryTimes(Integer.valueOf(tryTimes));
						}
					}
					// 无最大发送次数限制，不做处理
				}
				ix++;
			}
		}
	}

	/**
	 * 更新用户订购关系
	 * 
	 */
	public static class OrderCfm implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				SqlSession session = null;
				try {
					session = DBUtil.getDefaultSqlSession();
					while (true) {
						/** 查询已确认的订购关系变更信息 **/
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("bizType", BIZ_TYPE);
						OrderChgMapper mapper = session.getMapper(OrderChgMapper.class);
						List<OrderChgCfm> cfmList = mapper.getOrderChgCfmList(map, new RowBounds(0, MAX_REC_NUM * 10));
						if (cfmList != null && cfmList.size() > 0) {
							for (int ix = 0; ix < cfmList.size(); ix++) {
								OrderChgCfm cfm = cfmList.get(ix);
								// 删除记录
								mapper.deleteOrderChgCfm(cfm.getMaxAccept());
								// 备份
								cfm.setMonth(cfm.getOpTime().substring(0, 6));
								mapper.insertOrderChgCfmhis(cfm);
								// 更新用户订购关系
								if (updateOrderMsg(session, cfm) != 0)
									throw new BOException(210001, "更新用户订购关系失败");

								if ((ix + 1) % 100 == 0)
									session.commit();
							}
							session.commit();
					   }
					   else {
							LogUtil.infoLog("无已确认订购关系同步！程序休眠");
							break;
						}
					}
				} catch (Exception e) {
					LogUtil.errorLog("更新用户订购关系失败", e);
				} finally {
					if (session != null)
						session.close();
				}

				try {Thread.sleep(SLEEP_TIME);} catch (InterruptedException e) {}
			}
		}

		/**
		 * 更新用户订购关系
		 * 
		 * @param session
		 * @param cfm
		 */
		private int updateOrderMsg(SqlSession session, OrderChgCfm cfm)
				throws Exception {
			if (!"Y".equals(cfm.getUpdateFlag()))// 无需更新用户订购
				return 0;
			
			return 0;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		F5A131aDaemon f = null;
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
					f = new Test();
				}
				f = new Test(corePoolSize, maxPoolSize, capacity,recNum, sleepTime);
			} catch (Exception e) {
				LogUtil.errorLog("程序发生异常！退出", e);
				System.out
						.println("Usage:F5A131aDaemon <核心线程数> <最大线程数> <等待队列长度> <交易包最大数> <休眠时间>");
				System.exit(1);
			}
		} else
			f = new Test();
		try {
			LogUtil.infoLog("程序开始运行...");
			f.syncOrderMsg();
		} catch (Exception e) {
			LogUtil.errorLog("程序运行异常，退出！", e);
			System.exit(1);
		}
	}
}
