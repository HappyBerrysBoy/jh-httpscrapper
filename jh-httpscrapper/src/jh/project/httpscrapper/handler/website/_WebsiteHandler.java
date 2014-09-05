package jh.project.httpscrapper.handler.website;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
		return getHtml(httpclient, website, true);
	}
	
	/**
	* httpclient와 website 정보를 가지고 해당 url 의 html 코드를 반환한다.
	* @param httpclient http connection을 책임 질 httpclient
	* @param website 접속할 website 정보, url, parameter, encoding type...
	* @return html 코드
	*/
	protected String getHtml(CloseableHttpClient httpclient, Website website, boolean doEncoding){
		String html = "";
		try{
			
			if (website.getMethod().equals("GET")){
				String newUrl = website.getUrl();
				ArrayList<String> parameters = getKeyList(website.getParam());
				if (parameters.size() > 0) newUrl += "?";
				for(String parameter : parameters){
					newUrl += parameter + "=" + website.getParam().get(parameter);
				}
				website.setUrl(newUrl);
				
				URL obj = new URL(newUrl);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36");
				InputStreamReader isr;
				
				if(doEncoding)
					isr = new InputStreamReader(con.getInputStream(), website.getEncoding());
				else
					isr = new InputStreamReader(con.getInputStream());
				BufferedReader in = new BufferedReader(isr);
				String inputLine;
				StringBuffer response = new StringBuffer();
		 
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
		 
				html = response.toString();
//				HttpRequestBase terminalHttp = null;
//				terminalHttp = new HttpGet(website.getUrl());
//				CloseableHttpResponse response2 = httpclient.execute(terminalHttp);
//			    HttpEntity entity = response2.getEntity();
//			    
//			    html = EntityUtils.toString(entity, website.getEncoding());
//			    
//			    EntityUtils.consume(entity);
			    
			}else if (website.getMethod().equals("POST")){
				HttpRequestBase terminalHttp = null;
				terminalHttp = new HttpPost(website.getUrl());
				List <NameValuePair> nvps = new ArrayList <NameValuePair>();
				ArrayList<String> parameters = getKeyList(website.getParam());
				for(String parameter : parameters){
					nvps.add(new BasicNameValuePair(parameter, website.getParam().get(parameter)));
				}
				((HttpPost) terminalHttp).setEntity(new UrlEncodedFormEntity(nvps));
				CloseableHttpResponse response = httpclient.execute(terminalHttp);
			    HttpEntity entity = response.getEntity();
			    
			    html = EntityUtils.toString(entity, website.getEncoding());
			    
			    EntityUtils.consume(entity);
			}
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
		}else{
			return "";
		}
		
		return result.trim();
	}
	
	/**
	* 특정 id 가 포함된 Tag의 값(value)을 가져온다.
	* @param html html 코드
	* @param idName 검색 하고자 하는  id 명
	* @return 특정 id 가 포함된 Tag의 값(value)
	*/
	public String getValueById(String html, String idName){
		String result = html;
		Pattern pat = Pattern.compile("[<][^>]+((?i)id)+[ =\"']+((?i)" + idName + ")+['\"]+[\\s\\S]*<\\/[\\s\\S]*>");
		Matcher mat = pat.matcher(html);

		if (mat.find()){
			result = html.substring(mat.start(), mat.end());
			String tag = result.split(" ")[0].substring(1);
			result = this.getTag(result, tag);
		}else{
			return "";
		}
		
		return result.trim();
	}
	
	
	/**
	* 특정 attribute의 특정 값이 포함된 Tag의 값(value)을 가져온다.
	* @param html html 코드
	* @param attr 검색 하고자 하는  attribute 명
	* @param value 검색 하고자 하는 attribute의 value
	* @return 특정 class 가 포함된 Tag의 값(value)
	*/
	public String getValueByAttr(String html, String attr, String value){
		String result = html;
		Pattern pat = Pattern.compile("[<][^>]+((?i)" + attr + ")+[ =\"']+((?i)" + value + ")+['\"]+[\\s\\S]*<\\/[\\s\\S]*>");
		Matcher mat = pat.matcher(html);
		
		if (mat.find()){
			result = html.substring(mat.start(), mat.end());
			String tag = result.split(" ")[0].substring(1);
			result = this.getTag(result, tag);
		}else{
			return "";
		}
		
		return result.trim();
	}
		
	
	/**
	* 특정 class 가 포함된 Tag의 값(value)을 제거한다.
	* @param html html 코드
	* @param className 제거 하고자 하는  class 명
	* @return 특정 class 가 포함된 Tag가 제거된 html 코드
	*/
	public String removeValueByClass(String html, String className){
		String result = "";
		
		result = html.replace(this.getValueByClass(html, className).trim(), "");
		return result;
	}
	
	/**
	* 특정 id 가 포함된 Tag의 값(value)을 제거한다.
	* @param html html 코드
	* @param idName 제거 하고자 하는  id 명
	* @return 특정 id 가 포함된 Tag가 제거된 html 코드
	*/
	public String removeValueById(String html, String idName){
		String result = "";
		
		result = html.replace(this.getValueById(html, idName).trim(), "");
		return result;
	}
	
	/**
	* 특정 attribute에 특정 value 가 포함된 Tag의 값(value)을 제거한다.
	* @param html html 코드
	* @param attr 제거 하고자 하는  attr 명
	* @param value 제거하고자 하는 attr의 값
	* @return 특정 class 가 포함된 Tag가 제거된 html 코드
	*/
	public String removeValueByAttr(String html, String attr, String value){
		String result = "";
		
		result = html.replace(this.getValueByAttr(html, attr, value).trim(), "");
		return result;
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
	
	
	/**
	* 문자열내부에서 숫자값만 추출한다.
	* @param html 문자열
	* @return result 추출된 숫자 값
	*/
	public String getOnlyNumber(String html){
		String result = html;
		Pattern tag = Pattern.compile("[^0-9]+");
		Matcher mat = tag.matcher(result);
		result = mat.replaceAll("");
		
		return result;
	}
	
	
	/**
	* 문자열내부에서 특정 attribute의 value 값을 추출한다.
	* @param html 문자열
	* @param attr 추출할 attr 명
	* @return result 추출된 값
	*/
	public String getAttrValue(String html, String attr){
		String subStr = "";
		
		Pattern pattern = Pattern.compile(attr + "=[\'\"]+[^\'\"]+");
		Matcher mat = pattern.matcher(html);
		
		if (mat.find()) {
			subStr = html.substring(mat.start(), mat.end());
			subStr = subStr.replace(attr+"=\'", "").replace(attr+"=\"", "");
		}
		
		return subStr;
	}
	
	/**
	* 문자열내부에서 특정 값을 주어진 값으로 replace한다.
	* @param html 문자열
	* @param str 변경될 문자열
	* @param replace 대체될 문자열
	* @return result 대체된 문자
	*/
	public String replace(String html, String str, String replace){
		return html.replace(str, replace);
	}
}
