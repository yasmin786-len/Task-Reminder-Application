package com.example.taskreminder.controller;

import com.example.taskreminder.model.Task;
import com.example.taskreminder.service.EmailService;
import com.example.taskreminder.service.SchedulerService;
import com.example.taskreminder.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/set/{taskId}")
    public ResponseEntity<String> setReminder(@PathVariable int taskId) {

        Task task = taskService.getTaskById(taskId);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Task not found");
        }

        // SEND IMMEDIATELY ON SCHEDULE CLICK
        emailService.sendReminder(
                task.getEmail(),
                "Task Scheduled: " + task.getTitle()
        );


        boolean scheduled = schedulerService.scheduleTask(task, () -> {
            emailService.sendReminder(
                    task.getEmail(),
                    "Reminder: Complete task \"" + task.getTitle() +
                            "\" by " + task.getDueDate()
            );
        });

        if (!scheduled) {
            return ResponseEntity.badRequest()
                    .body("Failed to schedule reminder");
        }

        return ResponseEntity.ok("Task scheduled and email sent");
    }



    @GetMapping("/status/{taskId}")
    public ResponseEntity<?> reminderStatus(@PathVariable int taskId) {
        boolean scheduled = schedulerService.isTaskScheduled(taskId);
        return ResponseEntity.ok(new ApiResponse(true,
                scheduled ? "Reminder is scheduled" : "No reminder scheduled",
                taskId));
    }

    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object taskId;

        public ApiResponse(boolean success, String message, Object taskId) {
            this.success = success;
            this.message = message;
            this.taskId = taskId;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getTaskId() { return taskId; }
    }
}
