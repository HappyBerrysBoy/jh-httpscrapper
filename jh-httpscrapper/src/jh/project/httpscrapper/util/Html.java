package jh.project.httpscrapper.util;

import jh.project.httpscrapper.handler.website._WebsiteHandler;


public class Html{
	private String html;

	public Html(String html) {
		super();
		this.html = html;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
	
	
	
	@Override
	public String toString() {
		return this.html;
	}
	
	public String toStringWithConvert() {
		_WebsiteHandler wh = new _WebsiteHandler();
		
		return wh.convertSpecialChar(this.html);
	}
	

	/**
	* html 코드의 주석 (<!-- --> 을 제거한다.
	* @return 주석을 제거한 값
	*/
	public Html removeComment(){
		_WebsiteHandler wh = new _WebsiteHandler();
		
		return new Html(wh.removeComment(this.html));
	}
	
	
	/**
	* 특정 Tag 의 값만 추출한다.
	* @param tag 추출하려는 tag
	* @return 추출한 값
	*/
	public Html getTag(String tag){
		_WebsiteHandler wh = new _WebsiteHandler();
		
		return new Html(wh.getTag(this.html, tag));
	}
	
	
	/**
	* 특정 Tag 를 제거한 값을 반환한다.
	* @param tag 제거하려는 tag
	* @return 특정 tag 가 제거된 값
	*/
	public Html removeTag(String tag){
		_WebsiteHandler wh = new _WebsiteHandler();
		return new Html(wh.removeTag(this.html, tag));
	}
	
	
	/**
	* 특정 Tag 를 모두 제거한 값을 반환한다.
	* @param tag 제거하려는 tag
	* @return 특정 tag 가 모두제거된 값
	*/
	public Html removeTagAll(String tag){
		_WebsiteHandler wh = new _WebsiteHandler();
		return new Html(wh.removeTagAll(this.html, tag));
	}
	
	
	
	/**
	* 특정 class 가 포함된 Tag의 값(value)을 가져온다.
	* @param className 검색 하고자 하는  class 명
	* @return 특정 class 가 포함된 Tag의 값(value)
	*/
	public Html getValueByClass(String className){
		_WebsiteHandler wh = new _WebsiteHandler();
		
		return new Html(wh.getValueByClass(this.html, className));
	}
	
	/**
	* 특정 class 가 포함된 Tag의 값(value)을 제거한다.
	* @param className 제거 하고자 하는  class 명
	* @return 특정 class 가 포함된 Tag가 제거된 html 코드
	*/
	public Html removeValueByClass(String className){
		_WebsiteHandler wh = new _WebsiteHandler();
		
		return new Html(wh.removeValueByClass(this.html, className));
	}
	
	
	/**
	* 모든 html 태그를 제거 한다.
	* @return 태그가 모두 제거된 html
	*/
	public Html removeAllTags(){
		_WebsiteHandler wh = new _WebsiteHandler();
		return new Html(wh.removeAllTags(this.html));
	}
	
	
	/**
	* 특정 정규식이 포함된 문자열을 찾는다.
	* @param regex 정규식
	* @return 특정 정규식이 포함된 문자열을 찾는다.
	*/
	public Html findRegex(String regex){
		_WebsiteHandler wh = new _WebsiteHandler();
		return new Html(wh.findRegex(this.html, regex));
	}
	
	
	/**
	* &nbsp; &lt; &gt;... 같은 특수문자를 변환한다.
	* @return 변환된 값
	*/
	public Html convertSpecialChar(){
		_WebsiteHandler wh = new _WebsiteHandler();
	    return new Html(wh.convertSpecialChar(this.html));
	}

}
