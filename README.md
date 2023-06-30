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

* Description: This API retrieves news articles based on specified search criteria and groups them by a given interval.
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


### CICD Documentation:

The project includes a CI/CD (Continuous Integration/Continuous Deployment) pipeline for automating the build, testing, and deployment process. The pipeline is defined using GitHub Actions, and it consists of the following components:

### deployment.yml:

This file defines the workflow for the CI/CD pipeline.
The pipeline is triggered on every push to any branch and on pull requests targeting the "main" branch.
The pipeline runs on an Ubuntu-based environment (ubuntu-latest).
It includes several steps for building and deploying the project.

Steps:
* Checkout: This step checks out the source code from the repository.
* Set up JDK 17: This step sets up Java Development Kit (JDK) version 17 using the actions/setup-java GitHub Action.
* Build with Maven: This step builds the project using Maven by running the command "mvn clean install".
* Log in to Docker Hub: This step logs in to Docker Hub using the docker/login-action GitHub Action. It requires Docker Hub credentials stored as secrets in the repository settings.
* Docker build and push: This step builds the Docker image using the Dockerfile and pushes it to Docker Hub.

### Dockerfile:
The file is responsible for creating the Docker image.
It starts with the base image openjdk:17 and exposes port 8080.
It uses build arguments (ARG) to pass environment variables to the image during the build process.
The environment variables are set in the Dockerfile using the ARG and ENV directives.
The Dockerfile copies the built JAR file to the image and sets the entry point for running the application.
The environment variables specified in the Dockerfile are passed as build arguments during the Docker build step.
The Docker image is pushed to the Docker Hub repository specified as "mridul04795/news-group-service".
Secrets:

The CI/CD pipeline uses secrets to store sensitive information like Docker Hub credentials and environment variables.
The secrets are referenced in the deployment.yml file using the "${{ secrets.SECRET_NAME }}" syntax.
By configuring this CI/CD pipeline, the project can be automatically built, tested, and deployed whenever changes are pushed to the repository. This helps streamline the development process and ensures consistent and reliable deployments of the application.
