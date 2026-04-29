import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Tasks {
    ArrayList<Task> list;
    Path path = Paths.get("tasks.json");

    public Tasks() throws IOException {
        this.list = new ArrayList<>();
        this.init();
    }

    public int getNewId() {
        int idx = 0;
        for (Task i : list) {
            idx++;
            if (i.getId() != idx) {
                return idx;
            }
        }
        return idx + 1;
    }

    public void add(Task x) {
        if (x != null) {
            this.list.add(x);
        }
    }

    public void init() throws IOException {
        if (Files.exists(this.path)) {
            if (Files.size(this.path) == 0) {
                return;
            }
            String content = Files.readString(this.path);
            // 正则表达式匹配花括号及其内容
            String regex = "\\{[^}]*}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                String jsonObject = matcher.group();
                // 解析 JSON 对象
                Task task = getTask(jsonObject);
                this.list.add(task);
            }
        }
    }

    private static Task getTask(String jsonObject) {
        int id = Integer.parseInt(jsonObject.replaceAll(".*\"id\":(\\d+).*", "$1"));
        String description = jsonObject.replaceAll(".*\"description\":\"([^\"]*)\".*", "$1");
        Status status = Status.valueOf(jsonObject.replaceAll(".*\"status\":\"([^\"]*)\".*", "$1"));
        String createAt = jsonObject.replaceAll(".*\"createAt\":\"([^\"]*)\".*", "$1");
        String updateAt = jsonObject.replaceAll(".*\"updateAt\":\"([^\"]*)\".*", "$1");
        return new Task(id, description, status, LocalDateTime.parse(createAt), LocalDateTime.parse(updateAt));
    }

    public void showAllTasks() {
        System.out.println("任务列表：");
        for (Task i : list) {
            System.out.printf("%d  %s  %s%n", i.getId(), i.getDescription(), i.getStatus());
        }
    }

    public void showAllTasks(String status) {
        System.out.printf("任务列表(%s)：%n", status);
        for (Task i : list) {
            if (i.getStatus().toString().equals(status)) {
                System.out.printf("%d  %s  %s%n", i.getId(), i.getDescription(), i.getStatus());
            }
        }
    }

    public void delete(int id) {
        for (Task i : list) {
            if (i.getId() == id) {
                list.remove(i);
                return;
            }
        }
        throw new IndexOutOfBoundsException("未找到对应ID的任务");
    }

    public void saveList() throws IOException {
        // 将list按Task.id排序
        list.sort(Comparator.comparingInt(Task::getId));
        this.path = Paths.get("tasks.json");
        String output = String.join(",\n", this.list.stream().map(Task::toJSON).toArray(String[]::new));
        Files.writeString(path, "[\n" + output + "\n]");
    }


    public Task getForId(int id) {
        for (Task i : list) {
            if (i.getId() == id) {
                return i;
            }
        }
        throw new IndexOutOfBoundsException("未找到对应ID的任务");
    }
}
