# FIRS — Firearm Inventory & Repository System 🔴



[![Java](https://img.shields.io/badge/Java-17%2B-007396?logo=openjdk&logoColor=white)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.2-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.6%2B-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**FIRS** is a full‑stack, role‑based firearms marketplace demonstration platform.  
It showcases secure authentication, tiered catalog access, inventory management, and a complete order lifecycle – all built with a **Spring Boot** REST API and a **vanilla JavaScript** frontend.

> ⚠️ **Disclaimer**  
> This project is intended **solely for educational and demonstration purposes**. No real firearms are sold, transferred, or endorsed through this system.

---


## 🧠 Architecture Overview

- Layered architecture: Controller → Service → Repository  
- RESTful API design  
- JPA / Hibernate ORM for relational mapping  
- Stateless backend with client-side session handling  
- Data flow: Client → API → Service → Repository → Database  

---


## ✨ Key Features

### 🔐 Authentication & Role‑Based Access
- Four distinct user roles: **Customer**, **Government/Militia**, **Licensed Dealer**, **Administrator**
- Tiered catalog visibility – restricted items (rifles, snipers) require verified credentials
- Secure login and session handling

### 🛒 Shopping Experience
- Dynamic product catalog with filtering by category, brand, caliber, price, and rating
- Real‑time cart management with FFL transfer fee simulation
- Secure checkout process integrated with backend order creation

### 📦 Order Management
- Order history view with status tracking (Processing, Shipped, Delivered)
- Admin dashboard for order approvals, user management, and platform oversight

### 🏢 Dealer Portal
- Full inventory CRUD operations
- Bulk import via CSV
- Low‑stock alerts and stock level visualization
- FFL network management

### 🎨 Frontend
- Pure **HTML5**, **CSS3**, and **ES6 JavaScript** – no external frameworks
- Responsive, dark tactical theme with smooth animations
- Client‑side session & cart persistence using `localStorage`

---



## 🛠️ Technology Stack

| Layer       | Technologies                                                                 |
|-------------|-------------------------------------------------------------------------------|
| **Backend** | Java 17, Spring Boot 3.3.2, Spring Data JPA (Hibernate), Maven, Lombok        |
| **Frontend**| HTML5, CSS3, Vanilla JavaScript, Fetch API, Google Fonts                      |
| **Database**| MySQL 8.0+ (with Hibernate DDL auto‑generation)                               |
| **Tooling** | XAMPP / MySQL Workbench, VS Code, Postman                                     |

---



## 📁 Project Structure

```text
firs-project/
├── src/
│   ├── main/
│   │   ├── java/com/firs/project/
│   │   │   ├── config/        # DataInitializer (seeds default data)
│   │   │   ├── controller/    # REST controllers (Auth, Orders, Products, Users)
│   │   │   ├── model/         # JPA entities (User, Product, Order, OrderItem)
│   │   │   ├── repository/    # Spring Data JPA interfaces
│   │   │   └── service/       # Business logic services
│   │   └── resources/
│   │       ├── static/        # Frontend assets (HTML, CSS, JS, images)
│   │       └── application.properties
├── database/
│   └── firs_db.sql            # SQL schema + initial data
├── pom.xml
└── README.md
```

---



## 🚀 Getting Started

### 📋 Prerequisites
- **Java 17** or later ([Adoptium](https://adoptium.net/))
- **Maven** 3.6+ ([Download](https://maven.apache.org/download.cgi))
- **MySQL** 8.0+ (or XAMPP for local development)



### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/firs-project.git
cd firs-project
```

2️⃣ Configure Database
Edit src/main/resources/application.properties with your MySQL credentials:
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/firs_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password
```

3️⃣ (Optional) Import SQL Schema
If you prefer a pre‑populated database:
```bash
mysql -u root -p firs_db < database/firs_db.sql
```

4️⃣ Run the Backend
```bash
mvn clean spring-boot:run
```

The API will be available at http://localhost:8080


### 5️⃣ Launch Frontend

Serve the frontend from the following directory:
```bash
src/main/resources/static/
```
You can use any local HTTP server (e.g., VS Code Live Server).

Make sure your API base URL is correctly set in `script.js`:
```js
const API_BASE = "http://localhost:8080/api";
```

## 🔐 Demo Credentials

| Role        | Email               | Password     | Access Level                          |
|-------------|--------------------|--------------|----------------------------------------|
| Customer    | customer@firs.com  | customer123  | Handguns & Revolvers only              |
| Government  | gov@firs.com       | gov123       | Full catalog (rifles, snipers)         |
| Dealer      | dealer@firs.com    | dealer123    | Dealer dashboard, inventory management |
| Admin       | admin@firs.com     | admin123     | Full system control                  |

## 📡 API Endpoints

| Method | Endpoint        | Description              | Request Body Example |
|--------|----------------|--------------------------|----------------------|
| POST   | /api/register  | Register a new user      | `{ "name": "...", "email": "...", "password": "...", "role": "..." }` |
| POST   | /api/login     | Authenticate user        | `{ "email": "...", "password": "..." }` |
| GET    | /api/products  | Retrieve all products    | – |
| GET    | /api/orders    | Retrieve user orders     | – |
| POST   | /api/orders    | Create a new order       | `{ "userEmail": "...", "items": [ { "name": "...", "qty": ... } ] }` |

> **Note:** All endpoints are CORS-enabled for local development.


## 🖼️ Screenshots

<details>
<summary>Click to expand</summary>

### Homepage
![Homepage](screenshots/homepage.png)

### Product Catalog
![Product Catalog](screenshots/catalog.png)

### Dealer Dashboard
![Dealer Dashboard](screenshots/dealer.png)

</details>
## 🐞 Troubleshooting

| Issue                          | Solution |
|--------------------------------|----------|
| CORS errors                    | Ensure backend runs on `localhost:8080` and frontend is served from a different origin |
| Blank pages / JS errors        | Open browser DevTools (`F12`) → check Console for errors |
| Infinite recursion / huge JSON | Add `@JsonIgnore` to bidirectional JPA relationships (e.g., in `User.java`) |
| MySQL connection refused       | Verify MySQL service is running and credentials in `application.properties` are correct |
| Images not loading             | Confirm paths exist under `static/images/` or `static/icons/` |


## 🤝 Contributing

Contributions are welcome. To get started:

1. Fork the repository  
2. Create a new branch:
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add amazing feature"
   ```
4. Push to your branch:
   ```bash
   git push origin feature/amazing-feature
   ```
5. Open a Pull Request


## 📄 License

This project is licensed under the **MIT License**.  
See the [LICENSE](LICENSE) file for details.
