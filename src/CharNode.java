import java.util.Comparator;

public class CharNode implements Comparable<CharNode> {
    public char c;
    public double p;
    public double low;
    public double high;

    public CharNode(char c, double p, double low, double high) {
        this.c = c;
        this.p = p;
        this.low = low;
        this.high = high;
    }

    @Override
    public String toString() {
        return "("+ c + " = " + p + ")";
    }


    @Override
    public int compareTo(CharNode charNode) {
        if(this.p == charNode.p) return 0;
        else if(this.p < charNode.p) return -1;
        else return 1;
    }
}
