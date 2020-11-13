import java.util.List;
import java.util.ArrayList;

class StringList {

    List<String> strings = new ArrayList<String>();

    public synchronized void add(String str) {
        int pos = strings.indexOf(str);
        if (pos < 0) {
            strings.add(str);
        }
    }

    public synchronized boolean remove(int index){
        if(index < strings.size() && index >= 0){
            strings.remove(index);
            return true;
        }
        return false;
    }

    public synchronized void reverse(int index){
        StringBuilder reverse = new StringBuilder(strings.get(index));
        reverse.reverse();
        strings.set(index, reverse.toString());
    }

    public synchronized String count(){
        StringBuilder count = new StringBuilder();
        count.append("[");
        int length;
        for(int i = 0; i < strings.size() - 1; i++){
            length = strings.get(i).length();
            count.append(length);
            count.append(" ");
        }
        length = strings.get(strings.size() - 1).length();
        count.append(length).append("]");
        return count.toString();
    }

    public synchronized boolean contains(int index) {
        return index >= 0 && index < strings.size();
    }

    public synchronized int size() {
        return strings.size();
    }

    public synchronized String toString() {
        return strings.toString();
    }
}
