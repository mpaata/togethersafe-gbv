package com.togethersafe.desktop.util;

import java.util.HashMap;
import java.util.Map;

public class TranslationService {
    
    private static String currentLanguage = "English";
    private static final Map<String, Map<String, String>> translations = new HashMap<>();

    static {
        // English (Default)
        Map<String, String> en = new HashMap<>();
        en.put("Home", "Home");
        en.put("About", "About");
        en.put("EMERGENCY", "EMERGENCY");
        en.put("SOS Center", "SOS Center");
        en.put("Safety Planning", "Safety Planning");
        en.put("AI CHATBOT", "AI CHATBOT");
        en.put("Chat Interface", "Chat Interface");
        en.put("EVIDENCE VAULT", "EVIDENCE VAULT");
        en.put("My Cases", "My Cases");
        en.put("SOS Center", "SOS Center");
        en.put("In an emergency, you are not alone. Get immediate help, contact trusted people, and access safety resources.", "In an emergency, you are not alone. Get immediate help, contact trusted people, and access safety resources.");
        en.put("Need Immediate Help?", "Need Immediate Help?");
        en.put("Press the SOS button for emergency assistance.", "Press the SOS button for emergency assistance.");
        en.put("Emergency Contacts", "Emergency Contacts");
        en.put("Quick access to trusted helplines and support services.", "Quick access to trusted helplines and support services.");
        en.put("Police", "Police");
        en.put("Report crime, get immediate help", "Report crime, get immediate help");
        en.put("Medical", "Medical");
        en.put("Emergency medical assistance", "Emergency medical assistance");
        translations.put("English", en);

        // Luganda
        Map<String, String> lg = new HashMap<>();
        lg.put("Home", "Awaka");
        lg.put("About", "Ebikukwatako");
        lg.put("EMERGENCY", "AKATUBA");
        lg.put("SOS Center", "Ekifo ky'Akatuaba");
        lg.put("Safety Planning", "Enteekateeka y'Obukuumi");
        lg.put("AI CHATBOT", "AI CHATBOT");
        lg.put("Chat Interface", "Yogerera ne AI");
        lg.put("EVIDENCE VAULT", "EVIDENCE VAULT");
        lg.put("My Cases", "Emisango Gyange");
        lg.put("SOS Center", "Ekifo ky'Akatuaba");
        lg.put("In an emergency, you are not alone. Get immediate help, contact trusted people, and access safety resources.", "Mu katuba, toli weka. Funa obuyambi amangu, tukirira abantu abeesigika, era ofune obuyambi.");
        lg.put("Need Immediate Help?", "Weetaaga Obuyambi Amangu?");
        lg.put("Press the SOS button for emergency assistance.", "Nyiga akapapula k'SOS ofune obuyambi.");
        lg.put("Emergency Contacts", "Essimu z'Akatuaba");
        lg.put("Quick access to trusted helplines and support services.", "Okufuna mangu essimu z'abantu abeesigika n'obuyambi.");
        lg.put("Police", "Poliisi");
        lg.put("Report crime, get immediate help", "Ikaaba ebikolebwa, funa obuyambi amangu");
        lg.put("Medical", "Eddwaliro");
        lg.put("Emergency medical assistance", "Obuyambi bw'eddwaliro mu katuba");
        translations.put("Luganda", lg);

        // Kiswahili
        Map<String, String> sw = new HashMap<>();
        sw.put("Home", "Nyumbani");
        sw.put("About", "Kuhusu");
        sw.put("EMERGENCY", "DHARURA");
        sw.put("SOS Center", "Kituo cha Dharura");
        sw.put("Safety Planning", "Mpango wa Usalama");
        sw.put("AI CHATBOT", "AI CHATBOT");
        sw.put("Chat Interface", "Gumzo na AI");
        sw.put("EVIDENCE VAULT", "EVIDENCE VAULT");
        sw.put("My Cases", "Kesi Zangu");
        sw.put("SOS Center", "Kituo cha Dharura");
        sw.put("In an emergency, you are not alone. Get immediate help, contact trusted people, and access safety resources.", "Katika dharura, huko peke yako. Pata msaada wa haraka, wasiliana na watu wa kuaminika, na upate rasilimali za usalama.");
        sw.put("Need Immediate Help?", "Unahitaji Msaada wa Haraka?");
        sw.put("Press the SOS button for emergency assistance.", "Bonyeza kitufe cha SOS kwa msaada wa dharura.");
        sw.put("Emergency Contacts", "Anwani za Dharura");
        sw.put("Quick access to trusted helplines and support services.", "Upatikanaji wa haraka wa simu za kuaminika na huduma za msaada.");
        sw.put("Police", "Polisi");
        sw.put("Report crime, get immediate help", "Ripoti uhalifu, pata msaada wa haraka");
        sw.put("Medical", "Matibabu");
        sw.put("Emergency medical assistance", "Msaada wa matibabu ya dharura");
        translations.put("Kiswahili", sw);
    }

    public static void setLanguage(String lang) {
        currentLanguage = lang;
    }

    public static String get(String key) {
        Map<String, String> langMap = translations.getOrDefault(currentLanguage, translations.get("English"));
        return langMap.getOrDefault(key, key);
    }
}