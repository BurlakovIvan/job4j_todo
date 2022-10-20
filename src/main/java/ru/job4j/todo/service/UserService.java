package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.UserStore;

import java.util.Optional;

@Data
@AllArgsConstructor
@Service
@ThreadSafe
public class UserService {
    private final UserStore store;

    public Optional<User> findUserByLoginAndPwd(String login, String password) {
        return store.findUserByLoginAndPwd(login, password);
    }

    public boolean add(User user) {
        return store.add(user);
    }
}
