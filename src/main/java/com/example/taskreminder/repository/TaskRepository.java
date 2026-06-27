package com.example.taskreminder.repository;

import com.example.taskreminder.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addTask(Task task) {
        String sql = "INSERT INTO tasks(title, description, due_date, completed, email) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                false,
                task.getEmail());
    }

    public List<Task> getAllTasks() {
        String sql = "SELECT * FROM tasks";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Task.class));
    }

    public boolean markCompleted(int id) {
        int updated = jdbcTemplate.update("UPDATE tasks SET completed=true WHERE id=?", id);
        return updated > 0;
    }

    public Task getTaskById(int id) {
        try {
            String sql = "SELECT * FROM tasks WHERE id=?";
            return jdbcTemplate.queryForObject(sql,
                    new BeanPropertyRowMapper<>(Task.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null; // Task not found
        }
    }

    public int countAll() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tasks", Integer.class);
    }

    public int countCompleted() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tasks WHERE completed=true", Integer.class);
    }

    public int countPending() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tasks WHERE completed=false", Integer.class);
    }

    public List<Task> getCompletedTasks() {
        return jdbcTemplate.query(
                "SELECT * FROM tasks WHERE completed=true",
                new BeanPropertyRowMapper<>(Task.class));
    }

    public List<Task> getPendingTasks() {
        return jdbcTemplate.query(
                "SELECT * FROM tasks WHERE completed=false",
                new BeanPropertyRowMapper<>(Task.class));
    }


    public boolean deleteTask(int id) {
        int rows = jdbcTemplate.update(
                "DELETE FROM tasks WHERE id = ?",
                id
        );
        return rows > 0;
    }

}
