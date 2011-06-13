package com.diwan.translation;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.AxisFault;
import com.diwan.soap.SoapServiceStub;
import com.diwan.soap.SoapServiceStub.*;
import java.lang.String;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Reham Diwan Software Limited
 */
public class Translate {

    private String appid;
    private String contenttype;
    private String category;
    private String user;
    private String uri;
    private SoapServiceStub stub = null;
    private TranslateOptions options = null;
    private long startTime = 0;
    private long numberOfCalls = 0;
    private boolean firstTime = true;

    private void checkTime() {
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        try {
            //in case this is called multiple times with a new class - always delay first call for 1.2 seconds
            if (firstTime) {
                firstTime = false;
                if (totalTime < 1200) {
                    Thread.sleep(1200 - totalTime);
                }
                return;
            }

            if (startTime == 0 || totalTime > 60000) {
                startTime = System.currentTimeMillis();
                numberOfCalls = 0;
            } else {
                numberOfCalls++;
                if (numberOfCalls > 50) {
                    Thread.sleep(60000 - totalTime);
                    startTime = System.currentTimeMillis();
                    numberOfCalls = 0;
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Translate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Constructs class with default parameters.
     */
    public Translate() {
        this("6C9A92CF0DDDEF484F4C4ECEA2C82D8CE591A2AD", "text/plain", "general", "diwan", null);
    }

    /**
     * Constructs class with specific settings
     * @param id
     *  The application id used by the Microsoft Translator API
     * @param type
     *  The format of the text being translated.
     *  The supported formats are "text/plain" and "text/html". Any HTML needs to be well-formed.
     * @param catg
     *  The category of the text to translate. The only supported category is "general".
     * @param use
     *  A string used to track the originator of submissions to the translator.
     * @param ur
     *  Optional. A string containing the content location of submitted translations.
     */
    public Translate(String id, String type, String catg, String use, String uri) {
        appid = id;
        contenttype = type;
        category = catg;
        user = use;
        this.uri = uri;
        startTime = System.currentTimeMillis();
    }

    /**
     * Initialize the stub and add translation options
     * @throws TranslateFault
     */
    public void init() throws TranslateFault {
        try {
            stub = new SoapServiceStub();
            options = new SoapServiceStub.TranslateOptions();
            options.setCategory("general");
            options.setContentType("text/plain");
            options.setUser("siteuser");

        } catch (AxisFault e) {
            throw new TranslateFault(e.getReason());
        }
    }

    /**
     * Add translation to the translation memory
     * @param original
     *  text to translate from
     * @param text
     * translated text
     * @param from
     * the language code of the source
     *  @param to
     * the target language
     * @param rate
     * represents the quality rating for this text
     * @throws TranslateFault
     */
    public void addTranslation(String original, String text, String from, String to, int rate) throws TranslateFault {
        try {
            SoapServiceStub.AddTranslation addTranslation = new SoapServiceStub.AddTranslation();
            addTranslation.setAppId(appid);
            addTranslation.setOriginalText(original);
            addTranslation.setTranslatedText(text);
            addTranslation.setFrom(from);
            addTranslation.setTo(to);
            addTranslation.setRating(rate);
            addTranslation.setContentType(contenttype);
            addTranslation.setCategory(category);
            addTranslation.setUser(user);
            addTranslation.setUri(uri);
            stub.addTranslation(addTranslation);
            System.out.println("Your translation has been added");
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Breaks a piece of text into sentences and returns an array of integers that
     * contains the length in each sentence.
     * @param text
     * the text you need to split into sentences
     * @param language
     * the language code of the given text
     * @return array that contains length of each sentence
     * @throws TranslateFault
     */
    public int[] breakSentences(String text, String language) throws TranslateFault, InterruptedException {
        checkTime();
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.BreakSentences breakSentence = new SoapServiceStub.BreakSentences();
            breakSentence.setLanguage(language);
            breakSentence.setAppId(appid);
            breakSentence.setText(text);

            BreakSentencesResponse sentenceLen = stub.breakSentences(breakSentence);
            return sentenceLen.getBreakSentencesResult().get_int();
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Identify the language of given text
     * @param text
     * the text to detect
     * @return the detected language
     * @throws TranslateFault
     */
    public String detect(String text) throws TranslateFault {
        checkTime();
        if (stub == null) {
            init();
        }

        try {
            String detectString = "I have no idea what this language may be";
            SoapServiceStub.Detect detect = new SoapServiceStub.Detect();
            detect.setAppId(appid);
            detect.setText(detectString);
            DetectResponse detectResponse = stub.detect(detect);
            return detectResponse.getDetectResult();
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Identify the language of an array of string,Performs independent detection
     * of each individual array element and returns a result for each row of the array
     * @param texts
     * array of strings that need to detect
     * @return array of detected languages
     * @throws TranslateFault
     */
    public String[] detectArray(String[] texts) throws TranslateFault {
        checkTime();
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.DetectArray detectArray = new SoapServiceStub.DetectArray();
            SoapServiceStub.ArrayOfstring detectText = new SoapServiceStub.ArrayOfstring();
            detectText.setString(texts);
            detectArray.setAppId(appid);
            detectArray.setTexts(detectText);
            DetectArrayResponse detectedText = stub.detectArray(detectArray);
            return detectedText.getDetectArrayResult().getString();

        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }

    }

    /**
     * Returns a tokenized AppID which can be used as the AppID parameter in any method
     * @param minratingread
     * minimum rating translations require to be returned. Recommended is 5
     * as this will only include automatic translations and authority approved translations
     * @param maxratingwrite
     *  maximum rating that a user can write with using this token.
     * The recommended maxRatingWrite is from 1 to 4 for anonymous users, and from 6 to 10 for authoritative users whom you trust.
     * @param expireseconds
     * defines the duration in seconds from now that the token is valid. The value can be between 1 and 86400 (24 hours).
     * @return AppID value
     * @throws TranslateFault
     */
    public String getAppIdToken(int minratingread, int maxratingwrite, int expireseconds) throws TranslateFault {
        checkTime();
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.GetAppIdToken appIdToken = new SoapServiceStub.GetAppIdToken();
            appIdToken.setAppId(appid);
            appIdToken.setMinRatingRead(minratingread);
            appIdToken.setMaxRatingWrite(maxratingwrite);
            appIdToken.setExpireSeconds(expireseconds);
            GetAppIdTokenResponse appIdTokenRespons = stub.getAppIdToken(appIdToken);
            return appIdTokenRespons.getGetAppIdTokenResult();
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Retrieves friendly names for the languages passed in languageCodes
     * @param locale
     * representing a combination of an ISO 639 two-letter lowercase culture code associated with a language
     * and an ISO 3166 two-letter uppercase subculture code to localize the language names or a ISO 639 lowercase culture code by itself.
     * @param codeString
     * representing the ISO 639-1 language codes to retrieve the friendly name for.
     * @return names for languages passed in languageCodes
     * @throws TranslateFault
     */
    public String[] getLanguagesNames(String locale, String[] codeString)
            throws TranslateFault {
        checkTime();
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.GetLanguageNames languageNames = new SoapServiceStub.GetLanguageNames();
            languageNames.setAppId(appid);

            SoapServiceStub.ArrayOfstring languageCodes = new SoapServiceStub.ArrayOfstring();
            languageCodes.setString(codeString);
            languageNames.setLanguageCodes(languageCodes);
            languageNames.setLocale(locale);
            SoapServiceStub.GetLanguageNamesResponse languageNamesResponse = stub.getLanguageNames(languageNames);
            return languageNamesResponse.getGetLanguageNamesResult().getString();
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Retrieves the languages available for speech synthesis.
     * @return array of languages available for speak
     * @throws TranslateFault
     */
    public String[] getLanguageForSpeak() throws TranslateFault {
        checkTime();
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.GetLanguagesForSpeak languageSpeak = new SoapServiceStub.GetLanguagesForSpeak();
            languageSpeak.setAppId(appid);
            GetLanguagesForSpeakResponse languageSpeakA = stub.getLanguagesForSpeak(languageSpeak);
            return languageSpeakA.getGetLanguagesForSpeakResult().getString();
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Obtain a list of language codes representing languages that are supported by the Translation Service.
     * @return array of languages available for translation
     * @throws TranslateFault
     */
    public String[] getLanguagesForTranslate() throws TranslateFault {
        checkTime();
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.GetLanguagesForTranslate languagesTranslate = new SoapServiceStub.GetLanguagesForTranslate();
            languagesTranslate.setAppId(appid);
            GetLanguagesForTranslateResponse languagesForTranslate = stub.getLanguagesForTranslate(languagesTranslate);
            return languagesForTranslate.getGetLanguagesForTranslateResult().getString();
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Retrieves an array of translations for a given language pair from the store and the MT engine.
     * @param text
     *  text to translate.
     * @param from
     * the language code of the source
     * @param to
     * the target language
     * @param maxtranslations
     * representing the maximum number of translations to return.
     * @return array of translations
     * @throws TranslateFault
     */
    public String[] getTranslations(String text, String from, String to, int maxtranslations)
            throws TranslateFault {
        String[] matches = null;
        checkTime();
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.GetTranslations getTranslation = new SoapServiceStub.GetTranslations();
            getTranslation.setAppId(appid);
            getTranslation.setText(text);
            getTranslation.setFrom(from);
            getTranslation.setTo(to);
            getTranslation.setMaxTranslations(maxtranslations);
            getTranslation.setOptions(options);
            GetTranslationsResponse0 translationResponse = stub.getTranslations(getTranslation);

            TranslationMatch[] translationMatches = translationResponse.getGetTranslationsResult().getTranslations().getTranslationMatch();
            matches = new String[translationMatches.length];
            int i = 0;
            for (TranslationMatch theTranslationMatch : translationMatches) {
                matches[i++] = theTranslationMatch.getTranslatedText();
            }
            return matches;

        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }

    }

    /**
     * Retrieve multiple translation candidates for multiple source texts.
     * @param texts
     * array containing the texts for translation. All strings should be of the same language.
     * @param from
     * the language code of the source
     * @param to
     * the target language
     * @param maxtranslations
     * representing the maximum number of translations to return.
     * @return array of strings of translations for each text
     * @throws TranslateFault
     */
    public String[] getTranslationsArray(String[] texts, String from, String to, int maxtranslations)
            throws TranslateFault {
        String[] matches = null;
        checkTime();
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.GetTranslationsArray translationArray = new SoapServiceStub.GetTranslationsArray();
            SoapServiceStub.ArrayOfstring translateArrayString = new SoapServiceStub.ArrayOfstring();
            translateArrayString.setString(texts);
            translationArray.setAppId(appid);
            translationArray.setTexts(translateArrayString);
            translationArray.setFrom(from);
            translationArray.setTo(to);
            translationArray.setMaxTranslations(maxtranslations);
            translationArray.setOptions(options);
            GetTranslationsArrayResponse getTranslationResponses = stub.getTranslationsArray(translationArray);
            GetTranslationsResponse[] translationMatches = getTranslationResponses.getGetTranslationsArrayResult().getGetTranslationsResponse();
            matches = new String[translationMatches.length];
            int i = 0;
            for (GetTranslationsResponse translationResponse : translationMatches) {
                for (SoapServiceStub.TranslationMatch match : translationResponse.getTranslations().getTranslationMatch()) {
                    matches[i++] = match.getTranslatedText();
                }
            }

        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }

        return matches;
    }

    /**
     * Returns a string which is a URL to a wave stream of the passed-in text being spoken in the desired language.
     * @param text
     * Containing a sentence or sentences of the specified language to be spoken for the wave stream.
     * @param language
     * representing the supported language code to speak the text in.
     * @param format
     * A string specifying the content-type ID. The default value is "audio/wav" which is the only currently allowed value.
     * @return URL to wave stream of text spoken
     * @throws TranslateFault
     */
    public String speak(String text, String language, String format)
            throws TranslateFault {
        checkTime();
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.Speak speak = new SoapServiceStub.Speak();
            speak.setAppId(appid);
            speak.setText(text);
            speak.setLanguage(language);
            speak.setFormat(format);
            SpeakResponse speakOut = stub.speak(speak);
            return speakOut.getSpeakResult();
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Translates a text string from one language to another.
     * @param text
     * string representing the text to translate.
     * @param from
     * the language code of the source
     * @param to
     * the target language
     * @return the translated line
     * @throws TranslateFault
     */
    public String translateLine(String text, String from, String to)
            throws TranslateFault, InterruptedException {
        checkTime();
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.Translate translate = new SoapServiceStub.Translate();
            translate.setAppId(appid);
            translate.setText(text);
            translate.setFrom(from);
            translate.setTo(to);
            TranslateResponse result;
            result = stub.translate(translate);
            return result.getTranslateResult();
        } catch (RemoteException e) {
            if (e.getMessage().contains("NoTranslationFound")) {
                return text;
            } else if (e.getMessage().contains("V2_Soap")) {
                try {
                    Thread.sleep(60000);
                    SoapServiceStub.Translate translate = new SoapServiceStub.Translate();
                    translate.setAppId(appid);
                    translate.setText(text);
                    translate.setFrom(from);
                    translate.setTo(to);
                    TranslateResponse result;
                    result = stub.translate(translate);
                    return result.getTranslateResult();
                } catch (InterruptedException ex) {
                    throw new TranslateFault(e.getMessage());
                } catch (RemoteException ex) {
                    throw new TranslateFault(e.getMessage());
                }
            } else {
                throw new TranslateFault(e.getMessage());
            }
        }

    }

    /**
     * Retrieve translations for multiple source texts.
     * @param texts
     * array containing the texts for translation. All strings should be of the same language.
     * @param from
     * the language code of the source
     * @param to
     * the target language
     * @return the translated array
     * @throws TranslateFault
     */
    public TranslateArrayResult[] translateArray(String[] texts, String from, String to) throws TranslateFault {
        checkTime();
        if (stub == null) {
            init();
        }

        try {
            SoapServiceStub.TranslateArray translateArray = new SoapServiceStub.TranslateArray();
            SoapServiceStub.ArrayOfstring transArray1 = new SoapServiceStub.ArrayOfstring();
            transArray1.setString(texts);
            translateArray.setAppId(appid);
            translateArray.setTexts(transArray1);
            translateArray.setFrom(from);
            translateArray.setTo(to);
            translateArray.setOptions(options);
            TranslateArrayResponse1 translateArrayResponse = stub.translateArray(translateArray);
            TranslateArrayResponse[] t = translateArrayResponse.getTranslateArrayResult().getTranslateArrayResponse();
            TranslateArrayResult[] result = new TranslateArrayResult[t.length];
            int i = 0;
            for (TranslateArrayResponse t1 : t) {
                result[i++] = new TranslateArrayResult(t1);
            }
            return result;
        } catch (RemoteException e) {
            throw new TranslateFault(e.getMessage());
        }
    }

    /**
     * Translates an array of bytes extracted from an alto file and returns an array
     * of bytes that contains the translation with addition of needed tags
     * @param in
     * array of bytes extracted from an alto file
     * @param from
     * the language code of the source
     * @param to
     * the target language
     * @return array of bytes of the xml file
     * @throws TranslateFault
     */
    public void translateXML(String url, String pid, String from, String to, String outputFacet) {
        XMLEventWriter writer = null;
        ByteArrayOutputStream bos = null;
        TreeMap<Integer, String> idOffsetMap = null;
        StringBuffer blockText = null;
        Boolean inTextBlock = false;
        String currentTextBlockID = null;
        String currentTextLineID = null;
        String currentStringID = null;

        XMLEventFactory m_eventFactory = XMLEventFactory.newInstance();
        ArrayList<String> pageId = AltoDoc.getPageIds(url, pid);
        String altoPage = null;

        int i = 0;
        while (i < pageId.size()) {
            altoPage = AltoDoc.getAlto(url, pageId.get(i));
            StringReader serverStringReader = new StringReader(altoPage);
            BufferedReader in = new BufferedReader(serverStringReader);
            HttpURLConnection httpCon = null;
            // DataInputStream in = new DataInputStream(uc.getInputStream());
            try {
                EventProducerConsumer ms = new EventProducerConsumer();
                XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(in);
                bos = new ByteArrayOutputStream();
                writer = XMLOutputFactory.newInstance().createXMLEventWriter(bos);
                XMLEvent lastWhiteSpaceEvent = ms.getNewCharactersEvent(" ");
                while (reader.hasNext()) {
                    XMLEvent event = (XMLEvent) reader.next();
                    // detect start element
                    if (event.getEventType() == XMLEvent.START_ELEMENT) {
                        StartElement startEvent = event.asStartElement();
                        String startEventName = startEvent.getName().getLocalPart();
                        // detect start of a text block
                        if (startEventName.equalsIgnoreCase("textblock")) {
                            // initial state for a new text block
                            inTextBlock = true;
                            idOffsetMap = new TreeMap();
                            blockText = new StringBuffer();
                            currentTextBlockID = null;
                            currentTextLineID = null;
                            currentStringID = null;
                            // find the ID
                            currentTextBlockID = iterateAttibutes(startEvent, "ID");
                        }
                        if (startEventName.equalsIgnoreCase("textline")) {
                            currentTextLineID = iterateAttibutes(startEvent, "ID");
                        }
                        if (startEventName.equalsIgnoreCase("string")) {
                            currentStringID = iterateAttibutes(startEvent, "ID");
                            boolean inHyph = false;
                            String theString = null;
                            if (iterateAttibutes(startEvent, "subs_type") != null) {
                                inHyph = true;
                                theString = iterateAttibutes(startEvent, "SUBS_CONTENT");
                            } else {
                                inHyph = false;
                                theString = iterateAttibutes(startEvent, "content");
                                idOffsetMap.put(new Integer(blockText.length()), currentStringID);
                                blockText.append(theString);
                            }
                        }
                        if (startEventName.equalsIgnoreCase("sp")) {
                            blockText.append(" ");
                        }
                    } // I am at the closing text block tag so insert sentences
                    else if (currentStringID != null
                            && event.getEventType() == XMLEvent.END_ELEMENT
                            && event.asEndElement().getName().getLocalPart().equalsIgnoreCase("textblock")) {
                        try {
                            TranslateArrayResult[] translatedStrings;
                            int[] sentences = breakSentences(blockText.toString(), from);
                            ALTOSentence[] sentenceArray = extractSentencesArray(blockText.toString(), sentences, idOffsetMap);

                            //try to translate all the strings as an array
                            try {
                                String[] sentenceStrings = new String[sentenceArray.length];
                                int n = 0;
                                for (ALTOSentence theSentence : sentenceArray) {
                                    sentenceStrings[n++] = theSentence.sentence;
                                }
                                translatedStrings = translateArray(sentenceStrings, from, to);
                            } catch (TranslateFault ex) {
                                System.out.println("Translate Array failed (will translate lines) at: " + pid + " Word: " + sentenceArray[0].startID + " Page: " + pageId.get(i) + " From: " + from + " To: " + to);
                                translatedStrings = null;
                            }

                            //write the sentences out to the ALTO file
                            int n = 0;
                            for (ALTOSentence theSentence : sentenceArray) {
                                writer.add(ms.getNewSentenceEvent());
                                writer.add(ms.getNewSentenceStartId(theSentence.startID));
                                writer.add(ms.getNewSentenceEndId(theSentence.endID));
                                writer.add(lastWhiteSpaceEvent);

                                // calculate translated sentence
                                writer.add(ms.getNewAltEvent());
                                writer.add(ms.getNewAltLang(to));
                                writer.add(lastWhiteSpaceEvent);

                                //write the trnaslated strings if translate array succeeded otherwise call translateLine for each sentence
                                if (translatedStrings != null) {
                                    writer.add(ms.getNewCharactersEvent(translatedStrings[n++].getTranslatedText()));
                                } else {
                                    try {
                                        String translatedString = translateLine(theSentence.sentence, from, to);
                                        writer.add(ms.getNewCharactersEvent(translatedString));
                                    } catch (TranslateFault ex) {
                                        System.out.println(ex.toString());
                                        System.out.println("Translate fail at Book: " + pid + " Word: " + theSentence.startID + " Page: " + pageId.get(i) + " From: " + from + " To: " + to);
                                        writer.add(ms.getNewCharactersEvent(theSentence.sentence));
                                    }
                                }

                                writer.add(lastWhiteSpaceEvent);
                                writer.add(ms.getAltEndEvent());
                                writer.add(lastWhiteSpaceEvent);
                                writer.add(ms.getSentenceEndEvent());
                                writer.add(lastWhiteSpaceEvent);
                            }
                        } catch (TranslateFault ex) {
                            //break sentence failed so insert single dummy word here
                            System.out.println(ex.toString());
                            System.out.println("Break sentence fail at Book: " + pid + " Page: " + pageId.get(i) + " From: " + from + " To: " + to);
                            String sentenceStartID = idOffsetMap.firstEntry().getValue();
                            writer.add(ms.getNewSentenceEvent());
                            writer.add(ms.getNewSentenceStartId(sentenceStartID));
                            writer.add(ms.getNewSentenceEndId(sentenceStartID));
                            writer.add(lastWhiteSpaceEvent);
                            writer.add(ms.getNewAltEvent());
                            writer.add(ms.getNewAltLang(to));
                            writer.add(lastWhiteSpaceEvent);
                            writer.add(ms.getNewCharactersEvent("empty"));
                            writer.add(lastWhiteSpaceEvent);
                            writer.add(ms.getAltEndEvent());
                            writer.add(lastWhiteSpaceEvent);
                            writer.add(ms.getSentenceEndEvent());
                            writer.add(lastWhiteSpaceEvent);
                        }
                    } else if (event.getEventType() == XMLEvent.CHARACTERS && event.asCharacters().isWhiteSpace()) {
                        lastWhiteSpaceEvent = event;
                    }
                    writer.add(event);
                }
                writer.flush();
                if (outputFacet.length() > 0) {
                    URL outputUrl = new URL(url + "/objects/" + pageId.get(i) + "/datastreams/" + outputFacet);
                    httpCon = (HttpURLConnection) outputUrl.openConnection();
                    httpCon.setDoOutput(true);
                    httpCon.setUseCaches(false);
                    httpCon.setRequestProperty("Authorization", "Basic " + BasicAuth.encode("IQRAUser", "!QraUs3r"));
                    httpCon.setRequestProperty("Content-Type", "text/xml");
                    httpCon.setRequestMethod("POST");
                    httpCon.setRequestProperty("Content-Length", "" + Integer.toString(bos.toByteArray().length));
                    DataOutputStream wr = new DataOutputStream(httpCon.getOutputStream());
                    wr.write(bos.toByteArray());
                    wr.flush();
                    wr.close();
                    System.out.println(httpCon.getResponseCode());
                    System.out.println(httpCon.getResponseMessage());
                }
                System.out.println("Page: " + (i + 1) + " of " + pageId.size());
            } catch (Exception ex) {
                //nothing to do here move along
                System.out.println(ex.toString());
            } finally {
                if (httpCon != null) {
                    httpCon.disconnect();
                }
            }
            i++;
        }
        //OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());

    }

    public String getLanguage(String lang) {
        String stringLang = null;
        if (lang.equalsIgnoreCase("ar")) {
            stringLang = "ar_QA";
        } else if (lang.equalsIgnoreCase("en")) {
            stringLang = "en_EN";
        }
        return stringLang;

    }

    private ALTOSentence[] extractSentencesArray(String blocktext, int[] sentences, TreeMap<Integer, String> idOffsetMap) {
        ALTOSentence[] sentenceArray = new ALTOSentence[sentences.length];

        //setences may not match ALTO words. I will assume that the ALTO word breaks
        //take precedence. So I only start a new sentence at the beginning of an ALTO word
        //sentenceBreakDelta tracks this differnce. sentence length is the length minus the beginning
        //of an ALTO word.
        int sentenceBreakDelta = 0;
        int i = 0;
        int sentenceStart = 0;
        String sentenceStartID = idOffsetMap.firstEntry().getValue();
        Integer currentKey = idOffsetMap.firstKey();

        for (int sentenceLength : sentences) {
            int sentenceEnd = sentenceStart + sentenceLength - sentenceBreakDelta;
            // calculate the next sentence
            Integer previousKey = currentKey;
            while (currentKey != null && currentKey < sentenceEnd) {
                previousKey = currentKey;
                currentKey = idOffsetMap.higherKey(currentKey);
            }
            if (currentKey == null) {
                currentKey = sentenceEnd;
            }
            String sentenceEndID = idOffsetMap.get(previousKey);
            sentenceBreakDelta = currentKey - sentenceEnd;
            if (currentKey > sentenceEnd) {
                sentenceEnd = currentKey;
            }

            sentenceArray[i] = new ALTOSentence();
            sentenceArray[i].startID = sentenceStartID;
            sentenceArray[i].endID = sentenceEndID;
            sentenceArray[i].sentence = blocktext.substring(sentenceStart, sentenceEnd);

            // set ending state for next iteration
            sentenceStart += sentenceLength;
            sentenceStartID = idOffsetMap.get(currentKey);

            if (sentenceStartID == null) {
                //I have gone past the last key so get the lower key
                currentKey = idOffsetMap.lowerKey(currentKey);
                sentenceStartID = idOffsetMap.get(currentKey);
            }

            i++;
        }

        return sentenceArray;
    }

    private class ALTOSentence {

        String sentence;
        String startID;
        String endID;

        public String toString() {
            return sentence;
        }
    }

    /**
     * Iterate the attributes of start elements until getting a specific attribute and then return the value of that attribute
     * @param startEvent
     * the start element we are searching for
     * @param theAttribute
     * string that contains the name of the attribute
     * @return the value of the attribute
     */
    private static String iterateAttibutes(StartElement startEvent,
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

    /**
     * @return the appid
     */
    public String getAppid() {
        return appid;
    }

    /**
     * @param appid
     *            the appid to set
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * @return the contenttype
     */
    public String getContenttype() {
        return contenttype;
    }

    /**
     * @param contenttype
     *            the contenttype to set
     */
    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri
     *            the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the stub
     */
    public SoapServiceStub getStub() {
        return stub;
    }

    /**
     * @param stub
     *            the stub to set
     */
    public void setStub(SoapServiceStub stub) {
        this.stub = stub;
    }

    /**
     * @return the options
     */
    public TranslateOptions getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(TranslateOptions options) {
        this.options = options;
    }

    private static class EventProducerConsumer {

        XMLEventFactory m_eventFactory = XMLEventFactory.newInstance();

        /** Creates a new instance of EventProducerConsumer */
        public EventProducerConsumer() {
        }

        /**
         * @param args
         *            the command line arguments
         * @throws XMLStreamException
         * @throws IOException
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        /**
         * Create a new tag with name Sentence where it is a start element
         */
        public StartElement getNewSentenceEvent() {
            String name = "Sentence";
            return m_eventFactory.createStartElement("", null, name);
        }

        /**
         * Adding the sentence start ID
         * @param startId
         * the sentence start ID
         * @return attribute start
         */
        public Attribute getNewSentenceStartId(String startId) {
            Attribute start = m_eventFactory.createAttribute("Start", startId);
            return start;
        }

        /**
         * Adding the sentence end ID
         * @param endId
         * the sentence end ID
         * @return attribute end
         */
        public Attribute getNewSentenceEndId(String endId) {
            Attribute end = m_eventFactory.createAttribute("End", endId);
            return end;
        }

        /**
         * Adding characters
         * @param characters
         * Current character event.
         * @return Characters New Characters
         *            event.
         */
        public Characters getNewCharactersEvent(String characters) {
            return m_eventFactory.createCharacters(characters);
        }

        /**
         * Ending the sentence
         * @return the end element
         */
        public EndElement getSentenceEndEvent() {
            String name = "Sentence";
            return m_eventFactory.createEndElement("", null, name);
        }

        /**
         * Adding an alt event
         * @return Alt start element
         */
        public StartElement getNewAltEvent() {
            String name = "Alt";
            return m_eventFactory.createStartElement("", null, name);
        }

        /**
         * Get the Alt language
         * @param language
         * Alt language
         * @return the lang attribute
         */
        public Attribute getNewAltLang(String language) {
            Attribute lang = m_eventFactory.createAttribute("lang", language);
            return lang;
        }

        /**
         * Ending the Alt event
         * @return the Alt end element
         */
        public EndElement getAltEndEvent() {
            String name = "Alt";
            return m_eventFactory.createEndElement("", null, name);
        }
    }

    public static class TranslateArrayResult {

        /**
         * field for Error
         */
        protected java.lang.String localError;
        /**
         * field for From
         */
        protected java.lang.String localFrom;
        /*  This tracker boolean wil be used to detect whether the user called the set method
         *  for this attribute. It will be used to determine whether to include this field
         *  in the serialized XML
         */
        /**
         * field for OriginalTextSentenceLengths
         */
        protected int[] localOriginalTextSentenceLengths;
        /**
         * field for State
         */
        protected java.lang.String localState;
        /**
         * field for TranslatedText
         */
        protected java.lang.String localTranslatedText;
        /**
         * field for TranslatedTextSentenceLengths
         */
        protected int[] localTranslatedTextSentenceLengths;

        public TranslateArrayResult(TranslateArrayResponse t) {
            localError = t.getError();
            localState = t.getState();
            localFrom = t.getFrom();
            localOriginalTextSentenceLengths = t.getOriginalTextSentenceLengths().get_int();
            localTranslatedText = t.getTranslatedText();
            localTranslatedTextSentenceLengths = t.getTranslatedTextSentenceLengths().get_int();
        }

        /**
         * @return the localError
         */
        public java.lang.String getError() {
            return localError;
        }

        /**
         * @param localError the localError to set
         */
        public void setError(java.lang.String localError) {
            this.localError = localError;
        }

        /**
         * @return the localFrom
         */
        public java.lang.String getFrom() {
            return localFrom;
        }

        /**
         * @param localFrom the localFrom to set
         */
        public void setFrom(java.lang.String localFrom) {
            this.localFrom = localFrom;
        }

        /**
         * @return the localOriginalTextSentenceLengths
         */
        public int[] getOriginalTextSentenceLengths() {
            return localOriginalTextSentenceLengths;
        }

        /**
         * @param localOriginalTextSentenceLengths the localOriginalTextSentenceLengths to set
         */
        public void setOriginalTextSentenceLengths(int[] localOriginalTextSentenceLengths) {
            this.localOriginalTextSentenceLengths = localOriginalTextSentenceLengths;
        }

        /**
         * @return the localState
         */
        public java.lang.String getState() {
            return localState;
        }

        /**
         * @param localState the localState to set
         */
        public void setState(java.lang.String localState) {
            this.localState = localState;
        }

        /**
         * @return the localTranslatedText
         */
        public java.lang.String getTranslatedText() {
            return localTranslatedText;
        }

        /**
         * @param localTranslatedText the localTranslatedText to set
         */
        public void setTranslatedText(java.lang.String localTranslatedText) {
            this.localTranslatedText = localTranslatedText;
        }

        /**
         * @return the localTranslatedTextSentenceLengths
         */
        public int[] getTranslatedTextSentenceLengths() {
            return localTranslatedTextSentenceLengths;
        }

        /**
         * @param localTranslatedTextSentenceLengths the localTranslatedTextSentenceLengths to set
         */
        public void setTranslatedTextSentenceLengths(int[] localTranslatedTextSentenceLengths) {
            this.localTranslatedTextSentenceLengths = localTranslatedTextSentenceLengths;
        }
    }
}

class BasicAuth {

    private BasicAuth() {
    }
    // conversion table
    private static byte[] cvtTable = {
        (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E',
        (byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J',
        (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O',
        (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T',
        (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y',
        (byte) 'Z',
        (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e',
        (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j',
        (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o',
        (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't',
        (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y',
        (byte) 'z',
        (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4',
        (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9',
        (byte) '+', (byte) '/'
    };

    /**
     * Encode a name/password pair appropriate to
     * use in an HTTP header for Basic Authentication.
     *    name     the user's name
     *    passwd   the user's password
     *    returns  String   the base64 encoded name:password
     */
    static String encode(String name,
            String passwd) {
        byte input[] = (name + ":" + passwd).getBytes();
        byte[] output = new byte[((input.length / 3) + 1) * 4];
        int ridx = 0;
        int chunk = 0;

        /**
         * Loop through input with 3-byte stride. For
         * each 'chunk' of 3-bytes, create a 24-bit
         * value, then extract four 6-bit indices.
         * Use these indices to extract the base-64
         * encoding for this 6-bit 'character'
         */
        for (int i = 0; i < input.length; i += 3) {
            int left = input.length - i;

            // have at least three bytes of data left
            if (left > 2) {
                chunk = (input[i] << 16)
                        | (input[i + 1] << 8)
                        | input[i + 2];
                output[ridx++] = cvtTable[(chunk & 0xFC0000) >> 18];
                output[ridx++] = cvtTable[(chunk & 0x3F000) >> 12];
                output[ridx++] = cvtTable[(chunk & 0xFC0) >> 6];
                output[ridx++] = cvtTable[(chunk & 0x3F)];
            } else if (left == 2) {
                // down to 2 bytes. pad with 1 '='
                chunk = (input[i] << 16)
                        | (input[i + 1] << 8);
                output[ridx++] = cvtTable[(chunk & 0xFC0000) >> 18];
                output[ridx++] = cvtTable[(chunk & 0x3F000) >> 12];
                output[ridx++] = cvtTable[(chunk & 0xFC0) >> 6];
                output[ridx++] = '=';
            } else {
                // down to 1 byte. pad with 2 '='
                chunk = input[i] << 16;
                output[ridx++] = cvtTable[(chunk & 0xFC0000) >> 18];
                output[ridx++] = cvtTable[(chunk & 0x3F000) >> 12];
                output[ridx++] = '=';
                output[ridx++] = '=';
            }
        }
        return new String(output);
    }
}
