# NewsGroupService
Service to fetch news articles based on input parameters.


Project Overview:
This is a Spring Boot project that implements several APIs related to news articles. The project includes features such as security using JWT (JSON Web Token) authentication and a retry mechanism for fetching news articles.

# APIs Implemented:

## POST /api/v1/authenticate:

* Description: This API authenticates a user by validating their username and password.
* Request Body: UserInfo object containing the username and password.
* Response: SuccessJwtResponse object containing the authentication token.
* Security: No authentication is required to access this API.

## GET /api/v1/newsArticles/query:

Description: This API retrieves news articles based on specified search criteria and groups them by a given interval.
Request Parameters:
* searchKey: The keyword used to search for news articles.
* digit (optional): The number of digits used to group the articles by interval.
* interval (optional): The interval (e.g., day, week, month) for grouping the articles.
* Request Headers: The API expects an authorization header with a valid JWT token.
* Response: Response object containing the grouped news articles.
* Security: JWT authentication is required to access this API.
* Security Implementation using JWT:

The project uses JWT for authentication.
The authenticateUser API generates a JWT token using the jwtHelperService and returns it in the response.
The doFilterInternal method is a filter that intercepts requests and validates the JWT token present in the authorization header.
It extracts the token from the header, validates it using the jwtHelperService, and sets the authentication in the SecurityContextHolder if the token is valid.
Retry Mechanism Implementation:

The NewsFetcherService class includes a method named fetchNewsInfo that fetches news information.
This method is annotated with @Retryable, indicating that it should be retried in case of failures.
If the method encounters an exception, the retry mechanism will automatically retry the method invocation.

### Dependencies Used:
The project includes the following dependencies:

* spring-boot-starter-web: Provides the basic dependencies for developing Spring MVC applications.
* spring-boot-starter-test: Includes dependencies for testing Spring Boot applications.
* spring-boot-devtools: Provides development-time features for automatic application restart and live reloading.
* springdoc-openapi-starter-webmvc-ui: Adds OpenAPI (formerly known as Swagger) support for documenting APIs.
* spring-boot-configuration-processor: Generates custom configuration metadata from items annotated with @ConfigurationProperties.
* spring-boot-starter-security: Provides Spring Security dependencies for securing the application.
* spring-security-test: Includes Spring Security dependencies for testing.
* jjwt-api, jjwt-impl, jjwt-jackson: Dependencies for working with JSON Web Tokens (JWT).
* spring-retry: Enables retry functionality for methods annotated with @Retryable.
* spring-aspects: Provides Spring AOP (Aspect-Oriented Programming) support for applying aspects such as retry to methods.
* spring-boot-starter-cache: Includes dependencies for Spring Boot's caching support.