package com.dengzii.plugin.findview;

public class AndroidView {
    private String type;
    private String id;
    private String layout;
    private String fullName;
    private String mappingField;
    private boolean generate = true;

    public AndroidView(String type, String id, String layout) {
        this.type = type;
        this.id = id;
        this.layout = layout;
    }

    public AndroidView(String type, String id, String layout, String fullName) {
        this.type = type;
        this.id = id;
        this.layout = layout;
        this.fullName = fullName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMappingField() {
        return mappingField;
    }

    public void setMappingField(String mappingField) {
        this.mappingField = mappingField;
    }

    public boolean isGenerate() {
        return generate;
    }

    public void setGenerate(boolean generate) {
        this.generate = generate;
    }
}
