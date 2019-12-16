package my.springboot.integration.domain;

import java.util.StringJoiner;

public class Doc {
    private int id;
    private int rev;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRev() {
        return rev;
    }

    public void setRev(int rev) {
        this.rev = rev;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Doc.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("rev=" + rev)
                .add("content='" + content + "'")
                .toString();
    }
}
