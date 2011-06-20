/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.diwan.translation;

import java.io.*;
import org.apache.commons.io.output.FileWriterWithEncoding;
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
	String text = "Translate this text into German";
	Translate t = null;

    public TranslateTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Terminal output encoding is: " + System.getProperty("file.encoding"));
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
        String text2 = "I have no idea what this language may be";
        Translate instance = new Translate();
        String expResult = "en";
        String result = instance.detect(text2);
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
        int minratingread = 5;
        int maxratingwrite = 4;
        int expireseconds = 300;
        Translate instance = new Translate();
        String result = instance.getAppIdToken(minratingread, maxratingwrite, expireseconds);
 		System.out.println("Your tokenized AppId is: " + result);
   }

    /**
     * Test of getLanguagesNames method, of class Translate.
     */
    @Test
    public void testGetLanguagesNames() throws Exception {
        System.out.println("getLanguagesNames");
        String locale = "en";
        String[] codeString = { "de", "en", "fr", "ko" };
        String result[] = t.getLanguagesNames(locale, codeString);
        String[] expResult = {"German", "English", "French", "Korean"};
        for (int i = 0; i < result.length; i++) {
            assertEquals(expResult[i], result[i]);
        }
    }

    /**
     * Test of getLanguageForSpeak method, of class Translate.
     */
    @Test
    public void testGetLanguageForSpeak() throws Exception {
        System.out.println("getLanguageForSpeak");
        String[] speakLanguages = t.getLanguageForSpeak();
        for (int i = 0; i < speakLanguages.length; i++) {
            System.out.println("The languages available are:"
                    + speakLanguages[i]);
        }
    }

    /**
     * Test of getLanguagesForTranslate method, of class Translate.
     */
    @Test
    public void testGetLanguagesForTranslate() throws Exception {
        System.out.println("getLanguagesForTranslate");
        String[] translateLanguages = t.getLanguagesForTranslate();
        System.out.println("The languages available for translation are: ");
        for (int i = 0; i < translateLanguages.length; i++) {
            System.out.println(translateLanguages[i]);
        }
    }

    /**
     * Test of getTranslations method, of class Translate.
     */
    @Test
    public void testGetTranslations() throws Exception {
        System.out.println("getTranslations");
        String[] translations = t.getTranslations(
                "To change this template, choose Tools", "en", "de", 5);
        for (int i = 0; i < translations.length; i++) {
            System.out.println("The matches are :" + translations[i]);
        }
    }

    /**
     * Test of getTranslationsArray method, of class Translate.
     */
    @Test
    public void testGetTranslationsArray() throws Exception {
        System.out.println("getTranslationsArray");
        String[] translationarray = t.getTranslationsArray(textsArray,
                "en", "fr", 5);
        for (int i = 0; i < translationarray.length; i++) {
            System.out.println("The array of matches : "
                    + translationarray[i]);
        }
    }

    /**
     * Test of speak method, of class Translate.
     */
    @Test
    public void testSpeak() throws Exception {
        System.out.println("speak");
        System.out.println("Speak Method : "
                + t.speak("je suis pianiste", "fr", "audio/wav"));
    }

    /**
     * Test of translateLine method, of class Translate.
     */
    @Test
    public void testTranslateLine() throws Exception {
        System.out.println("translateLine");
        BufferedReader in = new BufferedReader(new FileReader("test-files/in.txt"));
        BufferedWriter out = new BufferedWriter(new FileWriterWithEncoding(
                "out.txt", "UTF8"));
        String str;
        out.write(0xfeff);
        System.out.println("The translated lines are:");
        while ((str = in.readLine()) != null) {
            str = t.translateLine(str, "en", "ar");
            System.out.println(str);
            out.write(str+"\r");
        }

        out.close();
        in.close();
    }

    /**
     * Test of translateArray method, of class Translate.
     */
    @Test
    public void testTranslateArray() throws Exception {
        System.out.println("translateArray");
        String from = "en";
        String to = "de";
        Translate.TranslateArrayResult[] results = t.translateArray(textsArray, from, to);
        Integer i = 0;
        for (Translate.TranslateArrayResult result : results) {
            System.out.println ("TranslateArrayResult[" + i.toString() + "]: " + result.localTranslatedText);
            i++;
        }
    }

    /**
     * Test of TranslateXML method, of class Translate.
     */
    @Test
    public void testTranslateXML() throws Exception {
        System.out.println("TranslateXML");
        t.translateXML("http://loghati.amuser-qstpb.com/fedora", "iqra:55", "en", "ar", ""); //last string blank so that a facet is not really created
    }
}