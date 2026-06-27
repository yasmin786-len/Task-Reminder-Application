package com.example.taskreminder.service;

import com.example.taskreminder.model.Task;
import com.example.taskreminder.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class CompletionService {

    @Autowired
    private TaskRepository taskRepository;

    public void markCompleted(int taskId) {
        Task task = taskRepository.getTaskById(taskId);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + taskId);
        }

        boolean success = taskRepository.markCompleted(taskId);
        if (!success) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to mark task as completed for id: " + taskId);
        }
    }


    public Task getTaskById(int taskId) {
        Task task = taskRepository.getTaskById(taskId);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + taskId);
        }
        return task;
    }
}
