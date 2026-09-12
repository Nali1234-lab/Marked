package app.services;

import app.config.HibernateConfig;
import app.entities.Category;
import app.entities.StandardProduct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Populator {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin(); // <- denne linje manglede, uden den kan man ikke gemme noget

        // Kategori 1: دمکنی‌ها (kedelovertræk)
        Category damkoni = new Category();
        damkoni.setName("دمکنی‌ها");
        em.persist(damkoni);

        StandardProduct damkoni1 = new StandardProduct(); // <- Product er nu abstrakt, brug StandardProduct
        damkoni1.setName("دمکنی مدل ساده چین لیفه‌ای");
        damkoni1.setPrice(180.0);
        damkoni1.setCategory(damkoni);

        StandardProduct damkoni2 = new StandardProduct();
        damkoni2.setName("دمکنی مدل کفشدوزک");
        damkoni2.setPrice(190.0);
        damkoni2.setCategory(damkoni);

        // Kategori 2: دستگیره‌ها (grydelapper)
        Category dastgireh = new Category();
        dastgireh.setName("دستگیره‌ها");
        em.persist(dastgireh);

        StandardProduct dastgireh1 = new StandardProduct();
        dastgireh1.setName("دستگیره مدل قلبی و جیب هایش");
        dastgireh1.setPrice(75.0);
        dastgireh1.setCategory(dastgireh);

        StandardProduct dastgireh2 = new StandardProduct();
        dastgireh2.setName("دستگیره مدل مربع");
        dastgireh2.setPrice(70.0);
        dastgireh2.setCategory(dastgireh);

        // Kategori 3: پیشبندها (forklæder)
        Category pishband = new Category();
        pishband.setName("پیشبندها");
        em.persist(pishband);

        StandardProduct pishband1 = new StandardProduct();
        pishband1.setName("بالاتنه پیشبند قلبی");
        pishband1.setPrice(220.0);
        pishband1.setCategory(pishband);

        StandardProduct pishband2 = new StandardProduct();
        pishband2.setName("پیشبند گارسونی");
        pishband2.setPrice(200.0);
        pishband2.setCategory(pishband);

        em.persist(damkoni1);
        em.persist(damkoni2);
        em.persist(dastgireh1);
        em.persist(dastgireh2);
        em.persist(pishband1);
        em.persist(pishband2);

        em.getTransaction().commit();
        em.close();

        System.out.println("Oprettede 3 kategorier med hver 2 produkter.");
    }
}