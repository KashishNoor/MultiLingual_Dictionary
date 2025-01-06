package dto;

public class Dictionary {
    private String name;
    private java.util.Date createdAt;

    public Dictionary(String name, java.util.Date createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }
    public java.util.Date getCreatedAt() {
        return createdAt;
    }
}
