package jh.project.httpscrapper;

import java.util.ArrayList;

import jh.project.httpscrapper.dataobject._DataObject;

public class ScrapResult {
	private String className;
	private ArrayList<_DataObject> results;
	
	/**
	* 결과값 class를 알려준다.
	* @return 결과값 class
	*/
	public String getClassName() {
		return className;
	}
	
	/**
	* 결과값 class를 설정한다.
	* @param className 결과값 class
	*/
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	* 결과값 object 들이 담긴 배열을 반환한다.
	* @return 결과값 object 들이 담긴 배열
	*/
	public ArrayList<_DataObject> getResults() {
		return results;
	}
	
	/**
	* 결과값 object 들이 담긴 배열을 설정한다.
	* @param results 결과값 object 들이 담긴 배열
	*/
	public void setResults(ArrayList<_DataObject> results) {
		this.results = results;
	}
	
	
}
