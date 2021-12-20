import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Huffman {

    private  Map<Node,String> dictionaryC=new HashMap<>();
    private  Map<String,Node> dictionaryD =new HashMap<>();
    String fileParent;
    String fileName;





    public void Decompress(String fileName) throws IOException, ClassNotFoundException {
        File file=new File(fileName);
        this.getNames(file);
        FileInputStream in=new FileInputStream(file);
        readHeaders(in);
        byte[] arrOfBytes=readFile(in);
        this.writeCompressedFile(arrOfBytes);


    }

    private void getNames(File file){
        if (file.getParent()==null)
            fileParent="";
        else
            fileParent=file.getParent();

        this.fileName=file.getName();
        int pos = this.fileName.lastIndexOf(".");
        if (pos > 0) {
            this.fileName = this.fileName.substring(0, pos);
        }
        for (int i=0;i<2;i++){
            pos=this.fileName.indexOf(".");
            this.fileName = this.fileName.substring( pos+1);

        }
        this.fileName="extracted."+this.fileName;

    }


    private void writeCompressedFile(byte[] file) throws IOException {
        File compressedFile=new File(this.fileParent+this.fileName);
        FileOutputStream os = new FileOutputStream(compressedFile);
        BufferedOutputStream output=new BufferedOutputStream(os);
        String sb ;
        String s="";
        int i=0;
        for(i=0;i<file.length;i++){
            sb=toBinary(Arrays.copyOfRange(file,i,i+1));

            for(int j=0;j<8;j++){
                s+=sb.charAt(0);
                sb=sb.substring(1);
                if(dictionaryD.containsKey(s)){
                    output.write(dictionaryD.get(s).getValue());
                    s="";
                }
            }
        }


        output.close();

    }

    private byte[] readFile(FileInputStream in) throws IOException {
        BufferedInputStream input=new BufferedInputStream(in);
        return input.readAllBytes();


    }


    private   String toBinary( byte[] bytes ) {
        StringBuilder sb=new StringBuilder();
        for( int i = 0; i < Byte.SIZE * bytes.length; i++ )
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }


    private void readHeaders(FileInputStream in) throws IOException {
        DataInputStream data=new DataInputStream(in);
        PriorityQueue<Node> queue=new PriorityQueue<>(new NodeComparator());
        int n= data.readInt();
        int size= data.readInt();
        for(int i=0;i<size;i++) {
            //System.out.println("Key = " + entry.getKey() + ", frequency = " +entry.getValue());
            Node newNode=new Node(data.readNBytes(n),data.readInt());
            queue.add(newNode);
        }
        this.Encode(false,GreedyHuffman(queue),"");

    }





    public void Compress(String fileName,int n) throws IOException {
        byte[] file=ReadFile(fileName);
        GetHuffmanTree(file,n);
        writeCompressedFile(file,n);
        //writeObject("obj.txt",dictionary2);


    }

    private byte[] ReadFile(String fileName) throws IOException {

        File file=new File(fileName);
        InputStream is=new FileInputStream(file);
        BufferedInputStream input=new BufferedInputStream(is);
        if (file.getParent()==null)
            fileParent="";
        else
            fileParent=file.getParent();

        this.fileName=file.getName();
        byte[] arr=input.readAllBytes();
        input.close();
        return arr;


    }


    //frquencies
    private void GetHuffmanTree(byte[] file, int n) throws IOException {
        Map<Node,Integer> map=new HashMap<>();
        for(int i=0;i<file.length;i=i+n){

            Node newNode=new Node(Arrays.copyOfRange(file,i,i+n));
            if(map.containsKey(newNode))
                map.put(newNode,map.get(newNode)+1);
            else{
                map.put(newNode,1);

            }

        }
        //put in the priority queue
        PriorityQueue<Node> queue=new PriorityQueue<>(map.size(),new NodeComparator());
        for (Map.Entry<Node, Integer> entry : map.entrySet()) {
            //System.out.println("Key = " + entry.getKey() + ", frequency = " +entry.getValue());
            entry.getKey().setFrequency(entry.getValue());
            queue.add(entry.getKey());
        }
        map.clear();
        this.Encode(true, GreedyHuffman(queue),"");

    }



    // building the tree
    private Node GreedyHuffman(PriorityQueue<Node> queue){
        int size=queue.size();
        if(size==1){
            Node newNode=new Node(null);
            Node minimum1=queue.remove();
            newNode.setLeft(minimum1);
            newNode.setFrequency(minimum1.getFrequency());
            queue.add(newNode);
        }
        for(int i=0;i<size-1;i++){

            Node newNode=new Node(null);
            Node minimum1=queue.remove();
            newNode.setLeft(minimum1);
            if(queue.size()!=0){
                Node minimum2=queue.remove();
                newNode.setFrequency(minimum1.getFrequency()+minimum2.getFrequency());
                newNode.setRight(minimum2);
            }
            else
                newNode.setFrequency(minimum1.getFrequency());
            queue.add(newNode);

        }

        return queue.remove();
    }


    private void Encode (boolean isCompressing,Node node ,String code){
        if(node==null)
            return;
        if(node.getValue()!=null){
            if(isCompressing)
                dictionaryC.put(node,code);
            else
                dictionaryD.put(code,node);
            // dictionary2.put(node.getValue(),node.getFrequency());
            //System.out.println("Key = " + node + ", code = " +code);

            return;
        }
        Encode(isCompressing,node.getLeft(),code+"0");
        Encode(isCompressing,node.getRight(),code+"1");


    }


    private void writeCompressedFile(byte[] file, int n) throws IOException {
        File compressedFile=new File(fileParent+"18010468."+n+"."+fileName+".hc");
        FileOutputStream os = new FileOutputStream(compressedFile);
        BufferedOutputStream output=new BufferedOutputStream(os);
        Node node=new Node(null);
        StringBuffer s=new StringBuffer("");
        writeHeaders(output,n);

        for(int i=0;i<file.length;i=i+n){
            node.setValue(Arrays.copyOfRange(file,i,i+n));
            s.append(dictionaryC.get(node));
            if(s.length()%1024==0){
                output.write(fromBinaryToBytes(s.toString()));
                s.delete(0,s.length());
            }
        }
        if(!s.toString().equals(""))
            output.write(fromBinaryToBytes(s.toString()));
        output.close();
    }



    private void writeHeaders(BufferedOutputStream output,int n) throws IOException {
        DataOutputStream data=new DataOutputStream(output);
        data.writeInt(n);
        data.writeInt(dictionaryC.size());
        for (Map.Entry<Node, String> entry : dictionaryC.entrySet()) {
            //System.out.println("Key = " + entry.getKey() + ", frequency = " +entry.getValue());
            output.write(entry.getKey().getValue());
            data.writeInt(entry.getKey().getFrequency());
        }

    }


    private byte[] fromBinaryToBytes(String largeString) {
        if(largeString.length()==0)
            return null;
        int size=largeString.length();
        byte[] arrayOfBytes=new byte[(size+7)/8];
        int i=0;
        //System.out.println(largeString);
        for(String str : largeString.split("(?<=\\G.{8})")){
            arrayOfBytes[i]= (byte)Integer.parseInt(str, 2);

            i++;
        }

        return arrayOfBytes;
    }








}