package com.example.taskreminder.controller;

import com.example.taskreminder.model.Task;
import com.example.taskreminder.service.ReportService;
import com.example.taskreminder.util.CSVGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @GetMapping("/overview")
    public ResponseEntity<?> overview() {
        try {
            Map<String, Integer> overview = reportService.getOverview();
            if (overview == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tasks found");
            }
            return ResponseEntity.ok(new ApiResponse(true, "Overview retrieved", overview));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error generating overview", e);
        }
    }


    @GetMapping("/export")
    public void exportCSV(HttpServletResponse response) {
        try {
            List<Task> tasks = reportService.getAllTasks();
            if (tasks == null || tasks.isEmpty()) {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                response.getWriter().write("No tasks available to export");
                return;
            }

            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=tasks_report.csv");

            try (PrintWriter writer = response.getWriter()) {
                CSVGenerator.writeTasksToCSV(writer, tasks);
                writer.flush();
            }

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error exporting CSV", e);
        }
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
