package app.api;

import app.dao.CustomerDAO;              // ← NY import
import app.dto.CustomerDTO;
import app.dto.RegisterRequest;
import app.entities.Customer;
import app.exceptions.ApiException;
import app.services.AuthService;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;   // ← NY import
import static io.javalin.apibuilder.ApiBuilder.post;

public class CustomerAPI {

    private final AuthService authService = new AuthService();
    private final CustomerDAO customerDAO = new CustomerDAO();   // ← NY felt

    public EndpointGroup getRoutes() {
        return () -> {

            post("/api/auth/register", ctx -> {
                RegisterRequest req = ctx.bodyAsClass(RegisterRequest.class);

                if (req.name() == null || req.name().isBlank())
                    throw new ApiException(400, "Field 'name' is required");
                if (req.email() == null || req.email().isBlank())
                    throw new ApiException(400, "Field 'email' is required");
                if (req.password() == null || req.password().length() < 6)
                    throw new ApiException(400, "Field 'password' is required (min 6 tegn)");

                Customer created = authService.register(req.name(), req.email(), req.password());
                ctx.status(201).json(CustomerDTO.from(created));
            });

            // ↓ NY route — indsæt HER, efter post(...) slutter, før lambda'en slutter
            get("/api/customers/{id}", ctx -> {
                long id = ctx.pathParamAsClass("id", Long.class).get();
                Customer customer = customerDAO.getById(id);
                if (customer == null) {
                    throw new ApiException(404, "Customer not found with id: " + id);
                }
                ctx.json(CustomerDTO.from(customer));
            });
            // ↑ NY route slutter her

        };  // ← lambda'en slutter her
    }
}