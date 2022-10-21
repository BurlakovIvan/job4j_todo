package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@ThreadSafe
@Repository
@AllArgsConstructor
public class UserStore {

    private final CrudRepository crudRepository;

    private final static String SELECT = """
                                         Select b FROM User AS b
                                         WHERE b.login = :fLogin AND b.password = :fPassword
                                         """;

    public boolean add(User user) {
        boolean rsl = true;
        try {
            crudRepository.run(session -> session.save(user));
        } catch (Exception e) {
            rsl = false;
        }
        return rsl;
    }

    public Optional<User> findUserByLoginAndPwd(String login, String password) {
        return crudRepository.optional(
                SELECT, User.class,
                Map.of("fLogin", login,
                        "fPassword", password)
        );
    }

}
