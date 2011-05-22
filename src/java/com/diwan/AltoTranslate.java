/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diwan;

import java.awt.Image;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author adilmbpro
 */
public class AltoTranslate extends HttpServlet {

    String ticketId;
    String inputLang;
    String outputLang;
    String sourceUrl;
    String inputFacet;
    String outputFacet;
    Image image = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here            */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AltoTranslate</title>");
            out.println("</head>");
            out.println("<body>");

            String data = request.getParameter("data");

            out.println(" <img src=http://dev.amuser-qstpb.com:8080/kk/images/logo.png  />");
            out.println("<h1>Automatic translation of the book has started. It may take some time</h1>");



            StringReader in = new StringReader(data);
            XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(in);

            while (reader.hasNext()) {
                XMLEvent event = (XMLEvent) reader.next();
                if (event.getEventType() == XMLEvent.START_ELEMENT) {
                    StartElement startEvent = event.asStartElement();
                    String startEventName = startEvent.getName().getLocalPart();
                    // detect start of a text block
                    if (startEventName.equalsIgnoreCase("url")) {
                        sourceUrl = reader.getElementText();
                    }
                    if (startEventName.equalsIgnoreCase("ticketId")) {
                        ticketId = reader.getElementText();
                    }
                    if (startEventName.equalsIgnoreCase("inputFacet")) {
                        inputLang = iterateAttibutes(startEvent, "language").substring(0, 2).toLowerCase();
                        inputFacet = reader.getElementText();
                    }
                    if (startEventName.equalsIgnoreCase("outputFacet")) {
                        outputLang = iterateAttibutes(startEvent, "language").substring(0, 2).toLowerCase();
                        outputFacet = reader.getElementText();
                    }
                }

            }

            out.println("</body>");
            out.println("</html>");

            TranslateThread trans = new TranslateThread(this);
            Thread th = new Thread(trans);
            if (th.isDaemon()) {
                th.setDaemon(false);
            }
            th.start();

        } catch (XMLStreamException ex) {
            Logger.getLogger(AltoTranslate.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.flush();
            out.close();
        }
    }

    private String iterateAttibutes(StartElement startEvent,
            String theAttribute) {
        String returnValue = null;
        Iterator<Attribute> blockAttributes = startEvent.getAttributes();
        while (blockAttributes.hasNext()) {
            Attribute attr = blockAttributes.next();
            if (attr.getName().getLocalPart().equalsIgnoreCase(theAttribute)) {
                returnValue = attr.getValue();
                break;
            }
        }

        return returnValue;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
