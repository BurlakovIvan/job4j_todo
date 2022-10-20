package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@ThreadSafe
@Repository
@AllArgsConstructor
public class UserStore {

    private final SessionFactory sf;

    private final static String SELECT = """
                                         Select b FROM User AS b
                                         WHERE b.login = :fLogin AND b.password = :fPassword
                                         """;

    public boolean add(User user) {
        Session session = sf.openSession();
        var rsl = false;
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            session.close();
            rsl = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return rsl;
    }

    public Optional<User> findUserByLoginAndPwd(String login, String password) {
        Session session = sf.openSession();
        session.beginTransaction();
        Optional<User> rsl = Optional.empty();
        var userList = session.createQuery(SELECT, User.class)
                .setParameter("fLogin", login)
                .setParameter("fPassword", password)
                .list();
        if (!userList.isEmpty()) {
            rsl = Optional.of(userList.get(0));
        }
        session.getTransaction().commit();
        session.close();
        return rsl;
    }

}
