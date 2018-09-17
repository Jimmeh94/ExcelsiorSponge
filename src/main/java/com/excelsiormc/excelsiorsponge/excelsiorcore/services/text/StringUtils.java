package com.excelsiormc.excelsiorsponge.excelsiorcore.services.text;

public class StringUtils {

    public static String capitalizeFirstLetter(String string){
        string = string.toLowerCase();
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String replaceSpacesWithUnderscores(String string){
        return string.replace(" ", "_");
    }

    public static String[] replaceSpacesWithUnderscores(String[] strings){
        String[] give = new String[strings.length];
        for(int i = 0; i < give.length; i++){
            give[i] = replaceSpacesWithUnderscores(strings[i]);
        }
        return give;
    }

    public static String enumToString(Enum e, boolean replaceUnderscores){
        String give = e.toString();

        if(replaceUnderscores && give.contains("_")){
            String[] temp = give.split("_");
            give = "";
            for(String s: temp){
                give += capitalizeFirstLetter(s) + " ";
            }
            return give.substring(0, give.length() - 1);
        } else return capitalizeFirstLetter(give);
    }

    public static String enumToString(boolean replaceUnderscores, Enum... e){
        String give = "";
        for(int i = 0; i < e.length; i++){
            give += enumToString(e[i], replaceUnderscores);
            if(i <= e.length - 2){
                give += ", ";
            }
        }
        return give;
    }

}
