package com.example.indigenous.i18n;

public enum Language {
    OSHIWAMBO("Oshiwambo", "🇳🇦"),
    SILOZI("Silozi", "🇳🇦"),
    RUKWANGALI("Rukwangali", "🇳🇦"),
    HERERO("Herero", "🇳🇦"); // (Otjiherero)

    private final String display;
    private final String flag;

    Language(String display, String flag) {
        this.display = display;
        this.flag = flag;
    }

    public String display() { return display; }
    public String flag() { return flag; }
}
