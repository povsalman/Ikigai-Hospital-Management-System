# Ikigai-Care: Hospital Management System

Ikigai-Care is a comprehensive hospital management system built with **JavaFX** and **MySQL**. It centralizes all hospital operations, transitioning from manual paperwork to an efficient digital platform. The system enhances patient care, optimizes workflows, and improves decision-making through unified data management.

---

## The Problem

Traditional hospital management often depends on **manual, paper-based systems** or **isolated digital tools**. This fragmented approach causes:

- Data silos and inefficiencies  
- Scheduling conflicts among staff  
- Inaccurate inventory tracking  
- Administrative delays and human errors  

Without a centralized system, hospitals face higher operational costs, slower treatments, and reduced communication efficiency between departments.

---

## The Solution

Ikigai-Care provides a **centralized digital platform** that connects administrators, doctors, and patients through real-time data and automated workflows. It ensures:

- Seamless communication among departments  
- Automated staff and patient management  
- Accurate and up-to-date record keeping  
- Improved care quality through efficiency and reliability  

---

## Key Features

The system consists of **three main modules** designed for different user roles.

### Admin Module
- Manage doctor accounts (add, remove, update)
- Approve doctor registration requests
- Schedule and manage staff shifts
- Track hospital inventory (log “Goods In” / “Goods Out”)
- Manage supply requests and restocking
- Schedule hospital-wide workshops

### Doctor Module
- Access personal dashboard of appointments
- Update patient medical records and treatment plans
- Schedule surgeries
- View assigned shifts and workshops
- Review patient feedback

### Patient Module
- Register and manage a personal account
- Book, view, or cancel doctor appointments
- Reserve rooms for admission
- Access prescriptions and surgery schedules
- Submit feedback on services
- View and generate bills for treatments

---

## Technology Stack

- **Frontend:** JavaFX  
- **Backend:** Java  
- **Database:** MySQL  
- **Version Control:** Git  
- **Core Design Principle:** MVC (Model-View-Controller) pattern  

---

# Preview

1) Doctor's Panel:
![D1](https://github.com/user-attachments/assets/9daaece0-a925-4fa0-a9e7-e8676cca2890)
![D2](https://github.com/user-attachments/assets/f4fb83f3-8860-4be8-8884-99438b7c39dd)
   
2) Patient's Panel:
![P1](https://github.com/user-attachments/assets/2c0333c3-88d2-4fb7-a16d-3071f19bc29a)
![P2](https://github.com/user-attachments/assets/fd1d307b-49be-48dc-bdd1-1755aafbec2c)

   
3) Admin's Panel:
![A1](https://github.com/user-attachments/assets/ad7f92a8-ae77-4b19-a8c3-4691065f53f7)
![A2](https://github.com/user-attachments/assets/e196a952-ec62-4fc2-9173-c1649e0c004d)


---


## Project Structure

```plaintext
Ikigai-I-CARE-main/
│
├── Database/
│   └── AllOwnCompiled.sql        # Main database schema and data
│
├── src/
│   ├── application/              # Main application entry point
│   ├── adminControllers/         # Controllers for admin logic
│   ├── adminModel/               # Data models for admin
│   ├── adminViews/               # FXML UI files for admin
│   ├── doctorController/         # Controllers for doctor logic
│   ├── doctorHandler/            # Database handlers for doctor
│   ├── doctorModel/              # Data models for doctor
│   ├── patientController/        # Controllers for patient logic
│   ├── patientHandler/           # Database handlers for patient
│   ├── patientModel/             # Data models for patient
│   └── ...                       # Other utility packages
│
├── SDA Project Presentation.pdf
├── SDA Report.pdf
├── build.fxbuild
└── README.md
```

---

## Getting Started

### Prerequisites

Before installation, ensure the following are installed:

- Java JDK  
- JavaFX SDK  
- MySQL Server  
- A Java IDE (e.g., IntelliJ IDEA, Eclipse)

---

### Installation

#### 1. Clone the Repository
```bash
git clone (https://github.com/povsalman/Ikigai-Hospital-Management-System.git)
```

#### 2. Database Setup
- Start your MySQL server  
- Create a database (e.g., `ikigai_db`)  
- Import the `Database/AllOwnCompiled.sql` file to initialize tables and data  

#### 3. Configure Application
- Open the project in your IDE  
- Locate database utilities in `doctorHandler`, `patientHandler`, or `adminModel`  
- Update the **URL**, **username**, and **password** in the code to match your MySQL configuration  

#### 4. Run the Application
- Set up your IDE with the JavaFX SDK path  
- Run the main application class (found in `src/application`)  

---

## Team

- **Mavra Mehak** – Scrum Master  
- **Maryum Tanvir** – Developer / Analyst  
- **Salman Khan** – QA / Design  

---

## License

This project is licensed under the **MIT License**.  
Refer to the `LICENSE` file for details.


