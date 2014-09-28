package jh.project.httpscrapper;

import java.util.ArrayList;
import java.util.HashMap;

import jh.project.httpscrapper.handler.website._WebsiteHandler;

/**
 * Web site information for scrapping
 * contains id, name, encoding type, url, sub sites...
 * 
 * @author purepleya
 * @since 2014-07-16
 */
public class Website {
	private String id;
	private String name;
	private String encoding;
	private String url;
	private String method;
	private HashMap<String, String> param;
	private HashMap<String, String> header;
	
	private _WebsiteHandler handler;
	private String handlerClassName;
	private String resultClassName;
	
	private void constructor(){
		this.param = new HashMap<String, String>();
		this.header = new HashMap<String, String>();
		this.handler = new _WebsiteHandler();
		this.handlerClassName = "hnc.tourtobee.scrapper.handler.website._WebsiteHandler";
		this.resultClassName = "hnc.tourtobee.scrapper.dataobject._DataObject";
	}
	
	public Website() {
		constructor();
	}
	
	public Website(String url, String method, String encoding){
		constructor();
		this.url = url;
		this.method = method;
		this.encoding = encoding;
	}

	/**
	* web site id를 반환한다.
	* @return web site id
	*/
	public String getId() {
		return id;
	}
	
	/**
	* web site id를 설정한다.
	* @param id web site id
	*/
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	* web site 명을 반환한다.
	* @return web site 명
	*/
	public String getName() {
		return name;
	}
	
	/**
	* web site id를 설정한다.
	* @param name web site 명
	*/
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	* web site의 encoding type을 반환한다.
	* @return web site의 encoding type
	*/
	public String getEncoding() {
		return encoding;
	}
	
	/**
	* web site의 encoding type을 설정 한다.
	* @param encoding web site의 encoding type
	*/
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	/**
	* web site의 url을 반환한다.
	* @return web site의 url
	*/
	public String getUrl() {
		return url;
	}
	
	/**
	* web site의 url을 설정한다.
	* @param url web site의 url
	*/
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	* web site로 전송할 parameter를 반환한다.
	* @return web site로 전송할 parameter
	*/
	public HashMap<String, String> getParam() {
		return param;
	}
	
	/**
	* web site로 전송할 parameter를 설정한다.
	* @param param web site로 전송할 parameter
	*/
	public void setParam(HashMap<String, String> param) {
		this.param = param;
	}
	
	/**
	* web site로 전송할 Header를 반환한다.
	* @return web site로 전송할 Header
	*/
	public HashMap<String, String> getHeader() {
		return header;
	}
	
	/**
	* web site로 전송할 Header를 설정한다.
	* @param param web site로 전송할 Header
	*/
	public void setHeader(HashMap<String, String> header) {
		this.header = header;
	}
	
	/**
	* web site로 전송할 parameter를 추가한다.
	* @param key web site로 전송할 parameter의 변수명
	* @param value web site로 전송할 parameter의 값
	*/
	public void addParam(String key, String value){
		this.param.put(key, value);
	}
		
	/**
	* web site를 처리할 Handler의 클래스 명을 반환한다.
	* @return web site를 처리할 Handler의 클래스 명
	*/
	public String getHandlerClassName() {
		return handlerClassName;
	}
	
	/**
	* web site를 처리할 Handler의 클래스 명을 설정한다.
	* @param handlerClassName web site를 처리할 Handler의 클래스 명
	*/
	public void setHandlerClassName(String handlerClassName) {
		this.handlerClassName = handlerClassName;
	}
	
	/**
	* web site를 처리후 반환되는 결과 클래스의 명을 반환한다.
	* @return web site를 처리후 반환되는 결과 클래스의 명
	*/
	public String getResultClassName() {
		return resultClassName;
	}
	
	/**
	* web site를 처리후 반환되는 결과 클래스의 명을 설정한다.
	* @param resultClassName web site를 처리후 반환되는 결과 클래스의 명
	*/
	public void setResultClassName(String resultClassName) {
		this.resultClassName = resultClassName;
	}

	/**
	* Handler 를 반환한다.
	* @return Handler
	*/
	public _WebsiteHandler getHandler() {
		return handler;
	}
	
	/**
	* Handler 를 설정한다.
	* @param handler Hanlder
	*/
	public void setHandler(_WebsiteHandler handler) {
		this.handler = handler;
	}
	
	/**
	* Method(POST, GET)를 반환한다.
	* @return Method(POST, GET)
	*/
	public String getMethod() {
		return method;
	}
	
	/**
	* Method(POST, GET)를 설정한다.
	* @param method Method(POST, GET)
	*/
	public void setMethod(String method) {
		this.method = method;
	}
	
}
