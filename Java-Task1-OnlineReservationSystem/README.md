# OASIS INFOBYTE Internship - Java Development
## Task 1: Online Reservation System

A desktop train ticket reservation and cancellation system built in **Java Swing** with **SQLite JDBC** and **PreparedStatement** security.

---

### 📋 Overview & Requirements Mapping

This project strictly adheres to the **OASIS INFOBYTE Java Development Task 1** specifications:

| Requirement | Implementation Details | Java Class / Method |
| :--- | :--- | :--- |
| **Login Form** | Authenticates users against hashed records in database. Rejects invalid credentials. | `LoginFrame.java` -> `authenticateUser()` |
| **Reservation Form** | Inputs passenger name, train number, class, date, source & destination stations. | `ReservationFrame.java` |
| **Auto Train Lookup** | Automatically populates train name upon entering the numeric train number without manual re-typing. | `TrainDAO.java` -> `findTrainName()` |
| **Book & PNR Generation** | Validates form fields, generates a unique PNR, and securely inserts the reservation into the database. | `ReservationDAO.java` -> `insert()`, `generatePNR()` |
| **Confirmation Dialog** | Displays a formatted summary popup with the unique PNR and full journey details upon successful booking. | `ReservationFrame.java` -> `JOptionPane.showMessageDialog()` |
| **Cancellation Form** | Fetches and displays booking details for a given PNR from the database. | `CancellationFrame.java` -> `fetchBookingByPNR()` |
| **Confirmation on Cancel** | Prompts "Are you sure?" before deleting the reservation record from the database. | `CancellationFrame.java` -> `cancelBooking()` |
| **Input Validation** | Rejects blank fields, non-numeric train numbers, invalid date formats, past dates, and same source/destination. | `ReservationFrame.java` -> `validateForm()` |

---

### 🗄️ Database Design

The system uses an embedded **SQLite** database (`reservation_system.db`) which requires no external server setup:

```sql
CREATE TABLE IF NOT EXISTS users (
    username TEXT PRIMARY KEY,
    password_hash TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS reservations (
    pnr TEXT PRIMARY KEY,
    passenger_name TEXT NOT NULL,
    train_number INTEGER NOT NULL,
    train_name TEXT NOT NULL,
    class_type TEXT NOT NULL,
    journey_date TEXT NOT NULL,
    source_station TEXT NOT NULL,
    destination_station TEXT NOT NULL
);
```

> **Security Note:** Passwords are never stored in plain text; they are securely hashed using **SHA-256** (`Database.hashPassword()`). All database queries utilize **`PreparedStatement`** to eliminate SQL injection vulnerabilities.

---

### 📂 Project Structure

```
OIBSIP/
└── Java-Task1-OnlineReservationSystem/
    ├── bin/                                # Compiled .class bytecode
    ├── lib/                                # JDBC Driver dependencies
    │   ├── sqlite-jdbc-3.45.1.0.jar
    │   ├── slf4j-api-2.0.12.jar
    │   └── slf4j-simple-2.0.12.jar
    ├── screenshots/                        # Demo & submission screenshots
    ├── src/
    │   └── com/oasis/reservation/
    │       ├── ReservationApp.java         # Main entry point
    │       ├── Database.java               # SQLite DB setup & SHA-256 hashing
    │       ├── Reservation.java            # Model class
    │       ├── TrainDAO.java               # Train database & auto-lookup
    │       ├── ReservationDAO.java         # CRUD operations using PreparedStatement
    │       ├── LoginFrame.java             # Swing Login & Registration UI
    │       ├── ReservationFrame.java       # Swing Ticket Booking UI
    │       └── CancellationFrame.java      # Swing Ticket Cancellation UI
    ├── README.md                           # Documentation
    ├── run.sh                              # macOS / Linux launch script
    └── run.bat                             # Windows launch script
```

---

### 🚀 Getting Started & Execution

#### Default Login Credentials:
- **Username:** `admin`
- **Password:** `admin123`
*(You can also use the **"Register New User"** button directly in the login dialog to create new accounts)*

#### Option 1: Using the Terminal / Scripts

##### macOS / Linux:
```bash
cd OIBSIP/Java-Task1-OnlineReservationSystem
./run.sh
```

##### Windows:
Double-click `run.bat` or run in Command Prompt:
```cmd
cd OIBSIP\Java-Task1-OnlineReservationSystem
run.bat
```

#### Option 2: Manual Compile & Run
```bash
# From inside Java-Task1-OnlineReservationSystem directory:
javac -cp "lib/*" -d bin src/com/oasis/reservation/*.java
java -cp "bin:lib/*" com.oasis.reservation.ReservationApp
```

---

### 🧪 Testing Checklist

1. **Login Test**: Enter invalid password $\rightarrow$ access is denied. Enter `admin` / `admin123` $\rightarrow$ login succeeds.
2. **Train Lookup Test**: Enter train number `12001` or `12301` $\rightarrow$ train name auto-fills automatically.
3. **Form Validation Test**: Leave passenger name blank, or input letters into train number, or choose same source and destination $\rightarrow$ clear error alerts are shown.
4. **Ticket Booking Test**: Fill details and click "Book Ticket" $\rightarrow$ generates unique PNR (e.g., `PNR748291`) and shows confirmation dialog.
5. **Fetch by PNR Test**: Open "Cancel Ticket Form", input the generated PNR and click "Fetch Booking" $\rightarrow$ full details are displayed.
6. **Cancellation Test**: Click "Cancel Reservation" $\rightarrow$ confirms with "Are you sure?" dialog before removing the record.

