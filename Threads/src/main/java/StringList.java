import java.util.List;
import java.util.ArrayList;

class StringList {

    List<String> strings = new ArrayList<String>();

    public void add(String str) {
        int pos = strings.indexOf(str);
        if (pos < 0) {
            strings.add(str);
        }
    }

    public boolean remove(int index){
        if(index < strings.size() && index >= 0){
            strings.remove(index);
            return true;
        }
        return false;
    }

    public boolean reverse(int index){
        if(index < strings.size() && index >= 0){
            StringBuilder sb = new StringBuilder(strings.get(index));
            sb.reverse();
            return true;
        }
        return false;
    }

    public boolean contains(String str) {
        return strings.indexOf(str) >= 0;
    }

    public int size() {
        return strings.size();
    }

    public String toString() {
        return strings.toString();
    }
}
