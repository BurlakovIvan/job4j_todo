package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.TimeZone;
import ru.job4j.todo.store.TimeZoneStore;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@ThreadSafe
public class TimeZoneService {
    private final TimeZoneStore timeZoneStore;

    public List<TimeZone> findAll() {
        return timeZoneStore.findAll();
    }

    public Optional<TimeZone> findById(int timeZoneId) {
        return timeZoneStore.findById(timeZoneId);
    }
}
