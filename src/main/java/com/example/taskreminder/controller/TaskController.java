package com.example.taskreminder.controller;

import com.example.taskreminder.model.Task;
import com.example.taskreminder.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/add")
    public ResponseEntity<?> addTask(@Valid @RequestBody Task task) {
        try {
            taskService.addTask(task);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Task added successfully", task.getId()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding task", e);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Task>> listTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Task>> completedTasks() {
        return ResponseEntity.ok(taskService.getCompletedTasks());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Task>> pendingTasks() {
        return ResponseEntity.ok(taskService.getPendingTasks());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }


    public static class ApiResponse {
        private boolean success;
        private String message;
        private Integer taskId;

        public ApiResponse(boolean success, String message, Integer taskId) {
            this.success = success;
            this.message = message;
            this.taskId = taskId;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Integer getTaskId() { return taskId; }

    }
}
