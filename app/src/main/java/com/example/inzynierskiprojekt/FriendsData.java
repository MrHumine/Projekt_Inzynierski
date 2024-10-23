package com.example.inzynierskiprojekt;

public class FriendsData {
    String name;
    String id;
    String localization;
    String character;
    String hair;
    String eyes;
    String skin;
    String height;
    String body;

    public FriendsData() {}
    public FriendsData(String id, String name, String localization, String character, String hair, String eyes, String skin, String height, String body) {
        this.id = id;
        this.name = name;
        this.localization = localization;
        this.character = character;
        this.hair = hair;
        this.eyes = eyes;
        this.skin = skin;
        this.height = height;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
