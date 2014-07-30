package jh.project.httpscrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jh.project.httpscrapper.handler.website._WebsiteHandler;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * Scrapper
 * 
 * @author purepleya
 * @since 2014-07-16
 */
public class Scrapper {
	
	/**
	 * Scrap 할 item이 정의된 file 명
	 */
	private final String ITEM_FILE_NAME = "ScrapItem.xml";
	
	private HashMap<String, String> extParams = new HashMap<String, String>();
	
	public Scrapper() {

	}
	

	/**
	* 외부에서 동적으로 입력하는 parameter 목록을 반환한다.
	* @return 외부에서 동적으로 입력하는 parameter
	*/
	public HashMap<String, String> getExtParams() {
		return extParams;
	}
	
	/**
	* 외부에서 동적으로 입력하는 parameter 목록을 설정한다.
	* @param extParams 외부에서 동적으로 입력하는 parameter 목록
	*/
	public void setExtParams(HashMap<String, String> extParams) {
		this.extParams = extParams;
	}
	
	/**
	* 외부에서 동적으로 입력하는 parameter를 추가한다.
	* @param key key 명
	* @param value 값
	*/
	public void addExtParams(String key, String value) {
		this.extParams.put(key, value);
	}
	


	/**
	* Scrap 할 item 목록을 xml로 부터 읽어온다.
	* @return Scrap 할 item 목록
	*/
	public ArrayList<ScrapItem> getScrapItem(){
		ArrayList<ScrapItem> itemList = new ArrayList<ScrapItem>();
		
		try{
			String filepath = new File(".").getCanonicalPath() + "/cfg/";
			Document itemDoc = getXMLDocument(filepath + this.ITEM_FILE_NAME);
			NodeList itemNode = itemDoc.getElementsByTagName("item");
			
			for(int i = 0; i < itemNode.getLength() ; i++){
				Element itemElement = (Element)itemNode.item(i);
				itemList.add(new ScrapItem(itemElement.getAttribute("id"), itemElement.getTextContent(), itemElement.getTextContent() + ".xml"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return itemList;
	}
	
	
	/**
	* Scrap 할 Site 정보를 xml로 부터 읽어온다.
	* @param scrapItem xmlFile 명이 명시된 ScrapItem
	* @return Scrap 할 Site 목록
	*/
	public ArrayList<Website> getWebsite(ScrapItem scrapItem){
		ArrayList<Website> websiteList = new ArrayList<Website>();
		
		try {
			if (scrapItem.getXmlFileName() == null || scrapItem.getXmlFileName().length() <= 0) {
				throw new Exception("no XML File Name");
			}
			
			String filepath = new File(".").getCanonicalPath() + "/cfg/";
			Document siteDoc = getXMLDocument(filepath + scrapItem.getXmlFileName());
			Element siteListElement = (Element) siteDoc.getElementsByTagName("site-list").item(0);
			String resultClassName = "";
			if (siteListElement.getAttribute("result") == null || siteListElement.getAttribute("result").length() <= 0){
				resultClassName = String.class.getName();
			}else{
				resultClassName = siteListElement.getAttribute("result");
			}
			
			NodeList siteNode = siteDoc.getElementsByTagName("site");
			
			for(int i = 0; i < siteNode.getLength() ; i++){
				Website website = new Website();
				Element siteElement = (Element) siteNode.item(i);
				
				website.setId(siteElement.getAttribute("id"));
				
				if (siteElement.getAttribute("handler") != null && siteElement.getAttribute("handler").length() > 0){
					website.setHandlerClassName(siteElement.getAttribute("handler"));
				}
				Class<?> Handler = Class.forName(website.getHandlerClassName());
				website.setHandler((_WebsiteHandler) Handler.newInstance());
				
				if (siteElement.getAttribute("result") != null || siteElement.getAttribute("result").length() > 0){
					website.setResultClassName(siteElement.getAttribute("result"));
				}
				website.setResultClassName(resultClassName);
				
				if (siteElement.getElementsByTagName("name").getLength() > 0) {
					Element nameElement = (Element) siteElement.getElementsByTagName("name").item(0);
					website.setName(nameElement.getTextContent());
				}
				
				if(siteElement.getElementsByTagName("url").getLength() > 0){
					Element urlElement = (Element)siteElement.getElementsByTagName("url").item(0);
					website.setUrl(urlElement.getTextContent());
				}
				
				if(siteElement.getElementsByTagName("encoding").getLength() > 0){
					Element encodingElement = (Element)siteElement.getElementsByTagName("encoding").item(0);
					website.setEncoding(encodingElement.getTextContent());
				}
				
				if(siteElement.getElementsByTagName("method").getLength() > 0){
					Element methodElement = (Element)siteElement.getElementsByTagName("method").item(0);
					website.setMethod(methodElement.getTextContent());
				}
				
				NodeList paramNode = siteElement.getElementsByTagName("param");
				for(int j = 0; j < paramNode.getLength() ; j++){
					Element paramElement = (Element)paramNode.item(j);
					String value = paramElement.getTextContent();
					
					if (value.contains("{[") && value.contains("]}")){
						String keyValue = value.replace("{[", "").replace("]}", "");
						value = this.extParams.get(keyValue);
					}
					website.addParam(paramElement.getAttribute("key"), value);
				}
				
				websiteList.add(website);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return websiteList;
	}
	
	
	/**
	* 특정 item의 웹 사이트들에 대해 스크래핑 한다.
	* @param item Scrap 할 item
	* @return Scrap 결과물
	*/
	public ScrapResult itemScrap(ScrapItem item, HashMap<String, String> options){
		ScrapResult scrapResult = new ScrapResult();
		ArrayList<Website> websites = getWebsite(item);
		
		for (Website website : websites){
			ScrapResult tempResult = websiteScrap(website, options);
			
			scrapResult.setClassName(tempResult.getClassName());
			try{
				scrapResult.getResults().addAll(tempResult.getResults());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return scrapResult;
	}
	
	
	/**
	* 특정 웹 사이트들에 대해 스크래핑 한다.
	* @param website Scrap 할 website
	* @return Scrap 결과물
	*/
	public ScrapResult websiteScrap(Website website, HashMap<String, String> options){
		ScrapResult scrapResult = new ScrapResult();
		_WebsiteHandler handler = website.getHandler();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		scrapResult.setClassName(website.getResultClassName());
		scrapResult.setResults(handler.getResult(httpclient, website, options));
		
		try{
			httpclient.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return scrapResult;
	}
	
	
	
	
	
	/**
	* XML 파일을 위치시켜야하는 위치를 알려준다.
	*/
	public void printLocationFileIs(){
		try {
			System.out.println(new File(".").getCanonicalPath() + "/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	* XML File로 부터 Document 객체를 생성한다.
	* @param file file 명
	* @return Document
	*/
	private Document getXMLDocument(String file){
		Document doc = null;
		
		try{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			File xmlFile = new File(file);
			doc = docBuilder.parse(xmlFile);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return doc;
	}
	
	
}
