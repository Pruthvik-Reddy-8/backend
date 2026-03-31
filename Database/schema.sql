CREATE DATABASE IF NOT EXISTS student_management_db;
USE student_management_db;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN'
);

-- Default admin user (username: admin, password: admin123)
INSERT IGNORE INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');

CREATE TABLE IF NOT EXISTS faculty (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    department VARCHAR(100),
    designation VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    course_name VARCHAR(150) NOT NULL,
    description TEXT,
    credits INT,
    faculty_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (faculty_id) REFERENCES faculty(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    date_of_birth DATE,
    enrollment_number VARCHAR(30) UNIQUE NOT NULL,
    department VARCHAR(100),
    semester INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'ABSENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);

INSERT INTO faculty (name, email, phone, department, designation) VALUES
('Dr. Rajesh Kumar', 'rajesh.kumar@sms.edu', '9876543210', 'Computer Science', 'Professor'),
('Dr. Priya Sharma', 'priya.sharma@sms.edu', '9876543211', 'Mathematics', 'Associate Professor'),
('Mr. Anil Verma', 'anil.verma@sms.edu', '9876543212', 'Physics', 'Assistant Professor');

INSERT INTO course (course_code, course_name, description, credits, faculty_id) VALUES
('CS101', 'Introduction to Programming', 'Basic programming concepts using Java', 4, 1),
('MA201', 'Calculus and Linear Algebra', 'Differential and integral calculus', 3, 2),
('PH101', 'Engineering Physics', 'Fundamentals of physics for engineers', 3, 3),
('CS202', 'Data Structures', 'Arrays, LinkedLists, Trees and Graphs', 4, 1);

INSERT INTO student (name, email, phone, date_of_birth, enrollment_number, department, semester) VALUES
('Aarav Singh', 'aarav.singh@student.sms.edu', '9123456781', '2003-04-12', 'SMS2024001', 'Computer Science', 2),
('Diya Patel', 'diya.patel@student.sms.edu', '9123456782', '2003-07-22', 'SMS2024002', 'Computer Science', 2),
('Rohan Mehta', 'rohan.mehta@student.sms.edu', '9123456783', '2002-11-05', 'SMS2024003', 'Mathematics', 4),
('Sneha Reddy', 'sneha.reddy@student.sms.edu', '9123456784', '2003-01-30', 'SMS2024004', 'Physics', 2),
('Kiran Joshi', 'kiran.joshi@student.sms.edu', '9123456785', '2002-09-15', 'SMS2024005', 'Computer Science', 4);

INSERT INTO attendance (student_id, course_id, attendance_date, status) VALUES
(1, 1, '2024-01-15', 'PRESENT'),
(1, 1, '2024-01-16', 'ABSENT'),
(2, 1, '2024-01-15', 'PRESENT'),
(2, 1, '2024-01-16', 'PRESENT'),
(3, 2, '2024-01-15', 'PRESENT'),
(4, 3, '2024-01-15', 'ABSENT');
