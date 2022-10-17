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

    private final static String SELECT = "FROM Task";

    private final static String SELECT_TRUE_DONE = String.format("%s WHERE done = true", SELECT);

    private final static String SELECT_FALSE_DONE = String.format("%s WHERE done = false", SELECT);

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
        var result = session.createQuery(query, Task.class).list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public Task complete(int taskId) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE Task SET done = :fDone WHERE id = :fId")
                    .setParameter("fDone", true)
                    .setParameter("fId", taskId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return findById(taskId);
    }

    public Task update(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE Task SET name = :fName, description = :fDescription WHERE id = :fId")
                    .setParameter("fName", task.getName())
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fId", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return findById(task.getId());
    }

    public void delete(int taskId) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "DELETE Task WHERE id = :fId")
                    .setParameter("fId", taskId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }
}