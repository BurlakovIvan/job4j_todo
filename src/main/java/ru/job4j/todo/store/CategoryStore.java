package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Map;

@ThreadSafe
@Repository
@AllArgsConstructor
public class CategoryStore {
    private final CrudRepository crudRepository;
    private final static String SELECT = "SELECT c FROM Category c";

    private static final String SELECT_WITH_WHERE_BY_IDS = String.format("%s WHERE id IN (:fId)", SELECT);

    public List<Category> findAll() {
        return crudRepository.query(SELECT, Category.class);
    }

    public List<Category> findByIds(List<Integer> categoriesID) {
        return crudRepository.query(SELECT_WITH_WHERE_BY_IDS, Category.class, Map.of("fId", categoriesID));
    }
}