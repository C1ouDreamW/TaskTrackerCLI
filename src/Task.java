import java.time.LocalDateTime;

class Task {
    private final int id;
    private String description;
    private String status;
    private final LocalDateTime createAt;
    private LocalDateTime updateAt;


    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.status = "todo";
        this.createAt = this.updateAt = LocalDateTime.now();
    }

    public Task(int id, String description, String status, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public String toJSON() {
        return String.format("{\"id\":%d,\"description\":\"%s\",\"status\":\"%s\",\"createAt\":\"%s\",\"updateAt\":\"%s\"}",
                this.id, this.description, this.status, this.createAt.toString(), this.updateAt.toString());
    }

    @Override
    public String toString() {
        return toJSON();
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
