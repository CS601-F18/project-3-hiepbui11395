package cs601.project3.utils;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigurationManager {
	private static String configurationUrl = "config.xml";
	public static String getXmlConfiguration(String type, String info) {
		Document dom = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		String value = "";
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(configurationUrl);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		if(dom!=null) {
			Element configElement = dom.getDocumentElement();
			NodeList reviewNode = configElement.getElementsByTagName(type);
			NodeList values = ((Element)reviewNode.item(0)).getElementsByTagName(info);
			if(values != null && values.getLength() > 0) {
				Element valueEle = (Element)values.item(0);
				String valueStr = valueEle.getFirstChild().getNodeValue();
				value = valueStr;
			}
		}
		return value;
	}
}
