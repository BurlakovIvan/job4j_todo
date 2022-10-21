package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;

@ThreadSafe
@Repository
@AllArgsConstructor
public class TaskStore {
    private final CrudRepository crudRepository;

    private final static String SELECT = "SELECT t FROM Task t";
    private final static String UPDATE = "UPDATE Task %s WHERE id = :fId";
    private final static String DELETE = "DELETE Task WHERE id = :fId";
    private final static String SELECT_TRUE_DONE = String.format("%s WHERE done = true", SELECT);
    private final static String SELECT_FALSE_DONE = String.format("%s WHERE done = false", SELECT);
    private final static String UPDATE_COMPLETE = String.format(UPDATE, "SET done = :fDone");
    private final static String UPDATE_NAME = String.format(UPDATE, """
                                                       SET name = :fName,
                                                       description = :fDescription
                                                       """);

    public void add(Task task) {
        crudRepository.run(session -> session.save(task));
    }

    public List<Task> findAll() {
        return find(SELECT);
    }

    public List<Task> findCompleted() {
        return find(SELECT_TRUE_DONE);
    }

    public List<Task> findNew() {
        return find(SELECT_FALSE_DONE);
    }

    public Task findById(int taskId) {
        return crudRepository.tx(session -> session.get(Task.class, taskId));
    }

    public List<Task> find(String query) {
        return crudRepository.query(query, Task.class);
    }

    public boolean complete(int taskId) {
        boolean rsl = true;
        try {
            crudRepository.run(UPDATE_COMPLETE,
                    Map.of("fDone", true,
                            "fId", taskId));
        } catch (Exception e) {
            rsl = false;
        }
        return rsl;
    }

    public boolean update(Task task) {
        boolean rsl = true;
        try {
            crudRepository.run(UPDATE_NAME,
                    Map.of("fName", task.getName(),
                            "fDescription", task.getDescription(),
                            "fId", task.getId()));
        } catch (Exception e) {
            rsl = false;
        }
        return rsl;
    }

    public boolean delete(int taskId) {
        boolean rsl = true;
        try {
            crudRepository.run(DELETE, Map.of("fId", taskId));
        } catch (Exception e) {
            rsl = false;
        }
        return rsl;
    }
}