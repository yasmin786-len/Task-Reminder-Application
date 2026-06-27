package com.example.taskreminder.service;

import com.example.taskreminder.model.Task;
import com.example.taskreminder.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private TaskRepository taskRepository;


    public Map<String, Integer> getOverview() {
        try {
            Map<String, Integer> overview = new HashMap<>();
            overview.put("totalTasks", taskRepository.countAll());
            overview.put("completedTasks", taskRepository.countCompleted());
            overview.put("pendingTasks", taskRepository.countPending());
            return overview;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error generating overview", e);
        }
    }

    public List<Task> getAllTasks() {
        try {
            List<Task> tasks = taskRepository.getAllTasks();
            if (tasks == null || tasks.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No tasks available");
            }
            return tasks;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error retrieving tasks", e);
        }
    }
}
