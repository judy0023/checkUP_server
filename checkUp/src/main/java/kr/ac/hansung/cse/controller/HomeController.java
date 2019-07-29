package kr.ac.hansung.cse.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.xml.sax.InputSource;

import kr.ac.hansung.cse.model.HosInfo;
import kr.ac.hansung.cse.model.HosSearchResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Controller
public class HomeController {
	HosSearchResult hosSearchResult = null;

	// @RequestMapping(value="/", method = RequestMethod.GET).
	@GetMapping("/")
	public String home(Model model) throws IOException {

		model.addAttribute("message", "hello world");

		getHosOpenApi("41","273", "1");

		return "index";
	}

	public void getHosOpenApi(String siDoCd, String siGunGuCd, String hchType) {

		String strUrl = null;

		if (hchType.equals("0")) { // 전체 검진 타입 검색
			strUrl = "http://openapi1.nhis.or.kr/openapi/service/rest/HmcSearchService/getHmcList?"
					+ "ServiceKey=e7BmSW6lq7Vbh%2FB4%2B%2BOGbm%2FymNMnbVddsKrpzQ9%2F56Gdeu6pt6Y7N8SKqKjJEQwM8D0AY04WmMCloZ9epwZVGA%3D%3D"
					+ "&siDoCd=" + siDoCd
					+ "&siGunGuCd=" + siGunGuCd
					+ "&numOfRows=30";
		} else { // 해당 검진 타입 검색
			strUrl = "http://openapi1.nhis.or.kr/openapi/service/rest/HmcSearchService/getHmcList?"
					+ "ServiceKey=e7BmSW6lq7Vbh%2FB4%2B%2BOGbm%2FymNMnbVddsKrpzQ9%2F56Gdeu6pt6Y7N8SKqKjJEQwM8D0AY04WmMCloZ9epwZVGA%3D%3D"
					+ "&hchType=" + hchType
					+ "&siDoCd=" + siDoCd 
					+ "&siGunGuCd=" + siGunGuCd
					+ "&numOfRows=30";
		}

		try {

			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/json");
			System.out.println("Response code: " + conn.getResponseCode());
			BufferedReader rd;

			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}

			// StringBuilder sb = new StringBuilder();
			String result = "";
			String line;

			while ((line = rd.readLine()) != null) {
				result = result + line.trim();
			}

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
			
			for(int j=0; j<30; j++) {
				System.out.println("j+1 번째 병원 : " + hosSearchResult.getHos_name()[j]);				
			}
			
			rd.close();
			conn.disconnect();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
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
