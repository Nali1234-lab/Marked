package app.dao;

import java.util.List;

/**
 * Generisk interface med CRUD-operationer (Create, Read, Update, Delete).
 * T = typen af entity, denne DAO arbejder med (fx Customer, Product, Order)
 * ID = typen af entity'ens primærnøgle (hos os altid Long)
 */
public interface IDAO<T, ID> {
    T create(T t);
    List<T> getAll();
    T getById(ID id);
    T update(T t);
    void delete(ID id);
}