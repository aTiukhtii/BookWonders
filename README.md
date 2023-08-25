# 📔 BookWonders

### 📄 Project description:

BookWonders is an innovative web stateless application designed to provide an exceptional book shopping experience to users depending on their role. The platform offers a diverse collection of books from various categories, allowing users to explore and purchase books. The project combines Java-based backend technologies, including Spring Framework, JWT and Docker, to ensure efficient data management and secure user interactions.

### 💻 Current project functionality:
Application users can have USER or ADMIN roles. USER role assigns automatically to every newly registered user.
Below you can observe a list of all endpoints in the app. Each endpoint is presented in the format: http request method, endpoint, role required for access, description.
- POST: /register - (all) - register a new user.
- POST: /login - (for registered users) - login user.
- GET: /books - (user) - get all books.
- GET: /books/{id} - (user) - get book by id.
- POST: /books - (admin) - create a new book.
- PUT: /books - (admin) - update a book.
- GET: /books - (user) - search books with parameters.
- DELETE: /books/{id} - (admin) - delete a book by id.
- GET: /categories - (user) - get all categories.
- POST: /categories - (admin) - create new category.
- PUT: /categories/{id} - (admin) - update category.
- GET: /categories/{id} - (user) - get category by id.
- GET: /categories/{id}/books - (user) - get books by category id.
- DELETE: /categories/{id} - (admin) - delete category by id.
- GET: /cart - (user) - get shopping cart.
- POST /cart - (user) - add book to shopping cart.
- PUT: /cart/cart-items/{cartItemId} - (user) - update quantity of book in shopping cart.
- DELETE: /cart/cart-items/{cartItemId} - (user) - delete book from shopping cart.
- GET: /orders - (user) - retrieve user's order history.
- POST: /orders - (user) - place an order.
- PATCH: /orders/{orderId} - (admin) - update order status.
- GET: /orders/{orderId}/items - (user) - retrieve all order items for a specific order.
- GET: /orders/{orderId}/items/{itemId} - (user) - retrieve a specific order item within an order.

### 🔨Technologies
- Java 17
- Spring Boot, Spring Security, Spring data JPA
- MySQL, Docker, Maven, Swagger


### 📂 Project structure:
#### [src/main/java/com.example.bookwonders/](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders)
- [config](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fconfig) - config classes for mapper and security
- [controller](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fcontroller) - http controllers
- [dto](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fdto) - classes for providing information in http requests and responses
- [exception](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fexception) - global exception handler and custom exceptions
- [lib](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Flib) - custom validators for email and password confirmation
- [mapper](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fmapper) - classes for map entities
- [model](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fmodel) - entity classes used in the application
- [repository](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Frepository) - classes for CRUD operation with database
- [security](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fsecurity) - classes for provide security
- [service](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fservice) - classes that provide business logic

#### [resources/](src%2Fmain%2Fresources)
- [changelog](src%2Fmain%2Fresources%2Fdb%2Fchangelog) - files for manage database with liquibase
- [application.properties](src%2Fmain%2Fresources%2Fapplication.properties) - contains application configuration

#### [src/test/java/com.example.bookwonders/](src%2Ftest%2Fjava%2Fcom%2Fexample%2Fbookwonders)
- [config](src%2Ftest%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fconfig) - config class for test user
- [controller](src%2Ftest%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fcontroller) - tests for [BookController.java](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fcontroller%2FBookController.java), [CategoryController.java](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fcontroller%2FCategoryController.java) and [ShoppingCartController.java](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fcontroller%2FShoppingCartController.java)
- [repository.category](src%2Ftest%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Frepository%2Fcategory) - test for [CategoryRepository.java](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Frepository%2Fcategory%2FCategoryRepository.java)


#### [test/resources/](src%2Ftest%2Fresources)
- [database/books&categories](src%2Ftest%2Fresources%2Fdatabase%2Fbooks%26categories) - sql scripts for [BookController.java](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fcontroller%2FBookController.java) and [CategoryController.java](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fcontroller%2FCategoryController.java)
- [database/cart](src%2Ftest%2Fresources%2Fdatabase%2Fcart) - sql scripts for [ShoppingCartController.java](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fbookwonders%2Fcontroller%2FShoppingCartController.java)

#### other files:

- [pom.xml](pom.xml) - contains maven configuration
- [checkstyle.xml](checkstyle.xml) - contains checkstyle rules
- [.env](.env) - contains credentials for database connection
- [Dockerfile](Dockerfile) - configuration for docker
- [docker-compose.yml](docker-compose.yml) - this file is used with Docker Compose to define multi-container applications. It specifies the services, networks, and volumes required for running the application, along with any environment variables.


### How to run the project locally:
1. Ensure you have Docker installed on your system.
2. Configure your database settings in the [.env](.env) file.
3. Open a terminal and navigate to the root directory of your project.
4. Run the application using Docker Compose: `docker-compose up`
5. Explore the endpoints using tools like Postman or Swagger

✏️ Note: Swagger available only for registered users.

✏️ Note: Аll endpoints that are available to the user are also available to the admin.
