package com.example.taskreminder.util;

import com.example.taskreminder.model.Task;

import java.io.PrintWriter;
import java.util.List;

public class CSVGenerator {

    public static void writeTasksToCSV(PrintWriter writer, List<Task> tasks) {
        if (writer == null || tasks == null || tasks.isEmpty()) return;


        writer.println("ID,Title,Description,DueDate,Completed,Email");


        for (Task task : tasks) {
            String description = task.getDescription() != null ? task.getDescription() : "";
            writer.println(
                    task.getId() + "," +
                            escapeCSV(task.getTitle()) + "," +
                            escapeCSV(description) + "," +
                            task.getDueDate() + "," +
                            task.isCompleted() + "," +
                            escapeCSV(task.getEmail())
            );
        }
    }

    private static String escapeCSV(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"")) {
            field = field.replace("\"", "\"\"");
            return "\"" + field + "\"";
        }
        return field;
    }
}
