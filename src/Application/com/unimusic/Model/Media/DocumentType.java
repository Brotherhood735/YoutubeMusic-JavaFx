package com.unimusic.Model.Media;

public enum DocumentType {
    SPARTITO("Spartito"),
    TESTO("Testo"),
    ACCORDI("Accordi");

    private final String label;
    DocumentType(String label) { this.label = label; }
    public String getLabel() { return label; }
}