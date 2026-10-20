package app.entities;

public enum OrderStatus {
    PENDING // Kunden har bestilt, afventer manuel betalingsbekræftelse
    , PAID  // admin efter betaling skifte til paid
    , DONE  // ordern er sendt manuelt
}
