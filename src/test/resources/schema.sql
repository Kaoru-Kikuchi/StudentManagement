DROP TABLE IF EXISTS students_courses_status;
DROP TABLE IF EXISTS students_courses;
DROP TABLE IF EXISTS students;

CREATE TABLE students
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    kana_name   VARCHAR(50) NOT NULL,
    nickname    VARCHAR(50),
    email       VARCHAR(50) NOT NULL,
    area        VARCHAR(50),
    age         INT,
    sex         VARCHAR(10),
    remark      VARCHAR(255),
    is_deleted  BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE students_courses
(
    id               VARCHAR(36) PRIMARY KEY,
    student_id       VARCHAR(36) NOT NULL,
    course_name      VARCHAR(50) NOT NULL,
    course_start_at  TIMESTAMP,
    course_end_at    TIMESTAMP
);

CREATE TABLE students_courses_status
(
    id                VARCHAR(36) PRIMARY KEY,
    student_course_id VARCHAR(36) NOT NULL UNIQUE,
    status            VARCHAR(20) NOT NULL,

    CONSTRAINT fk_students_courses_status_student_course
        FOREIGN KEY (student_course_id)
        REFERENCES students_courses (id)
);