package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.TimeZone;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ThreadSafe
@Repository
@AllArgsConstructor
public class TimeZoneStore {
    private final CrudRepository crudRepository;

    private final static String SELECT = "SELECT t FROM TimeZone t";
    private final static String SELECT_BY_ID = "SELECT t FROM TimeZone t WHERE t.id = :fId";

    public List<TimeZone> findAll() {
        return crudRepository.query(SELECT, TimeZone.class);
    }

    public Optional<TimeZone> findById(int timeZoneId) {
        return crudRepository.optional(SELECT_BY_ID, TimeZone.class,
                Map.of("fId", timeZoneId));
    }
}