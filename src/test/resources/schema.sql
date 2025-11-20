DROP TABLE IF EXISTS course_registration_status;
DROP TABLE IF EXISTS students_courses;
DROP TABLE IF EXISTS students;

-- studentsテーブル
CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    kana_name VARCHAR(50) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(50) NOT NULL,
    area VARCHAR(50),
    age INT,
    sex VARCHAR(10),
    remark TEXT,
    is_deleted BOOLEAN
);

-- students_coursesテーブル
CREATE TABLE students_courses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_name VARCHAR(50) NOT NULL,
    course_start_at TIMESTAMP,
    course_end_at TIMESTAMP
);

-- course_registration_statusテーブル（H2対応）
CREATE TABLE course_registration_status (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_course_id INT NOT NULL,
    registration_status VARCHAR(20) NOT NULL
);
