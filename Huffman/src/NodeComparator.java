import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

    public int compare(Node n1,Node n2){
        if(n1.getFrequency()<n2.getFrequency())
            return -1;
        else if(n1.getFrequency()>n2.getFrequency())
            return 1;
        else
            return 0;
    }

}