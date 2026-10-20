# Marked Backend
En Java-backend der driver en webshop med standardprodukter og custom-design. Kunder kan browse kataloget, designe deres egne produkter (fx T-shirts og håndklæder), lægge dem i kurven og gennemføre et køb. Admin kan administrere produkter, varianter, priser, lager og ordrer.

## Formål
Bygge en backend der kan:

- Håndtere sikker brugerregistrering og login med JWT og BCrypt
- Browse standardprodukter og custom-produkter (med valg af farve/materiale)
- Beregne priser for custom-produkter dynamisk
- Håndtere en indkøbskurv for både standardvarer og custom-designs
- Oprette og administrere ordrer (PENDING → PAID → DONE)
- Sikre rollebaseret adgang (CUSTOMER vs. ADMIN) via middleware

## Arkitektur
HTTP Request → Middleware (Auth) → Routes → Controller → Service → DAO → PostgreSQL

## Teknologier
Java 25, Javalin 6.7, Hibernate/JPA 7, PostgreSQL, Jackson, Lombok, JWT (java-jwt), BCrypt (jbcrypt), JUnit 5, Testcontainers, SLF4J/Logback

## Funktionalitet
- Registrering og login (returnerer JWT-token)
- List alle produkter, kategorier og varer på tilbud
- Design custom-produkter (vælg farve og materiale, få prisberegning)
- Føj standardvarer eller custom-designs til kurven
- Se kurvens indhold og samlet pris
- Opret ordrer og se egne ordrestatusser
- Admin: Opret kategorier og produkter (Standard/Custom)
- Admin: Administrer varianter (pris, lager, tilbudsstatus)
- Admin: Se betalte ordrer og opdater ordrestatus
