package com.yl.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.yl.common.dao.PublicDao;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

@Service("dbUtil")
public class DBUtil {

	public static String getMaxaccept(PublicDao publicDao){
		return publicDao.getMaxaccept();
	}
	
	public static void insertSMS(PublicDao publicDao, String sendOpr, String phone, String message, String patientID, String code, String maxaccept){
		//插入信息发送表
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("maxaccept", publicDao.getMaxaccept());
		param.put("sendOpr", sendOpr);
		param.put("phone", phone);
		param.put("message", message);
		param.put("patientID", patientID);
		publicDao.insertSMS(param);
		
		//插入验证码记录表
		if(StringUtils.isNotBlank(code)){
			param.put("maxaccept", publicDao.getMaxaccept());
			param.put("code", code);
			param.put("orderID", maxaccept);
			publicDao.insertCodeNum(param);
		}
		
	}
	
	/**默认数据库连接工厂**/
	private static SqlSessionFactory DEFAULT_SQLSESSIONFACTORY = null;			
	/**存放数据库连接工厂的Map对象**/
	private static HashMap<String, SqlSessionFactory> FACTORY_MAP = new HashMap<String, SqlSessionFactory>();	
	
	/**类初始化时，加载数据库配置**/
	static{
		try{
			createFactory("mybatis-config.xml");
			LogUtil.debugLog("create default sqlsessionfactory success!");
		} catch(Exception e){
			DEFAULT_SQLSESSIONFACTORY = null;
		}
	}
	
	/**
	 * 创建数据库连接工厂		
	 * @param resource		类路径下的资源文件名
	 * @return
	 * @throws IOException
	 */
	public synchronized static SqlSessionFactory createFactory(String resource) throws IOException{
		Reader reader = Resources.getResourceAsReader(resource);
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		DEFAULT_SQLSESSIONFACTORY = builder.build(reader);
		
		return DEFAULT_SQLSESSIONFACTORY;
	}
	
	/**
	 * 创建数据库连接工厂		
	 * @param resource		类路径下的资源文件名
	 * @param name			数据库配置名
	 * @return
	 * @throws IOException
	 */
	public synchronized static SqlSessionFactory createFactory(String resource, String name) throws IOException{
		Reader reader = Resources.getResourceAsReader(resource);
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		SqlSessionFactory factory = builder.build(reader, name);
		FACTORY_MAP.put(name, factory);
		
		return factory;
	}
	
	/**
	 * 取默认数据库连接工厂（默认使用mybatis-config.xml文件做为mybatis的配置文件）
	 * @return
	 */
	public static SqlSessionFactory getDefaultSqlSessionFactory(){
		return getDefaultSqlSessionFactory("mybatis-config.xml");
	}
	
	/**
	 * 取默认数据库连接工厂
	 * @param resource		类路径下的资源文件名
	 * @return
	 */
	public static SqlSessionFactory getDefaultSqlSessionFactory(String resource){
		if(DEFAULT_SQLSESSIONFACTORY == null)
			try {
				createFactory(resource);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogUtil.errorLog("创建数据库连接工厂失败", e);
				return null;
			}
		
		return DEFAULT_SQLSESSIONFACTORY;
	}
	
	/**
	 * 取指定名称的数据库连接工厂（默认使用mybatis-config.xml文件做为mybatis的配置文件）
	 * @param name			数据库配置名
	 * @return
	 */
	public synchronized static SqlSessionFactory getSqlSessionFactory(String name){
		return getSqlSessionFactory("mybatis-config.xml", name);
	}
	
	/**
	 * 取指定名称的数据库连接工厂
	 * @param resource		类路径下的资源文件名
	 * @param name			数据库配置名
	 * @return
	 */
	public synchronized static SqlSessionFactory getSqlSessionFactory(String resource, String name){
		SqlSessionFactory factory = FACTORY_MAP.get(name);
		if(factory == null)
			try {
				factory = createFactory(resource,name);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogUtil.errorLog("创建数据库连接工厂失败", e);
				return null;
			}
		
		return factory;
	}
	
	/**
	 * 取默认数据库连接（默认使用mybatis-config.xml文件做为mybatis的配置文件）
	 * @return
	 */
	public static SqlSession getDefaultSqlSession(){
		return getDefaultSqlSessionFactory().openSession();
	}
	
	/**
	 * 取默认数据库连接
	 * @param resource		类路径下的资源文件名
	 * @return
	 */
	public static SqlSession getDefaultSqlSession(String resource){
		return getDefaultSqlSessionFactory(resource).openSession();
	}
	
	/**
	 * 取指定数据库的数据库连接（默认使用mybatis-config.xml文件做为mybatis的配置文件）
	 * @param name
	 * @return
	 */
	public static SqlSession getSqlSession(String name){
		return getSqlSessionFactory(name).openSession();
	}
	
	/**
	 * 取指定数据库的数据库连接
	 * @param resource		类路径下的资源文件名
	 * @param name			数据库配置名
	 * @return
	 */
	public static SqlSession getSqlSession(String resource, String name){
		return getSqlSessionFactory(resource, name).openSession();
	}
	
	/**
	 * 取系统流水号（默认使用mybatis-config.xml文件做为mybatis的配置文件）
	 * @return
	 */
	public static String getMaxAccept(){
		SqlSession sqlSession = null;
		String maxAccept = "";
		
		try{
			sqlSession = getDefaultSqlSession();
			maxAccept = getMaxAccept(sqlSession);
		} finally{
			if(sqlSession != null)
				sqlSession.close();
		}
		
		return maxAccept;
	}
	
	/**
	 * 取系统流水号
	 * @param resource		类路径下的资源文件名
	 * @return
	 */
	public static String getMaxAccept(String resource){
		SqlSession sqlSession = null;
		String maxAccept = "";
		
		try{
			sqlSession = getDefaultSqlSession(resource);
			maxAccept = getMaxAccept(sqlSession);
		} finally{
			if(sqlSession != null)
				sqlSession.close();
		}
		
		return maxAccept;
	}
	
	/**
	 * 取系统流水号（默认使用mybatis-config.xml文件做为mybatis的配置文件）
	 * @param sqlSession 数据库连接
	 * @return
	 */
	public static String getMaxAccept(SqlSession sqlSession){
		String maxAccept = "";
		
		/**清理Session级缓存，防止同一Session多次执行同一SQL语句，得到相同值**/
		sqlSession.clearCache();
		PublicDao mapper = sqlSession.getMapper(PublicDao.class);
		maxAccept = mapper.getMaxaccept();
		
		return maxAccept;
	}
	
	/**
	 * 取序列值
	 * @param seq_name
	 * @return
	 */
	/*public static Long getSequence(String seq_name){
		SqlSession sqlSession = null;
		Long value = 0L;
		
		try{
			sqlSession = getDefaultSqlSession();
			value = getSequence(sqlSession, seq_name);
		} finally{
			if(sqlSession != null)
				sqlSession.close();
		}
		
		return value;
	}*/
	
	/*public static Long getSequence(SqlSession sqlSession, String seq_name){
		Long value = 0L;
		
		*//**清理Session级缓存，防止同一Session多次执行同一SQL语句，得到相同值**//*
		sqlSession.clearCache();
		CommonMapper mapper = sqlSession.getMapper(CommonMapper.class);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seq_name", seq_name);
		value = mapper.getSequence(map);
		
		return value;
	}*/
	
	/**
	 * 取当前系统时间
	 * @param pattern
	 * @return
	 */
	/*public static String getSysdate(String pattern){
		SqlSession sqlSession = null;
		String value = "";
		
		try{
			sqlSession = getDefaultSqlSession();
			value = getSysdate(sqlSession, pattern);
		} finally{
			if(sqlSession != null)
				sqlSession.close();
		}
		
		return value;
	}*/
	
	/*public static String getSysdate(SqlSession sqlSession, String pattern){
		String value = "";
		
		*//**清理Session级缓存，防止同一Session多次执行同一SQL语句，得到相同值**//*
		sqlSession.clearCache();
		CommonMapper mapper = sqlSession.getMapper(CommonMapper.class);
		value = mapper.getSysdate(pattern);
		
		return value;
	}*/
	
	public static void main(String[] args){
		System.out.println(String.format("%06d", new Long(12345)));
//		try {
//			createFactory("mybatis-config.xml");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
