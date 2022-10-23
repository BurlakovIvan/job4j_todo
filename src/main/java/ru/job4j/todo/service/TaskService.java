package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.TaskStore;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskStore taskStore;

    public void add(Task task) {
        taskStore.add(task);
    }

    public List<Task> findAll(User user) {
        return taskStore.findAll(user);
    }

    public List<Task> findCompleted(User user) {
        return taskStore.findCompleted(user);
    }

    public List<Task> findNew(User user) {
        return taskStore.findNew(user);
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
