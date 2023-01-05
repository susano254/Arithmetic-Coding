public class Main {
    public static void main(String[] args) {
        String stream = "AABCBCCBCAABBBC";

        BinaryArithmetic arithmeticFloat = new BinaryArithmetic(stream);
        arithmeticFloat.compressTable.put('A', new CharNode('A', 0.6, -1, -1));
        arithmeticFloat.compressTable.put('B', new CharNode('B', 0.2, -1, -1));
        arithmeticFloat.compressTable.put('C', new CharNode('C', 0.2, -1, -1));

        System.out.println(arithmeticFloat.compress());
        System.out.println(arithmeticFloat.decompress());
        System.out.println(arithmeticFloat.compressFloat());
        System.out.println(arithmeticFloat.decompressFloat());
    }
}