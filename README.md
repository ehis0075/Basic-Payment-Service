                                        # Basic-Payment-Service

### Project Description 

This project involves the development of a basic payment service using SpringBoot. The application facilitates the 
creation of payments and viewing of payment details.

### Features

    Payment Creation:
        Users can create payments by specifying an amount.
        Each transaction is assigned a unique transaction ID.
        Payment status is initially set to "Pending."

    Payment Retrieval:
        Users can retrieve payment details by providing the payment ID.
        The system returns payment information, including the payment ID, amount, timestamp, and status.

    Payment Processing:
        A service method is implemented to simulate payment processing.
        The payment status is updated from "Pending" to "Completed" after a short delay (e.g., 5 seconds).

    Error Handling:
        Validation is in place to ensure that payment amounts are positive numbers.
        Graceful error handling provides clear error messages to users.
    Database Integration:

    An H2 in-memory database is utilized to store payment records.
    A Payment entity is created with fields for payment ID, amount, timestamp, and status.

### API Documentation:

    Swagger is employed to generate API documentation for the payment creation and retrieval endpoints.

### Technical Requirements

    Spring Boot is used for the project setup.
    RESTful APIs are implemented for payment creation and retrieval.
    Spring Data JPA is employed for database interaction.
    Unit tests and integration tests are included for services and controllers.
    Proper exception handling and logging are implemented.
    Clear and meaningful error responses in JSON format are provided for API errors.
    Maven is used for dependency management.

### Note that :

    Endpoints are secured and require a JWT bearer token for access.
    Users have the ability to view their payment history.
    The application is containerized using Docker.




