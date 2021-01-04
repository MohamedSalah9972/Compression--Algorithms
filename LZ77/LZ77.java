import java.util.ArrayList;

public class LZ77 {

    private class Tag {
        private int i, len;
        private char c;

        Tag() {
        }

        Tag(int i, int len, char c) {
            this.i = i; // index
            this.len = len; // length
            this.c = c; // new char
        }
    }

    private String data;
    private ArrayList<Tag> tags;

    LZ77(String data) {
        this.data = data;
        tags = new ArrayList<>();
    }

    LZ77() {
        tags = new ArrayList<>();
        this.data = null;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ArrayList<Tag> getTags() {
        return this.tags;
    }

    public String getData() {
        return this.data;
    }

    private int match(String string, String sub, int j) {
        int idx = -1;
        String subString = "";
        for (int i = 0; i < j - sub.length() + 1; i++) {
            if (sub.length() + i > j) {
                break;
            }
            try {
                subString = string.substring(i, i + sub.length());

            } catch (Exception e) {
                System.out.println(i + " " + j + " " + sub + " " + string);
                System.out.println(e);
            }

            if (subString.equals(sub)) {
                idx = i;
            }

        }
        return idx;
    }

    public void compress() {
        tags.clear();
        String sub = "";
        int idx = -1;
        for (int i = 0; i < data.length(); i++) {
            sub += data.charAt(i);
            if (match(data, sub, i - sub.length() + 1) != -1 && i != data.length() - 1) { /// second condition for last sub in data
                idx = match(data, sub, i - sub.length() + 1);

            } else {
                int len = sub.length();
                int toBack = idx == -1 ? 0 : i - idx - len + 1;
                tags.add(new Tag(toBack, len - 1, data.charAt(i)));
                idx = -1;
                sub = "";
            }
        }

    }

    public String decompress() {
        StringBuilder decodedString = new StringBuilder();
        for (Tag tag : this.tags) {
            int j = decodedString.length() - tag.i;
            for (int i = 0; i < tag.len; i++) {
                decodedString.append(decodedString.charAt(j++));
            }
            decodedString.append(tag.c);
        }
        return decodedString.toString();
    }

    public void printTags() {
        for (Tag tag : tags) {
            System.out.println(tag.i + " " + tag.len + " " + tag.c);
        }
    }

}
