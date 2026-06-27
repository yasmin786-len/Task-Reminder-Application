package com.example.taskreminder.controller;

import com.example.taskreminder.model.Task;
import com.example.taskreminder.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/completion")
public class CompletionController {

    @Autowired
    private TaskService taskService;

    @PutMapping("/mark/{taskId}")
    public ResponseEntity<?> markCompleted(@PathVariable int taskId) {
        Task task = taskService.getTaskById(taskId);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        taskService.markCompleted(taskId);
        return ResponseEntity.ok(new ApiResponse(true, "Task marked as completed", taskId));
    }

    @GetMapping("/status/{taskId}")
    public ResponseEntity<?> getStatus(@PathVariable int taskId) {
        Task task = taskService.getTaskById(taskId);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        return ResponseEntity.ok(new ApiResponse(true,
                "Task status retrieved",
                task.isCompleted() ? "COMPLETED" : "PENDING"));
    }

    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }
}
