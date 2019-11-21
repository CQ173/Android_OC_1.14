package com.huoniao.oc.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * Gson工具类
 * @author 雷鹏杰
 *
 */
public class GsonTools {

	public GsonTools() {
		// TODO Auto-generated constructor stub
	}

	// 使用Gson进行解析Person
	public static <T> T getPerson(String jsonString, Class<T> cls) {
		T t = null;
		try {
			Gson gson = new Gson();
			t = gson.fromJson(jsonString, cls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}

	// 使用Gson进行解析 List<Person>
	public static <T> List<T> getPersons(String jsonString, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
			}.getType());
		} catch (Exception e) {
		}
		return list;
	}

}