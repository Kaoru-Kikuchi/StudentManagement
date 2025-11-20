-- studentsテーブル
CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    kana_name VARCHAR(50) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(50) NOT NULL,
    area VARCHAR(50),
    age INT,
    sex VARCHAR(10),
    remark TEXT,
    isDeleted BOOLEAN
);

-- students_coursesテーブル
CREATE TABLE IF NOT EXISTS students_courses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_name VARCHAR(50) NOT NULL,
    course_start_at TIMESTAMP,
    course_end_at TIMESTAMP
);

-- course_registration_statusテーブル（ENUM削除、VARCHARへ変更）
CREATE TABLE IF NOT EXISTS course_registration_status (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_course_id INT NOT NULL,
    registration_status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_student_course_status FOREIGN KEY (student_course_id)
        REFERENCES students_courses(id)
);
