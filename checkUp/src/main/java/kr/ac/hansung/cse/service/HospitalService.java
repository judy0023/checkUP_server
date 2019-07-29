package kr.ac.hansung.cse.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import kr.ac.hansung.cse.model.HosInfo;
import kr.ac.hansung.cse.model.HosSearchResult;
import kr.ac.hansung.cse.model.TotalResult;

@Service
public class HospitalService {

	HosInfo hosInfo = null;

	HosSearchResult hosSearchResult = null;
	
	HttpURLConnection conn = null;	
	BufferedReader rd = null;	
	
	public HosSearchResult searchService(String siDoCd, String siGunGuCd, String hchType) {

		String strUrl = null;

		if (hchType.equals("0")) { // 전체 검진 타입 검색
			strUrl = "http://openapi1.nhis.or.kr/openapi/service/rest/HmcSearchService/getHmcList?"
					+ "ServiceKey=e7BmSW6lq7Vbh%2FB4%2B%2BOGbm%2FymNMnbVddsKrpzQ9%2F56Gdeu6pt6Y7N8SKqKjJEQwM8D0AY04WmMCloZ9epwZVGA%3D%3D"
					+ "&siDoCd=" + siDoCd + "&siGunGuCd=" + siGunGuCd + "&numOfRows=30";
		} else { // 해당 검진 타입 검색
			strUrl = "http://openapi1.nhis.or.kr/openapi/service/rest/HmcSearchService/getHmcList?"
					+ "ServiceKey=e7BmSW6lq7Vbh%2FB4%2B%2BOGbm%2FymNMnbVddsKrpzQ9%2F56Gdeu6pt6Y7N8SKqKjJEQwM8D0AY04WmMCloZ9epwZVGA%3D%3D"
					+ "&hchType=" + hchType + "&siDoCd=" + siDoCd + "&siGunGuCd=" + siGunGuCd + "&numOfRows=30";
		}

		try {

			String result = apiConn(strUrl);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder;
			Document doc = null;

			// xml 파싱하기
			InputSource is = new InputSource(new StringReader(result));
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();

			XPathExpression expr = xpath.compile("//items/item");
			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			Node node = null;
			String hmcNmExpress = null;
			String locAddrExpress = null;
			String hmcNoExpress = null;
			hosSearchResult = new HosSearchResult();

			System.out.println(nodeList.getLength());

			for (int i = 0; i < nodeList.getLength(); i++) {

				hmcNmExpress = "//items/item[" + (i + 1) + "]/hmcNm";
				locAddrExpress = "//items/item[" + (i + 1) + "]/locAddr";
				hmcNoExpress = "//items/item[" + (i + 1) + "]/hmcNo";

				node = (Node) xpath.evaluate(hmcNmExpress, doc, XPathConstants.NODE);
				hosSearchResult.getHos_name()[i] = node.getTextContent();
				System.out.println(hosSearchResult.getHos_name()[i]);

				node = (Node) xpath.evaluate(locAddrExpress, doc, XPathConstants.NODE);
				hosSearchResult.getHos_addr()[i] = cvrtAddr(node.getTextContent());

				node = (Node) xpath.evaluate(hmcNoExpress, doc, XPathConstants.NODE);
				hosSearchResult.getHos_no()[i] = node.getTextContent();

			}

			/*
			 * for (int j = 0; j < 30; j++) { System.out.println("j+1 번째 병원 : " +
			 * hosSearchResult.getHos_name()[j]); }
			 */

			rd.close();
			conn.disconnect();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return hosSearchResult;
	}

	public HosInfo getInfoService(String hos_name, String hos_no) {

		String EncodeHosName = null;

		try {
			EncodeHosName = URLEncoder.encode(hos_name, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String strUrl = "http://openapi1.nhis.or.kr/openapi/service/rest/HmcSearchService/getHmcList?"
				+ "&ServiceKey=e7BmSW6lq7Vbh%2FB4%2B%2BOGbm%2FymNMnbVddsKrpzQ9%2F56Gdeu6pt6Y7N8SKqKjJEQwM8D0AY04WmMCloZ9epwZVGA%3D%3D"
				+ "&hmcNm=" + EncodeHosName;

		try {

			String result = apiConn(strUrl);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder;
			Document doc = null;

			// xml 파싱하기
			InputSource is = new InputSource(new StringReader(result));
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();

			XPathExpression expr = xpath.compile("//items/item");
			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			Node node = null;

			String expression = null;
			String cdNmArr[] = { "locAddr", "hmcTelNo", "hmcNm", "bcExmdChrgTypeCd", "ccExmdChrgTypeCd",
					"cvxcaExmdChrgTypeCd", "stmcaExmdChrgTypeCd", "lvcaExmdChrgTypeCd", "grenChrgTypeCd",
					"ichkChrgTypeCd", "mchkChrgTypeCd" };
			String hosInfoArr[] = new String[11];
			Arrays.fill(hosInfoArr, "");

			for (int i = 0; i < nodeList.getLength(); i++) {

				// 병원 번호가 일치하는 병원의 정보 list 쪽으로 가기
				expression = "//items/item[" + (i + 1) + "]/hmcNo";
				node = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);

				if (node.getTextContent().equals(hos_no)) {

					int k = 0;
					// 해당 병원의 정보를 가져오기
					for (k = 0; k < 3; k++) {
						expression = "//items/item[" + (i + 1) + "]/" + cdNmArr[k];
						node = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);
						hosInfoArr[k] = node.getTextContent();
					}

					for (k = 3; k < cdNmArr.length; k++) {
						expression = "//items/item[" + (i + 1) + "]/" + cdNmArr[k];
						node = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);
						if (node.getTextContent().equals("0"))
							hosInfoArr[k] = "-";
						else
							hosInfoArr[k] = "O";
					}

					hosInfo = new HosInfo(hosInfoArr[0], hosInfoArr[1], hosInfoArr[2], hosInfoArr[3], hosInfoArr[4],
							hosInfoArr[5], hosInfoArr[6], hosInfoArr[7], hosInfoArr[8], hosInfoArr[9], hosInfoArr[10]);

					/*for (int p = 0; p < hosInfoArr.length; p++) {
						System.out.println(hosInfoArr[p]);
					}*/

					break;
				}

			}
			rd.close();
			conn.disconnect();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return hosInfo;
	}

	public void recommandHosService(String siDoCd, String siGunGuCd, List<TotalResult> totalResultList) {
		
		String strUrl = "http://openapi1.nhis.or.kr/openapi/service/rest/HmcSearchService/getHchkTypesHmcList?"
				+ "ServiceKey=e7BmSW6lq7Vbh%2FB4%2B%2BOGbm%2FymNMnbVddsKrpzQ9%2F56Gdeu6pt6Y7N8SKqKjJEQwM8D0AY04WmMCloZ9epwZVGA%3D%3D&hchType=3"
				+ "&numOfRows=55&"
				+ "siDoCd=" + siDoCd + "&"
				+ "siGunGuCd=" + siGunGuCd;
				
		try {		

			String result = apiConn(strUrl);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder;
			Document doc = null;

			// xml 파싱하기
			InputSource is = new InputSource(new StringReader(result));
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			
			XPathExpression expr = xpath.compile("//items/item");
			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			String cancerTypeCode;
			List<String> cancerTypeCodeList = new ArrayList<String>();

			for (int k = 0; k < totalResultList.size(); k++) {

				String cancerName = totalResultList.get(k).getCancer_name();

				// 폐암이거나 갑상선암이면 넘어가기
				if (cancerName.equals("폐암") || cancerName.equals("갑상선암"))
					continue;

				// 암 검진 리스트를 코드로 바꿔서 cancerTypeCodeList에 넣기!
				cancerTypeCode = checkCancerTypeCode(cancerName);
				cancerTypeCodeList.add(cancerTypeCode);
			}
			
			String [] hos_name = new String[10];
			Arrays.fill(hos_name, "");
			String [] hos_addr = new String[10];
			Arrays.fill(hos_addr, "");
			String [] hos_no = new String[10];
			Arrays.fill(hos_no, "");
			
			int s = 0;
					
			// cancerTypeCodeList에 있는 암 코드에 해당하는 병원만 뽑아내기
			for (int i = 0; i < nodeList.getLength(); i++) {
				
				if(s >= 10)
					break;				
				
				// item 안에서 암 검진 코드 목록의 값이 1인지 확인해서 조건을 만족하는 병원 이름 출력하기
				for (int j = 0; j < cancerTypeCodeList.size(); j++) {
					
					String str = "//items/item[" + (i + 1) + "]/" + cancerTypeCodeList.get(j);
					Node typeCode = (Node) xpath.evaluate(str, doc, XPathConstants.NODE);
					
					if (typeCode.getTextContent().equals("1")) { // 해당 암 검진 코드의 값이 1이면
						
						if (cancerTypeCodeList.size() == (j + 1)) { // 마지막 까지 만족했다면
														
							String strhmcNm = "//items/item[" + (i + 1) + "]/hmcNm"; // //items/item[2]/hmcNm
							String strlocAddr = "//items/item[" + (i + 1) + "]/locAddr";
							String strhmcNo = "//items/item[" + (i + 1) + "]/hmcNo";
							
							Node node = (Node) xpath.evaluate(strhmcNm, doc, XPathConstants.NODE);														
							System.out.println(i + 1 + "번째 item의 병원 이름 :" + node.getTextContent());
							hos_name[s] = node.getTextContent();
														
							node = (Node) xpath.evaluate(strlocAddr, doc, XPathConstants.NODE);
							//System.out.println(i + 1 + "번째 item의 병원 주소 :" + node.getTextContent());							
							hos_addr[s] = cvrtAddr(node.getTextContent());
							
							node = (Node) xpath.evaluate(strhmcNo, doc, XPathConstants.NODE);														
							//System.out.println(i + 1 + "번째 item의 병원 번호 :" + node.getTextContent());
							hos_no[s] = node.getTextContent();
								
							s++;
							
							System.out.println();
							
						} else
							continue;
					} else
						break;
				}

			}
			
			totalResultList.get(0).setHos_name(hos_name);
			totalResultList.get(0).setHos_addr(hos_addr);
			totalResultList.get(0).setHos_no(hos_no);
			
			rd.close();
			conn.disconnect();

			// System.out.println(sb.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String apiConn(String strUrl) {

		String result = "";
		String line;

		try {

			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/json");
			System.out.println("Response code: " + conn.getResponseCode());

			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}

			while ((line = rd.readLine()) != null) {
				result = result + line.trim();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return result;
	}

	public String checkCancerTypeCode(String cancerName) {

		String typeCode = null;

		switch (cancerName) {
		case "간암":
			typeCode = "lvcaExmdChrgTypeCd";
			break;
		case "대장암":
			typeCode = "ccExmdChrgTypeCd";
			break;
		case "자궁경부암":
			typeCode = "cvxcaExmdChrgTypeCd";
			break;
		case "위암":
			typeCode = "stmcaExmdChrgTypeCd";
			break;
		case "유방암":
			typeCode = "bcExmdChrgTypeCd";
			break;

		}

		return typeCode;
	}

	public String cvrtAddr(String longAddr) {

		String cvrtAddr = null;
		String addrSplitArr[] = longAddr.split(" ");
		String str2 = null;

		for (int l = 0; l < addrSplitArr.length; l++) {
			str2 = addrSplitArr[l].substring(addrSplitArr[l].length() - 1, addrSplitArr[l].length());
			if (str2.equals("로") || str2.equals("길")) {
				cvrtAddr = addrSplitArr[0];
				for (int m = 1; m <= l + 1; m++) {
					cvrtAddr += (" " + addrSplitArr[m]);
				}
				break;
			}
		}

		return cvrtAddr;
	}

}
