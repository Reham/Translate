/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diwan.translation;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AltoDoc {

    public static Document parserXML(String uri) throws SAXException, IOException, ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
    }

    public static String getAlto(String pid) {
        InputStream in = null;
        String altoXml = "", tmp = "";
        try {
            URL u = new URL("http://dev.amuser-qstpb.com:8080/fedora/objects/" + pid + "/datastreams/F_OCR/content");
            in = u.openStream();
            int size = in.available();
            byte[] buf = new byte[size];
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                    u.openStream()));

            int r = 0;
            while ((tmp = br.readLine()) != null) {
                altoXml += tmp + "\n";
            }
            in.close();
        } catch (Exception ex) {
            System.err.println("not a URL Java understands.");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return altoXml;
    }

    public static ArrayList<String> getPageIds(String pid) {
        ArrayList<String> pageIds = null;
        try {
            Document doc = parserXML("http://dev.amuser-qstpb.com:8080/fedora/objects/" + pid + "/datastreams/RELS-EXT/content");
            if (doc != null) {
                NodeList hasPage = doc.getElementsByTagName("hasPageStruct");
                if (hasPage != null) {
                    Node node = hasPage.item(0);
                    if (node != null) {
                        NamedNodeMap attrList = node.getAttributes();
                        if (attrList != null) {
                            Node attr = attrList.getNamedItem("rdf:resource");
                            if (attr != null) {
                                String value = attr.getNodeValue();
                                if (value != null) {
                                    String pageId = value.split("/")[1];
                                    Document doc2 = parserXML("http://dev.amuser-qstpb.com:8080/fedora/objects/" + pageId + "/datastreams/RELS-EXT/content");
                                    if (doc2 != null) {
                                        NodeList desc = doc2.getElementsByTagName("rdf:Description");
                                        if (desc != null) {
                                            Node descNode = desc.item(0);
                                            if (descNode != null) {
                                                NodeList partList = descNode.getChildNodes();
                                                if (partList != null) {
                                                    pageIds = new ArrayList<String>();
                                                    for (int i = 0; i < partList.getLength(); i++) {
                                                        Node part = partList.item(i);
                                                        if (part != null && part.getNodeType() == Node.ELEMENT_NODE) {
                                                            if (part.getNodeName().indexOf("hasPart") > -1) {
                                                                NamedNodeMap partAttrList = part.getAttributes();
                                                                if (partAttrList != null) {
                                                                    Node partAttr = partAttrList.getNamedItem("rdf:resource");
                                                                    if (partAttr != null) {
                                                                        String partValue = partAttr.getNodeValue();
                                                                        if (partValue != null) {
                                                                            String partId = partValue.split("/")[1];
                                                                            pageIds.add(partId);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (SAXException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (ParserConfigurationException e) {
            return null;
        }
        return pageIds;
    }
}
