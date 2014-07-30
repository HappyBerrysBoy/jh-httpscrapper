package jh.project.httpscrapper.dataobject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.google.gson.Gson;


/**
 * DataObject skeleton
 * Java Reflection을 이용한 doGet, doSet, getFieldList 와 같은 함수를 지원함.
 * 
 * @author purepleya
 * @since 2014-07-18
 */
public class _DataObject {
	private String html;
	
	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
	
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public ArrayList<String> getFieldList(){
		Field[] fields = this.getClass().getDeclaredFields();
		
		ArrayList<String> fieldList = new ArrayList<>();
		for(int i = 0; i < fields.length; i++){
			fieldList.add(fields[i].toString());
		}
		
		return fieldList;
	}

	public void doSet(String fieldName, String value) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method setter = this.getClass().getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), String.class);
		setter.invoke(this, value);
	}
	
	public Object doGet(String fieldName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method getter = this.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
		return getter.invoke(this);
	}
}
