/**   
 * @Title: MD5Util.java 
 * @Package com.zhaochong.querycars.util 
 * @Description: TODO(用一句话描述该文件做�?��) 
 * @author zhaochong   
 * @date 2013-9-5 下午2:27:32 
 * @version V1.0   
 */
package com.scorpio.frame.util;
import java.security.MessageDigest;

/** 
 * @ClassName: MD5Util 
 * @Description: MD5加密工具�?
 * @author zhaochong 
 * @date 2013-9-5 下午2:27:32  
 */
public class MD5Util {
	public static  String md5(String source) {
	    StringBuffer sb = new StringBuffer(32);  
	          
	    try {  
	        MessageDigest md = MessageDigest.getInstance("MD5");  
	        byte[] array = md.digest(source.getBytes("utf-8"));  
	              
	        for (int i = 0; i < array.length; i++) {  
	            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));  
	        }  
	    } catch (Exception e) {  
//	        logger.error("Can not encode the string '" + source + "' to MD5!", e);  
	        return null;  
	    }  
	          
	    return sb.toString();  
	}  
	
	/**
     * 灏唖tr瀛楃涓茶繘琛孧D5缂栫爜锛屽皢缂栫爜缁撴灉杩斿洖
     * 
     * @author zhijun.wu
     * @param str
     * @return
     *
     * @date 2011-5-11
     */
//    public static String getMD5(String str) {
//        try {
//            return getMD5(str.getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
}
