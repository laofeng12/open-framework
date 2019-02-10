package org.ljdp.common.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XMLUtils {
	public static Document createDocument(String root, String[] elements) {
		Document document = DocumentHelper.createDocument();
		Element rootEL = document.addElement(root);
		addElements(rootEL, elements);
		return document;
	}

	public static void addElements(Element rootEL, String[] elements) {
		if(elements == null) {
			return;
		}
		for(int i = 0; i < elements.length; ++i) {
			rootEL.addElement(elements[i]);
		}
	}
	
	public static Element createElement(String root, String[] elements) {
		Element rootEL = DocumentHelper.createElement(root);
		addElements(rootEL, elements);
		return rootEL;
	}
	
	public static Element createElement(String name, String text) {
		Element el = DocumentHelper.createElement(name);
		el.setText(text);
		return el;
	}
	
	public static String asXML(Document document) {
		return asXML(document, "UTF-8");
	}
	
	public static String asXML(Document document, String encode) {
		OutputFormat format = new OutputFormat();
        format.setEncoding(encode);
        try {
            StringWriter out = new StringWriter();
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(document);
            writer.flush();
           return out.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOException while generating textual "
                    + "representation: " + e.getMessage());
        }
	}
	
	public static Document getDocument(String text) throws DocumentException {
		return DocumentHelper.parseText(text);
	}
	
	@SuppressWarnings("rawtypes")
	public static String asString(Element el) {
    	String beg = "<"+el.getName()+">";
    	String end = "</"+el.getName()+">";
    	String content = "";
    	if(el.elements().size() > 0) {
    		Iterator it = el.elementIterator();
    		while(it.hasNext()) {
    			content += asString((Element)it.next());
    		}
    	} else {
    		content = el.getText();
    	}
    	return beg+content+end;
    }
	
	public static String asXMLString(Document document) {
		String res = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		String xml = asString(document.getRootElement());
		return res + xml;
	}
	
	public static Document readDocument(String fileName) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(fileName));
		return doc;
	}
}
