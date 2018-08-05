package com.company.babysteps.utils;

public class UrlConst {

    private static UrlConst urlConst;

    private UrlConst () {}

    public static UrlConst getInstance() {
        if(urlConst == null) {
            urlConst = new UrlConst();
        }
        return urlConst;
    }

    public String getUrl(int month, int week) {
        if(month > 0 && week > 3) {
            week = 3;
        }
        if(week > 7) {
            week = 7;
        }
        if(month == 11 && week > 3) {
            week = 3;
        }
        return URLS[month][week];
    }

    private String[][] URLS = {
            {URL_NEWBORN, URL_W1, URL_W2, URL_W3, URL_W4, URL_W5, URL_W6, URL_W7},
            {},
            {URL_M2_W1, URL_M2_W2, URL_M2_W3, URL_M2_W4},
            {URL_M3_W1, URL_M3_W2, URL_M3_W3, URL_M3_W4},
            {URL_M4_W1, URL_M4_W2, URL_M4_W3, URL_M4_W4},
            {URL_M5_W1, URL_M5_W2, URL_M5_W3, URL_M5_W4},
            {URL_M6_W1, URL_M6_W2, URL_M6_W3, URL_M6_W4},
            {URL_M7_W1, URL_M7_W2, URL_M7_W3, URL_M7_W4},
            {URL_M8_W1, URL_M8_W2, URL_M8_W3, URL_M8_W4},
            {URL_M9_W1, URL_M9_W2, URL_M9_W3, URL_M9_W4},
            {URL_M10_W1, URL_M10_W2, URL_M10_W3, URL_M10_W4},
            {URL_M11_W1, URL_M11_W2, URL_M11_W3, URL_M11_W4}
    };

    private static final String BASE_URL = "https://www.babycenter.com/";

    private static final String URL_NEWBORN = BASE_URL + "6_your-newborn_1130.bc";
    private static final String URL_W1 = BASE_URL + "6_your-1-week-old_1131.bc";
    private static final String URL_W2 = BASE_URL + "6_your-2-week-old_1132.bc";
    private static final String URL_W3 = BASE_URL + "6_your-3-week-old_1133.bc";

    private static final String URL_W4 = BASE_URL + "6_your-4-week-old_1134.bc";
    private static final String URL_W5 = BASE_URL + "6_your-5-week-old_1135.bc";
    private static final String URL_W6 = BASE_URL + "6_your-6-week-old_1136.bc";
    private static final String URL_W7 = BASE_URL + "6_your-7-week-old_1137.bc";

    private static final String URL_M2_W1 = BASE_URL + "6_your-2-month-old-week-1_5821.bc";
    private static final String URL_M2_W2 = BASE_URL + "6_your-2-month-old-week-2_1491335.bc";
    private static final String URL_M2_W3 = BASE_URL + "6_your-2-month-old-week-3_1491683.bc";
    private static final String URL_M2_W4 = BASE_URL + "6_your-2-month-old-week-4_1492896.bc";

    private static final String URL_M3_W1 = BASE_URL + "6_your-3-month-old-week-1_1138.bc";
    private static final String URL_M3_W2 = BASE_URL + "6_your-3-month-old-week-2_1495327.bc";
    private static final String URL_M3_W3 = BASE_URL + "6_your-3-month-old-week-3_1495329.bc";
    private static final String URL_M3_W4 = BASE_URL + "6_your-3-month-old-week-4_1495527.bc";

    private static final String URL_M4_W1 = BASE_URL + "6_your-4-month-old-week-1_1139.bc";
    private static final String URL_M4_W2 = BASE_URL + "6_your-4-month-old-week-2_1495531.bc";
    private static final String URL_M4_W3 = BASE_URL + "6_your-4-month-old-week-3_1495533.bc";
    private static final String URL_M4_W4 = BASE_URL + "6_your-4-month-old-week-4_1495535.bc";

    private static final String URL_M5_W1 = BASE_URL + "6_your-5-month-old-week-1_1141.bc";
    private static final String URL_M5_W2 = BASE_URL + "6_your-5-month-old-week-2_1495727.bc";
    private static final String URL_M5_W3 = BASE_URL + "6_your-5-month-old-week-3_1495737.bc";
    private static final String URL_M5_W4 = BASE_URL + "6_your-5-month-old-week-4_1495739.bc";

    private static final String URL_M6_W1 = BASE_URL + "6_your-6-month-old-week-1_1142.bc";
    private static final String URL_M6_W2 = BASE_URL + "6_your-6-month-old-week-2_1490890.bc";
    private static final String URL_M6_W3 = BASE_URL + "6_your-6-month-old-week-3_1495749.bc";
    private static final String URL_M6_W4 = BASE_URL + "6_your-6-month-old-week-4_1495751.bc";

    private static final String URL_M7_W1 = BASE_URL + "6_your-7-month-old-week-1_1143.bc";
    private static final String URL_M7_W2 = BASE_URL + "6_your-7-month-old-week-2_1495941.bc";
    private static final String URL_M7_W3 = BASE_URL + "6_your-7-month-old-week-3_1495943.bc";
    private static final String URL_M7_W4 = BASE_URL + "6_your-7-month-old-week-4_1495947.bc";

    private static final String URL_M8_W1 = BASE_URL + "6_your-8-month-old-week-1_1144.bc";
    private static final String URL_M8_W2 = BASE_URL + "6_your-8-month-old-week-2_1495973.bc";
    private static final String URL_M8_W3 = BASE_URL + "6_your-8-month-old-week-3_1495978.bc";
    private static final String URL_M8_W4 = BASE_URL + "6_your-8-month-old-week-4_1496147.bc";

    private static final String URL_M9_W1 = BASE_URL + "6_your-9-month-old-week-1_1145.bc";
    private static final String URL_M9_W2 = BASE_URL + "6_your-9-month-old-week-2_1496186.bc";
    private static final String URL_M9_W3 = BASE_URL + "6_your-9-month-old-week-3_1496195.bc";
    private static final String URL_M9_W4 = BASE_URL + "6_your-9-month-old-week-4_1496228.bc";

    private static final String URL_M10_W1 = BASE_URL + "6_your-10-month-old-week-1_1146.bc";
    private static final String URL_M10_W2 = BASE_URL + "6_your-10-month-old-week-2_1496233.bc";
    private static final String URL_M10_W3 = BASE_URL + "6_your-10-month-old-week-3_1496241.bc";
    private static final String URL_M10_W4 = BASE_URL + "6_your-10-month-old-week-4_1496243.bc";

    private static final String URL_M11_W1 = BASE_URL + "6_your-11-month-old-week-1_1147.bc";
    private static final String URL_M11_W2 = BASE_URL + "6_your-11-month-old-week-2_1496255.bc";
    private static final String URL_M11_W3 = BASE_URL + "6_your-11-month-old-week-3_1496257.bc";
    private static final String URL_M11_W4 = BASE_URL + "6_your-11-month-old-week-4_1496452.bc";
}