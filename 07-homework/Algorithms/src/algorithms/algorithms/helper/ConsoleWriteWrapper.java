package algorithms.algorithms.helper;

public class ConsoleWriteWrapper {

    private boolean isLogEnabled = false;

    public ConsoleWriteWrapper(boolean isLogEnabled) {
        this.isLogEnabled = isLogEnabled;
    }

    public void log(String message){
        if (isLogEnabled) {
            System.out.println(message);
        }
    }

    public void error(String message){
        System.err.println(message);
    }
}
