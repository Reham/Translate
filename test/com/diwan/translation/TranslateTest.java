/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.diwan.translation;

import com.diwan.soap.SoapServiceStub;
import com.diwan.soap.SoapServiceStub.TranslateArrayResponse;
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
public class TranslateTest {
    String AppId = "6C9A92CF0DDDEF484F4C4ECEA2C82D8CE591A2AD";
    String[] textsArray = { "I want this translated", "to something", "in another language" };
	Translate t = null;

    public TranslateTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        t = new Translate(AppId, "text/plain", "general", "username", null);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of init method, of class Translate.
     */
    @Test
    public void testInit() throws Exception {
        System.out.println("init");
        {
            Translate instance = new Translate();
            instance.init();
            assertNotNull(instance.getStub());
            assertNotNull(instance.getOptions());
        }

        {
            Translate instance = new Translate(AppId, "text/plain", "general", "username", null);
            instance.init();
            assertNotNull(instance.getStub());
            assertNotNull(instance.getOptions());
        }
    }

    /**
     * Test of breakSentences method, of class Translate.
     */
    @Test
    public void testBreakSentences() throws Exception {
        System.out.println("breakSentences");
        String breaksentence = "Please break up this string into sentences! I would like this string to be broken into its respective sentences. Is this possible?";
        String language = "en";
        System.out.println("The text is: " + breaksentence);
        Translate instance = new Translate();
        int[] expResult = new int[3];
        int[] result = instance.breakSentences(breaksentence, language);
        System.out.println("BreakSentences broke up the above sentence into " + result.length + " sentences.");
        assertEquals(expResult.length, result.length);
    }

    /**
     * Test of detect method, of class Translate.
     */
    @Test
    public void testDetect() throws Exception {
        System.out.println("detect");
        String text = "I have no idea what this language may be";
        Translate instance = new Translate();
        String expResult = "en";
        String result = instance.detect(text);
        assertEquals(expResult, result);
    }

    /**
     * Test of detectArray method, of class Translate.
     */
    @Test
    public void testDetectArray() throws Exception {
        System.out.println("detectArray");
        String[] detectArray = t.detectArray(textsArray);
        assertEquals(detectArray.length, 3);
        for (int i = 0; i < detectArray.length; i++) {
            assertEquals(detectArray[i], "en");
        }
    }

    /**
     * Test of getAppIdToken method, of class Translate.
     */
    @Test
    public void testGetAppIdToken() throws Exception {
        System.out.println("getAppIdToken");
        int minratingread = 0;
        int maxratingwrite = 0;
        int expireseconds = 0;
        Translate instance = new Translate();
        String expResult = "";
        String result = instance.getAppIdToken(minratingread, maxratingwrite, expireseconds);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLanguagesNames method, of class Translate.
     */
    @Test
    public void testGetLanguagesNames() throws Exception {
        System.out.println("getLanguagesNames");
        String locale = "";
        String[] codeString = null;
        Translate instance = new Translate();
        String[] expResult = null;
        String[] result = instance.getLanguagesNames(locale, codeString);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLanguageForSpeak method, of class Translate.
     */
    @Test
    public void testGetLanguageForSpeak() throws Exception {
        System.out.println("getLanguageForSpeak");
        Translate instance = new Translate();
        String[] expResult = null;
        String[] result = instance.getLanguageForSpeak();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLanguagesForTranslate method, of class Translate.
     */
    @Test
    public void testGetLanguagesForTranslate() throws Exception {
        System.out.println("getLanguagesForTranslate");
        Translate instance = new Translate();
        String[] expResult = null;
        String[] result = instance.getLanguagesForTranslate();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTranslations method, of class Translate.
     */
    @Test
    public void testGetTranslations() throws Exception {
        System.out.println("getTranslations");
        String text = "";
        String from = "";
        String to = "";
        int maxtranslations = 0;
        Translate instance = new Translate();
        String[] expResult = null;
        String[] result = instance.getTranslations(text, from, to, maxtranslations);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTranslationsArray method, of class Translate.
     */
    @Test
    public void testGetTranslationsArray() throws Exception {
        System.out.println("getTranslationsArray");
        String[] texts = null;
        String from = "";
        String to = "";
        int maxtranslations = 0;
        Translate instance = new Translate();
        String[] expResult = null;
        String[] result = instance.getTranslationsArray(texts, from, to, maxtranslations);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of speak method, of class Translate.
     */
    @Test
    public void testSpeak() throws Exception {
        System.out.println("speak");
        String text = "";
        String language = "";
        String format = "";
        Translate instance = new Translate();
        String expResult = "";
        String result = instance.speak(text, language, format);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of translateLine method, of class Translate.
     */
    @Test
    public void testTranslateLine() throws Exception {
        System.out.println("translateLine");
        String text = "";
        String from = "";
        String to = "";
        Translate instance = new Translate();
        String expResult = "";
        String result = instance.translateLine(text, from, to);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of translateArray method, of class Translate.
     */
    @Test
    public void testTranslateArray() throws Exception {
        System.out.println("translateArray");
        String[] texts = null;
        String from = "";
        String to = "";
        Translate instance = new Translate();
        TranslateArrayResponse[] expResult = null;
        TranslateArrayResponse[] result = instance.translateArray(texts, from, to);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of TranslateXML method, of class Translate.
     */
    @Test
    public void testTranslateXML() throws Exception {
        System.out.println("TranslateXML");
        byte[] in = null;
        Translate instance = new Translate();
        byte[] expResult = null;
        byte[] result = instance.TranslateXML(in);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAppid method, of class Translate.
     */
    @Test
    public void testGetAppid() {
        System.out.println("getAppid");
        Translate instance = new Translate();
        String expResult = "";
        String result = instance.getAppid();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAppid method, of class Translate.
     */
    @Test
    public void testSetAppid() {
        System.out.println("setAppid");
        String appid = "";
        Translate instance = new Translate();
        instance.setAppid(appid);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContenttype method, of class Translate.
     */
    @Test
    public void testGetContenttype() {
        System.out.println("getContenttype");
        Translate instance = new Translate();
        String expResult = "";
        String result = instance.getContenttype();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setContenttype method, of class Translate.
     */
    @Test
    public void testSetContenttype() {
        System.out.println("setContenttype");
        String contenttype = "";
        Translate instance = new Translate();
        instance.setContenttype(contenttype);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCategory method, of class Translate.
     */
    @Test
    public void testGetCategory() {
        System.out.println("getCategory");
        Translate instance = new Translate();
        String expResult = "";
        String result = instance.getCategory();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCategory method, of class Translate.
     */
    @Test
    public void testSetCategory() {
        System.out.println("setCategory");
        String category = "";
        Translate instance = new Translate();
        instance.setCategory(category);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUser method, of class Translate.
     */
    @Test
    public void testGetUser() {
        System.out.println("getUser");
        Translate instance = new Translate();
        String expResult = "";
        String result = instance.getUser();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUser method, of class Translate.
     */
    @Test
    public void testSetUser() {
        System.out.println("setUser");
        String user = "";
        Translate instance = new Translate();
        instance.setUser(user);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUri method, of class Translate.
     */
    @Test
    public void testGetUri() {
        System.out.println("getUri");
        Translate instance = new Translate();
        String expResult = "";
        String result = instance.getUri();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUri method, of class Translate.
     */
    @Test
    public void testSetUri() {
        System.out.println("setUri");
        String uri = "";
        Translate instance = new Translate();
        instance.setUri(uri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStub method, of class Translate.
     */
    @Test
    public void testGetStub() {
        System.out.println("getStub");
        Translate instance = new Translate();
        SoapServiceStub expResult = null;
        SoapServiceStub result = instance.getStub();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStub method, of class Translate.
     */
    @Test
    public void testSetStub() {
        System.out.println("setStub");
        SoapServiceStub stub = null;
        Translate instance = new Translate();
        instance.setStub(stub);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}