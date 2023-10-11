package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }



    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            String sql = "CREATE TABLE IF NOT EXISTS users " +
                    "(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(50) NOT NULL, lastName VARCHAR(50) NOT NULL, " +
                    "age TINYINT NOT NULL)";

            session.createSQLQuery(sql).addEntity(User.class).executeUpdate();

            session.getTransaction().commit();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            String sql = "DROP TABLE IF EXISTS users";

            session.createSQLQuery(sql).addEntity(User.class).executeUpdate();

            session.getTransaction().commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            User us = new User(name,lastName, age);
            transaction = session.beginTransaction();
            session.save(us);
            session.getTransaction().commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            User userDel = session.get(User.class,id);
            session.delete(userDel);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUs;
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            allUs = session.createQuery("from User", User.class).getResultList();
            for (User u: allUs) {
                System.out.println(u.toString());
            }
            session.getTransaction().commit();
        }
        return allUs;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
