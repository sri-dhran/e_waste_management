# Smart E-Waste Management System

A complete, premium digital ecosystem designed to streamline electronic waste recycling. Customers can submit electronics, download a unique QR code, drop them off at certified collection centers, track logistics transit progress, and earn rewards.

---

## 🛠️ Technology Stack
- **Frontend**: HTML5, CSS3, JavaScript (ES6+), Bootstrap 5, Chart.js, Bootstrap Icons.
- **Backend**: Java 21, Spring Boot 3.3.0, Spring Data JPA, Maven.
- **QR Code**: ZXing QR library.
- **Database**: MySQL 9.x.

---

## 📂 Project Structure
```text
Smart-EWaste-Management/
│
├── frontend/                     # Modern Bootstrap Slate-Green UI
│   ├── index.html                # Home Landing Page
│   ├── register.html             # User Registration
│   ├── login.html                # Unified Login (User, Staff, Admin)
│   ├── dashboard.html            # User Dashboard
│   ├── submitWaste.html          # Submit E-Waste details
│   ├── rewards.html              # Reward points & vouchers redemptions
│   ├── center.html               # Collection Center staff scans & verifications
│   ├── tracking.html             # Progress Stepper logistics status
│   ├── admin.html                # Admin Dashboard (Charts + Updates)
│   ├── reports.html              # Dedicated Reports print/export view
│   └── css/
│       └── style.css             # Main styling sheet (glassmorphism details)
│
├── backend/                      # Spring Boot Project
│   ├── src/main/java/com/ewaste/
│   │   ├── config/               # Static folder resource handlers
│   │   ├── controller/           # REST endpoints
│   │   ├── model/                # JPA entities
│   │   ├── repository/           # JPA repositories
│   │   ├── util/                 # ZXing QR Generator
│   │   └── EwasteApplication.java # Runner file
│   ├── src/main/resources/
│   │   └── application.properties # Server, database & static resource config
│   └── pom.xml                   # Maven dependencies
│
├── database/
│   └── ewaste.sql                # MySQL schema definitions + seed values
│
├── uploads/                      # Auto-created; stores user uploaded device images
├── qr/                           # Auto-created; stores generated QR Code images
└── README.md
```

---

## 🚀 Getting Started

### 1. Database Setup
Make sure MySQL is running on your system. Run the following command to create the database and seed it:
```bash
mysql -u root -p1234 < database/ewaste.sql
```
*Note: The system username is configured as `root` and password as `1234` in `backend/src/main/resources/application.properties`.*

### 2. Build & Launch Backend
Navigate to the `backend` folder and run the Maven Spring Boot plugin:
```bash
cd backend
mvn clean package
mvn spring-boot:run
```

Once started, the server will list on:
- **Web App URL**: [http://localhost:8080/index.html](http://localhost:8080/index.html)

---

## 🔑 Demo Access credentials
For quick testing, you can use the preloaded demo accounts:

1. **System Administrator**:
   - **Username**: `admin`
   - **Password**: `admin123`
   - **Dashboard**: [http://localhost:8080/admin.html](http://localhost:8080/admin.html)

2. **Collection Center Staff (Green Earth Recycling)**:
   - **Email**: `staff@greenearth.com`
   - **Password**: `staff123`
   - **Dashboard**: [http://localhost:8080/center.html](http://localhost:8080/center.html)

3. **Customer / User**:
   - Register on the portal [http://localhost:8080/register.html](http://localhost:8080/register.html) or log in with any newly created credentials.
