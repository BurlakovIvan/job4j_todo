package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskStore;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskStore taskStore;

    public void add(Task task) {
        taskStore.add(task);
    }

    public List<Task> findAll() {
        return taskStore.findAll();
    }

    public List<Task> findCompleted() {
        return taskStore.findCompleted();
    }

    public List<Task> findNew() {
        return taskStore.findNew();
    }

    public Task findById(int taskId) {
        return taskStore.findById(taskId);
    }

    public boolean complete(int taskId) {
        return taskStore.complete(taskId);
    }

    public boolean update(Task task) {
        return taskStore.update(task);
    }

    public boolean delete(int taskId) {
        return taskStore.delete(taskId);
    }
}
