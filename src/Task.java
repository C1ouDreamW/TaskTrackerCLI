import java.time.LocalDateTime;

class Task {
    private final int id;
    private String description;
    private String descriptionF;
    private Status status;
    private final LocalDateTime createAt;
    private LocalDateTime updateAt;


    public Task(int id, String description) {
        this.id = id;
        this.setDescription(description);
        this.status = Status.todo;
        this.createAt = this.updateAt = LocalDateTime.now();
    }

    public Task(int id, String description, Status status, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.setDescription(description);
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public String toJSON() {
        return String.format("{\"id\":%d,\"description\":\"%s\",\"status\":\"%s\",\"createAt\":\"%s\",\"updateAt\":\"%s\"}",
                this.id, this.descriptionF, this.status, this.createAt.toString(), this.updateAt.toString());
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
        // 转移替换换行符和双引号、单引号，避免JSON格式错误
        this.description = description;
        this.descriptionF = description.replace("\n", "\\n")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("'", "\\'")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\f", "\\f");
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
