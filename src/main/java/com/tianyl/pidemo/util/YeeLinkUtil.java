package com.tianyl.pidemo.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class YeeLinkUtil {

	public static String post(String url, Map<String, String> paramMap) {
		return post(url, paramMap, "utf-8");
	}

	public static String post(String url, Map<String, String> paramMap, String charsetName) {
		String result = "";
		if (paramMap == null) {
			paramMap = new HashMap<String, String>();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) (new URL(checkUrl(url)).openConnection());
			if (!paramMap.isEmpty()) {
				conn.setDoInput(true);
			}
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			// 仅对当前请求自动重定向
			conn.setInstanceFollowRedirects(true);
			// header 设置编码
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 连接
			conn.connect();
			writeParameters(conn, paramMap);
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new IOException();
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charsetName));
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				result += temp + "\n";
			}
			reader.close();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String post(String url, String body) {
		String result = "";
		try {
			HttpURLConnection conn = (HttpURLConnection) (new URL(checkUrl(url)).openConnection());
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			// 仅对当前请求自动重定向
			conn.setInstanceFollowRedirects(true);
			// header 设置编码
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("U-ApiKey", getKey());
			// 连接
			conn.connect();
			writeBody(conn, body);
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				String str = readStream(conn.getErrorStream());
				System.out.println(str);
				throw new IOException();
			}
			result = readStream(conn.getInputStream());
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String get(String url) {
		String result = "";
		try {
			HttpURLConnection conn = (HttpURLConnection) (new URL(checkUrl(url)).openConnection());
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setUseCaches(false);
			// 仅对当前请求自动重定向
			conn.setInstanceFollowRedirects(true);
			// header 设置编码
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("U-ApiKey", getKey());
			// 连接
			conn.connect();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				String str = readStream(conn.getErrorStream());
				System.out.println(str);
				throw new IOException();
			}
			result = readStream(conn.getInputStream());
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String getKey() {
		String key = FileUtil.read(new File("/home/pi/pidata/yeelink.key"));
		if (StringUtil.isBlank(key)) {
			throw new RuntimeException("获取 U-ApiKey 识别");
		}
		return key;
	}

	private static String readStream(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];
		int readLen = -1;
		while ((readLen = is.read(buff)) != -1) {
			baos.write(buff, 0, readLen);
		}
		return new String(baos.toByteArray());
	}

	private static void writeBody(HttpURLConnection conn, String body) throws IOException {
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes(body);
		out.flush();
		out.close();
	}

	private static void writeParameters(HttpURLConnection conn, Map<String, String> map) throws IOException {
		if (map == null) {
			return;
		}
		String content = "";
		Set<String> keySet = map.keySet();
		int i = 0;
		for (String key : keySet) {
			String val = map.get(key);
			content += (i == 0 ? "" : "&") + key + "=" + URLEncoder.encode(val, "utf-8");
			i++;
		}
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes(content);
		out.flush();
		out.close();
	}

	private static String checkUrl(String url) {
		String result = url;
		if (url.startsWith("http://")) {
			url = url.replaceFirst("http://", "");
			if (url.contains("//")) {
				url = url.replaceAll("//", "/");
			}
			result = "http://" + url;
		}
		return result;
	}

	public static void main(String[] args) {
		String url = "http://api.yeelink.net/v1.0/device/343680/sensor/381672/datapoints";
		JSONArray json = new JSONArray();

		JSONObject obj1 = new JSONObject();
		obj1.put("timestamp", "2015-12-28T16:13:14");
		obj1.put("value", 40.5d);
		json.add(obj1);

		JSONObject obj2 = new JSONObject();
		obj2.put("timestamp", "2015-12-28T16:15:14");
		obj2.put("value", 43.5d);
		json.add(obj2);

		JSONObject obj3 = new JSONObject();
		obj3.put("timestamp", "2015-12-28T16:19:14");
		obj3.put("value", 38.5d);
		json.add(obj3);

		// String str = post(url, json.toJSONString());

		url = "http://api.yeelink.net/v1.0/device/343680/sensor/381678/datapoints";
		String str = get(url);
		System.out.println(str);
	}
}
