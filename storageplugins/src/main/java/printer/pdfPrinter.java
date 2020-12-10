package printer;

public class pdfPrinter extends Printer {

    String test;

    public pdfPrinter(String path) {
        super(path);
    }

    /**
     * 如果是word,转pdf 再打 否则直接打
     */
    public void before() {

    }

    public void after() {

    }

    public Boolean printer() {
        return true;
    }
}
