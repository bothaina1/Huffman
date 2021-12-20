import java.util.Arrays;

public class Node  {

    private byte[] value;
    private int frequency;
    private Node left;
    private Node right;


    public Node(byte[] value,int frequency){
        this.value=value;
        this.frequency=frequency;
    }
    public Node(byte[] value){
        this.value=value;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }



    @Override
    public boolean equals(Object other) {
        return Arrays.equals(this.value, ((Node)other).getValue());
    }


    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }
}