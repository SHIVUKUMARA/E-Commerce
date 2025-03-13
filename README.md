# **E-Commerce API** 🚀  
### **A Role-Based E-Commerce Platform using Spring Boot**  

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-brightgreen)  
![Java](https://img.shields.io/badge/Java-21-orange)  
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)  
![Sendgrid](https://img.shields.io/badge/Sendgrid-Email-yellow)  
![Stripe](https://img.shields.io/badge/Stripe-Payments-blueviolet) 

---

## **📌 Overview**  
This is a **RESTful E-Commerce API** built using **Spring Boot, Spring Security, Spring JPA, Hibernate, and MySQL**. The application provides **role-based access control (RBAC)** for **Admins, Vendors, and Customers** with secure authentication using **JWT tokens**.  

The platform supports:
- **Store and Product Management**
- **Cart and Order Processing**
- **Stripe Payment Integration**
- **OTP-Based Order Confirmation**
- **Email Notifications for Transactions & Updates**

---

## **📜 Features**
### **🔑 Authentication & Authorization**
✔ **JWT-based authentication** for secure API access.  
✔ **Role-based access control (RBAC)** for Admin, Vendor, and Customer.  
✔ **Token blacklisting** to prevent unauthorized access after logout.  

### **🛒 Store & Product Management**
✔ **Admins** approve or reject store requests from vendors.  
✔ **Vendors** can add, update, and delete products after store approval.  
✔ **Customers** can browse products, sort by category, and search by store.  

### **📦 Cart & Order Management**
✔ Customers can **add products to cart** and proceed to checkout.  
✔ Before placing an order, customers must **provide a delivery address**.  
✔ After payment, cart items are moved to the **orders table**.  
✔ If payment is not completed, the order is **automatically canceled**.  

### **💳 Payment Integration (Stripe Third Party API)**
✔ Customers can make secure **online payments via Stripe**.  
✔ After payment:
  - **Order & payment details are stored.**  
  - **Email confirmation is sent to the customer.**  

### **🔢 OTP-Based Order Confirmation**
✔ After successful payment, customers receive a **6-digit OTP** via email.  
✔ OTP is valid for **10 days** to confirm or cancel the order.  
✔ If OTP is not used, the order **automatically gets canceled**.  

### **📧 Email Notifications (Sendgrid Third Party API)**
✔ Sent for **registration, login, profile updates**.  
✔ Sent when **store requests are approved/rejected**.  
✔ Sent for **successful payments**.  
✔ Sent when customers **add/update/delete reviews**.  
✔ Sent when an **OTP is generated for order confirmation**.  

---

## **🛠 Tech Stack**
| Technology | Description |
|------------|------------|
| **Java 21** | Backend Language |
| **Spring Boot** | Framework for building RESTful APIs |
| **Spring Security** | Authentication and Authorization |
| **JWT** | Token-based authentication |
| **Spring JPA & Hibernate** | ORM for database interaction |
| **MySQL** | Relational database |
| **Stripe API** | Payment gateway integration |
| **Cloudinary** | Image storage |
| **Git & GitHub** | Version Control |

---

## **🚀 Getting Started**
### **📌 Prerequisites**
Ensure you have the following installed:
- **Java 21**
- **Maven**
- **MySQL**
- **Git & GitHub**
- **Postman (Optional, for API testing)**

### **🔧 Installation Steps**
1. **Clone the repository**  
   ```bash
   git clone https://github.com/SHIVUKUMARA/E-Commerce.git
   cd E-Commerce
   ```

2. **Configure the `.env` file**  
   Create a `.env` file in the root directory and add:

   ```
   # Database Configuration (Update with your details)
   DB_URL=mysql://localhost:3306/ecommerce
   DB_USERNAME=root
   DB_PASSWORD=Your_password
   DB_DRIVER=com.mysql.cj.jdbc.Driver

   # Hibernate & JPA Properties
   DB_PLATFORM=org.hibernate.dialect.MySQL8Dialect
   DB_AUTO=update
   DB_SQL=true
   FORMAT_SQL=true

   #JWT SECRET KEY
   JWT_SECRET=uour_jwt_secret

   #You can create your own or Use the same
   ADMIN_NAME=Admin
   ADMIN_EMAIL=admin@gmail.com
   ADMIN_PASSWORD=Admin@123
   ADMIN_ROLE=ADMIN

   SENDGRID_HOST=smtp.sendgrid.net
   SENDGRID_PORT=587
   SENDGRID_MAIL_FROM=Add your Email from which you want to send email
   SENDGRID_PASSWORD=Your_sendgrid_api_key
   SENDGRID_AUTH=true
   SENDGRID_STARTTLS=true
   SENDGRID_PROTOCOL=smtp
   SENDGRID_SSL=ssmtp.sendgrid.net

   # PAYMENT Integration using STRIPE API
   STRIPE_API_KEY=Your_Stripe_Key

   # use same if You are using in local system else Replace with your deployment link with correct path
   STRIPRE_SUCCESS_URL=http://localhost:8080/api/payment/success
   STRIPE_CANCEL_URL=http://localhost:8080/api/payment/cancel

   CLOUD_NAME=Add-Cloudname
   CLOUD_API_KEY=Your_cloud_Api_key
   CLOUD_API_SECRET=Your_cloud_secret

   FILE_ENABLE=true
   FILE_MAX_SIZE=10MB
   FILE_MAX_REQUEST_SIZE=10MB

   ```

3. **Build and Run the Application**  
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

---

## **📡 API Endpoints**
- **[API Documentation of PostMan API](https://documenter.getpostman.com/view/30794754/2sAYdoGTjN)**
---

## **👨‍💻 Contributors**
- **[Shivukumara](https://github.com/SHIVUKUMARA)**
- **[Portfolio](https://shivukumara-portfolio.netlify.app/)**
- Open for Contributions! 🚀

---
```
EcommerceApi/
├─ .mvn/
│  └─ wrapper/
│     └─ maven-wrapper.properties
├─ .settings/
│  ├─ org.eclipse.core.resources.prefs
│  ├─ org.eclipse.jdt.apt.core.prefs
│  ├─ org.eclipse.jdt.core.prefs
│  ├─ org.eclipse.m2e.core.prefs
│  └─ org.springframework.ide.eclipse.prefs
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ com/
│  │  │     └─ example/
│  │  │        └─ E_Commerce/
│  │  │           ├─ config/
│  │  │           │  ├─ AppConfig.java
│  │  │           │  ├─ CloudinaryConfig.java
│  │  │           │  └─ SecurityConfig.java
│  │  │           ├─ controller/
│  │  │           │  ├─ AddressController.java
│  │  │           │  ├─ CartController.java
│  │  │           │  ├─ EmailController.java
│  │  │           │  ├─ ImageController.java
│  │  │           │  ├─ OrderController.java
│  │  │           │  ├─ PaymentController.java
│  │  │           │  ├─ ProductController.java
│  │  │           │  ├─ ProductReviewController.java
│  │  │           │  ├─ StoreController.java
│  │  │           │  └─ UserController.java
│  │  │           ├─ dto/
│  │  │           │  ├─ AddressDTO.java
│  │  │           │  ├─ CartItemResponse.java
│  │  │           │  ├─ CartResponse.java
│  │  │           │  ├─ LoginRequest.java
│  │  │           │  ├─ LoginResponse.java
│  │  │           │  ├─ PaymentRequest.java
│  │  │           │  ├─ ProductRequest.java
│  │  │           │  ├─ ProductResponse.java
│  │  │           │  ├─ ReviewRequest.java
│  │  │           │  ├─ StoreRequest.java
│  │  │           │  ├─ StoreResponse.java
│  │  │           │  ├─ UpdateUserRequest.java
│  │  │           │  └─ UserDto.java
│  │  │           ├─ entity/
│  │  │           │  ├─ Address.java
│  │  │           │  ├─ Cart.java
│  │  │           │  ├─ CartItem.java
│  │  │           │  ├─ Order.java
│  │  │           │  ├─ OrderItem.java
│  │  │           │  ├─ Payment.java
│  │  │           │  ├─ Product.java
│  │  │           │  ├─ ProductReview.java
│  │  │           │  ├─ Role.java
│  │  │           │  ├─ Store.java
│  │  │           │  ├─ StoreStatus.java
│  │  │           │  └─ User.java
│  │  │           ├─ exceptionHandler/
│  │  │           │  ├─ AddressNotFoundException.java
│  │  │           │  ├─ CustomerNotFoundException.java
│  │  │           │  ├─ GlobalExceptionHandler.java
│  │  │           │  ├─ PaymentProcessingException.java
│  │  │           │  ├─ ResourceNotFoundException.java
│  │  │           │  └─ UserNotFoundException.java
│  │  │           ├─ repository/
│  │  │           │  ├─ AddressRepository.java
│  │  │           │  ├─ CartItemRepository.java
│  │  │           │  ├─ CartRepository.java
│  │  │           │  ├─ OrderItemRepository.java
│  │  │           │  ├─ OrderRepository.java
│  │  │           │  ├─ PaymentRepository.java
│  │  │           │  ├─ ProductRepository.java
│  │  │           │  ├─ ProductReviewRepository.java
│  │  │           │  ├─ StoreRepository.java
│  │  │           │  └─ UserRepository.java
│  │  │           ├─ security/
│  │  │           │  ├─ JwtAuthenticationFilter.java
│  │  │           │  ├─ JwtAuthenticationToken.java
│  │  │           │  └─ JwtUtil.java
│  │  │           ├─ service/
│  │  │           │  ├─ AddressService.java
│  │  │           │  ├─ CartService.java
│  │  │           │  ├─ CloudinaryService.java
│  │  │           │  ├─ EmailService.java
│  │  │           │  ├─ OrderCleanupService.java
│  │  │           │  ├─ OrderService.java
│  │  │           │  ├─ PaymentService.java
│  │  │           │  ├─ ProductReviewService.java
│  │  │           │  ├─ ProductService.java
│  │  │           │  ├─ StoreService.java
│  │  │           │  └─ UserService.java
│  │  │           └─ EcommerceApiApplication.java
│  │  └─ resources/
│  │     └─ application.properties
│  └─ test/
│     └─ java/
│        └─ com/
│           └─ example/
│              └─ E_Commerce/
│                 └─ EcommerceApiApplicationTests.java
├─ target/
│  ├─ classes/
│  │  ├─ com/
│  │  │  └─ example/
│  │  │     └─ E_Commerce/
│  │  │        ├─ config/
│  │  │        │  ├─ AppConfig.class
│  │  │        │  ├─ CloudinaryConfig.class
│  │  │        │  └─ SecurityConfig.class
│  │  │        ├─ controller/
│  │  │        │  ├─ AddressController.class
│  │  │        │  ├─ CartController.class
│  │  │        │  ├─ EmailController.class
│  │  │        │  ├─ ImageController.class
│  │  │        │  ├─ OrderController.class
│  │  │        │  ├─ PaymentController.class
│  │  │        │  ├─ ProductController.class
│  │  │        │  ├─ ProductReviewController.class
│  │  │        │  ├─ StoreController.class
│  │  │        │  └─ UserController.class
│  │  │        ├─ dto/
│  │  │        │  ├─ AddressDTO.class
│  │  │        │  ├─ CartItemResponse.class
│  │  │        │  ├─ CartResponse.class
│  │  │        │  ├─ LoginRequest.class
│  │  │        │  ├─ LoginResponse.class
│  │  │        │  ├─ PaymentRequest.class
│  │  │        │  ├─ ProductRequest.class
│  │  │        │  ├─ ProductResponse.class
│  │  │        │  ├─ ReviewRequest.class
│  │  │        │  ├─ StoreRequest.class
│  │  │        │  ├─ StoreResponse.class
│  │  │        │  ├─ UpdateUserRequest.class
│  │  │        │  ├─ UserDto.class
│  │  │        │  └─ UserDto$UserDtoBuilder.class
│  │  │        ├─ entity/
│  │  │        │  ├─ Address.class
│  │  │        │  ├─ Address$AddressBuilder.class
│  │  │        │  ├─ Cart.class
│  │  │        │  ├─ CartItem.class
│  │  │        │  ├─ Order.class
│  │  │        │  ├─ OrderItem.class
│  │  │        │  ├─ Payment.class
│  │  │        │  ├─ Payment$PaymentBuilder.class
│  │  │        │  ├─ Product.class
│  │  │        │  ├─ Product$ProductBuilder.class
│  │  │        │  ├─ ProductReview.class
│  │  │        │  ├─ Role.class
│  │  │        │  ├─ Store.class
│  │  │        │  ├─ Store$StoreBuilder.class
│  │  │        │  ├─ StoreStatus.class
│  │  │        │  ├─ User.class
│  │  │        │  └─ User$UserBuilder.class
│  │  │        ├─ exceptionHandler/
│  │  │        │  ├─ AddressNotFoundException.class
│  │  │        │  ├─ CustomerNotFoundException.class
│  │  │        │  ├─ GlobalExceptionHandler.class
│  │  │        │  ├─ PaymentProcessingException.class
│  │  │        │  ├─ ResourceNotFoundException.class
│  │  │        │  └─ UserNotFoundException.class
│  │  │        ├─ repository/
│  │  │        │  ├─ AddressRepository.class
│  │  │        │  ├─ CartItemRepository.class
│  │  │        │  ├─ CartRepository.class
│  │  │        │  ├─ OrderItemRepository.class
│  │  │        │  ├─ OrderRepository.class
│  │  │        │  ├─ PaymentRepository.class
│  │  │        │  ├─ ProductRepository.class
│  │  │        │  ├─ ProductReviewRepository.class
│  │  │        │  ├─ StoreRepository.class
│  │  │        │  └─ UserRepository.class
│  │  │        ├─ security/
│  │  │        │  ├─ JwtAuthenticationFilter.class
│  │  │        │  ├─ JwtAuthenticationToken.class
│  │  │        │  └─ JwtUtil.class
│  │  │        ├─ service/
│  │  │        │  ├─ AddressService.class
│  │  │        │  ├─ CartService.class
│  │  │        │  ├─ CloudinaryService.class
│  │  │        │  ├─ EmailService.class
│  │  │        │  ├─ OrderCleanupService.class
│  │  │        │  ├─ OrderService.class
│  │  │        │  ├─ PaymentService.class
│  │  │        │  ├─ ProductReviewService.class
│  │  │        │  ├─ ProductService.class
│  │  │        │  ├─ StoreService.class
│  │  │        │  └─ UserService.class
│  │  │        └─ EcommerceApiApplication.class
│  │  └─ application.properties
│  ├─ generated-sources/
│  │  └─ annotations/
│  ├─ generated-test-sources/
│  │  └─ test-annotations/
│  ├─ maven-status/
│  │  └─ maven-compiler-plugin/
│  │     └─ compile/
│  │        └─ default-compile/
│  │           ├─ createdFiles.lst
│  │           └─ inputFiles.lst
│  └─ test-classes/
│     └─ com/
│        └─ example/
│           └─ E_Commerce/
│              └─ EcommerceApiApplicationTests.class
├─ .classpath
├─ .env
├─ .factorypath
├─ .gitattributes
├─ .gitignore
├─ .project
├─ HELP.md
├─ mvnw
├─ mvnw.cmd
└─ pom.xml
```
---
