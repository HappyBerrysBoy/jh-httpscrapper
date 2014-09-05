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
	* html 肄붾뱶��二쇱꽍 (<!-- --> ���쒓굅�쒕떎.
	* @return 二쇱꽍���쒓굅��媛�
	*/
	public Html removeComment(){
		_WebsiteHandler wh = new _WebsiteHandler();
		
		return new Html(wh.removeComment(this.html));
	}
	
	
	/**
	* �뱀젙 Tag ��媛믩쭔 異붿텧�쒕떎.
	* @param tag 異붿텧�섎젮��tag
	* @return 異붿텧��媛�
	*/
	public Html getTag(String tag){
		_WebsiteHandler wh = new _WebsiteHandler();
		
		return new Html(wh.getTag(this.html, tag));
	}
	
	
	/**
	* �뱀젙 Tag 瑜��쒓굅��媛믪쓣 諛섑솚�쒕떎.
	* @param tag �쒓굅�섎젮��tag
	* @return �뱀젙 tag 媛��쒓굅��媛�
	*/
	public Html removeTag(String tag){
		_WebsiteHandler wh = new _WebsiteHandler();
		return new Html(wh.removeTag(this.html, tag));
	}
	
	
	/**
	* �뱀젙 Tag 瑜�紐⑤몢 �쒓굅��媛믪쓣 諛섑솚�쒕떎.
	* @param tag �쒓굅�섎젮��tag
	* @return �뱀젙 tag 媛�紐⑤몢�쒓굅��媛�
	*/
	public Html removeTagAll(String tag){
		_WebsiteHandler wh = new _WebsiteHandler();
		return new Html(wh.removeTagAll(this.html, tag));
	}
	
	
	
	/**
	* �뱀젙 class 媛��ы븿��Tag��媛�value)��媛�졇�⑤떎.
	* @param className 寃�깋 �섍퀬���섎뒗  class 紐�
	* @return �뱀젙 class 媛��ы븿��Tag��媛�value)
	*/
	public Html getValueByClass(String className){
		_WebsiteHandler wh = new _WebsiteHandler();
		
		return new Html(wh.getValueByClass(this.html, className));
	}
	
	/**
	* �뱀젙 class 媛��ы븿��Tag��媛�value)��媛�졇�⑤떎.
	* @param idName 寃�깋 �섍퀬���섎뒗  id 紐�
	* @return �뱀젙 id 媛��ы븿��Tag��媛�value)
	*/
	public Html getValueById(String idName){
		_WebsiteHandler wh = new _WebsiteHandler();
		
		return new Html(wh.getValueById(this.html, idName));
	}
	
	/**
	* �뱀젙 attribute���뱀젙 value媛��ы븿��Tag��媛�value)��媛�졇�⑤떎.
	* @param attr 寃�깋 �섍퀬���섎뒗  attribute 紐�
	* @param value 寃�깋 �섍퀬���섎뒗  attribute��value
	* @return �뱀젙 attribute 媛��ы븿��Tag��媛�value)
	*/
	public Html getValueByAttr(String attr, String value){
		_WebsiteHandler wh = new _WebsiteHandler();
		
		return new Html(wh.getValueByAttr(this.html, attr, value));
	}
	
	/**
	* �뱀젙 class 媛��ы븿��Tag��媛�value)���쒓굅�쒕떎.
	* @param className �쒓굅 �섍퀬���섎뒗  class 紐�
	* @return �뱀젙 class 媛��ы븿��Tag媛��쒓굅��html 肄붾뱶
	*/
	public Html removeValueByClass(String className){
		_WebsiteHandler wh = new _WebsiteHandler();
		
		return new Html(wh.removeValueByClass(this.html, className));
	}
	
	
	/**
	* �뱀젙 id 媛��ы븿��Tag��媛�value)���쒓굅�쒕떎.
	* @param idName �쒓굅 �섍퀬���섎뒗  id 紐�
	* @return �뱀젙 id 媛��ы븿��Tag媛��쒓굅��html 肄붾뱶
	*/
	public Html removeValueById(String idName){
		_WebsiteHandler wh = new _WebsiteHandler();
		
		return new Html(wh.removeValueById(this.html, idName));
	}
	
	/**
	* �뱀젙 attribute���뱀젙 value媛��ы븿��Tag��媛�value)���쒓굅�쒕떎.
	* @param attr �쒓굅 �섍퀬���섎뒗  attribute 紐�
	* @param value �쒓굅 �섍퀬���섎뒗  attribute��value
	* @return �뱀젙 attribute 媛��ы븿��Tag媛��쒓굅��html 肄붾뱶
	*/
	public Html removeValueByAttr(String attr, String value){
		_WebsiteHandler wh = new _WebsiteHandler();
		
		return new Html(wh.removeValueByAttr(this.html, attr, value));
	}

	
	/**
	* 紐⑤뱺 html �쒓렇瑜��쒓굅 �쒕떎.
	* @return �쒓렇媛�紐⑤몢 �쒓굅��html
	*/
	public Html removeAllTags(){
		_WebsiteHandler wh = new _WebsiteHandler();
		return new Html(wh.removeAllTags(this.html));
	}
	
	
	/**
	* �뱀젙 �뺢퇋�앹씠 �ы븿��臾몄옄�댁쓣 李얜뒗��
	* @param regex �뺢퇋��
	* @return �뱀젙 �뺢퇋�앹씠 �ы븿��臾몄옄�댁쓣 李얜뒗��
	*/
	public Html findRegex(String regex){
		_WebsiteHandler wh = new _WebsiteHandler();
		return new Html(wh.findRegex(this.html, regex));
	}
	
	
	/**
	* &nbsp; &lt; &gt;... 媛숈� �뱀닔臾몄옄瑜�蹂�솚�쒕떎.
	* @return 蹂�솚��媛�
	*/
	public Html convertSpecialChar(){
		_WebsiteHandler wh = new _WebsiteHandler();
	    return new Html(wh.convertSpecialChar(this.html));
	}

	
	/**
	* 臾몄옄���대��먯꽌 �レ옄留�異붿텧�쒕떎.
	* @param sentence 臾몄옄��
	* @return �レ옄媛믩쭔 異붿텧�댁꽌 return
	*/
	public Html getOnlyNumber(){
		_WebsiteHandler wh = new _WebsiteHandler();
		return new Html(wh.getOnlyNumber(this.html));
	}
	
	
	/**
	* 臾몄옄���대��먯꽌 �뱀젙 attribute��媛믪쓣 異붿텧�쒕떎.
	* @param attr 異붿텧��attribute 紐�
	* @return 異붿텧��媛�
	*/
	public Html getAttrValue(String attr){
		_WebsiteHandler wh = new _WebsiteHandler();
		return new Html(wh.getAttrValue(this.html, attr));
	}
	
	/**
	* html 내에서 특정 값을 replace 한다.
	* @param str 찾을 문자열
	* @param replace 대체할 문자열
	* @return 대체된 문자를 리턴한다.
	*/
	public Html replace(String str, String replace){
		_WebsiteHandler wh = new _WebsiteHandler();
		return new Html(wh.replace(this.html, str, replace));
	}
}
