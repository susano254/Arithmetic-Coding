import java.util.ArrayList;
import java.util.Collections;

public class Helper {
    public static double calcProbability(char c, String stream) {
        double charCounter = 0;
        //each time similar char found increment counter
        for(int i = 0; i < stream.length(); i++)
            if(c == stream.charAt(i)) charCounter++;
        //finally divide by length to return probability
        return charCounter/stream.length();
    }

    //just a helper function to accumulate the probabilities of the nodes to construct their ranges
    public static void constructRanges(ArrayList<CharNode> list){
        double prevP = 0;
        for(int i = 0; i < list.size(); i++){
            CharNode temp = list.get(i);
            temp.low = prevP;
            temp.high = prevP + temp.p;
            prevP = temp.high;
        }
    }

    public static double scale(double value){
        //scale into E1
        if(value < 0.5) return value*2;
        //scale into E2
        else return (value-0.5)*2;
    }

    public static String padBinary(int precision){
        //always start with 1 and pad zeros until we meet precision length
        StringBuilder valueStr = new StringBuilder("1");
        //pad with zeros
        while(valueStr.length() < precision) valueStr.append('0');
        //return the String representation of the binary
        return valueStr.toString();
    }

    public static int findPrecision(ArrayList<CharNode> list){
        //sort ascending
        Collections.sort(list);
        //first element is the min
        double value = list.get(0).p;
        //temp represents 1/2 each time it's multiplied it increments 1/2^k
        double temp = 0.5;
        //k starts from one
        int k = 1;
        //increments each time we multiply temp
        while(temp > value){
            temp = temp*0.5;
            k++;
        }
        return 2*k;
    }

    public static String formatOutput(ArrayList<CharNode> list, String binaryString){
        StringBuilder output = new StringBuilder();
        //add the probabilities to the output
        for(int i = 0; i < list.size(); i++)
            output.append(list.get(i) + ", ");
        output.append("\n");
        //add the binary representation
        output.append("rB = " + binaryString);
        //return the output
        return output.toString();
    }

    
}
