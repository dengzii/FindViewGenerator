package com.dengzii.plugin.findview;

public class AndroidView {
    String name;
    String id;
    String layout;
    String fullName;

    public AndroidView(String name, String id, String layout) {
        this.name = name;
        this.id = id;
        this.layout = layout;
    }

    public AndroidView(String name, String id, String layout, String fullName) {
        this.name = name;
        this.id = id;
        this.layout = layout;
        this.fullName = fullName;
    }
}
