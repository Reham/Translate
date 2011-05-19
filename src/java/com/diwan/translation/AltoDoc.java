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

    public static String getAlto(String sourceUrl,String pid) {
        InputStream in = null;
        String altoXml = "", tmp = "";
        try {
            URL u = new URL(sourceUrl + "/objects/" + pid + "/datastreams/F_OCR/content");
            in = u.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((tmp = br.readLine()) != null) {
                altoXml += tmp + "\n";
            }
            in.close();
        } catch (Exception ex) {
            System.err.println("not a URL Java understands.");
            System.err.println(ex.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.err.println(e.toString());
                }
            }
        }
        return altoXml;
    }

    public static ArrayList<String> getPageIds(String sourceURL,String pid) {
        ArrayList<String> pageIds = null;
        try {
            Document doc = parserXML(sourceURL + "/objects/" + pid + "/datastreams/RELS-EXT/content");
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
                                    Document doc2 = parserXML(sourceURL + "/objects/" + pageId + "/datastreams/RELS-EXT/content");
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
