import java.util.*;

public class BinaryArithmetic implements Compressor{
    public String stream;
    public double value;
    StringBuilder outputBinary = new StringBuilder();

    public Map<Character, CharNode> compressTable = new HashMap();
    public MyMap decompressTable = new MyMap();

    BinaryArithmetic(double value){ this.value = value; }
    BinaryArithmetic(String stream){ this.stream = stream; }

    public String compressFloat(){
        double low = 0, high = 1, range;
        double prevLow = 0, prevHigh = 1;           //low will be overwritten before high is updated so it must be saved
        CharNode currentCharNode;

        //construct ranges from probabilities
        Helper.constructRanges(new ArrayList<>(compressTable.values()));

        for(int i = 0; i < stream.length(); i++) {
            //get currentCharNode properties (ex. probability, lowBoundary, highBoundary etc)
            currentCharNode = compressTable.get(stream.charAt(i));

            //compute new values
            range = prevHigh - prevLow;
            low = prevLow + range * currentCharNode.low;
            high = prevLow + range * currentCharNode.high;

            //update the values
            prevLow = low;
            prevHigh = high;
        }
        value = (low+high)/2;
        return String.valueOf(value);
    }


    @Override
    public String compress() {
        double low = 0, high = 1, range;
        double prevLow = 0, prevHigh = 1;           //low will be overwritten before high is updated so it must be saved
        CharNode currentCharNode;
        int precision;

        //construct ranges from probabilities
        Helper.constructRanges(new ArrayList<>(compressTable.values()));
        precision = Helper.findPrecision(new ArrayList<>(compressTable.values()));

        for(int i = 0; i < stream.length(); i++){
            //get currentCharNode properties (ex. probability, lowBoundary, highBoundary etc)
            currentCharNode = compressTable.get(stream.charAt(i));

            //compute new values
            range = prevHigh - prevLow;
            low = prevLow + range*currentCharNode.low;
            high = prevLow + range*currentCharNode.high;

            //scale if needed
            while(0.5 < low || 0.5 > high){
                if(0.5 > high)  outputBinary.append('0');
                else            outputBinary.append('1');
                low = Helper.scale(low);
                high = Helper.scale(high);
            }

            //update the values
            prevLow = low;
            prevHigh = high;
        }

        //since 0.5 is  always in range padBinary has fixed value of "1"
        outputBinary.append(Helper.padBinary(precision));

        String output = Helper.formatOutput(new ArrayList<>(compressTable.values()), outputBinary.toString());
        return output;
    }

    public String decompressFloat(){
        double low = 0, high = 1, range;
        double prevLow = 0, prevHigh = 1;
        double decodeValue;
        CharNode currentCharNode;
        StringBuilder output = new StringBuilder();

        LinkedList<CharNode> list = new LinkedList<>(compressTable.values());
        for(int i = 0; i < list.size(); i++)
            decompressTable.put(list.get(i).low, list.get(i));

        for(int i = 0; i < stream.length(); i++){
            decodeValue = (value-low)/(high-low);

            //get currentCharNode properties (ex. probability, lowBoundary, highBoundary etc)
            currentCharNode = decompressTable.get(decodeValue);
            output.append(currentCharNode.c);

            //compute new values
            range = prevHigh - prevLow;
            low = prevLow + range*currentCharNode.low;
            high = prevLow + range*currentCharNode.high;

            //update the values
            prevLow = low;
            prevHigh = high;
        };
        return output.toString();
    }


    @Override
    public String decompress() {
        double low = 0, high = 1, range;
        double prevLow = 0, prevHigh = 1;           //low will be overwritten before high is updated so it must be saved
        double floatValue, decodeValue;
        CharNode currentCharNode;
        StringBuilder output = new StringBuilder();

        //get the length of input for testing for now
        int lengthInput = stream.length();

        //fill the Decompress table for decompressing
        //pass the already defined nodes in the compress table to the decompress table to try and reverse the operation
        LinkedList<CharNode> list = new LinkedList<>(compressTable.values());
        for(int i = 0; i < list.size(); i++)
            decompressTable.put(list.get(i).low, list.get(i));

        //k is the precision bits
        int k = Helper.findPrecision(new ArrayList<>(decompressTable.values()));
        //shiftIndex is for shifting for the next bit in the Binary representation
        int shiftIndex = k;
        //inputBinary always holds the current binary representation of the code (floatValue)
        StringBuffer inputBinary = new StringBuffer(outputBinary.substring(0,k));

        //init the float value for the loop
        floatValue = Integer.parseInt(inputBinary.toString(), 2) / Math.pow(2,k);

        for(int i = 0; i < lengthInput; i++){
            //scale the float value
            decodeValue = (floatValue-low)/(high-low);

            //get currentChar Node (ex. probability, lowBoundary, highBoundary etc)
            currentCharNode = decompressTable.get(decodeValue);
            output.append(currentCharNode.c);

            //compute new low and high values
            range = prevHigh - prevLow;
            low = prevLow + range*currentCharNode.low;
            high = prevLow + range*currentCharNode.high;

            //scale if needed
            while((0.5 < low || 0.5 > high)){
                //shift the input buffer by one char
                inputBinary.append(outputBinary.charAt(shiftIndex++));
                inputBinary.deleteCharAt(0);


                low = Helper.scale(low);
                high = Helper.scale(high);

                //update the float value
                floatValue = Integer.parseInt(inputBinary.toString(), 2)/Math.pow(2,k);
            }

            //update the values
            prevLow = low;
            prevHigh = high;
        };
        return output.toString();
    }
}

