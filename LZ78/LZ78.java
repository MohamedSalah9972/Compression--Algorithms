import java.util.*;

public class LZ78 {
    private class Tag {
        private int index;
        private char nextChar;

        Tag() {
        }

        Tag(int index, char nextChar) {
            this.index = index;
            this.nextChar = nextChar;
        }

        void print() {
            System.out.println(this.index + " " + this.nextChar);
        }
    }

    private ArrayList<Tag> tags;
    private String data;

    LZ78() {
        this.tags = new ArrayList<>();
        this.data = new String();
    }

    LZ78(String data) {
        this.tags = new ArrayList<>();
        this.data = data;
    }

    public void compress() {
        tags.clear();
        if (data.isEmpty())
            return;
        ArrayList<String> dictionary = new ArrayList<>();
        String sub = new String();
        dictionary.add(sub);
        sub = new String();
        for (int i = 0; i < data.length(); i++) {
            sub += (data.charAt(i));
            if (!dictionary.contains(sub)) {
                String previousSub = sub;
                previousSub = previousSub.substring(0, previousSub.length() - 1);
                tags.add(new Tag(dictionary.indexOf(previousSub), data.charAt(i)));
                dictionary.add(sub);
                sub = new String();
            }
        }
        if (!sub.isEmpty()) {
            String previousSub = sub;
            previousSub = sub.substring(0, sub.length() - 1);
            tags.add(new Tag(dictionary.indexOf(previousSub), data.charAt(data.length() - 1)));
        }
    }

    public void printTags() {
        for (Tag tag : tags) {
            tag.print();
        }
    }

    public String decompress() {
        String decodedString = new String();
        ArrayList<String> dictionary = new ArrayList<>();
        dictionary.add(new String());
        for (Tag tag : tags) {
            decodedString += dictionary.get(tag.index);
            dictionary.add(dictionary.get(tag.index) + tag.nextChar);
            decodedString += tag.nextChar;
        }
        return decodedString;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return this.data;
    }

    public ArrayList<Tag> getTags() {
        return this.tags;
    }

}
