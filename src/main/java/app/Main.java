package app;


import app.config.HibernateConfig;
import app.dao.DAO;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        DAO dao = new DAO(emf);

        dao.savePoints(1000);

        long total = dao.countPoints();
        double avgX = dao.avgX();
        System.out.println("Total Points: " + total);
        System.out.println("Average X: " + avgX);

        dao.findAll().forEach(System.out::println);

        emf.close();
    }
}



