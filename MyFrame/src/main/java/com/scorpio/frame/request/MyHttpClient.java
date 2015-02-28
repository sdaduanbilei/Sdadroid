package com.scorpio.frame.request;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.XmlHttpResponseHandler;
import com.lurencun.cfuture09.androidkit.AKHttp;
import com.scorpio.frame.util.LogUtil;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;


/**
 * 这个封装的网络http请求类，是整合async-http-lib及androidkit-lib功能的集合 通过这个类，可以方便实现异步与同步的操作。
 * 包括对于直接post xml或者json形式数据的支持。 其他的标准请求，直接继承成这两个库中的方法，方便使用
 * 
 * @author sdaduanbilei
 * 
 */
public class MyHttpClient extends AKHttp {
	private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	public static AsyncHttpClient client;
	public static String baseUrl;

	public static void init(String url) {
		client = new AsyncHttpClient();
        client.setTimeout(1000*60);
		baseUrl = url;
	}

	// 异步---------------------------------------------

	/**
	 * post请求，xml格式到baseurl
	 * 
	 * @param context
	 * @param xml
	 * @param responseHandler
	 */
	public static void requestWithXML(Context context, String xml,
			XmlHttpResponseHandler responseHandler) {
		try {
			StringEntity entity = new StringEntity(xml, HTTP.UTF_8);
			client.post(context, baseUrl, entity, "text/xml", responseHandler);
		} catch (UnsupportedEncodingException e) {
			LogUtil.Error(e.getLocalizedMessage());
		}
	}

	/**
	 * post请求，json格式到baseurl
	 * 
	 * @param context
	 * @param json
	 * @param responseHandler
	 */
	public static void requestWithJSON(Context context, String json,
			JsonHttpResponseHandler responseHandler) {
		try {
			StringEntity entity = new StringEntity(json, HTTP.UTF_8);
			client.post(context, baseUrl, entity, "application/json",
					responseHandler);
		} catch (UnsupportedEncodingException e) {
			LogUtil.Error(e.getLocalizedMessage());
		}
	}

	/**
	 * get请求，异步处理，使用async-http-lib中的get
	 * 
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

    public static void get(String url, String method ,RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        LogUtil.Debug("servce config==="+getAbsoluteUrl(url));
        client.get(getAbsoluteUrl(url)+"/"+method, params, responseHandler);
    }

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		LogUtil.Debug(getAbsoluteUrl(url));
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

    public static void post(String url ,String method, RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {
        LogUtil.Debug(getAbsoluteUrl(url)+"/"+method);
        client.post(getAbsoluteUrl(url)+"/"+method, params, responseHandler);
    }

	public static String getAbsoluteUrl(String relativeUrl) {
		if (relativeUrl.startsWith("http://")) {
			return relativeUrl;
		}
		return baseUrl + relativeUrl;
	}

	
	// 同步--------------------------------------------------------

	/**
	 * 同步post请求，xml格式，直接返回dom4j的document
	 * 
	 * @param xml
	 * @return
	 */
	public static Document syncRequestWithXML(String xml) {
		String response;
		Document doc = null;
		try {
			response = post(baseUrl, xml, "text/xml");
			LogUtil.Debug("sync get content:" + response);
			doc = DocumentHelper.parseText(response.trim());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtil.Error("error:" + e.getLocalizedMessage());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			LogUtil.Error("error:" + e.getLocalizedMessage());
		}
		return doc;
	}

	/**
	 * 同步post请求，json格式，只返回string
	 * 
	 * @param json
	 * @return
	 */
	public static String syncRequestWithJSON(String json) {
		String response = null;
		try {
			response = post(baseUrl, json, "application/json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtil.Error(e.getLocalizedMessage());
		}
		return response;
	}

	/**
	 * 生成简易的xml请求，以mobile为root节点
	 * 
	 * @param dic
	 * @return
	 */
	public static String genXML(HashMap<String, String> dic) {
		String xml = XML_HEAD + "<mobile>";
		Iterator<String> iterator = dic.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			xml += "<" + key + ">" + dic.get(key) + "</" + key + ">";
		}
		xml += "</mobile>";
		return xml;
	}
}
