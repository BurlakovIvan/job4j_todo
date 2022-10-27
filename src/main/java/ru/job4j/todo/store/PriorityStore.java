package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;

@ThreadSafe
@Repository
@AllArgsConstructor
public class PriorityStore {
    private final CrudRepository crudRepository;

    private final static String SELECT = "SELECT p FROM Priority p";
    private final static String SELECT_BY_ID = "SELECT p FROM Priority p WHERE p.id = :fId";

    public List<Priority> findAll() {
        return crudRepository.query(SELECT, Priority.class);
    }

    public Priority findById(int priorityId) {
        return crudRepository.optional(SELECT_BY_ID, Priority.class,
                Map.of("fId", priorityId)).orElse(null);
    }
}