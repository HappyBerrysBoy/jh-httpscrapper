package jh.project.httpscrapper.handler.website;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jh.project.httpscrapper.Website;
import jh.project.httpscrapper.dataobject._DataObject;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class _WebsiteHandler {

	/**
	* httpclient와 website 정보를 가지고 해당 url 의 html 코드를 반환한다.
	* @param httpclient http connection을 책임 질 httpclient
	* @param website 접속할 website 정보, url, parameter, encoding type...
	* @return html 코드
	*/
	protected String getHtml(CloseableHttpClient httpclient, Website website){
		String html = "";
		try{
			HttpRequestBase terminalHttp = null;
			
			if (website.getMethod().equals("GET")){
				String newUrl = website.getUrl();
				ArrayList<String> parameters = getKeyList(website.getParam());
				if (parameters.size() > 0) newUrl += "?";
				for(String parameter : parameters){
					newUrl += parameter + "=" + website.getParam().get(parameter);
				}
				website.setUrl(newUrl);
				terminalHttp = new HttpGet(website.getUrl());
			}else if (website.getMethod().equals("POST")){
				terminalHttp = new HttpPost(website.getUrl());
				List <NameValuePair> nvps = new ArrayList <NameValuePair>();
				ArrayList<String> parameters = getKeyList(website.getParam());
				for(String parameter : parameters){
					nvps.add(new BasicNameValuePair(parameter, website.getParam().get(parameter)));
				}
				((HttpPost) terminalHttp).setEntity(new UrlEncodedFormEntity(nvps));
			}
			
		    CloseableHttpResponse response = httpclient.execute(terminalHttp);
		    HttpEntity entity = response.getEntity();
		    
		    html = EntityUtils.toString(entity, website.getEncoding());
		    
		    EntityUtils.consume(entity);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return html;
	}
	
	
	public ArrayList getResult(CloseableHttpClient httpclient, Website website, HashMap<String, String> options){
		ArrayList<_DataObject> result = new ArrayList<_DataObject>();
		_DataObject d = new _DataObject();
		d.setHtml(this.getHtml(httpclient, website));
		result.add(d);
		
		return result;
	}
	
	
	/**
	* html 코드의 주석 (<!-- --> 을 제거한다.
	* @param html 추출하고자하는 html
	* @return 주석을 제거한 값
	*/
	public String removeComment(String html){
		String result = html;
		
		while(true){
			int beginIndex = result.indexOf("<!--");
			if (beginIndex < 0) break;
			
			int endIndex = result.indexOf("-->") + "-->".length();
			if (endIndex < "-->".length()) break;
			
			String left = result.substring(0, beginIndex);
			String right = result.substring(endIndex, result.length());
			result = left + right;
			
//			result = result.replaceAll(result.substring(beginIndex, endIndex), "");
		}
		
		return result;
	}
	
	
	/**
	* 특정 Tag 의 값만 추출한다.
	* @param html 추출하고자하는 html
	* @param tag 추출하려는 tag
	* @return 추출한 값
	*/
	public String getTag(String html, String tag){
		String result = "";
		tag = tag.toUpperCase();
		Pattern pat = Pattern.compile("(<(?i)" + tag + ")+[>| ][\\s\\S]*(<(?i)\\/" + tag + ">)+");
		Matcher mat = pat.matcher(html);
		
		if (mat.find()){
			result = html.substring(mat.start(), mat.end());
		}else{
			return result;
		}
		
		int cutIndex = result.length();
		int beginTagCount = 0;
		int endTagCount = 0;
		
		for (int i = 2; i < result.length() - tag.length() - 3; i++){
			
			if (result.substring(i, i + tag.length() + 1).toUpperCase().equals("<" + tag) && 
					(result.substring(i + tag.length() + 1, i + tag.length() + 2).equals(" ") || result.substring(i + tag.length() + 1, i + tag.length() + 2).equals(">"))){
				beginTagCount++;
				continue;
			}	
			
			if (result.substring(i, i + tag.length() + 3).toUpperCase().equals("</" + tag + ">")){
				endTagCount ++;
				
				if (endTagCount > beginTagCount){
					cutIndex = i + tag.length() + 3;
					break;
				}
				
				continue;
			}
					
		}
		
		/*
		int startIndex = 0;
		int endIndex = result.length();
		int cutIndex = 0;
		while(true){
			startIndex ++;
			pat = Pattern.compile("(<(?i)" + tag + ")+[>| ]");
			mat = pat.matcher(result.substring(startIndex, endIndex));
			
			if (mat.find()){
				int tagStart = startIndex + mat.start();
				int tagEnd = 0;
				
				pat = Pattern.compile("(</(?i)" + tag + ">)+");
				mat = pat.matcher(result.substring(startIndex, endIndex));
				
				if (mat.find()){
					tagEnd = startIndex + mat.end();
				}else{
					break;
				}
				
				if (tagStart >= tagEnd){
					cutIndex = tagEnd;
					break;
				}else{
					startIndex = tagEnd;
				}
			}else{
				if (cutIndex == 0) cutIndex = endIndex;
				break;
			}
		}
		*/
		
		result = result.substring(0, cutIndex);
		
		return result;
	}
	
	
	/**
	* 특정 Tag 를 제거한 값을 반환한다.
	* @param html 가공하고자하는 html
	* @param tag 제거하려는 tag
	* @return 특정 tag 가 제거된 값
	*/
	public String removeTag(String html, String tag){
		String result = "";
		
		result = html.replace(this.getTag(html, tag).trim(), "");
		return result;
	}
	
	
	/**
	* 특정 Tag 를 모두 제거한 값을 반환한다.
	* @param tag 제거하려는 tag
	* @return 특정 tag 가 모두제거된 값
	*/
	public String removeTagAll(String html, String tag){
		String result = "";
		
		while(!this.getTag(html, tag).toString().trim().equals("")){
			result = html.replace(this.getTag(html, tag).toString().trim(), "");
		}
		return result;
	}
	
	
	/**
	* 특정 class 가 포함된 Tag의 값(value)을 가져온다.
	* @param html html 코드
	* @param className 검색 하고자 하는  class 명
	* @return 특정 class 가 포함된 Tag의 값(value)
	*/
	public String getValueByClass(String html, String className){
		String result = html;
		Pattern pat = Pattern.compile("[<][^>]+((?i)class)+[ =\"']+((?i)" + className + ")+['\"]+[\\s\\S]*<\\/[\\s\\S]*>");
		Matcher mat = pat.matcher(html);
		
		if (mat.find()){
			result = html.substring(mat.start(), mat.end());
			String tag = result.split(" ")[0].substring(1);
			result = this.getTag(result, tag);
//			result = result.substring(0, result.indexOf("</" + tag + ">"));
//			pat = Pattern.compile("<[^<]+>");
//			mat = pat.matcher(result);
//			result = mat.replaceFirst("");
		}
		
		return result.trim();
	}
		
	
	/**
	* 모든 html 태그를 제거 한다.
	* @param html html 코드
	* @return 태그가 모두 제거된 html
	*/
	public String removeAllTags(String html){
		String result = html;
		Pattern tag = Pattern.compile("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>");
		Matcher mat = tag.matcher(result);
		result = mat.replaceAll("");
		
		return result;
	}
	
	
	/**
	* 특정 정규식이 포함된 문자열을 찾는다.
	* @param html html 코드
	* @param regex 정규식
	* @return 특정 정규식이 포함된 문자열을 찾는다.
	*/
	public String findRegex(String html, String regex){
		String subStr = "";
		
		Pattern pattern = Pattern.compile(regex);
		Matcher mat = pattern.matcher(html);
		
		if (mat.find()) subStr = html.substring(mat.start(), mat.end());
		
		return subStr;
	}
	
	
	/**
	* &nbsp; &lt; &gt;... 같은 특수문자를 변환한다.
	* @param html 변환하고자하는 html
	* @return 변환된 값
	*/
	public String convertSpecialChar(String html){
		html = html.replaceAll("&nbsp;", " ");
		html = html.replaceAll("&lt;", "<");
		html = html.replaceAll("&gt;", ">");
		html = html.replaceAll("&amp;", "&");
		html = html.replaceAll("&quot;", "\"");
	    
	    return html;
	}
	
	
	
	/**
	* HashMap<String, String> 자료 구조로 부터 key 목록을 ArrayList<String> 형태로 추출 한다.
	* @param h <String, String> 타입의 HashMap
	* @return ArrayList<String> key 목록
	*/
	private ArrayList<String> getKeyList(HashMap<String, String> h){
		ArrayList<String> keyList = new ArrayList<>();
		
		Object[] keys = h.keySet().toArray();
		
		for(int i =0 ; i < keys.length ; i++){
			keyList.add(keys[i].toString());
		}
		
		return keyList;
	}
}
