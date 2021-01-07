package cn.j.netstorage.Entity;

import java.util.*;

public enum Type {
    Music("Music"), Video("Video"), Other("Other"), Document("Document"), Folder("Folder"), Picture("Picture");

    String type;

    public String getType() {
        return this.type;
    }

    Type(String type) {
        this.type = type;
    }

    public static Type getInstance(String ext) {
        ext = ext.toLowerCase();
        String[] music = {".mp3", ".flac", ".ogg"};
        String[] video = {".mp4", ".mkv", ".avi"};
        String[] doc = {".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf", ".txt"};
        if (Arrays.asList(music).contains(ext)) {
            return Music;
        }

        if (Arrays.asList(video).contains(ext)) {
            return Video;
        }

        if (Arrays.asList(doc).contains(ext)) {
            return Document;
        }

        return Other;
//        if (equals(ext, "mp3", "flac", "ogg")) {
//            return Music;
//        } else if (equals(ext, "mp4", "mkv", "avi")) {
//            return Video;
//        } else if (equals(ext, "doc", "docx", "xls", "xlsx", "ppt", "pptx")) {
//            return Document;
//        } else {
//            return Other;
//        }
    }

    public static Boolean equals(String ext, String... target) {
        ext = ext.toLowerCase();
        for (String aTarget : target) {
            if (ext.equals("." + aTarget)) return true;
        }
        return false;
    }
}
