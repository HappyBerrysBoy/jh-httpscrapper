package jh.project.httpscrapper.handler.website;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
	* @param doEncoding Website의 Encoding 형태로 Encoding이 필요한 경우 True
	* @return html 코드
	*/
	protected String getHtml(CloseableHttpClient httpclient, Website website, boolean doEncoding){
		String html = "";
		try{
			String newUrl = website.getUrl();
			String params = "";
			ArrayList<String> parameters = getKeyList(website.getParam());
			
			for(String parameter : parameters){
				params += parameter + "=" + website.getParam().get(parameter) + "&";
			}
			
			if(params.length() > 0)
				params = params.substring(0, params.length() - 1);
			
			if (parameters.size() > 0)
				website.setUrl(newUrl + "?" + params);
			else
				website.setUrl(newUrl);
			
			URL url = new URL(newUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(website.getMethod());
			
			if (website.getMethod().equals("GET")){
				con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36");
			}else if (website.getMethod().equals("POST")){
				con.setDoInput(true);
				con.setDoOutput(true);
				// Header Values..
				for(String key : website.getHeader().keySet()){
					con.setRequestProperty(key, website.getHeader().get(key));
				}
				
				OutputStream os = con.getOutputStream();
				// 그리고 write메소드로 메시지로 작성된 파라미터정보를 바이트단위로 "EUC-KR"로 인코딩해서 요청한다.
				// 여기서 중요한 점은 "UTF-8"로 해도 되는데 한글일 경우는 "EUC-KR"로 인코딩해야만 한글이 제대로 전달된다.
				os.write( params.getBytes());
			    os.flush();
			    os.close();
			}
			
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
