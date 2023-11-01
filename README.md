   ### Basic-Payment-Service

### Project Description 

This project involves the development of a basic payment service using SpringBoot. The application facilitates the 
creation of payments and viewing of payment details.

### Features

    Payment Creation:
        Users can create payments by specifying an amount.
        Each transaction is assigned a unique transaction ID.
        Payment status is initially set to "Pending."

    Payment Processing:
        A service method is implemented to simulate payment processing.
        The payment status is updated from "Pending" to "Completed" after a short delay (5 seconds).

    Payment Retrieval:
        Users can retrieve payment details by providing the payment ID.
        The system returns payment information, including the payment ID, amount, timestamp, and status.

    Error Handling:
        Validation is in place to ensure that payment amounts are positive numbers.
        Graceful error handling provides clear error messages to users.

    Database Integration:
        An H2 in-memory database is utilized to store payment records.
        A Payment entity is created with fields for payment ID, amount, timestamp, and status.

### API Documentation:

    Swagger is employed to generate API documentation for the payment creation and retrieval endpoints.

### Technical Requirements



### Note that :

    Endpoints are secured and require a JWT bearer token for access.
    Users have the ability to view their payment history.
    The application is containerized using Docker.




