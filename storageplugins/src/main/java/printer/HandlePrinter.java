package printer;

public class HandlePrinter {

    public void print(String file){
        //根据格式选printer
        this.print(new Printer(file));
    }

    public void print(Printer p){
        p.before();
        p.printer();
        p.after();
    }
}
