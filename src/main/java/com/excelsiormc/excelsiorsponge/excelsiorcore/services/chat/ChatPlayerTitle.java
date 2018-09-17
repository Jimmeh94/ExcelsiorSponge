package com.excelsiormc.excelsiorsponge.excelsiorcore.services.chat;

public enum ChatPlayerTitle {

    TEST("Test");

    private String display;

    ChatPlayerTitle(String display){this.display = display;}

    public String getDisplay(){
        return "[" + display + "] ";
    }

}
