package app.daos;

import app.entities.Customer;
import app.entities.Role;
import app.exceptions.ApiException;

public interface ISecurityDAO {
    Customer getVerifiedUser(String email, String password) throws ApiException;
    Customer createUser(String name, String email, String password);
    Customer addUserRole(String email, Role role);
    Role createRole(String roleName);
}