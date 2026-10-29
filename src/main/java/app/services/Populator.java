package app.services;

import app.config.HibernateConfig;
import app.entities.Category;
import app.entities.CustomProduct;
import app.entities.DesignSlot;
import app.entities.StandardProduct;
import app.entities.Variant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class Populator {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();


        // 1. STANDARD PRODUKTER

        Category damkoni = new Category();
        damkoni.setName("Grydelapper");
        em.persist(damkoni);

        StandardProduct damkoni1 = new StandardProduct();
        damkoni1.setName("Grydelap med blomst");
        damkoni1.setPrice(75.0);
        damkoni1.setCategory(damkoni);
        em.persist(damkoni1);

        StandardProduct damkoni2 = new StandardProduct();
        damkoni2.setName("Grydelap firkantet");
        damkoni2.setPrice(70.0);
        damkoni2.setCategory(damkoni);
        em.persist(damkoni2);

        Category dastgireh = new Category();
        dastgireh.setName("Forklæder");
        em.persist(dastgireh);

        StandardProduct dastgireh1 = new StandardProduct();
        dastgireh1.setName("Forklæde med hjerte");
        dastgireh1.setPrice(220.0);
        dastgireh1.setCategory(dastgireh);
        em.persist(dastgireh1);

        StandardProduct dastgireh2 = new StandardProduct();
        dastgireh2.setName("Forklæde klassisk");
        dastgireh2.setPrice(200.0);
        dastgireh2.setCategory(dastgireh);
        em.persist(dastgireh2);

        Category pishband = new Category();
        pishband.setName("Køkkenhåndklæder");
        em.persist(pishband);

        StandardProduct pishband1 = new StandardProduct();
        pishband1.setName("Køkkenhåndklæde med striber");
        pishband1.setPrice(45.0);
        pishband1.setCategory(pishband);
        em.persist(pishband1);

        StandardProduct pishband2 = new StandardProduct();
        pishband2.setName("Køkkenhåndklæde ensfarvet");
        pishband2.setPrice(40.0);
        pishband2.setCategory(pishband);
        em.persist(pishband2);


        // 2. CUSTOM PRODUKTER

        Category toej = new Category();
        toej.setName("Tøj");
        em.persist(toej);

        Category tekstiler = new Category();
        tekstiler.setName("Tekstiler");
        em.persist(tekstiler);

        // --- Custom produkt 1: T-shirt ---
        CustomProduct tshirt = new CustomProduct();
        tshirt.setName("T-shirt (Design selv)");
        tshirt.setCategory(toej);
        tshirt.setLaborCost(50.0);
        tshirt.setHiddenMargin(20.0);
        em.persist(tshirt);

        Variant tsRed = new Variant();
        tsRed.setName("Rød");
        tsRed.setPrice(15.0);
        tsRed.setStock(100);
        tsRed.setDesignSlot(DesignSlot.COLOR);
        tsRed.setProduct(tshirt);
        em.persist(tsRed);

        Variant tsBlue = new Variant();
        tsBlue.setName("Blå");
        tsBlue.setPrice(15.0);
        tsBlue.setStock(100);
        tsBlue.setDesignSlot(DesignSlot.COLOR);
        tsBlue.setProduct(tshirt);
        em.persist(tsBlue);

        Variant tsBlack = new Variant();
        tsBlack.setName("Sort");
        tsBlack.setPrice(15.0);
        tsBlack.setStock(100);
        tsBlack.setDesignSlot(DesignSlot.COLOR);
        tsBlack.setProduct(tshirt);
        em.persist(tsBlack);

        Variant tsCotton = new Variant();
        tsCotton.setName("Bomuld");
        tsCotton.setPrice(30.0);
        tsCotton.setStock(500);
        tsCotton.setDesignSlot(DesignSlot.MATERIAL);
        tsCotton.setProduct(tshirt);
        em.persist(tsCotton);

        Variant tsLinen = new Variant();
        tsLinen.setName("Hør");
        tsLinen.setPrice(45.0);
        tsLinen.setStock(300);
        tsLinen.setDesignSlot(DesignSlot.MATERIAL);
        tsLinen.setProduct(tshirt);
        em.persist(tsLinen);

        tshirt.setDesignOptions(List.of(tsRed, tsBlue, tsBlack, tsCotton, tsLinen));
        em.persist(tshirt);

        // --- Custom produkt 2: Håndklæde ---
        CustomProduct towel = new CustomProduct();
        towel.setName("Håndklæde (Design selv)");
        towel.setCategory(tekstiler);
        towel.setLaborCost(40.0);
        towel.setHiddenMargin(15.0);
        em.persist(towel);

        Variant twWhite = new Variant();
        twWhite.setName("Hvid");
        twWhite.setPrice(10.0);
        twWhite.setStock(200);
        twWhite.setDesignSlot(DesignSlot.COLOR);
        twWhite.setProduct(towel);
        em.persist(twWhite);

        Variant twGreen = new Variant();
        twGreen.setName("Grøn");
        twGreen.setPrice(10.0);
        twGreen.setStock(200);
        twGreen.setDesignSlot(DesignSlot.COLOR);
        twGreen.setProduct(towel);
        em.persist(twGreen);

        Variant twNavy = new Variant();
        twNavy.setName("Marineblå");
        twNavy.setPrice(10.0);
        twNavy.setStock(200);
        twNavy.setDesignSlot(DesignSlot.COLOR);
        twNavy.setProduct(towel);
        em.persist(twNavy);

        Variant twFrotte = new Variant();
        twFrotte.setName("Frotté");
        twFrotte.setPrice(40.0);
        twFrotte.setStock(400);
        twFrotte.setDesignSlot(DesignSlot.MATERIAL);
        twFrotte.setProduct(towel);
        em.persist(twFrotte);

        Variant twWaffle = new Variant();
        twWaffle.setName("Waffel");
        twWaffle.setPrice(35.0);
        twWaffle.setStock(400);
        twWaffle.setDesignSlot(DesignSlot.MATERIAL);
        twWaffle.setProduct(towel);
        em.persist(twWaffle);

        towel.setDesignOptions(List.of(twWhite, twGreen, twNavy, twFrotte, twWaffle));
        em.persist(towel);

        em.getTransaction().commit();
        em.close();

        System.out.println("Populator færdig: 6 standardprodukter og 2 custom-produkter oprettet.");
    }
}