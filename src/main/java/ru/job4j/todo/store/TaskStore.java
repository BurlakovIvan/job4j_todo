package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;

@Repository
@AllArgsConstructor
public class TaskStore {
    private final SessionFactory sf;

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
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(task);
        session.getTransaction().commit();
        session.close();
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
        Session session = sf.openSession();
        session.beginTransaction();
        var result = session.get(Task.class, taskId);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public List<Task> find(String query) {
        Session session = sf.openSession();
        session.beginTransaction();
        var result = session.createQuery(query, Task.class).getResultList();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public boolean complete(int taskId) {
        var rsl = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(UPDATE_COMPLETE)
                    .setParameter("fDone", true)
                    .setParameter("fId", taskId)
                    .executeUpdate();
            session.getTransaction().commit();
            rsl = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return rsl;
    }

    public boolean update(Task task) {
        var rsl = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(UPDATE_NAME)
                    .setParameter("fName", task.getName())
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fId", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            rsl = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return rsl;
    }

    public boolean delete(int taskId) {
        var rsl = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(DELETE)
                    .setParameter("fId", taskId)
                    .executeUpdate();
            session.getTransaction().commit();
            rsl = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return rsl;
    }
}