package com.diwan.translation;

import java.rmi.RemoteException;
import org.apache.axis2.AxisFault;
import com.diwan.soap.SoapServiceStub;
import com.diwan.soap.SoapServiceStub.GetTranslationsResponse;
import com.diwan.soap.SoapServiceStub.*;
import java.lang.String;

public class Translate {
	private String appid;
    private String contenttype;
    private String category;
    private String user;
    private String uri;
    private SoapServiceStub stub = null;
    private TranslateOptions options = null;

	public Translate(String id, String type, String catg, String use, String ur)
        {
		appid = id;
		contenttype = type;
		category = catg;
		user = use;
		uri = ur;
	}

	public void init() throws TranslateFault {
		try {
			stub = new SoapServiceStub();
            options = new SoapServiceStub.TranslateOptions();
            options.setCategory("general");
            options.setContentType("text/plain");
            options.setUser("siteuser");

		} catch (AxisFault e) {
			e.printStackTrace();
			throw new TranslateFault(e.getReason());
		}
	}

	public void addTranslation(String original, String text, String from,
			String to, int rate) throws TranslateFault {
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

	public int[] breakSentences(String text, String language)
			throws TranslateFault {
		if (stub == null)
			throw new TranslateFault("Class not initalized");

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

	public String detect(String text) throws TranslateFault {
		if (stub == null)
			throw new TranslateFault("Class not initalized");

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

	public String[] detectArray(String[] texts) throws TranslateFault {

		if (stub == null)
			throw new TranslateFault("Class not initalized");

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

	public String getAppIdToken(int minratingread, int maxratingwrite,
			int expireseconds) throws TranslateFault {
		if (stub == null)
			throw new TranslateFault("Class not initalized");

		try {
			SoapServiceStub.GetAppIdToken appIdToken = new SoapServiceStub.GetAppIdToken();
			appIdToken.setAppId(appid);
			appIdToken.setMinRatingRead(minratingread);
			appIdToken.setMaxRatingWrite(maxratingwrite);
			appIdToken.setExpireSeconds(expireseconds);
			GetAppIdTokenResponse appIdTokenRespons = stub
					.getAppIdToken(appIdToken);
			return appIdTokenRespons.getGetAppIdTokenResult();
		} catch (RemoteException e) {
			throw new TranslateFault(e.getMessage());
		}
	}

	public String[] getLanguagesNames(String locale, String[] codeString)
			throws TranslateFault {
		if (stub == null)
			throw new TranslateFault("Class not initalized");

		try {
			SoapServiceStub.GetLanguageNames languageNames = new SoapServiceStub.GetLanguageNames();
			languageNames.setAppId(appid);

			SoapServiceStub.ArrayOfstring languageCodes = new SoapServiceStub.ArrayOfstring();
			languageCodes.setString(codeString);
			languageNames.setLanguageCodes(languageCodes);
			languageNames.setLocale(locale);
			SoapServiceStub.GetLanguageNamesResponse languageNamesResponse = stub
					.getLanguageNames(languageNames);
			return languageNamesResponse.getGetLanguageNamesResult()
					.getString();
		} catch (RemoteException e) {
			throw new TranslateFault(e.getMessage());
		}
	}

	public String[] getLanguageForSpeak() throws TranslateFault {
		if (stub == null)
			throw new TranslateFault("Class not initalized");

		try {
			SoapServiceStub.GetLanguagesForSpeak languageSpeak = new SoapServiceStub.GetLanguagesForSpeak();
			languageSpeak.setAppId(appid);
			GetLanguagesForSpeakResponse languageSpeakA = stub
					.getLanguagesForSpeak(languageSpeak);
			return languageSpeakA.getGetLanguagesForSpeakResult().getString();
		} catch (RemoteException e) {
			throw new TranslateFault(e.getMessage());
		}
	}

	public String[] getLanguagesForTranslate() throws TranslateFault {
		if (stub == null)
			throw new TranslateFault("Class not initalized");

		try {
			SoapServiceStub.GetLanguagesForTranslate languagesTranslate = new SoapServiceStub.GetLanguagesForTranslate();
			languagesTranslate.setAppId(appid);
			GetLanguagesForTranslateResponse languagesForTranslate = stub
					.getLanguagesForTranslate(languagesTranslate);
			return languagesForTranslate.getGetLanguagesForTranslateResult()
					.getString();
		} catch (RemoteException e) {
			throw new TranslateFault(e.getMessage());
		}
	}

	public String[] getTranslations(String text, String from, String to, int maxtranslations)
			throws TranslateFault {
		String[] matches = null;
		if (stub == null)
			throw new TranslateFault("Class not initalized");

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
            for (TranslationMatch theTranslationMatch : translationMatches)
                matches[i++] = theTranslationMatch.getTranslatedText();
            return matches;

		} catch (RemoteException e) {
			throw new TranslateFault(e.getMessage());
		}

	}

	public String[] getTranslationsArray(String[] texts, String from, String to, int maxtranslations)
			throws TranslateFault {
		String[] matches = null;
		if (stub == null)
			throw new TranslateFault("Class not initalized");

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
			GetTranslationsArrayResponse getTranslationResponses = stub
					.getTranslationsArray(translationArray);
			GetTranslationsResponse[] translationMatches =getTranslationResponses.getGetTranslationsArrayResult().getGetTranslationsResponse();
			matches = new String[translationMatches.length];
            int i = 0;
			for (GetTranslationsResponse translationResponse :translationMatches) {
				for (SoapServiceStub.TranslationMatch match : translationResponse.getTranslations().getTranslationMatch()) {
					 matches[i++] = match.getTranslatedText();
				}
			}

		} catch (RemoteException e) {
			throw new TranslateFault(e.getMessage());
		}

		return matches;

	}

	public String speak(String text, String language, String format)
			throws TranslateFault {
		if (stub == null)
			throw new TranslateFault("Class not initalized");

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

	public String translateLine(String text, String from, String to)
			throws TranslateFault {
		if (stub == null)
			throw new TranslateFault("Class not initalized");

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
			throw new TranslateFault(e.getMessage());
		}

	}

	public TranslateArrayResponse[] translateArray(String[] texts, String from, String to) throws TranslateFault {
		if (stub == null)
			throw new TranslateFault("Class not initalized");

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
			return translateArrayResponse.getTranslateArrayResult().getTranslateArrayResponse();
		} catch (RemoteException e) {
			throw new TranslateFault(e.getMessage());
		}
	}
	


    /**
     * @return the appid
     */
    public String getAppid() {
        return appid;
    }

    /**
     * @param appid the appid to set
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
     * @param contenttype the contenttype to set
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
     * @param category the category to set
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
     * @param user the user to set
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
     * @param uri the uri to set
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
     * @param stub the stub to set
     */
    public void setStub(SoapServiceStub stub) {
        this.stub = stub;
    }

}
