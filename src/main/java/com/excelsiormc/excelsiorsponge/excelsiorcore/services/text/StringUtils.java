package com.excelsiormc.excelsiorsponge.excelsiorcore.services.text;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public static List<Text> getLongTextAsShort(Text text) {
        List<Text> give = new ArrayList<>();

        String string = text.toPlain();

        if(string.length() > 50){
            String temp;
            int limit = (string.length() / 50) + (string.length() % 50 > 0 ? 1 : 0);
            for(int i = 0; i < limit; i++){
                if(i < limit - 1) {
                    temp = string.substring(0 + (50 * i), 50 * (i + 1));
                } else {
                    temp = string.substring(0 + (50 * i));
                }
                give.add(Text.of(text.getFormat(), temp));
            }
        }

        return give;
    }

    public static List<Text> getLongTextAsShortScoreboard(Text text, Optional<TextColor> color) {
        List<Text> give = new ArrayList<>();

        String string = text.toPlain();

        if(string.length() > 36){
            String temp;
            int limit = (string.length() / 36) + (string.length() % 36 > 0 ? 1 : 0);
            for(int i = 0; i < limit; i++){
                if(i < limit - 1) {
                    temp = string.substring(0 + (36 * i), 36 * (i + 1));
                } else {
                    temp = string.substring(0 + (36 * i));
                }
                if(color.isPresent()){
                    give.add(Text.of(color.get(), temp));
                } else {
                    give.add(Text.of(text.getColor(), temp));
                }
            }
        }

        return give;
    }
}
