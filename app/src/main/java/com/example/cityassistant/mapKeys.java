package com.example.cityassistant;

import android.util.Log;

public class mapKeys {

    private static final String tag = "our map fragment";

    public static String getMapSDKKey() {

        Log.d(tag, "retrieving MapSDKKey");

        return "94a2c45462d45953ba506378ecd7765b";

       // return "53b6387f19367f93621fafdc19de70da";

    }

    public static String getRestAPIKey() {

        Log.d(tag, "retrieving RestAPIKey ");

        return "94a2c45462d45953ba506378ecd7765b";

       // return "53b6387f19367f93621fafdc19de70da";
    }

    public static String getAtlasClientSecret() {
        Log.d(tag, "retrieving AtlasClientSecret");

        return "lrFxI-iSEg-rOctzIvMuCAQXOym5UFR7e7ZRehd02axrKfF600O9aIMIfmk1I4trRB4ZnFHaEVyKcDowb7_DBb3zth2K3gmuWvZCXuiuecw";
    }

    public static String getAtlasClientId() {
        Log.d(tag, "retrieving AtlasClientId");

        return "OkryzDZsLGNQZH78-Y52UgztWggmGBTo0iTRm1InIxHiOGjo1DoZa_a0gPtl4PmyqTvtekWllFsqVp4B4EWLj9N66TOGL2";
    }

}
