/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.diwan.translation;

import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author adilmbpro
 */
public class EventProducerConsumerTest {

    public EventProducerConsumerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getNewSentenceEvent method, of class EventProducerConsumer.
     */
    @Test
    public void testGetNewSentenceEvent() {
        System.out.println("getNewSentenceEvent");
        EventProducerConsumer instance = new EventProducerConsumer();
        StartElement expResult = null;
        StartElement result = instance.getNewSentenceEvent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNewSentenceStartId method, of class EventProducerConsumer.
     */
    @Test
    public void testGetNewSentenceStartId() {
        System.out.println("getNewSentenceStartId");
        String startId = "";
        EventProducerConsumer instance = new EventProducerConsumer();
        Attribute expResult = null;
        Attribute result = instance.getNewSentenceStartId(startId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNewSentenceEndId method, of class EventProducerConsumer.
     */
    @Test
    public void testGetNewSentenceEndId() {
        System.out.println("getNewSentenceEndId");
        String endId = "";
        EventProducerConsumer instance = new EventProducerConsumer();
        Attribute expResult = null;
        Attribute result = instance.getNewSentenceEndId(endId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNewCharactersEvent method, of class EventProducerConsumer.
     */
    @Test
    public void testGetNewCharactersEvent() {
        System.out.println("getNewCharactersEvent");
        String characters = "";
        EventProducerConsumer instance = new EventProducerConsumer();
        Characters expResult = null;
        Characters result = instance.getNewCharactersEvent(characters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSentenceEndEvent method, of class EventProducerConsumer.
     */
    @Test
    public void testGetSentenceEndEvent() {
        System.out.println("getSentenceEndEvent");
        EventProducerConsumer instance = new EventProducerConsumer();
        EndElement expResult = null;
        EndElement result = instance.getSentenceEndEvent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNewAltEvent method, of class EventProducerConsumer.
     */
    @Test
    public void testGetNewAltEvent() {
        System.out.println("getNewAltEvent");
        EventProducerConsumer instance = new EventProducerConsumer();
        StartElement expResult = null;
        StartElement result = instance.getNewAltEvent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNewAltLang method, of class EventProducerConsumer.
     */
    @Test
    public void testGetNewAltLang() {
        System.out.println("getNewAltLang");
        String language = "";
        EventProducerConsumer instance = new EventProducerConsumer();
        Attribute expResult = null;
        Attribute result = instance.getNewAltLang(language);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAltEndEvent method, of class EventProducerConsumer.
     */
    @Test
    public void testGetAltEndEvent() {
        System.out.println("getAltEndEvent");
        EventProducerConsumer instance = new EventProducerConsumer();
        EndElement expResult = null;
        EndElement result = instance.getAltEndEvent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}