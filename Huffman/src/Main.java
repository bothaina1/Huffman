import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {



    public static void main(String[] args) throws IOException, ClassNotFoundException {

        String mode = null;
        String fileName =null;
        int  n = 1;


        if(args.length==3){
               mode=args[0];
               fileName=args[1];
               n=Integer.parseInt(args[2]);
        }
        if(args.length==2) {
            mode = args[0];
            fileName = args[1];
        }


        long start = System.currentTimeMillis();
        Huffman huffman=new Huffman();
        if(mode.equals("c"))
             huffman.Compress(fileName,n);
        if(mode.equals("d"))
            huffman.Decompress(fileName);
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        System.out.println(elapsedTime);






    }


}