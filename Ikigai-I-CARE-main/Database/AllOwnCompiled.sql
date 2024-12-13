drop database if exists Ikigai;

-- Database creation
CREATE DATABASE IF NOT EXISTS Ikigai;
USE Ikigai;


-- Admin Table
CREATE TABLE Admin (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    admin_Fname VARCHAR(50) NOT NULL,
    admin_Lname VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_no VARCHAR(15) NOT NULL
);

-- Doctor Table
CREATE TABLE Doctor (
    doctor_id INT PRIMARY KEY AUTO_INCREMENT,
    doc_Fname VARCHAR(100) NOT NULL,
    doc_Lname VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    specialized VARCHAR(50),
    gender ENUM('Male', 'Female', 'Other'),
    mobile_number VARCHAR(15),
    image VARCHAR(100) DEFAULT NULL
);

-- MedicalSupply Table
CREATE TABLE MedicalSupply (
    medsupply_id INT AUTO_INCREMENT PRIMARY KEY,
    medsupply_name VARCHAR(50) NOT NULL UNIQUE
);

-- MedicalSupplyDescription Table
CREATE TABLE MedicalSupplyDescription (
    description_id INT AUTO_INCREMENT PRIMARY KEY,
    medsupply_id INT NOT NULL,
    description VARCHAR(200) NOT NULL,
    medsupply_type VARCHAR(50) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (medsupply_id) REFERENCES MedicalSupply(medsupply_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- MedicalSupplyCatalogue Table
CREATE TABLE MedicalSupplyCatalogue (
    catalogue_id INT AUTO_INCREMENT PRIMARY KEY,
    description_id INT NOT NULL,
    FOREIGN KEY (description_id) REFERENCES MedicalSupplyDescription(description_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- StockItem Table
CREATE TABLE StockItem (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    medsupply_id INT NOT NULL,
    item_name VARCHAR(50) NOT NULL,
    quantity_available INT NOT NULL DEFAULT 0,
    threshold INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (medsupply_id) REFERENCES MedicalSupply(medsupply_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- MedicalSupplyRequest Table
CREATE TABLE MedicalSupplyRequest (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    admin_id INT,
    quantity_requested INT NOT NULL,
    request_date DATE NOT NULL,
    status ENUM('Pending', 'Received') NOT NULL,
    FOREIGN KEY (item_id) REFERENCES StockItem(item_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES Admin(admin_id) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Inventory Table
CREATE TABLE Inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    FOREIGN KEY (item_id) REFERENCES StockItem(item_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- GoodIn Table
CREATE TABLE GoodIn (
    goodin_id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT,
    item_id INT NOT NULL,
    quantity INT NOT NULL,
    purchase_price DECIMAL(10,2),
    received_date DATE NOT NULL,
    FOREIGN KEY (request_id) REFERENCES MedicalSupplyRequest(request_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (item_id) REFERENCES StockItem(item_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- GoodOut Table
CREATE TABLE GoodOut (
    goodout_id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    quantity INT NOT NULL,
    sale_price DECIMAL(10,2),
    issued_date DATE NOT NULL,
    FOREIGN KEY (item_id) REFERENCES StockItem(item_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Workshop Table
CREATE TABLE Workshop (
    workshop_id INT AUTO_INCREMENT PRIMARY KEY,
    admin_id INT,
    workshop_name VARCHAR(50) NOT NULL,
    speaker VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    workshop_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    location VARCHAR(50) NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES Admin(admin_id) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Associative Table: Doctor_Workshop
CREATE TABLE Doctor_Workshop (
    doctor_id INT,
    workshop_id INT,
    status ENUM('Registered', 'Unregistered') DEFAULT 'Registered',
    PRIMARY KEY (doctor_id, workshop_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (workshop_id) REFERENCES Workshop(workshop_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Shift Table
CREATE TABLE Shift (
    shift_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT,
    admin_id INT,
    description VARCHAR(200) NOT NULL,
    start_time TIME NULL,
    end_time TIME NULL,
    shift_date DATE NULL,
    status ENUM('Active', 'Available') DEFAULT 'Active',
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES Admin(admin_id) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Patient Table
CREATE TABLE Patient (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    gender ENUM('Male', 'Female', 'Other'),
    address VARCHAR(255),
    phone_no VARCHAR(15),
    email VARCHAR(100) UNIQUE NOT NULL,
    _password VARCHAR(255) NOT NULL,
    medical_details TEXT,
    image VARCHAR(100) DEFAULT NULL
);

-- Room Table
CREATE TABLE Room (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT DEFAULT NULL,
    room_type VARCHAR(50),
    price DECIMAL(10,2) NOT NULL,
    availability_status ENUM('Available', 'Occupied') NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Booking Table
CREATE TABLE Booking (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    room_id INT NOT NULL,
    booking_date DATE NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (room_id) REFERENCES Room(room_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Prescription Table
CREATE TABLE Prescription (
    prescription_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    date_issued DATE NOT NULL,
    status_ ENUM('Complete', 'Pending') DEFAULT 'Pending',
    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Sale Table
CREATE TABLE Sale (
    sale_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    sale_date DATETIME NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- SaleLineItem Table
CREATE TABLE SaleLineItem (
    line_item_id INT AUTO_INCREMENT PRIMARY KEY,
    sale_id INT NOT NULL,
    medsupply_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES Sale(sale_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (medsupply_id) REFERENCES MedicalSupply(medsupply_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Bill Table
CREATE TABLE Bill (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Appointment Table
CREATE TABLE Appointment (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    status_ ENUM('Prescribed', 'On Treatment', 'Surgery', 'Completed') DEFAULT 'Prescribed',
    price DECIMAL(10,2),
    description varchar(255) DEFAULT NULL,
    diagnosis VARCHAR(100) DEFAULT NULL,
    treatment VARCHAR(100) DEFAULT NULL,
    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Feedback Table
CREATE TABLE Feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    feedback_date DATE NOT NULL,
    description_ TEXT NOT NULL,
    rating_stars INT NOT NULL CHECK (rating_stars BETWEEN 1 AND 5),
    status_ ENUM('Reviewed', 'Pending') DEFAULT 'Pending',
    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Patient_Treatment Table
CREATE TABLE Patient_Treatment (
    treatment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    item_id INT DEFAULT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status ENUM('Good', 'Worse') DEFAULT 'Good',
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (item_id) REFERENCES StockItem(item_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Surgery Table
CREATE TABLE Surgery (
    surgery_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    room_id INT NOT NULL,
    operation_date DATE NOT NULL,
    operation_time TIME NOT NULL,
    status ENUM('Pending', 'Complete') DEFAULT 'Pending',
    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (room_id) REFERENCES Room(room_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Doctor's request for login
CREATE TABLE DoctorRequest(
    request_id INT PRIMARY KEY AUTO_INCREMENT,
    doc_Fname VARCHAR(100) NOT NULL,
    doc_Lname VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    specialized VARCHAR(50),
    gender ENUM('Male', 'Female', 'Other'),
    mobile_number VARCHAR(15)
);



-- Doctor table
INSERT INTO Doctor (doctor_id, doc_Fname, doc_Lname, email, password, specialized, gender, mobile_number, image) VALUES
(1, 'John', 'Smith', 'john.smith@hospital.com', 'DocPass1', 'Cardiologist', 'Male', '03011234567', NULL),
(2, 'Emily', 'Brown', 'emily.brown@hospital.com', 'DocPass2', 'Neurologist', 'Female', '03011234568', NULL),
(3, 'Michael', 'Davis', 'michael.davis@hospital.com', 'DocPass3', 'Orthopedic', 'Male', '03011234569', NULL),
(4, 'Sophia', 'Johnson', 'sophia.johnson@hospital.com', 'DocPass4', 'Pediatrician', 'Female', '03011234570', NULL),
(5, 'James', 'Williams', 'james.williams@hospital.com', 'DocPass5', 'General', 'Male', '03011234571', NULL),
(6, 'Olivia', 'Miller', 'olivia.miller@hospital.com', 'DocPass6', 'Cardiologist', 'Female', '03011234572', NULL),
(7, 'William', 'Taylor', 'william.taylor@hospital.com', 'DocPass7', 'Neurologist', 'Male', '03011234573', NULL),
(8, 'Isabella', 'Anderson', 'isabella.anderson@hospital.com', 'DocPass8', 'Orthopedic', 'Female', '03011234574', NULL),
(9, 'David', 'Thomas', 'david.thomas@hospital.com', 'DocPass9', 'Pediatrician', 'Male', '03011234575', NULL),
(10, 'Emma', 'Jackson', 'emma.jackson@hospital.com', 'DocPass10', 'General', 'Female', '03011234576', NULL),
(11, 'Daniel', 'White', 'daniel.white@hospital.com', 'DocPass11', 'Cardiologist', 'Male', '03011234577', NULL),
(12, 'Charlotte', 'Harris', 'charlotte.harris@hospital.com', 'DocPass12', 'Neurologist', 'Female', '03011234578', NULL),
(13, 'Joseph', 'Martin', 'joseph.martin@hospital.com', 'DocPass13', 'Orthopedic', 'Male', '03011234579', NULL),
(14, 'Amelia', 'Thompson', 'amelia.thompson@hospital.com', 'DocPass14', 'Pediatrician', 'Female', '03011234580', NULL),
(15, 'Henry', 'Garcia', 'henry.garcia@hospital.com', 'DocPass15', 'General', 'Male', '03011234581', NULL),
(16, 'Mia', 'Martinez', 'mia.martinez@hospital.com', 'DocPass16', 'Cardiologist', 'Female', '03011234582', NULL),
(17, 'Alexander', 'Robinson', 'alexander.robinson@hospital.com', 'DocPass17', 'Neurologist', 'Male', '03011234583', NULL),
(18, 'Evelyn', 'Clark', 'evelyn.clark@hospital.com', 'DocPass18', 'Orthopedic', 'Female', '03011234584', NULL),
(19, 'Benjamin', 'Rodriguez', 'benjamin.rodriguez@hospital.com', 'DocPass19', 'Pediatrician', 'Male', '03011234585', NULL),
(20, 'Harper', 'Lewis', 'harper.lewis@hospital.com', 'DocPass20', 'General', 'Female', '03011234586', NULL),
(21, 'Elijah', 'Walker', 'elijah.walker@hospital.com', 'DocPass21', 'Cardiologist', 'Male', '03011234587', NULL),
(22, 'Liam', 'Hall', 'liam.hall@hospital.com', 'DocPass22', 'Neurologist', 'Female', '03011234588', NULL),
(23, 'Noah', 'Young', 'noah.young@hospital.com', 'DocPass23', 'Orthopedic', 'Male', '03011234589', NULL),
(24, 'Ava', 'King', 'ava.king@hospital.com', 'DocPass24', 'Pediatrician', 'Female', '03011234590', NULL),
(25, 'Logan', 'Wright', 'logan.wright@hospital.com', 'DocPass25', 'General', 'Male', '03011234591', NULL),
(26, 'Ella', 'Scott', 'ella.scott@hospital.com', 'DocPass26', 'Cardiologist', 'Female', '03011234592', NULL),
(27, 'Oliver', 'Torres', 'oliver.torres@hospital.com', 'DocPass27', 'Neurologist', 'Male', '03011234593', NULL),
(28, 'Lily', 'Nguyen', 'lily.nguyen@hospital.com', 'DocPass28', 'Orthopedic', 'Female', '03011234594', NULL),
(29, 'Jackson', 'Hill', 'jackson.hill@hospital.com', 'DocPass29', 'Pediatrician', 'Male', '03011234595', NULL),
(30, 'Grace', 'Moore', 'grace.moore@hospital.com', 'DocPass30', 'General', 'Female', '03011234596', NULL),
(31, 'Samuel', 'Adams', 'samuel.adams@hospital.com', 'DocPass31', 'Cardiologist', 'Male', '03011234597', NULL),
(32, 'Hannah', 'Perez', 'hannah.perez@hospital.com', 'DocPass32', 'Neurologist', 'Female', '03011234598', NULL),
(33, 'Ethan', 'Collins', 'ethan.collins@hospital.com', 'DocPass33', 'Orthopedic', 'Male', '03011234599', NULL),
(34, 'Sofia', 'Stewart', 'sofia.stewart@hospital.com', 'DocPass34', 'Pediatrician', 'Female', '03011234600', NULL),
(35, 'Lucas', 'Carter', 'lucas.carter@hospital.com', 'DocPass35', 'General', 'Male', '03011234601', NULL),
(36, 'Scarlett', 'Phillips', 'scarlett.phillips@hospital.com', 'DocPass36', 'Cardiologist', 'Female', '03011234602', NULL),
(37, 'Jack', 'Evans', 'jack.evans@hospital.com', 'DocPass37', 'Neurologist', 'Male', '03011234603', NULL),
(38, 'Layla', 'Mitchell', 'layla.mitchell@hospital.com', 'DocPass38', 'Orthopedic', 'Female', '03011234604', NULL),
(39, 'Nathan', 'Taylor', 'nathan.taylor@hospital.com', 'DocPass39', 'Pediatrician', 'Male', '03011234605', NULL),
(40, 'Aria', 'Baker', 'aria.baker@hospital.com', 'DocPass40', 'General', 'Female', '03011234606', NULL),
(41, 'Matthew', 'Allen', 'matthew.allen@hospital.com', 'DocPass41', 'Cardiologist', 'Male', '03011234607', NULL),
(42, 'Zoe', 'Nelson', 'zoe.nelson@hospital.com', 'DocPass42', 'Neurologist', 'Female', '03011234608', NULL),
(43, 'Andrew', 'Parker', 'andrew.parker@hospital.com', 'DocPass43', 'Orthopedic', 'Male', '03011234609', NULL),
(44, 'Chloe', 'Hughes', 'chloe.hughes@hospital.com', 'DocPass44', 'Pediatrician', 'Female', '03011234610', NULL),
(45, 'Ryan', 'Flores', 'ryan.flores@hospital.com', 'DocPass45', 'General', 'Male', '03011234611', NULL),
(46, 'Victoria', 'Green', 'victoria.green@hospital.com', 'DocPass46', 'Cardiologist', 'Female', '03011234612', NULL),
(47, 'Jacob', 'Sanders', 'jacob.sanders@hospital.com', 'DocPass47', 'Neurologist', 'Male', '03011234613', NULL),
(48, 'Ellie', 'Peterson', 'ellie.peterson@hospital.com', 'DocPass48', 'Orthopedic', 'Female', '03011234614', NULL),
(49, 'Dylan', 'Murphy', 'dylan.murphy@hospital.com', 'DocPass49', 'Pediatrician', 'Male', '03011234615', NULL),
(50, 'Abigail', 'Cooper', 'abigail.cooper@hospital.com', 'DocPass50', 'General', 'Female', '03011234616', NULL);


-- Insert data into the Admin table
INSERT INTO Admin (admin_Fname, admin_Lname, email, password, phone_no)
VALUES
('Alice', 'Johnson', 'alice.j@example.com', 'password123', '555-1001'),
('John', 'Smith', 'john.smith@example.com', 'securepass1', '555-1002'),
('Emma', 'Davis', 'emma.d@example.com', 'emma789', '555-1003'),
('Michael', 'Brown', 'michael.brown@example.com', 'mikePass!', '555-1004'),
('Olivia', 'Wilson', 'olivia.wilson@example.com', 'livpass99', '555-1005'),
('William', 'Martinez', 'william.m@example.com', 'willpass45', '555-1006'),
('Sophia', 'Anderson', 'sophia.and@example.com', 'sophiasecure', '555-1007'),
('James', 'Garcia', 'james.g@example.com', 'passJames!', '555-1008'),
('Mia', 'Lee', 'mia.lee@example.com', 'passwordMia', '555-1009'),
('Benjamin', 'Hall', 'ben.hall@example.com', 'benny456', '555-1010'),
('Charlotte', 'Clark', 'charlotte.c@example.com', 'charlott3', '555-1011'),
('Lucas', 'Lewis', 'lucas.lewis@example.com', 'luc4spass', '555-1012'),
('Amelia', 'Walker', 'amelia.walker@example.com', 'ameliaPass', '555-1013'),
('Alexander', 'Young', 'alex.young@example.com', 'alex2020', '555-1014'),
('Isabella', 'King', 'isabella.king@example.com', 'isaPass!', '555-1015'),
('Daniel', 'Wright', 'daniel.wright@example.com', 'dan456pass', '555-1016'),
('Sofia', 'Hill', 'sofia.h@example.com', 'secureSofia', '555-1017'),
('Henry', 'Scott', 'henry.scott@example.com', 'hpass789', '555-1018'),
('Grace', 'Green', 'grace.green@example.com', 'gracePass1', '555-1019'),
('Ethan', 'Adams', 'ethan.adams@example.com', 'ethan@123', '555-1020'),
('Ella', 'Baker', 'ella.baker@example.com', 'ella#2023', '555-1021'),
('Jack', 'Gonzalez', 'jack.g@example.com', 'jack_pass', '555-1022'),
('Lily', 'Nelson', 'lily.nelson@example.com', 'lily@pass', '555-1023'),
('David', 'Carter', 'david.carter@example.com', 'david456', '555-1024'),
('Ava', 'Perez', 'ava.perez@example.com', 'ava1234', '555-1025'),
('Matthew', 'Roberts', 'matt.roberts@example.com', 'mattpass!', '555-1026'),
('Emily', 'Evans', 'emily.evans@example.com', 'emilySecure', '555-1027'),
('Oliver', 'Turner', 'oliver.t@example.com', 'oliv_pass', '555-1028'),
('Zoe', 'Parker', 'zoe.parker@example.com', 'zoe@789', '555-1029'),
('Sebastian', 'Campbell', 'seb.campbell@example.com', 'seb_secure', '555-1030'),
('Liam', 'Richardson', 'liam.richardson@example.com', 'liam_secure123', '555-1031'),
('Chloe', 'Murphy', 'chloe.murphy@example.com', 'chloeP@ssword', '555-1032'),
('Mason', 'Collins', 'mason.collins@example.com', 'mas0npass!', '555-1033'),
('Harper', 'Ramirez', 'harper.ramirez@example.com', 'harper_pass', '555-1034'),
('Elijah', 'Ward', 'elijah.ward@example.com', 'wardelijah45', '555-1035'),
('Ella', 'Rodriguez', 'ella.rod@example.com', 'ellaSecure89', '555-1036'),
('Jackson', 'Barnes', 'jackson.b@example.com', 'jack_pass22', '555-1037'),
('Avery', 'Powell', 'avery.powell@example.com', 'avery!pass', '555-1038'),
('Noah', 'Watson', 'noah.watson@example.com', 'noahSecure', '555-1039'),
('Layla', 'Price', 'layla.price@example.com', 'layl@pass99', '555-1040'),
('Leo', 'Howard', 'leo.howard@example.com', 'leo_pass45', '555-1041'),
('Samantha', 'Ross', 'sam.ross@example.com', 'sam_secure', '555-1042'),
('Nathan', 'Bell', 'nathan.bell@example.com', 'nathan123', '555-1043'),
('Hannah', 'Peterson', 'hannah.peterson@example.com', 'hannah!pass', '555-1044'),
('Dylan', 'Butler', 'dylan.butler@example.com', 'dylanP@ss!', '555-1045'),
('Lily', 'Foster', 'lily.foster@example.com', 'lily12345', '555-1046'),
('Jacob', 'Bailey', 'jacob.bailey@example.com', 'jakeSecure', '555-1047'),
('Scarlett', 'Hughes', 'scarlett.h@example.com', 'scarlett45', '555-1048'),
('Gabriel', 'Cooper', 'gabriel.cooper@example.com', 'gab_cooper', '555-1049'),
('Zoey', 'Morgan', 'zoey.morgan@example.com', 'zoeyPass', '555-1050');

-- Insert data into the Medical Supply table
INSERT INTO MedicalSupply (medsupply_name)
VALUES 
('Paracetamol'), 
('Ibuprofen'), 
('Syringe'), 
('Bandage'), 
('Medical Tape'), 
('Gauze'), 
('Antiseptic Wipes'), 
('Thermometer'), 
('Stethoscope'), 
('Blood Pressure Monitor'), 
('Face Mask'), 
('Gloves'), 
('Scalpel'), 
('Surgical Mask'), 
('IV Fluid'), 
('Needle'), 
('Cotton Balls'), 
('Oxygen Mask'), 
('Alcohol Swabs'), 
('Surgical Gown'), 
('Tongue Depressor'), 
('Splint'), 
('Inhaler'), 
('Nebulizer'), 
('Penicillin'), 
('Aspirin'), 
('Hand Sanitizer'), 
('Vaccine'), 
('Medical Scissors'), 
('Elastic Bandage'), 
('Hot Water Bag'), 
('Cold Pack'), 
('Blood Collection Tubes'), 
('Catheter'), 
('Saline Solution'), 
('ECG Machine'), 
('Glucometer'), 
('Pulse Oximeter'), 
('Urine Bag'), 
('Defibrillator');

INSERT INTO MedicalSupply (medsupply_name)
VALUES 
('Blood Glucose Test Strips'), 
('Surgical Blade'), 
('IV Stand'), 
('Eye Drops'), 
('Digital Thermometer'), 
('EKG Electrodes'), 
('Surgical Tape'), 
('Anesthesia Mask'), 
('Protective Gown'), 
('Sterile Gloves');


-- Insert data into the medical supply desccription

INSERT INTO MedicalSupplyDescription (medsupply_id, description, medsupply_type, unit_price)
VALUES
(1, 'Analgesic and antipyretic tablet', 'Medication', 0.50),
(2, 'Non-steroidal anti-inflammatory drug (NSAID)', 'Medication', 0.75),
(3, 'Disposable syringe for injections', 'Equipment', 0.25),
(4, 'Elastic bandage for wound dressing', 'First Aid', 1.20),
(5, 'Adhesive medical tape for dressings', 'First Aid', 0.60),
(6, 'Sterile gauze for wound care', 'First Aid', 1.50),
(7, 'Disposable antiseptic wipes', 'Hygiene', 0.10),
(8, 'Digital thermometer for temperature readings', 'Diagnostic', 15.00),
(9, 'Acoustic device for listening to internal sounds', 'Diagnostic', 30.00),
(10, 'Electronic blood pressure monitor', 'Diagnostic', 45.00),
(11, 'Disposable face mask for protection', 'Protective Gear', 0.20),
(12, 'Sterile examination gloves', 'Protective Gear', 0.50),
(13, 'Sterile scalpel for surgical use', 'Surgical Instrument', 2.00),
(14, 'High-filtration surgical mask', 'Protective Gear', 0.30),
(15, 'Intravenous fluid for patient hydration', 'Medication', 5.00),
(16, 'Disposable needle for injections', 'Equipment', 0.15),
(17, 'Sterile cotton balls for cleaning', 'Hygiene', 0.05),
(18, 'Oxygen mask for patient ventilation', 'Equipment', 1.50),
(19, 'Sterile alcohol swabs for disinfection', 'Hygiene', 0.10),
(20, 'Disposable surgical gown', 'Protective Gear', 3.50),
(21, 'Wooden tongue depressor', 'Equipment', 0.10),
(22, 'Adjustable splint for fractures', 'First Aid', 5.00),
(23, 'Inhaler for respiratory relief', 'Medication', 10.00),
(24, 'Portable nebulizer for aerosol treatments', 'Equipment', 35.00),
(25, 'Antibiotic injection', 'Medication', 3.00),
(26, 'Pain relief aspirin tablets', 'Medication', 0.20),
(27, 'Hand sanitizer for hygiene', 'Hygiene', 2.00),
(28, 'Immunization vaccine', 'Medication', 20.00),
(29, 'Stainless steel medical scissors', 'Surgical Instrument', 2.50),
(30, 'Elastic bandage for sprains', 'First Aid', 1.50),
(31, 'Hot water bag for pain relief', 'Equipment', 3.00),
(32, 'Instant cold pack for injury treatment', 'First Aid', 1.00),
(33, 'Blood collection tubes', 'Laboratory Equipment', 0.30),
(34, 'Flexible urinary catheter', 'Equipment', 5.50),
(35, 'Saline solution for dehydration treatment', 'Medication', 2.00),
(36, 'Portable ECG machine', 'Diagnostic', 150.00),
(37, 'Blood glucose meter', 'Diagnostic', 20.00),
(38, 'Finger pulse oximeter', 'Diagnostic', 25.00),
(39, 'Urine drainage bag', 'Equipment', 1.00),
(40, 'Automated external defibrillator', 'Emergency Equipment', 250.00),
(41, 'Test strips for blood glucose meters', 'Diagnostic', 0.50),
(42, 'Disposable surgical blade', 'Surgical Instrument', 1.00),
(43, 'IV stand for fluid bags', 'Equipment', 10.00),
(44, 'Sterile eye drops', 'Medication', 5.00),
(45, 'Waterproof digital thermometer', 'Diagnostic', 20.00),
(46, 'EKG electrodes for heart monitoring', 'Diagnostic', 0.75),
(47, 'Surgical adhesive tape', 'First Aid', 0.25),
(48, 'Disposable anesthesia mask', 'Equipment', 2.00),
(49, 'Protective gown for patient use', 'Protective Gear', 4.00),
(50, 'Sterile nitrile gloves', 'Protective Gear', 0.75);


INSERT INTO MedicalSupplyCatalogue (description_id)
VALUES 
(1), 
(2), 
(3), 
(4), 
(5), 
(6), 
(7), 
(8), 
(9), 
(10), 
(11), 
(12), 
(13), 
(14), 
(15), 
(16), 
(17), 
(18), 
(19), 
(20), 
(21), 
(22), 
(23), 
(24), 
(25), 
(26), 
(27), 
(28), 
(29), 
(30), 
(31), 
(32), 
(33), 
(34), 
(35), 
(36), 
(37), 
(38), 
(39), 
(40), 
(41), 
(42), 
(43), 
(44), 
(45), 
(46), 
(47), 
(48), 
(49), 
(50);

INSERT INTO StockItem (medsupply_id, item_name, quantity_available, threshold, unit_price)
VALUES 
(1, 'Paracetamol Tablets', 500, 100, 0.50),
(2, 'Ibuprofen Tablets', 400, 100, 0.75),
(3, 'Disposable Syringes', 1000, 200, 0.25),
(4, 'Elastic Bandages', 300, 50, 1.20),
(5, 'Medical Adhesive Tape', 200, 30, 0.60),
(6, 'Sterile Gauze Pads', 500, 100, 1.50),
(7, 'Antiseptic Wipes', 1000, 200, 0.10),
(8, 'Digital Thermometer', 50, 10, 15.00),
(9, 'Stethoscope', 30, 5, 30.00),
(10, 'Blood Pressure Monitor', 20, 5, 45.00),
(11, 'Face Mask', 2000, 500, 0.20),
(12, 'Examination Gloves', 1500, 300, 0.50),
(13, 'Sterile Scalpel', 100, 20, 2.00),
(14, 'Surgical Mask', 1800, 400, 0.30),
(15, 'IV Fluid Bags', 100, 10, 5.00),
(16, 'Injection Needles', 2000, 500, 0.15),
(17, 'Cotton Balls', 1200, 300, 0.05),
(18, 'Oxygen Mask', 150, 20, 1.50),
(19, 'Alcohol Swabs', 2500, 500, 0.10),
(20, 'Disposable Gowns', 300, 50, 3.50),
(21, 'Tongue Depressors', 1000, 200, 0.10),
(22, 'Adjustable Splint', 80, 10, 5.00),
(23, 'Respiratory Inhaler', 60, 15, 10.00),
(24, 'Nebulizer Machine', 40, 5, 35.00),
(25, 'Antibiotic Injection', 150, 30, 3.00),
(26, 'Aspirin Tablets', 500, 100, 0.20),
(27, 'Hand Sanitizer', 300, 50, 2.00),
(28, 'Immunization Vaccine', 60, 10, 20.00),
(29, 'Medical Scissors', 100, 20, 2.50),
(30, 'Elastic Bandage Rolls', 300, 50, 1.50),
(31, 'Hot Water Bag', 100, 10, 3.00),
(32, 'Instant Cold Pack', 150, 20, 1.00),
(33, 'Blood Collection Tubes', 1000, 200, 0.30),
(34, 'Urinary Catheter', 80, 10, 5.50),
(35, 'Saline Solution', 300, 50, 2.00),
(36, 'Portable ECG Machine', 10, 3, 150.00),
(37, 'Blood Glucose Meter', 25, 5, 20.00),
(38, 'Finger Pulse Oximeter', 30, 5, 25.00),
(39, 'Urine Drainage Bag', 200, 50, 1.00),
(40, 'Defibrillator', 5, 1, 250.00),
(41, 'Glucose Test Strips', 1000, 200, 0.50),
(42, 'Surgical Blade', 150, 20, 1.00),
(43, 'IV Stand', 20, 3, 10.00),
(44, 'Eye Drops', 200, 50, 5.00),
(45, 'Waterproof Thermometer', 60, 10, 20.00),
(46, 'EKG Electrodes', 500, 100, 0.75),
(47, 'Medical Adhesive Tape', 300, 50, 0.25),
(48, 'Anesthesia Mask', 50, 10, 2.00),
(49, 'Protective Gown', 300, 50, 4.00),
(50, 'Sterile Gloves', 1500, 300, 0.75);


INSERT INTO MedicalSupplyRequest (item_id, admin_id, quantity_requested, request_date, status)
VALUES 
(1, 1, 50, '2024-11-01', 'Pending'),
(2, 2, 30, '2024-11-02', 'Received'),
(3, 3, 100, '2024-11-03', 'Pending'),
(4, 4, 75, '2024-11-04', 'Received'),
(5, 5, 200, '2024-11-05', 'Pending'),
(6, 6, 150, '2024-11-06', 'Received'),
(7, 7, 200, '2024-11-07', 'Received'),
(8, 8, 20, '2024-11-08', 'Pending'),
(9, 9, 15, '2024-11-09', 'Received'),
(10, 10, 10, '2024-11-10', 'Received'),
(11, 11, 500, '2024-11-11', 'Pending'),
(12, 12, 300, '2024-11-12', 'Received'),
(13, 13, 25, '2024-11-13', 'Pending'),
(14, 14, 40, '2024-11-14', 'Received'),
(15, 15, 100, '2024-11-15', 'Received'),
(16, 16, 150, '2024-11-16', 'Pending'),
(17, 17, 100, '2024-11-17', 'Received'),
(18, 18, 50, '2024-11-18', 'Received'),
(19, 19, 300, '2024-11-19', 'Pending'),
(20, 20, 200, '2024-11-20', 'Received'),
(21, 21, 150, '2024-11-21', 'Pending'),
(22, 22, 80, '2024-11-22', 'Received'),
(23, 23, 60, '2024-11-23', 'Received'),
(24, 24, 100, '2024-11-24', 'Pending'),
(25, 25, 50, '2024-11-25', 'Received'),
(26, 26, 200, '2024-11-26', 'Received'),
(27, 27, 250, '2024-11-27', 'Pending'),
(28, 28, 100, '2024-11-28', 'Received'),
(29, 29, 75, '2024-11-29', 'Received'),
(30, 30, 100, '2024-11-30', 'Pending'),
(31, 31, 50, '2024-12-01', 'Received'),
(32, 32, 20, '2024-12-02', 'Received'),
(33, 33, 30, '2024-12-03', 'Pending'),
(34, 34, 100, '2024-12-04', 'Received'),
(35, 35, 60, '2024-12-05', 'Received'),
(36, 36, 10, '2024-12-06', 'Pending'),
(37, 37, 150, '2024-12-07', 'Received'),
(38, 38, 20, '2024-12-08', 'Received'),
(39, 39, 40, '2024-12-09', 'Pending'),
(40, 40, 80, '2024-12-10', 'Received'),
(41, 41, 250, '2024-12-11', 'Received'),
(42, 42, 300, '2024-12-12', 'Pending'),
(43, 43, 100, '2024-12-13', 'Received'),
(44, 44, 60, '2024-12-14', 'Received'),
(45, 45, 50, '2024-12-15', 'Received'),
(46, 46, 100, '2024-12-16', 'Pending'),
(47, 47, 75, '2024-12-17', 'Received'),
(48, 48, 40, '2024-12-18', 'Pending'),
(49, 49, 200, '2024-12-19', 'Received'),
(50, 50, 500, '2024-12-20', 'Received');

-- Insert values In  Good in 
INSERT INTO GoodIn (request_id, item_id, quantity, purchase_price, received_date)
VALUES 
(1, 1, 50, 10.00, '2024-11-01'),
(2, 2, 40, 15.00, '2024-11-02'),
(3, 3, 60, 5.00, '2024-11-03'),
(4, 4, 30, 20.00, '2024-11-04'),
(5, 5, 45, 8.00, '2024-11-05'),
(6, 6, 25, 18.00, '2024-11-06'),
(7, 7, 55, 12.00, '2024-11-07'),
(8, 8, 50, 9.00, '2024-11-08'),
(9, 9, 40, 16.00, '2024-11-09'),
(10, 10, 60, 7.00, '2024-11-10'),
(11, 11, 30, 22.00, '2024-11-11'),
(12, 12, 50, 10.00, '2024-11-12'),
(13, 13, 40, 14.00, '2024-11-13'),
(14, 14, 45, 11.00, '2024-11-14'),
(15, 15, 60, 6.00, '2024-11-15'),
(16, 16, 50, 13.00, '2024-11-16'),
(17, 17, 30, 19.00, '2024-11-17'),
(18, 18, 55, 9.50, '2024-11-18'),
(19, 19, 40, 17.00, '2024-11-19'),
(20, 20, 25, 21.00, '2024-11-20'),
(21, 21, 60, 8.50, '2024-11-21'),
(22, 22, 50, 14.50, '2024-11-22'),
(23, 23, 40, 16.50, '2024-11-23'),
(24, 24, 45, 10.50, '2024-11-24'),
(25, 25, 60, 12.50, '2024-11-25'),
(26, 26, 50, 9.20, '2024-11-26'),
(27, 27, 40, 15.80, '2024-11-27'),
(28, 28, 45, 11.20, '2024-11-28'),
(29, 29, 60, 7.30, '2024-11-29'),
(30, 30, 50, 14.80, '2024-11-30'),
(31, 31, 30, 18.50, '2024-12-01'),
(32, 32, 55, 8.00, '2024-12-02'),
(33, 33, 40, 19.50, '2024-12-03'),
(34, 34, 60, 7.90, '2024-12-04'),
(35, 35, 50, 10.60, '2024-12-05'),
(36, 36, 30, 22.00, '2024-12-06'),
(37, 37, 50, 9.30, '2024-12-07'),
(38, 38, 40, 13.20, '2024-12-08'),
(39, 39, 45, 12.80, '2024-12-09'),
(40, 40, 60, 6.50, '2024-12-10'),
(41, 41, 50, 16.70, '2024-12-11'),
(42, 42, 30, 18.30, '2024-12-12'),
(43, 43, 55, 9.10, '2024-12-13'),
(44, 44, 40, 14.10, '2024-12-14'),
(45, 45, 60, 5.80, '2024-12-15'),
(46, 46, 50, 8.40, '2024-12-16'),
(47, 47, 30, 20.10, '2024-12-17'),
(48, 48, 55, 11.50, '2024-12-18'),
(49, 49, 40, 17.60, '2024-12-19'),
(50, 50, 60, 6.20, '2024-12-20');


-- Insert values In  Good Out 

INSERT INTO GoodOut (item_id, quantity, sale_price, issued_date)
VALUES 
(1, 30, 15.00, '2024-11-02'),
(2, 25, 18.00, '2024-11-03'),
(3, 40, 8.00, '2024-11-04'),
(4, 35, 22.00, '2024-11-05'),
(5, 50, 12.00, '2024-11-06'),
(6, 30, 20.00, '2024-11-07'),
(7, 45, 16.00, '2024-11-08'),
(8, 50, 10.00, '2024-11-09'),
(9, 40, 19.00, '2024-11-10'),
(10, 60, 7.00, '2024-11-11'),
(11, 30, 23.00, '2024-11-12'),
(12, 50, 13.00, '2024-11-13'),
(13, 40, 14.00, '2024-11-14'),
(14, 45, 18.00, '2024-11-15'),
(15, 60, 6.00, '2024-11-16'),
(16, 50, 17.00, '2024-11-17'),
(17, 30, 20.00, '2024-11-18'),
(18, 55, 9.50, '2024-11-19'),
(19, 40, 21.00, '2024-11-20'),
(20, 25, 22.00, '2024-11-21'),
(21, 60, 8.00, '2024-11-22'),
(22, 50, 15.00, '2024-11-23'),
(23, 40, 16.50, '2024-11-24'),
(24, 45, 11.00, '2024-11-25'),
(25, 60, 14.00, '2024-11-26'),
(26, 50, 9.20, '2024-11-27'),
(27, 40, 15.80, '2024-11-28'),
(28, 45, 12.00, '2024-11-29'),
(29, 60, 7.00, '2024-11-30'),
(30, 50, 14.80, '2024-12-01'),
(31, 30, 18.50, '2024-12-02'),
(32, 55, 8.50, '2024-12-03'),
(33, 40, 19.50, '2024-12-04'),
(34, 60, 7.90, '2024-12-05'),
(35, 50, 10.60, '2024-12-06'),
(36, 30, 21.00, '2024-12-07'),
(37, 50, 9.30, '2024-12-08'),
(38, 40, 13.20, '2024-12-09'),
(39, 45, 12.80, '2024-12-10'),
(40, 60, 6.50, '2024-12-11'),
(41, 50, 17.00, '2024-12-12'),
(42, 30, 18.30, '2024-12-13'),
(43, 55, 9.10, '2024-12-14'),
(44, 40, 14.10, '2024-12-15'),
(45, 60, 5.80, '2024-12-16'),
(46, 50, 8.40, '2024-12-17'),
(47, 30, 20.10, '2024-12-18'),
(48, 55, 11.50, '2024-12-19'),
(49, 40, 17.60, '2024-12-20'),
(50, 60, 6.20, '2024-12-21');

-- Workshop table
INSERT INTO Workshop (admin_id, workshop_name, speaker, description, workshop_date, start_time, end_time, location) VALUES
(1, 'Heart Health', 'Dr. John', 'Insights on heart health', '2024-12-01', '09:00:00', '12:00:00', 'Auditorium'),
(2, 'Brain Awareness', 'Dr. Jane', 'Understanding neurology', '2024-12-02', '10:00:00', '13:00:00', 'Conference Hall'),
(3, 'Bone Care', 'Dr. Robert', 'Advances in orthopedics', '2024-12-03', '14:00:00', '17:00:00', 'Room A'),
(4, 'Skin Safety', 'Dr. Emily', 'Dermatology tips', '2024-12-04', '15:00:00', '18:00:00', 'Room B'),
(5, 'Pediatrics Today', 'Dr. Michael', 'Focus on child health', '2024-12-05', '16:00:00', '19:00:00', 'Main Hall');


-- Patient table

INSERT INTO Patient (patient_id, first_name, last_name, gender, address, phone_no, email, _password, medical_details, image) VALUES
(1, 'Ethan', 'Williams', 'Male', '123 Maple Street', '03211234567', 'ethan.williams@email.com', 'Pass1', 'Diabetes Type 2', NULL),
(2, 'Sophia', 'Johnson', 'Female', '456 Elm Avenue', '03211234568', 'sophia.johnson@email.com', 'Pass2', 'Asthma', NULL),
(3, 'Michael', 'Brown', 'Male', '789 Oak Lane', '03211234569', 'michael.brown@email.com', 'Pass3', 'Hypertension', NULL),
(4, 'Emma', 'Davis', 'Female', '321 Pine Drive', '03211234570', 'emma.davis@email.com', 'Pass4', 'Arthritis', NULL),
(5, 'James', 'Miller', 'Male', '654 Cedar Road', '03211234571', 'james.miller@email.com', 'Pass5', 'Migraine', NULL),
(6, 'Isabella', 'Taylor', 'Female', '987 Spruce Street', '03211234572', 'isabella.taylor@email.com', 'Pass6', 'Chronic Pain', NULL),
(7, 'William', 'Anderson', 'Male', '159 Birch Boulevard', '03211234573', 'william.anderson@email.com', 'Pass7', 'Heart Disease', NULL),
(8, 'Olivia', 'Moore', 'Female', '753 Walnut Way', '03211234574', 'olivia.moore@email.com', 'Pass8', 'Allergy', NULL),
(9, 'Benjamin', 'Jackson', 'Male', '951 Chestnut Court', '03211234575', 'benjamin.jackson@email.com', 'Pass9', 'Anemia', NULL),
(10, 'Ava', 'Martinez', 'Female', '357 Willow Terrace', '03211234576', 'ava.martinez@email.com', 'Pass10', 'Acid Reflux', NULL),
(11, 'Henry', 'White', 'Male', '753 Birchwood Lane', '03211234577', 'henry.white@email.com', 'Pass11', 'Cholesterol', NULL),
(12, 'Charlotte', 'Harris', 'Female', '246 Redwood Avenue', '03211234578', 'charlotte.harris@email.com', 'Pass12', 'Allergy', NULL),
(13, 'Alexander', 'Clark', 'Male', '135 Maple Grove', '03211234579', 'alexander.clark@email.com', 'Pass13', 'Eczema', NULL),
(14, 'Amelia', 'Lewis', 'Female', '864 Oakwood Path', '03211234580', 'amelia.lewis@email.com', 'Pass14', 'Depression', NULL),
(15, 'Lucas', 'Walker', 'Male', '678 Magnolia Circle', '03211234581', 'lucas.walker@email.com', 'Pass15', 'Anxiety', NULL),
(16, 'Mia', 'Hall', 'Female', '453 Birchwood Lane', '03211234582', 'mia.hall@email.com', 'Pass16', 'Obesity', NULL),
(17, 'Noah', 'Allen', 'Male', '369 Spruce Trail', '03211234583', 'noah.allen@email.com', 'Pass17', 'Sleep Apnea', NULL),
(18, 'Layla', 'Young', 'Female', '975 Poplar Terrace', '03211234584', 'layla.young@email.com', 'Pass18', 'Migraines', NULL),
(19, 'Liam', 'King', 'Male', '537 Cypress Drive', '03211234585', 'liam.king@email.com', 'Pass19', 'Acne', NULL),
(20, 'Zoe', 'Wright', 'Female', '791 Cedar Lane', '03211234586', 'zoe.wright@email.com', 'Pass20', 'Asthma', NULL),
(21, 'Daniel', 'Scott', 'Male', '687 Redwood Trail', '03211234587', 'daniel.scott@email.com', 'Pass21', 'Allergy', NULL),
(22, 'Hannah', 'Torres', 'Female', '479 Magnolia Way', '03211234588', 'hannah.torres@email.com', 'Pass22', 'Diabetes', NULL),
(23, 'Matthew', 'Nguyen', 'Male', '215 Maple Avenue', '03211234589', 'matthew.nguyen@email.com', 'Pass23', 'Hypertension', NULL),
(24, 'Ella', 'Hill', 'Female', '657 Elmwood Grove', '03211234590', 'ella.hill@email.com', 'Pass24', 'Eczema', NULL),
(25, 'Jack', 'Carter', 'Male', '871 Pine Circle', '03211234591', 'jack.carter@email.com', 'Pass25', 'Anemia', NULL),
(26, 'Scarlett', 'Mitchell', 'Female', '348 Willow Avenue', '03211234592', 'scarlett.mitchell@email.com', 'Pass26', 'Chronic Pain', NULL),
(27, 'Nathan', 'Perez', 'Male', '432 Birchwood Avenue', '03211234593', 'nathan.perez@email.com', 'Pass27', 'Migraine', NULL),
(28, 'Aria', 'Robinson', 'Female', '784 Elmwood Circle', '03211234594', 'aria.robinson@email.com', 'Pass28', 'Anxiety', NULL),
(29, 'Ethan', 'Murphy', 'Male', '692 Cedarwood Terrace', '03211234595', 'ethan.murphy@email.com', 'Pass29', 'Depression', NULL),
(30, 'Victoria', 'Rivera', 'Female', '248 Oak Grove', '03211234596', 'victoria.rivera@email.com', 'Pass30', 'Asthma', NULL),
(31, 'Andrew', 'Gray', 'Male', '468 Walnut Avenue', '03211234597', 'andrew.gray@email.com', 'Pass31', 'Cholesterol', NULL),
(32, 'Grace', 'Long', 'Female', '195 Cypress Terrace', '03211234598', 'grace.long@email.com', 'Pass32', 'Obesity', NULL),
(33, 'Ryan', 'Foster', 'Male', '831 Maplewood Drive', '03211234599', 'ryan.foster@email.com', 'Pass33', 'Heart Disease', NULL),
(34, 'Harper', 'Sanders', 'Female', '573 Willow Way', '03211234600', 'harper.sanders@email.com', 'Pass34', 'Arthritis', NULL),
(35, 'Joseph', 'Brooks', 'Male', '465 Elmwood Grove', '03211234601', 'joseph.brooks@email.com', 'Pass35', 'Allergy', NULL),
(36, 'Abigail', 'Price', 'Female', '257 Cedarwood Avenue', '03211234602', 'abigail.price@email.com', 'Pass36', 'Acid Reflux', NULL),
(37, 'Samuel', 'Jenkins', 'Male', '892 Poplar Lane', '03211234603', 'samuel.jenkins@email.com', 'Pass37', 'Diabetes', NULL),
(38, 'Sofia', 'Bennett', 'Female', '183 Spruce Terrace', '03211234604', 'sofia.bennett@email.com', 'Pass38', 'Eczema', NULL),
(39, 'Caleb', 'Cox', 'Male', '396 Maple Avenue', '03211234605', 'caleb.cox@email.com', 'Pass39', 'Hypertension', NULL),
(40, 'Lily', 'Richardson', 'Female', '593 Elmwood Lane', '03211234606', 'lily.richardson@email.com', 'Pass40', 'Anemia', NULL),
(41, 'Owen', 'Howard', 'Male', '218 Cedar Grove', '03211234607', 'owen.howard@email.com', 'Pass41', 'Chronic Pain', NULL),
(42, 'Chloe', 'Ward', 'Female', '462 Birchwood Drive', '03211234608', 'chloe.ward@email.com', 'Pass42', 'Asthma', NULL),
(43, 'Jonathan', 'Reed', 'Male', '348 Oakwood Avenue', '03211234609', 'jonathan.reed@email.com', 'Pass43', 'Acid Reflux', NULL),
(44, 'Lillian', 'Bell', 'Female', '174 Walnut Grove', '03211234610', 'lillian.bell@email.com', 'Pass44', 'Cholesterol', NULL),
(45, 'Connor', 'Rogers', 'Male', '579 Maplewood Terrace', '03211234611', 'connor.rogers@email.com', 'Pass45', 'Depression', NULL),
(46, 'Aubrey', 'James', 'Female', '283 Elm Avenue', '03211234612', 'aubrey.james@email.com', 'Pass46', 'Heart Disease', NULL),
(47, 'Levi', 'Watson', 'Male', '462 Spruce Drive', '03211234613', 'levi.watson@email.com', 'Pass47', 'Sleep Apnea', NULL),
(48, 'Zoey', 'Russell', 'Female', '195 Poplar Circle', '03211234614', 'zoey.russell@email.com', 'Pass48', 'Migraines', NULL),
(49, 'Elijah', 'Griffin', 'Male', '582 Willow Lane', '03211234615', 'elijah.griffin@email.com', 'Pass49', 'Asthma', NULL),
(50, 'Nora', 'Hayes', 'Female', '196 Oak Grove', '03211234616', 'nora.hayes@email.com', 'Pass50', 'Diabetes', NULL);

-- Room table
INSERT INTO Room (room_id, patient_id, room_type, price, availability_status)
VALUES
(1, NULL, 'Single', 3000.00, 'Available'),
(3, NULL, 'ICU', 5000.00, 'Available'),
(5, NULL, 'Double', 4500.00, 'Available'),
(7, NULL, 'Single', 3100.00, 'Available'),
(9, NULL, 'ICU', 5100.00, 'Available'),
(11, NULL, 'Double', 4100.00, 'Available'),
(13, NULL, 'Single', 3050.00, 'Available'),
(15, NULL, 'ICU', 5050.00, 'Available'),
(17, NULL, 'Double', 4150.00, 'Available'),
(19, NULL, 'Single', 3150.00, 'Available'),
(21, NULL, 'ICU', 5150.00, 'Available'),
(23, NULL, 'Double', 4200.00, 'Available'),
(25, NULL, 'Single', 3075.00, 'Available'),
(27, NULL, 'ICU', 5075.00, 'Available'),
(29, NULL, 'Double', 4175.00, 'Available'),
(31, NULL, 'Single', 3175.00, 'Available'),
(33, NULL, 'ICU', 5175.00, 'Available'),
(35, NULL, 'Double', 4250.00, 'Available'),
(37, NULL, 'Single', 3080.00, 'Available'),
(39, NULL, 'ICU', 5080.00, 'Available'),
(41, NULL, 'Double', 4180.00, 'Available'),
(45, NULL, 'ICU', 5180.00, 'Available'),
(47, NULL, 'Double', 4300.00, 'Available'),
(49, NULL, 'Single', 3100.00, 'Available'),
(2, 43, 'Double', 4000.00, 'Occupied'), 
(4, 8, 'Single', 3200.00, 'Occupied'), 
(6, 7, 'ICU', 5200.00, 'Occupied'),     
(8, 19, 'Double', 4300.00, 'Occupied'),
(10, 31, 'Single', 3400.00, 'Occupied'),
(12, 47, 'ICU', 5300.00, 'Occupied'),
(14, 50, 'Double', 4050.00, 'Occupied'),
(16, 11, 'Single', 3250.00, 'Occupied'),
(18, 20, 'ICU', 5250.00, 'Occupied'),
(20, 22, 'Double', 4350.00, 'Occupied'),
(22, 6, 'Single', 3350.00, 'Occupied'),
(24, 15, 'ICU', 5400.00, 'Occupied'),
(26, 29, 'Double', 4075.00, 'Occupied'),
(28, 33, 'Single', 3275.00, 'Occupied'),
(30, 37, 'ICU', 5275.00, 'Occupied'),
(32, 40, 'Double', 4375.00, 'Occupied'),
(34, 44, 'Single', 3375.00, 'Occupied'),
(36, 46, 'ICU', 5450.00, 'Occupied'),
(38, 48, 'Double', 4080.00, 'Occupied'),
(40, 10, 'Single', 3280.00, 'Occupied'),
(42, 4, 'ICU', 5280.00, 'Occupied'),
(44, 12, 'Double', 4380.00, 'Occupied'),
(46, 18, 'Single', 3380.00, 'Occupied'),
(48, 16, 'ICU', 5500.00, 'Occupied'),
(50, 13, 'Double', 4200.00, 'Occupied');


-- Shift table
INSERT INTO Shift (shift_id, doctor_id, admin_id, description, start_time, end_time, shift_date, status) VALUES
(1, 1, 5, 'Morning shift', '08:00:00', '12:00:00', '2024-11-01', 'Active'),
(2, 2, 6, 'Evening shift', NULL, NULL, NULL, 'Available'),
(3, 3, 7, 'Night shift', '16:00:00', '20:00:00', '2024-11-03', 'Active'),
(4, 4, 8, 'Morning shift', '08:00:00', '12:00:00', '2024-11-04', 'Active'),
(5, 5, 9, 'Evening shift', NULL, NULL, NULL, 'Available'),
(6, 6, 10, 'Night shift', '16:00:00', '20:00:00', '2024-11-06', 'Active'),
(7, 7, 11, 'Morning shift', '08:00:00', '12:00:00', '2024-11-07', 'Active'),
(8, 8, 12, 'Evening shift', NULL, NULL, NULL, 'Available'),
(9, 9, 13, 'Night shift', '16:00:00', '20:00:00', '2024-11-09', 'Active'),
(10, 10, 14, 'Morning shift', '08:00:00', '12:00:00', '2024-11-10', 'Active'),
(11, 11, 15, 'Evening shift', NULL, NULL, NULL, 'Available'),
(12, 12, 16, 'Night shift', '16:00:00', '20:00:00', '2024-11-12', 'Active'),
(13, 13, 17, 'Morning shift', '08:00:00', '12:00:00', '2024-11-13', 'Active'),
(14, 14, 18, 'Evening shift', NULL, NULL, NULL, 'Available'),
(15, 15, 19, 'Night shift', '16:00:00', '20:00:00', '2024-11-15', 'Active'),
(16, 16, 20, 'Morning shift', '08:00:00', '12:00:00', '2024-11-16', 'Active'),
(17, 17, 21, 'Evening shift', NULL, NULL, NULL, 'Available'),
(18, 18, 22, 'Night shift', '16:00:00', '20:00:00', '2024-11-18', 'Active'),
(19, 19, 23, 'Morning shift', '08:00:00', '12:00:00', '2024-11-19', 'Active'),
(20, 20, 24, 'Evening shift', NULL, NULL, NULL, 'Available'),
(21, 21, 25, 'Night shift', '16:00:00', '20:00:00', '2024-11-21', 'Active'),
(22, 22, 26, 'Morning shift', '08:00:00', '12:00:00', '2024-11-22', 'Active'),
(23, 23, 27, 'Evening shift', NULL, NULL, NULL, 'Available'),
(24, 24, 28, 'Night shift', '16:00:00', '20:00:00', '2024-11-24', 'Active'),
(25, 25, 29, 'Morning shift', '08:00:00', '12:00:00', '2024-11-25', 'Active'),
(26, 26, 30, 'Evening shift', NULL, NULL, NULL, 'Available'),
(27, 27, 31, 'Night shift', '16:00:00', '20:00:00', '2024-11-27', 'Active'),
(28, 28, 32, 'Morning shift', '08:00:00', '12:00:00', '2024-11-28', 'Active'),
(29, 29, 33, 'Evening shift', NULL, NULL, NULL, 'Available'),
(30, 30, 34, 'Night shift', '16:00:00', '20:00:00', '2024-11-30', 'Active'),
(31, 31, 35, 'Morning shift', '08:00:00', '12:00:00', '2024-12-01', 'Active'),
(32, 32, 36, 'Evening shift', NULL, NULL, NULL, 'Available'),
(33, 33, 37, 'Night shift', '16:00:00', '20:00:00', '2024-12-03', 'Active'),
(34, 34, 38, 'Morning shift', '08:00:00', '12:00:00', '2024-12-04', 'Active'),
(35, 35, 39, 'Evening shift', NULL, NULL, NULL, 'Available'),
(36, 36, 40, 'Night shift', '16:00:00', '20:00:00', '2024-12-06', 'Active'),
(37, 37, 41, 'Morning shift', '08:00:00', '12:00:00', '2024-12-07', 'Active'),
(38, 38, 42, 'Evening shift', NULL, NULL, NULL, 'Available'),
(39, 39, 43, 'Night shift', '16:00:00', '20:00:00', '2024-12-09', 'Active'),
(40, 40, 44, 'Morning shift', '08:00:00', '12:00:00', '2024-12-10', 'Active'),
(41, 41, 45, 'Evening shift', NULL, NULL, NULL, 'Available'),
(42, 42, 46, 'Night shift', '16:00:00', '20:00:00', '2024-12-12', 'Active'),
(43, 43, 47, 'Morning shift', '08:00:00', '12:00:00', '2024-12-13', 'Active'),
(44, 44, 48, 'Evening shift', NULL, NULL, NULL, 'Available'),
(45, 45, 49, 'Night shift', '16:00:00', '20:00:00', '2024-12-15', 'Active'),
(46, 46, 50, 'Morning shift', '08:00:00', '12:00:00', '2024-12-16', 'Active'),
(47, 47, 1, 'Evening shift', NULL, NULL, NULL, 'Available'),
(48, 48, 2, 'Night shift', '16:00:00', '20:00:00', '2024-12-18', 'Active'),
(49, 49, 3, 'Morning shift', '08:00:00', '12:00:00', '2024-12-19', 'Active'),
(50, 50, 4, 'Evening shift', NULL, NULL, NULL, 'Available');


-- Booking table
INSERT INTO Booking (patient_id, room_id, booking_date) VALUES
(1, 1, '2024-11-20'),
(2, 2, '2024-11-21'),
(3, 3, '2024-11-22'),
(4, 4, '2024-11-23'),
(5, 5, '2024-11-24');

-- Sale table
INSERT INTO Sale (patient_id, sale_date) VALUES
(1, '2024-11-16 10:30:00'),
(2, '2024-11-17 11:00:00'),
(3, '2024-11-18 12:00:00'),
(4, '2024-11-19 13:00:00'),
(5, '2024-11-20 14:00:00');

-- SaleLineItem table
INSERT INTO SaleLineItem (sale_id, medsupply_id, quantity) VALUES
(1, 1, 2),
(2, 2, 5),
(3, 3, 1),
(4, 4, 10),
(5, 5, 3);

-- Bill table
INSERT INTO Bill (patient_id, total_price) VALUES
(1, 200.00),
(2, 350.00),
(3, 100.00),
(4, 500.00),
(5, 150.00);

INSERT INTO Appointment (appointment_id, patient_id, doctor_id, appointment_date, status_, price, description, diagnosis, treatment) VALUES
(1, 1, 10, '2024-11-01', 'Prescribed', 300.00, 'Routine check-up and medication review', NULL, NULL),
(2, 2, 9, '2024-11-02', 'On Treatment', 450.00, 'Flu diagnosis and treatment plan', 'Flu', 'Antiviral Medication'),
(3, 3, 8, '2024-11-03', 'Surgery', 500.00, 'Appendicitis surgery preparation and aftercare', 'Appendicitis', 'Appendectomy'),
(4, 4, 7, '2024-11-04', 'Completed', 200.00, 'Routine follow-up after treatment', NULL, NULL),
(5, 5, 6, '2024-11-05', 'Prescribed', 350.00, 'Initial prescription for allergy symptoms', NULL, NULL),
(6, 6, 5, '2024-11-06', 'On Treatment', 400.00, 'Treatment for chronic migraine relief', 'Chronic Migraine', 'Pain Management'),
(7, 7, 4, '2024-11-07', 'Surgery', 600.00, 'Knee surgery for ligament injury', 'Knee Injury', 'Knee Arthroscopy'),
(8, 8, 3, '2024-11-08', 'Completed', 250.00, 'Routine post-surgery recovery assessment', NULL, NULL),
(9, 9, 2, '2024-11-09', 'Prescribed', 300.00, 'Prescription for asthma relief', NULL, NULL),
(10, 10, 1, '2024-11-10', 'On Treatment', 450.00, 'Asthma control treatment plan', 'Asthma', 'Inhaler Treatment'),
(11, 11, 10, '2024-11-11', 'Surgery', 500.00, 'Gallstone surgery procedure and aftercare', 'Gallstones', 'Cholecystectomy'),
(12, 12, 9, '2024-11-12', 'Completed', 200.00, 'Post-procedure check-up and assessment', NULL, NULL),
(13, 13, 8, '2024-11-13', 'Prescribed', 350.00, 'Antibiotic prescription for infection', NULL, NULL),
(14, 14, 7, '2024-11-14', 'On Treatment', 400.00, 'High blood pressure medication prescription', 'Hypertension', 'Blood Pressure Medication'),
(15, 15, 6, '2024-11-15', 'Surgery', 600.00, 'Hip fracture surgery plan and recovery guidance', 'Hip Fracture', 'Hip Replacement'),
(16, 16, 5, '2024-11-16', 'Completed', 250.00, 'Post-operative evaluation and discharge instructions', NULL, NULL),
(17, 17, 4, '2024-11-17', 'Prescribed', 300.00, 'Antihistamines for seasonal allergies', NULL, NULL),
(18, 18, 3, '2024-11-18', 'On Treatment', 450.00, 'Cold treatment and medication plan', 'Cold', 'Decongestants'),
(19, 19, 2, '2024-11-19', 'Surgery', 500.00, 'Hernia repair surgery consultation', 'Hernia', 'Hernia Repair Surgery'),
(20, 20, 1, '2024-11-20', 'Completed', 200.00, 'Final post-surgery recovery check', NULL, NULL),
(21, 21, 10, '2024-11-21', 'Prescribed', 350.00, 'Antibiotics for respiratory infection', NULL, NULL),
(22, 22, 9, '2024-11-22', 'On Treatment', 400.00, 'Bronchitis medication prescription and guidance', 'Bronchitis', 'Antibiotics'),
(23, 23, 8, '2024-11-23', 'Surgery', 600.00, 'Spinal surgery consultation and plan', 'Spinal Disc Herniation', 'Spinal Surgery'),
(24, 24, 7, '2024-11-24', 'Completed', 250.00, 'Post-surgery check-up and recovery progress', NULL, NULL),
(25, 25, 6, '2024-11-25', 'Prescribed', 300.00, 'Prescribed medication for allergy symptoms', NULL, NULL),
(26, 26, 5, '2024-11-26', 'On Treatment', 450.00, 'Diabetes management and insulin therapy', 'Diabetes', 'Insulin Therapy'),
(27, 27, 4, '2024-11-27', 'Surgery', 500.00, 'Cataract surgery consultation and procedure', 'Cataract', 'Cataract Removal'),
(28, 28, 3, '2024-11-28', 'Completed', 200.00, 'Post-surgery check-up for cataract removal', NULL, NULL),
(29, 29, 2, '2024-11-29', 'Prescribed', 350.00, 'Prescription for high blood pressure medication', NULL, NULL),
(30, 30, 1, '2024-11-30', 'On Treatment', 400.00, 'Back pain relief treatment plan', 'Back Pain', 'Physical Therapy'),
(31, 31, 10, '2024-12-01', 'Surgery', 600.00, 'Ligament repair surgery and recovery plan', 'Torn Ligament', 'Ligament Repair'),
(32, 32, 9, '2024-12-02', 'Completed', 250.00, 'Post-surgery check-up and discharge instructions', NULL, NULL),
(33, 33, 8, '2024-12-03', 'Prescribed', 300.00, 'Pain relief prescription for sinus issues', NULL, NULL),
(34, 34, 7, '2024-12-04', 'On Treatment', 450.00, 'Pneumonia treatment and care plan', 'Pneumonia', 'Antibiotics'),
(35, 35, 6, '2024-12-05', 'Surgery', 500.00, 'Gallstone surgery preparation and guidance', 'Gallstones', 'Laparoscopic Surgery'),
(36, 36, 5, '2024-12-06', 'Completed', 200.00, 'Post-surgery evaluation and discharge advice', NULL, NULL),
(37, 37, 4, '2024-12-07', 'Prescribed', 350.00, 'Treatment for seasonal allergies', NULL, NULL),
(38, 38, 3, '2024-12-08', 'On Treatment', 400.00, 'Steroid nasal spray for sinus infection', 'Sinus Infection', 'Steroid Nasal Spray'),
(39, 39, 2, '2024-12-09', 'Surgery', 600.00, 'Fracture surgery and rehabilitation plan', 'Fracture', 'Fracture Fixation'),
(40, 40, 1, '2024-12-10', 'Completed', 250.00, 'Post-surgery follow-up and healing assessment', NULL, NULL),
(41, 41, 10, '2024-12-11', 'Prescribed', 300.00, 'Prescription for treatment of chronic pain', NULL, NULL),
(42, 42, 9, '2024-12-12', 'On Treatment', 450.00, 'Asthma management with inhaler treatment', 'Asthma', 'Inhaler Treatment'),
(43, 43, 8, '2024-12-13', 'Surgery', 500.00, 'Tonsillectomy surgery consultation and procedure', 'Tonsillitis', 'Tonsillectomy'),
(44, 44, 7, '2024-12-14', 'Completed', 200.00, 'Post-operative care and final check-up', NULL, NULL),
(45, 45, 6, '2024-12-15', 'Prescribed', 350.00, 'Anxiety treatment and prescribed medication', NULL, NULL),
(46, 46, 5, '2024-12-16', 'On Treatment', 400.00, 'Counseling session for anxiety treatment', 'Anxiety', 'Counseling'),
(47, 47, 4, '2024-12-17', 'Surgery', 600.00, 'Cancer treatment and tumor removal plan', 'Cancer', 'Tumor Removal'),
(48, 48, 3, '2024-12-18', 'Completed', 250.00, 'Post-surgery check-up and recovery evaluation', NULL, NULL),
(49, 49, 2, '2024-12-19', 'Prescribed', 300.00, 'Prescription for chronic pain relief', NULL, NULL),
(50, 50, 1, '2024-12-20', 'On Treatment', 450.00, 'Chronic fatigue treatment and vitamin therapy', 'Chronic Fatigue', 'Vitamin Therapy');

-- Populate Patient_Treatment table with 'On Treatment' records
INSERT INTO Patient_Treatment (patient_id, start_date, end_date, status, price)
SELECT 
    patient_id, 
    appointment_date AS start_date, 
    DATE_ADD(appointment_date, INTERVAL 14 DAY) AS end_date, -- Assuming treatment lasts 14 days
    'Good', -- Default status
    price
FROM Appointment
WHERE status_ = 'On Treatment';

-- Populate Surgery table with 'Surgery' records
INSERT INTO Surgery (patient_id, doctor_id, room_id, operation_date, operation_time, status)
SELECT 
    patient_id, 
    doctor_id, 
    (patient_id % 10 + 1) AS room_id, -- Example logic to assign room IDs
    appointment_date AS operation_date, 
    '09:00:00' AS operation_time, -- Default operation time
    'Pending' -- Default status
FROM Appointment
WHERE status_ = 'Surgery';

-- DoctorRequest table
INSERT INTO DoctorRequest (doc_Fname, doc_Lname, email, password, specialized, gender, mobile_number)
VALUES
('Ahmed', 'Khan', 'ahmed.khan@example.com', 'pass1', 'Cardiologist', 'Male', '03001234567'),
('Ayesha', 'Malik', 'ayesha.malik@example.com', 'pass2', 'Dermatologist', 'Female', '03111234567'),
('Bilal', 'Ahmad', 'bilal.ahmad@example.com', 'pass3', 'Neurologist', 'Male', '03211234567'),
('Fatima', 'Sheikh', 'fatima.sheikh@example.com', 'pass4', 'Pediatrician', 'Female', '03321234567'),
('Hassan', 'Ali', 'hassan.ali@example.com', 'pass5', 'Orthopedic', 'Male', '03451234567'),
('Rabia', 'Saeed', 'rabia.saeed@example.com', 'pass6', 'Gynecologist', 'Female', '03031234567'),
('Usman', 'Zafar', 'usman.zafar@example.com', 'pass7', 'Oncologist', 'Male', '03151234567'),
('Nida', 'Rashid', 'nida.rashid@example.com', 'pass8', 'Psychiatrist', 'Female', '03241234567'),
('Zain', 'Qureshi', 'zain.qureshi@example.com', 'pass9', 'Surgeon', 'Male', '03361234567'),
('Sana', 'Hameed', 'sana.hameed@example.com', 'pass10', 'Radiologist', 'Female', '03471234567');



-- UPDATES 23/11/24

-- Extra patient records to accomodate appointment's new records 
INSERT INTO Patient (patient_id, first_name, last_name, gender, address, phone_no, email, _password, medical_details, image)
VALUES
(51, 'John', 'Doe', 'Male', '101 Maple Street', '03211234567', 'john.doe@email.com', 'Pass51', 'Hypertension', NULL),
(52, 'Jane', 'Smith', 'Female', '202 Elm Avenue', '03211234568', 'jane.smith@email.com', 'Pass52', 'Asthma', NULL),
(53, 'Alice', 'Johnson', 'Female', '303 Oak Lane', '03211234569', 'alice.johnson@email.com', 'Pass53', 'Arthritis', NULL),
(54, 'Bob', 'Brown', 'Male', '404 Pine Drive', '03211234570', 'bob.brown@email.com', 'Pass54', 'Chronic Pain', NULL),
(55, 'Charlie', 'Davis', 'Male', '505 Cedar Road', '03211234571', 'charlie.davis@email.com', 'Pass55', 'Diabetes', NULL),
(56, 'Emily', 'Wilson', 'Female', '606 Spruce Street', '03211234572', 'emily.wilson@email.com', 'Pass56', 'Eczema', NULL),
(57, 'Frank', 'Moore', 'Male', '707 Birch Boulevard', '03211234573', 'frank.moore@email.com', 'Pass57', 'Acid Reflux', NULL),
(58, 'Grace', 'Taylor', 'Female', '808 Walnut Way', '03211234574', 'grace.taylor@email.com', 'Pass58', 'Anxiety', NULL),
(59, 'Henry', 'Anderson', 'Male', '909 Chestnut Court', '03211234575', 'henry.anderson@email.com', 'Pass59', 'Heart Disease', NULL),
(60, 'Ivy', 'Thomas', 'Female', '1010 Willow Terrace', '03211234576', 'ivy.thomas@email.com', 'Pass60', 'Allergy', NULL),
(61, 'Jake', 'Jackson', 'Male', '1111 Birchwood Lane', '03211234577', 'jake.jackson@email.com', 'Pass61', 'Cholesterol', NULL),
(62, 'Kathy', 'White', 'Female', '1212 Redwood Avenue', '03211234578', 'kathy.white@email.com', 'Pass62', 'Sleep Apnea', NULL),
(63, 'Liam', 'Harris', 'Male', '1313 Maple Grove', '03211234579', 'liam.harris@email.com', 'Pass63', 'Obesity', NULL),
(64, 'Mia', 'Martin', 'Female', '1414 Oakwood Path', '03211234580', 'mia.martin@email.com', 'Pass64', 'Depression', NULL),
(65, 'Noah', 'Garcia', 'Male', '1515 Magnolia Circle', '03211234581', 'noah.garcia@email.com', 'Pass65', 'Migraines', NULL),
(66, 'Olivia', 'Martinez', 'Female', '1616 Spruce Trail', '03211234582', 'olivia.martinez@email.com', 'Pass66', 'Acne', NULL),
(67, 'Paul', 'Hernandez', 'Male', '1717 Poplar Terrace', '03211234583', 'paul.hernandez@email.com', 'Pass67', 'Anemia', NULL),
(68, 'Quinn', 'Lopez', 'Male', '1818 Cypress Drive', '03211234584', 'quinn.lopez@email.com', 'Pass68', 'Heart Disease', NULL),
(69, 'Ruby', 'Gonzalez', 'Female', '1919 Maple Avenue', '03211234585', 'ruby.gonzalez@email.com', 'Pass69', 'Asthma', NULL),
(70, 'Steve', 'Clark', 'Male', '2020 Elmwood Terrace', '03211234586', 'steve.clark@email.com', 'Pass70', 'Chronic Pain', NULL);

-- Update status_ in Appointments Table (added 'Pending status' to cater to untreated cases)
ALTER TABLE Appointment 
MODIFY status_ ENUM('Prescribed', 'On Treatment', 'Surgery', 'Completed', 'Pending') DEFAULT 'Pending';

-- Updates to Appointments record (added 'Pending status' to cater to untreated cases)
INSERT INTO Appointment (appointment_id, patient_id, doctor_id, appointment_date, status_, price, description, diagnosis, treatment) VALUES
(51, 51, 1, '2024-12-10', 'Pending', 150.00, 'Initial consultation and evaluation', NULL, NULL),
(52, 52, 1, '2024-11-10', 'Pending', 200.00, 'Routine physical examination', NULL, NULL),
(53, 53, 2, '2024-12-09', 'Pending', 175.00, 'General health check-up', NULL, NULL),
(54, 54, 2, '2024-11-09', 'Pending', 225.00, 'Initial evaluation for asthma symptoms', NULL, NULL),
(55, 55, 3, '2024-12-08', 'Pending', 180.00, 'Basic diagnostic and screening', NULL, NULL),
(56, 56, 3, '2024-11-08', 'Pending', 250.00, 'Evaluation for cold symptoms', NULL, NULL),
(57, 57, 4, '2024-12-07', 'Pending', 190.00, 'Routine wellness check-up', NULL, NULL),
(58, 58, 4, '2024-11-07', 'Pending', 210.00, 'Consultation for seasonal allergies', NULL, NULL),
(59, 59, 5, '2024-12-06', 'Pending', 175.00, 'Consultation for initial migraine symptoms', NULL, NULL),
(60, 60, 5, '2024-11-06', 'Pending', 220.00, 'General health evaluation post-migraine', NULL, NULL),
(61, 61, 6, '2024-12-05', 'Pending', 200.00, 'Initial evaluation for blood sugar levels', NULL, NULL),
(62, 62, 6, '2024-11-05', 'Pending', 230.00, 'Diabetes follow-up and diet review', NULL, NULL),
(63, 63, 7, '2024-12-04', 'Pending', 220.00, 'Consultation for knee pain', NULL, NULL),
(64, 64, 7, '2024-11-04', 'Pending', 250.00, 'Evaluation for post-operative care', NULL, NULL),
(65, 65, 8, '2024-12-03', 'Pending', 180.00, 'General diagnostic and health screening', NULL, NULL),
(66, 66, 8, '2024-11-03', 'Pending', 260.00, 'Follow-up on respiratory issues', NULL, NULL),
(67, 67, 9, '2024-12-02', 'Pending', 200.00, 'Initial assessment for flu symptoms', NULL, NULL),
(68, 68, 9, '2024-11-02', 'Pending', 240.00, 'General health consultation and guidance', NULL, NULL),
(69, 69, 10, '2024-12-01', 'Pending', 225.00, 'Routine check-up and blood tests', NULL, NULL),
(70, 70, 10, '2024-11-01', 'Pending', 250.00, 'General wellness consultation', NULL, NULL);

-- Adjusted Records for Appointment Table, Prescribed records now has treatment and diagnosis text instead of NULL
UPDATE Appointment
SET diagnosis = CASE 
    WHEN appointment_id = 1 THEN 'Routine Check-up'
    WHEN appointment_id = 5 THEN 'Allergy Symptoms'
    WHEN appointment_id = 9 THEN 'Asthma Relief'
    WHEN appointment_id = 13 THEN 'Antibiotic Prescription'
    WHEN appointment_id = 17 THEN 'Seasonal Allergies'
    WHEN appointment_id = 21 THEN 'Respiratory Infection'
    WHEN appointment_id = 25 THEN 'Allergy Symptoms'
    WHEN appointment_id = 29 THEN 'High Blood Pressure'
    WHEN appointment_id = 33 THEN 'Sinus Pain'
    WHEN appointment_id = 37 THEN 'Seasonal Allergies'
    WHEN appointment_id = 41 THEN 'Chronic Pain Treatment'
    WHEN appointment_id = 45 THEN 'Anxiety'
    WHEN appointment_id = 49 THEN 'Chronic Pain Relief'
    ELSE diagnosis
END,
    treatment = CASE 
    WHEN appointment_id = 1 THEN 'General Check-up'
    WHEN appointment_id = 5 THEN 'Allergy Medication'
    WHEN appointment_id = 9 THEN 'Inhaler Treatment'
    WHEN appointment_id = 13 THEN 'Antibiotic Prescription'
    WHEN appointment_id = 17 THEN 'Antihistamines'
    WHEN appointment_id = 21 THEN 'Antibiotics'
    WHEN appointment_id = 25 THEN 'Allergy Medication'
    WHEN appointment_id = 29 THEN 'Blood Pressure Medication'
    WHEN appointment_id = 33 THEN 'Pain Relief'
    WHEN appointment_id = 37 THEN 'Antihistamines'
    WHEN appointment_id = 41 THEN 'Pain Medication'
    WHEN appointment_id = 45 THEN 'Anxiety Medication'
    WHEN appointment_id = 49 THEN 'Pain Medication'
    ELSE treatment
END
WHERE status_ = 'Prescribed';


-- Prescription table now has appointment_id 
-- Step 1: Insert records into Prescription without `appointment_id`
INSERT INTO Prescription (patient_id, doctor_id, date_issued)
SELECT 
    a.patient_id, 
    a.doctor_id, 
    a.appointment_date AS date_issued
FROM Appointment a
WHERE a.status_ = 'Prescribed';

-- Step 2: Alter the Prescription table to add the `appointment_id` column
ALTER TABLE Prescription 
ADD COLUMN appointment_id INT NOT NULL;

-- Step 3: Update the Prescription table to link prescriptions to appointments
-- Based on matching `patient_id` and `date_issued` (which corresponds to `appointment_date`)
UPDATE Prescription p
JOIN Appointment a
ON p.patient_id = a.patient_id 
AND p.date_issued = a.appointment_date
SET p.appointment_id = a.appointment_id;

-- Step 4: Add the foreign key constraint to link Prescription with Appointment
ALTER TABLE Prescription
ADD CONSTRAINT fk_appointment_id FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id) ON DELETE CASCADE ON UPDATE CASCADE;


-- Extend length of image in Patient Table
ALTER TABLE Patient MODIFY COLUMN image VARCHAR(255);
ALTER TABLE Booking ADD COLUMN payment_status VARCHAR(10) DEFAULT 'Unpaid';
ALTER TABLE Appointment ADD COLUMN payment_status VARCHAR(10) DEFAULT 'Unpaid';

UPDATE Shift
SET 
    start_time = CASE 
        WHEN description = 'Morning Shift' THEN '09:00'
        WHEN description = 'Evening Shift' THEN '17:00'
        WHEN description = 'Night Shift' THEN '23:00'
    END,
    end_time = CASE 
        WHEN description = 'Morning Shift' THEN '17:00'
        WHEN description = 'Evening Shift' THEN '01:00'
        WHEN description = 'Night Shift' THEN '07:00'
    END
WHERE description IN ('Morning Shift', 'Evening Shift', 'Night Shift');

DROP TABLE IF EXISTS SaleLineItem;
DROP TABLE IF EXISTS Sale;

-- Updates 24/11/24

-- Table for PrescriptionItem cause 1 prescrption can have multiple items
CREATE TABLE PrescriptionItem (
    prescription_item_id INT AUTO_INCREMENT PRIMARY KEY,
    prescription_id INT NOT NULL,
    item_id INT DEFAULT NULL,
    quantity INT NOT NULL CHECK (quantity > 0), -- Quantity of each item
    FOREIGN KEY (prescription_id) REFERENCES Prescription(prescription_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (item_id) REFERENCES StockItem(item_id) ON DELETE CASCADE ON UPDATE CASCADE
);


-- Updates 26/11/23

INSERT INTO Feedback (patient_id, doctor_id, feedback_date, description_, rating_stars, status_)
SELECT 
    subquery.patient_id,
    subquery.doctor_id,
    CURRENT_DATE AS feedback_date, 
    CASE 
        WHEN subquery.rating_stars = 5 THEN CONCAT('Outstanding service during the appointment on ', subquery.appointment_date, '. Highly satisfied!')
        WHEN subquery.rating_stars = 4 THEN CONCAT('Good experience on ', subquery.appointment_date, '. Would recommend.')
        WHEN subquery.rating_stars = 3 THEN CONCAT('The appointment on ', subquery.appointment_date, ' was average. Some improvements could be made.')
        WHEN subquery.rating_stars = 2 THEN CONCAT('The appointment on ', subquery.appointment_date, ' was below expectations. Improvement is needed.')
        ELSE CONCAT('Poor service during the appointment on ', subquery.appointment_date, '. Very dissatisfied.')
    END AS description_,
    subquery.rating_stars, 
    'Pending' AS status_
FROM (
    SELECT 
        a.patient_id,
        a.doctor_id,
        a.appointment_date,
        FLOOR(RAND() * 5) + 1 AS rating_stars -- Generate random rating between 1 and 5
    FROM 
        Appointment a
    WHERE 
        a.status_ = 'Completed'
) AS subquery;

    
-- Appointments with Completed status now must have some treatment + diagnosis receieved beforehand    
UPDATE Appointment
SET 
    diagnosis = CASE
        WHEN appointment_id = 4 THEN 'Follow-up Check-up'
        WHEN appointment_id = 8 THEN 'Post-Surgery Recovery'
        WHEN appointment_id = 12 THEN 'Procedure Recovery'
        WHEN appointment_id = 16 THEN 'Post-Operative Care'
        WHEN appointment_id = 20 THEN 'Final Recovery Check'
        WHEN appointment_id = 24 THEN 'Post-Surgery Assessment'
        WHEN appointment_id = 28 THEN 'Cataract Removal Follow-up'
        WHEN appointment_id = 32 THEN 'Spinal Surgery Recovery'
        WHEN appointment_id = 36 THEN 'Post-Laparoscopic Recovery'
        WHEN appointment_id = 40 THEN 'Surgery Healing Evaluation'
        WHEN appointment_id = 44 THEN 'Tonsillectomy Recovery'
        WHEN appointment_id = 48 THEN 'Post-Surgery Health Monitoring'
        ELSE diagnosis -- Keep existing values if already set
    END,
    treatment = CASE
        WHEN appointment_id = 4 THEN 'General Check-up'
        WHEN appointment_id = 8 THEN 'Routine Recovery Assessment'
        WHEN appointment_id = 12 THEN 'Post-Procedure Care'
        WHEN appointment_id = 16 THEN 'Post-Operative Treatment'
        WHEN appointment_id = 20 THEN 'Surgery Recovery Plan'
        WHEN appointment_id = 24 THEN 'Recovery Check-up'
        WHEN appointment_id = 28 THEN 'Cataract Recovery Guidance'
        WHEN appointment_id = 32 THEN 'Spinal Recovery Program'
        WHEN appointment_id = 36 THEN 'Laparoscopic Surgery Recovery'
        WHEN appointment_id = 40 THEN 'Healing Progress Plan'
        WHEN appointment_id = 44 THEN 'Tonsillectomy Recovery'
        WHEN appointment_id = 48 THEN 'Surgery Recovery Plan'
        ELSE treatment -- Keep existing values if already set
    END
WHERE 
    status_ = 'Completed';



INSERT INTO PrescriptionItem (prescription_id, item_id, quantity)
SELECT 
    p.prescription_id, 
    1 AS item_id,        -- Always assign item_id = 1
    1 AS quantity        -- Always assign quantity = 1
FROM Prescription p
WHERE p.status_ = 'Pending';




-- Select all records from each table
SELECT * FROM Admin;
SELECT * FROM Doctor;
SELECT * FROM MedicalSupply;
SELECT * FROM MedicalSupplyDescription;
SELECT * FROM MedicalSupplyCatalogue;
SELECT * FROM StockItem;
SELECT * FROM MedicalSupplyRequest;
SELECT * FROM Inventory;
SELECT * FROM GoodIn;
SELECT * FROM GoodOut;
SELECT * FROM Workshop;
SELECT * FROM Doctor_Workshop;
SELECT * FROM Shift;
SELECT * FROM Patient;
SELECT * FROM Room;
SELECT * FROM Booking;
SELECT * FROM Prescription;
SELECT * FROM Sale;
SELECT * FROM SaleLineItem;
SELECT * FROM Bill;
SELECT * FROM Appointment;
SELECT * FROM Feedback;
SELECT * FROM Patient_Treatment;
SELECT * FROM Surgery;
SELECT * FROM DoctorRequest;
SELECT * FROM PrescriptionItem;



SELECT 
    p.prescription_id,
    CONCAT(pa.first_name, ' ', pa.last_name) AS patient,
    a.treatment AS treatment,
    si.item_name AS item,
    msd.medsupply_type AS type,
    si.unit_price AS price,
    pi.quantity AS quantity,
    p.date_issued,
    (SELECT SUM(pi.quantity * si.unit_price) 
     FROM PrescriptionItem pi 
     JOIN StockItem si ON pi.item_id = si.item_id 
     WHERE pi.prescription_id = p.prescription_id) AS total_price
FROM 
    Prescription p
JOIN 
    Patient pa ON pa.patient_id = p.patient_id
JOIN 
    Appointment a ON a.appointment_id = p.appointment_id
JOIN 
    PrescriptionItem pi ON p.prescription_id = pi.prescription_id
JOIN 
    StockItem si ON si.item_id = pi.item_id
JOIN 
    MedicalSupplyDescription msd ON msd.medsupply_id = si.medsupply_id
WHERE 
    p.patient_id = 29;
  
  
SELECT 
    si.item_id, 
    si.item_name AS item, 
    msd.medsupply_type AS type, 
    si.quantity_available AS stock,        -- Aggregate stock quantities
    si.unit_price AS price, 
    CASE 
        WHEN si.quantity_available > si.threshold THEN 'Available' 
        ELSE 'Low Stock' 
    END AS status, 
    MAX(pi.prescription_id) AS prescription_id,   -- Get the latest prescription_id (if available)
    MAX(p.patient_id) AS patient_id,              -- Get the associated patient_id (if available)
    MAX(p.doctor_id) AS doctor_id,                -- Get the associated doctor_id (if available)
    MAX(p.appointment_id) AS appointment_id       -- Get the associated appointment_id (if available)
FROM 
    StockItem si 
JOIN 
    MedicalSupplyDescription msd ON si.medsupply_id = msd.medsupply_id 
LEFT JOIN 
    PrescriptionItem pi ON si.item_id = pi.item_id 
LEFT JOIN 
    Prescription p ON pi.prescription_id = p.prescription_id
GROUP BY 
    si.item_id, si.item_name, msd.medsupply_type, si.unit_price, si.threshold, si.quantity_available;














DROP TABLE IF EXISTS Feedback;
DROP TABLE IF EXISTS Appointment;
DROP TABLE IF EXISTS Surgery;
DROP TABLE IF EXISTS Patient_Treatment;
DROP TABLE IF EXISTS SaleLineItem;
DROP TABLE IF EXISTS Sale;
DROP TABLE IF EXISTS Prescription;
DROP TABLE IF EXISTS Bill;
DROP TABLE IF EXISTS Booking;
DROP TABLE IF EXISTS Room;
DROP TABLE IF EXISTS Patient;
DROP TABLE IF EXISTS Shift;
DROP TABLE IF EXISTS Workshop;
DROP TABLE IF EXISTS Doctor_Workshop;
DROP TABLE IF EXISTS GoodOut;
DROP TABLE IF EXISTS GoodIn;
DROP TABLE IF EXISTS Inventory;
DROP TABLE IF EXISTS MedicalSupplyRequest;
DROP TABLE IF EXISTS StockItem;
DROP TABLE IF EXISTS MedicalSupplyCatalogue;
DROP TABLE IF EXISTS MedicalSupplyDescription;
DROP TABLE IF EXISTS MedicalSupply;
DROP TABLE IF EXISTS Doctor;
DROP TABLE IF EXISTS Admin;
DROP TABLE IF EXISTS DoctorRequest;
DROP TABLE IF EXISTS PrescriptionItem;

