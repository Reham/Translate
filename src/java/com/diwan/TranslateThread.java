package com.diwan;

import com.diwan.translation.Translate;
import com.diwan.translation.TranslateFault;
import java.util.logging.Level;
import java.util.logging.Logger;

class TranslateThread extends Thread {

    AltoTranslate outer;

    TranslateThread(AltoTranslate outer) {
        this.outer = outer;
    }

    // This method is called when the thread runs
    public void run() {
        try {
            String AppId = "6C9A92CF0DDDEF484F4C4ECEA2C82D8CE591A2AD";
            Translate t = new Translate(AppId, "text/plain", "general", "username", null);
            t.init();
            System.out.println("TranslateXML");
            t.translateXML(outer.sourceUrl, outer.ticketId, outer.inputLang, outer.outputLang, outer.outputFacet);
        } catch (TranslateFault ex) {
            Logger.getLogger(AltoTranslate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
