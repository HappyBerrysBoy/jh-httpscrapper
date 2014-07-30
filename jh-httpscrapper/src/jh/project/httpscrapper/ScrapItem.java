package jh.project.httpscrapper;


/**
 * Scrap Item object
 * contains id, title, xml file name ...
 * 
 * @author purepleya
 * @since 2014-07-16
 */

public class ScrapItem {
	private String id;
	private String title;
	private String xmlFileName;
	
	
	public ScrapItem(String id, String title, String xmlFileName) {
		super();
		this.id = id;
		this.title = title;
		this.xmlFileName = xmlFileName;
	}
	
	
	/**
	* scrap item 의 id를 반환한다.
	* @return scrap item id
	*/
	public String getId() {
		return id;
	}
	
	/**
	* scrap item 의 id를 설정한다.
	* @param id scrap item id
	*/
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	* scrap item 의 title을 반환한다.
	* @return scrap item 의 title
	*/
	public String getTitle() {
		return title;
	}
	
	/**
	* scrap item 의 title을 설정한다.
	* @param title scrap item 의 title
	*/
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	* scrap 상세 정보가 작성된 xml file 명을 반환한다.
	* @return scrap 상세 정보 xml file 명
	*/
	public String getXmlFileName() {
		return xmlFileName;
	}
	
	/**
	* scrap 상세 정보가 작성된 xml file 명을 설정한다.
	* @param xmlFileName scrap 상세 정보가 작성된 xml file 명
	*/
	public void setXmlFileName(String xmlFileName) {
		this.xmlFileName = xmlFileName;
	}

	
}
