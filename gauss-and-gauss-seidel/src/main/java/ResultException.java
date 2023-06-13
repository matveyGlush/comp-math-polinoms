public class ResultException extends SecurityException{
    private final int i;
    public ResultException(int i) {
        this.i = i;
    }
    public int getI() { return i; }
}