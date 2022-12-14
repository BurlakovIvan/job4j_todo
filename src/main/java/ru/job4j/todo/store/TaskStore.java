package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Map;

@ThreadSafe
@Repository
@AllArgsConstructor
public class TaskStore {
    private final CrudRepository crudRepository;

    private final static String SELECT = """
                                         SELECT DISTINCT t FROM Task t
                                         LEFT JOIN FETCH t.priority
                                         LEFT JOIN FETCH t.categories
                                         WHERE t.user = :fUser
                                         """;
    private final static String UPDATE = "UPDATE Task %s WHERE id = :fId";
    private final static String DELETE = "DELETE Task WHERE id = :fId";
    private final static String SELECT_TRUE_DONE = String.format("%s AND t.done = true", SELECT);
    private final static String SELECT_FALSE_DONE = String.format("%s AND t.done = false", SELECT);
    private final static String SELECT_BY_ID = """
                                               SELECT DISTINCT t FROM Task t
                                               LEFT JOIN FETCH t.priority
                                               LEFT JOIN FETCH t.categories
                                               WHERE t.id = :fId
                                               """;
    private final static String UPDATE_COMPLETE = String.format(UPDATE, "SET done = :fDone");

    public boolean add(Task task) {
        boolean rsl = true;
        try {
            crudRepository.run(session -> session.save(task));
        } catch (Exception e) {
            rsl = false;
        }
        return rsl;
    }

    public List<Task> findAll(User user) {
        return find(SELECT, user);
    }

    public List<Task> findCompleted(User user) {
        return find(SELECT_TRUE_DONE, user);
    }

    public List<Task> findNew(User user) {
        return find(SELECT_FALSE_DONE, user);
    }

    public Task findById(int taskId) {
        return crudRepository.optional(SELECT_BY_ID, Task.class, Map.of("fId", taskId)).orElse(null);
    }

    public List<Task> find(String query, User user) {
        return crudRepository.query(query, Task.class, Map.of("fUser", user));
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
            crudRepository.run(session -> session.merge(task));
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