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
    isDeleted boolean
);

CREATE TABLE IF NOT EXISTS students_courses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_name VARCHAR(50) NOT NULL,
    course_start_at TIMESTAMP,
    course_end_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS course_registration_status (
    id INT NOT NULL AUTO_INCREMENT COMMENT '申込状況ID',
    student_course_id INT NOT NULL COMMENT '受講生コースID（外部キー）',
    registration_status ENUM('仮申込','本申込','受講中','受講終了') NOT NULL COMMENT '申込ステータス',
    PRIMARY KEY (id),
    KEY fk_student_course_status (student_course_id),
    CONSTRAINT fk_student_course_status FOREIGN KEY (student_course_id)
        REFERENCES students_courses(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='コース申込状況テーブル';