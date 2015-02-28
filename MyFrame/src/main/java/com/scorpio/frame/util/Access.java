/**   
 * @Title: Access.java 
 * @Package com.zhaochong.querycars.util 
 * @Description: TODO(用一句话描述该文件做�?��) 
 * @author zhaochong   
 * @date 2013-9-5 下午2:48:00 
 * @version V1.0   
 */
package com.scorpio.frame.util;

import java.text.SimpleDateFormat;
import java.util.Date;


/** 
 * @ClassName: Access 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhaochong 
 * @date 2013-9-5 下午2:48:00  
 */
public class Access {

	/** 
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作�? 
	 * @param @param args	
	 * @return void    返回类型 
	 * @throws 
	 */
//	public static void main(String[] args) {
////		String strUrl = 
//		Access access = new Access();
//		access.getTimestamp();
//	}

	public String getTimestamp() {
		SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String timestamp = formatter.format(date);
		
		return timestamp;
	}
	
	
	public String getSign(String appKey, String appSecrect, 
			String method, String format, String timestamp, String v, 
			String signMethod,String packets){
//		String source = "test1AppKeytest1FormatJsonMethodPublic.Car.PeccancyPackets" + packets + "Timestamp" + timestamp + "V1.0test1";
		String source = "";
		StringBuilder sb = new StringBuilder();
		sb.append(appSecrect);
		sb.append("AppKey");
		sb.append(appKey);
		sb.append("Format");
		sb.append(format);
		sb.append("Method");
		sb.append(method);
		sb.append("Packets");
		sb.append(packets);
		sb.append("Timestamp");
		sb.append(timestamp);
		sb.append("V");
		sb.append(v);
		sb.append(appSecrect);
		
		source = sb.toString();
        LogUtil.Debug("fuck===="+sb.toString());
		//LogOutHelper.LogOutError("TEST", source);
		
		MD5Util md5Util = new MD5Util();
		String sign = md5Util.md5(source);
		System.out.println(sign);
		
		return sign;
	}
	
	
	public String getIAuthenticationUrl(String appKey, String appSecrect, 
			String method, String format, String timestamp, String v, 
			String signMethod,  String sign,String packets) {
		String address = "http://iyz.yizhebao.net/Rest.aspx";
		StringBuilder sb = new StringBuilder();
		sb.append(address);
		sb.append("?");
		sb.append("AppKey=");
		sb.append(appKey);
		sb.append("&Method=");
		sb.append(method);
		sb.append("&Format=");
		sb.append(format);
		sb.append("&Timestamp=");
		sb.append(timestamp);
		sb.append("&SignMethod=");
		sb.append(signMethod);
		sb.append("&V=");
		sb.append(v);
		sb.append("&Packets=");
		sb.append(packets);
		sb.append("&Sign=");
		sb.append(sign);
		
//		String strUrl = "http://m.api.yizhebao.com/rest/default.aspx?AppKey=" +  appKey + "&Method=" + method + "&Format=" + format + "&Timestamp=" + timestamp + "&SignMethod=" + signMethod + "&V=" + v + "&Packets=" + packets + "&Sign=" + sign;
		String strUrl = sb.toString();
		
		return strUrl;
	}
}
