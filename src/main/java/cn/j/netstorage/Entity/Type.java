package cn.j.netstorage.Entity;

public enum Type {
    Music("Music"), Video("Video"), Other("Other"), Document("Document"), Folder("Folder");

    String type;

    public String getType(){
        return this.type;
    }

    Type(String type) {
        this.type = type;
    }

    public static Type getInstance(String ext) {
        if (equals(ext, "mp3", "flac", "ogg")) {
            return Music;
        } else if (equals(ext, "mp4", "mkv", "avi")) {
            return Video;
        } else if (equals(ext, "doc", "docx", "xls", "xlsx", "ppt", "pptx")) {
            return Document;
        } else if (equals(ext, "folder")) {
            return Folder;
        } else {
            return Other;
        }
    }

    public static Boolean equals(String ext, String... target) {
        ext = ext.toLowerCase();
        for (String aTarget : target) {
            if (ext.equals("."+aTarget)) return true;
        }
        return false;
    }
}
