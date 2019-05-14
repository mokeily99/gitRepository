package com.yl.common.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import org.apache.log4j.Logger;
/**
 * 记录程序运行中的日志信息。Log4j建议只使用四个级别，优先级从高到低分别是 ERROR、WARN、INFO、DEBUG
 * @author Yangzd 2011-06-14
 * @version 1.0
 *
 */
public class LogUtil {

	/**
	 * log4j的root日志对象
	 */
	private static Logger ROOT_LOG = null;
	private static final HashMap<String, Logger> LOGMAP	= new HashMap<String, Logger>();

	static {
		loadLogger();
	}
	  
	/**
	 * 装载系统使用的 loger
	 * 
	 */
	private static void loadLogger() {

		// PropertyConfigurator.configure("log4j.properties");
		try{
			ROOT_LOG = Logger.getRootLogger();
		} catch(Exception e){
		}
	}

	/**
	 * 记录程序运行中的错误信息记录，对应log4j中的ERROR级别的log
	 * @param msg	错误信息
	 */
	public static void errorLog(Object msg) {
		try{
			if( msg instanceof Exception )
				ROOT_LOG.error( getExceptionTrace((Exception)msg));
			else
				ROOT_LOG.error(getParentClassname()+" "+msg);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的错误信息及异常信息，对应log4j中的ERROR级别的log
	 * @param msg	错误信息
	 * @param t		错误或异常
	 */
	public static void errorLog(Object msg, Throwable t){
		try{
			if( msg instanceof Exception )
				ROOT_LOG.error( getExceptionTrace((Exception)msg));
			else
				ROOT_LOG.error(getParentClassname()+" "+msg, t);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的错误信息，对应log4j中的ERROR级别的log
	 * @param msg		错误信息
	 * @param logName	记录日志的日志名
	 */
	public static void errorLog(Object msg, String logName){
		try{
			Logger logger = LOGMAP.get(logName);
			if(logger == null){
				logger	= Logger.getLogger(logName);
				LOGMAP.put(logName, logger);
			}
			if(logger != null)
				if( msg instanceof Exception )
					ROOT_LOG.error( getExceptionTrace((Exception)msg));
				else
					logger.error(getParentClassname()+" "+msg);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的错误信息及异常信息，对应log4j中的ERROR级别的log
	 * @param msg		错误信息
	 * @param t			错误或异常
	 * @param logName	记录日志的日志名
	 */
	public static void errorLog(Object msg, Throwable t, String logName){
		try{
			Logger logger = LOGMAP.get(logName);
			if(logger == null){
				logger	= Logger.getLogger(logName);
				LOGMAP.put(logName, logger);
			}
			if(logger != null)
				if( msg instanceof Exception )
					ROOT_LOG.error( getExceptionTrace((Exception)msg));
				else
					logger.error(getParentClassname()+" "+msg, t);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的告警信息，对应log4j中的WARN级别的log
	 * @param msg	告警信息
	 */
	public static void warnLog(Object msg){
		try{
			ROOT_LOG.warn(getParentClassname()+" "+msg);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的告警信息以及引起告警的错误或异常，对应log4j中的WARN级别的log
	 * @param msg	告警信息
	 * @param t		错误或异常
	 */
	public static void warnLog(Object msg, Throwable t){
		try{
			ROOT_LOG.warn(getParentClassname()+" "+msg, t);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的告警信息，对应log4j中的WARN级别的log
	 * @param msg		告警信息
	 * @param logName	记录日志的日志名
	 */
	public static void warnLog(Object msg, String logName){
		try{
			Logger logger = LOGMAP.get(logName);
			if(logger == null){
				logger	= Logger.getLogger(logName);
				LOGMAP.put(logName, logger);
			}
			if(logger != null)
				logger.warn(getParentClassname()+" "+msg);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的告警信息以及引起告警的错误或异常，对应log4j中的WARN级别的log
	 * @param msg		告警信息
	 * @param t			错误或异常
	 * @param logName	记录日志的日志名
	 */
	public static void warnLog(Object msg, Throwable t, String logName){
		try{
			Logger logger = LOGMAP.get(logName);
			if(logger == null){
				logger	= Logger.getLogger(logName);
				LOGMAP.put(logName, logger);
			}
			if(logger != null)
				logger.warn(getParentClassname()+" "+msg, t);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的运行信息，对应log4j中的INFO级别的log
	 * @param msg	运行信息
	 */
	public static void infoLog(Object msg){
		try{
			if(ROOT_LOG.isInfoEnabled())
				ROOT_LOG.info(getParentClassname()+" "+msg);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的运行信息以及引起该信息的错误或异常，对应log4j中的INFO级别的log
	 * @param msg	运行信息
	 * @param t		错误或异常
	 */
	public static void infoLog(Object msg, Throwable t){
		try{
			if(ROOT_LOG.isInfoEnabled())
				ROOT_LOG.info(getParentClassname()+" "+msg, t);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的运行信息，对应log4j中的INFO级别的log
	 * @param msg	运行信息
	 * @param logName	记录日志的日志名
	 */
	public static void infoLog(Object msg, String logName){
		try{
			Logger logger = LOGMAP.get(logName);
			if(logger == null){
				logger	= Logger.getLogger(logName);
				LOGMAP.put(logName, logger);
			}
			if(logger != null && logger.isInfoEnabled())
				logger.info(getParentClassname()+" "+msg);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的运行信息以及引起该信息的错误或异常，对应log4j中的INFO级别的log
	 * @param msg	运行信息
	 * @param t		错误或异常
	 * @param logName	记录日志的日志名
	 */
	public static void infoLog(Object msg, Throwable t, String logName){
		try{
			Logger logger = LOGMAP.get(logName);
			if(logger == null){
				logger	= Logger.getLogger(logName);
				LOGMAP.put(logName, logger);
			}
			if(logger != null && logger.isInfoEnabled())
				logger.info(getParentClassname()+" "+msg, t);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的调试信息，对应log4j中的DEBUG级别的log
	 * @param msg	调试信息
	 */
	public static void debugLog(Object msg){
		try{
			if(ROOT_LOG.isDebugEnabled())
				ROOT_LOG.debug(getParentClassname()+" "+msg);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的调试信息以及引起该信息的错误或异常，对应log4j中的DEBUG级别的log
	 * @param msg	调试信息
	 * @param t		错误或异常
	 */
	public static void debugLog(Object msg, Throwable t){
		try{
			if(ROOT_LOG.isDebugEnabled())
				ROOT_LOG.debug(getParentClassname()+" "+msg, t);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的调试信息，对应log4j中的DEBUG级别的log
	 * @param msg	调试信息
	 * @param logName	记录日志的日志名
	 */
	public static void debugLog(Object msg, String logName){
		try{
			Logger logger = LOGMAP.get(logName);
			if(logger == null){
				logger	= Logger.getLogger(logName);
				LOGMAP.put(logName, logger);
			}
			if(logger != null && logger.isDebugEnabled())
				logger.debug(getParentClassname()+" "+msg);
		} catch(Exception e){
		}
	}
	
	/**
	 * 记录程序运行中的调试信息以及引起该信息的错误或异常，对应log4j中的DEBUG级别的log
	 * @param msg	调试信息
	 * @param t		错误或异常
	 * @param logName	记录日志的日志名
	 */
	public static void debugLog(Object msg, Throwable t, String logName){
		try{
			Logger logger = LOGMAP.get(logName);
			if(logger == null){
				logger	= Logger.getLogger(logName);
				LOGMAP.put(logName, logger);
			}
			if(logger != null && logger.isDebugEnabled())
				logger.debug(getParentClassname()+" "+msg, t);
		} catch(Exception e){
		}
	}

 /**
  * 打印类名
  * @return
  */
  private static String getParentClassname(){
      StackTraceElement stack[] = (new Throwable()).getStackTrace();
      return stack[2].getClassName();

  }
  
  private static String getExceptionTrace(Exception e) {
	    String s = null;
	    ByteArrayOutputStream bout = new ByteArrayOutputStream();
	    PrintWriter wrt = new PrintWriter(bout);
	    e.printStackTrace(wrt);
	    wrt.close();
	    s = bout.toString();
	    return s;
	  }

}
