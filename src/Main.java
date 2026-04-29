import java.io.IOException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    static void main(String[] args) throws IOException {
        if(args.length==0||args[0].equals("help")){
            System.out.println("""
                    可用命令：
                    add <description> - 添加一个新任务
                    list [status] - 列出所有任务，或列出指定状态的任务（todo、in-progress、done）
                    delete <id> - 删除指定ID的任务
                    update <id> <new description> - 更新指定ID的任务描述
                    make-todo <id> - 将指定ID的任务状态更新为todo
                    make-in-progress <id> - 将指定ID的任务状态更新为in-progress
                    make-done <id> - 将指定ID的任务状态更新为done
                    help - 显示此帮助信息""");
            return;
        }
        Tasks taskList = new Tasks();
        String cmd = args[0];
        switch (cmd) {
            case "add" -> {
                int id = taskList.getNewId();
                String msg = args[1];
                Task task01 = new Task(id, msg);
                taskList.add(task01);
                System.out.printf("任务添加成功(ID:%d)%n", id);
                taskList.saveList();
            }
            case "list" -> {
                if (args.length == 1) {
                    taskList.showAllTasks();
                } else {
                    try {
                        Status choice = Status.valueOf(args[1].replace("-", "_"));
                        taskList.showAllTasks(String.valueOf(choice));
                    } catch (IllegalArgumentException e) {
                        System.out.println("无效的状态，请输入todo、in-progress或done！");
                    }
                }
            }
            case "delete","remove" -> {
                if (args.length < 2) {
                    System.out.println("无效的参数，请在命令后输入序号，以空格分隔！");
                } else {
                    try {
                        int id = Integer.parseInt(args[1]);
                        taskList.delete(id);
                        System.out.printf("任务%d删除成功%n", id);
                        taskList.saveList();
                    } catch (IndexOutOfBoundsException e) {
                        System.out.printf("任务不存在%n");
                    } catch (Exception e) {
                        System.out.println("发生错误，请检查输入参数！");
                    }
                }
            }
            case "update" -> {
                if (args.length < 2) {
                    System.out.println("无效的参数，请在命令后输入序号和新的描述，以空格分隔！");
                } else {
                    int id = Integer.parseInt(args[1]);
                    try {
                        taskList.getForId(id).setDescription(args[2]);
                        taskList.getForId(id).setUpdateAt(LocalDateTime.now());
                        System.out.printf("任务%d更新成功%n", id);
                        taskList.saveList();
                    } catch (IndexOutOfBoundsException e) {
                        System.out.printf("任务%d不存在%n", id);
                    }
                }
            }
            case "make-todo", "make-in-progress", "make-done" -> {
                if (args.length < 2) {
                    System.out.println("无效的参数，请在命令后输入序号和新的状态，以空格分隔！");
                }
                int id = Integer.parseInt(args[1]);
                try {
                    // 正则表达式去除make-in-progress中前缀make-
                    Pattern pattern = Pattern.compile("make-(.*)");
                    Matcher matcher = pattern.matcher(cmd);
                    if (matcher.find()) {
                        cmd = matcher.group(1);
                    }
                    Status status = Status.valueOf(cmd.replace("-", "_"));
                    switch (status) {
                        case todo:
                            taskList.getForId(id).setStatus("todo");
                            break;
                        case in_progress:
                            taskList.getForId(id).setStatus("in_progress");
                            break;
                        case done:
                            taskList.getForId(id).setStatus("done");
                            break;
                    }
                    taskList.getForId(id).setUpdateAt(LocalDateTime.now());
                    System.out.printf("任务%d状态更新成功%n", id);
                    taskList.saveList();
                } catch (IndexOutOfBoundsException e) {
                    System.out.printf("任务%d不存在%n", id);
                }
            }
            default -> System.out.println("无效的命令");
        }
    }
}

enum Status{
    todo,
    in_progress,
    done,
}

