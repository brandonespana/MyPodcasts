package edu.asu.bsse.biespana.mypodcasts;

import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by biespana on 4/30/15.
 */
public class RssParser {
    public List parseXML(InputStream inputStream){
        System.out.println("In parseXML method");
        //DocumentBuilder builder = null;
        Document xmlDOM = null;
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            xmlDOM = builder.parse(inputStream);
            //return readFeed(xmlDOM);

        }catch (Exception e){
            e.printStackTrace();
        }

        return readFeed(xmlDOM);
    }

    public List readFeed(Document dom){
        List items = new ArrayList<String>();

        Element rssElement = dom.getDocumentElement();
        NodeList channelElements = rssElement.getElementsByTagName("channel");
        Node channel = channelElements.item(0);
        NodeList channelChildElements = channel.getChildNodes();

        int length = channelChildElements.getLength();
        System.out.println("Number of channel children: "+length);
        for(int i = 0; i < length; i++){
            Node currentNode = channelChildElements.item(i);
            System.out.println("at child: "+i+ "  name is: "+currentNode.getNodeName());
            if(currentNode.getNodeType()== Node.ELEMENT_NODE){
                System.out.println("child is ELEMENT ");
                Element currentElement = (Element)currentNode;
                if(currentElement.getTagName().equalsIgnoreCase("Description")){
                    System.out.println("child is also a Description");
                    String actualDescription = currentElement.getTextContent();
                    System.out.println("description is: "+actualDescription);
                    items.add(actualDescription);
                }
            }
        }
        return items;
    }
}
