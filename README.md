# Spring Boot Category and Country Management API

This project is a Spring Boot application that provides RESTful APIs for managing categories and countries. It includes caching mechanisms to improve performance and reduce database load.

## Features

- CRUD operations for Categories
- CRUD operations for Countries
- Caching using Redis
- Data transfer objects (DTO) for response shaping
- Exception handling for invalid requests

## Technologies Used

- Spring Boot
- Spring Data JPA
- Spring Web
- Spring Cache (with Redis)
- Redis
- MapStruct (for DTO mapping)
- Lombok (for reducing boilerplate code)
- Jackson (for JSON processing)

## Setup Instructions

### Prerequisites

- Java 21 or higher
- Maven
- Redis server

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/turgaycabalak/redis-cache.git
   cd redis-cache
   
2. **Install dependencies**
   ```bash
   mvn clean install
   
3. **Configure Redis**
Ensure that Redis is running on your local machine or configure the application to connect to your Redis instance by updating <b>application.properties<b>:
   ```bash
   spring.redis.host=localhost
   spring.redis.port=6379
   
4. **Run the application**
   ```bash
   mvn spring-boot:run
  
The application will start and be accessible at <b>`http://localhost:4070`.<b>

## API Endpoints

### Category Endpoints

-   **GET /api/v1/category**: Get all categories
-   **GET /api/v1/category/{id}**: Get a category by ID
-   **POST /api/v1/category**: Create a new category
-   **PUT /api/v1/category**: Update an existing category
-   **DELETE /api/v1/category/{id}**: Delete a category by ID

### Country Endpoints

-   **GET /api/v1/country**: Get all countries
-   **GET /api/v1/country/{id}**: Get a country by ID
-   **POST /api/v1/country**: Create a new country
-   **PUT /api/v1/country**: Update an existing country
-   **DELETE /api/v1/country/{id}**: Delete a country by ID

## Cache Management

-   The `CategoryService` and `CountryService` classes use caching to reduce database access and improve performance.
-   The `CacheService` component handles the caching logic, using Redis as the cache store.
-   Cached entries are set to expire after 30 minutes.

## DTO Mapping

-   DTOs are used to shape the data returned by the API.
-   MapStruct is used for mapping between entities and DTOs.

## Exception Handling

-   Custom exceptions are thrown for invalid requests, such as when a category or country is not found by ID.
-   These exceptions are handled globally to return meaningful error messages.

## Contributing

1.  Fork the repository
2.  Create a new branch (`git checkout -b feature-branch`)
3.  Make your changes
4.  Commit your changes (`git commit -am 'Add new feature'`)
5.  Push to the branch (`git push origin feature-branch`)
6.  Create a new Pull Request

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgements

-   Thanks to the Spring Boot and Redis communities for their valuable resources and support.
