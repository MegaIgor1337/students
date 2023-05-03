package org.example;

import lombok.Cleanup;
import org.example.entity.Course;
import org.example.entity.Student;
import org.example.entity.StudentProfile;
import org.example.entity.Trainer;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class HibernateTest {



    // получить всех на java enterprise
    @Test
    public void getStudentsOnJavaEnterprise() {
        Configuration configuration = new Configuration();
        configuration.configure();
        @Cleanup var sessionFactory = configuration.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        List<Student> studentsOnJavaEnterprise = session
                .createQuery("FROM Student ", Student.class)
                .getResultList()
                .stream()
                .filter(it -> it.getCourse().getName().equals("Java Enterprise"))
                .toList();
        session.getTransaction().commit();
        System.out.println(studentsOnJavaEnterprise);
    }

    // удалить всех с оценкой ниже 6
    @Test
    public void deleteStudents() {
        Configuration configuration = new Configuration();
        configuration.configure();
        @Cleanup var sessionFactory = configuration.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        List<Student> studentsToDelete = session
                .createQuery("FROM Student ", Student.class)
                .getResultList()
                .stream()
                .filter(it -> it.getStudentProfile().getMark() < 6.0)
                .toList();
        for (Student student : studentsToDelete) {
            session.delete(student);
        }
        session.getTransaction().commit();
    }

    // добавить студента
    @Test
    public void addStudent() {
        Student student = Student.builder()
                .name("Petr")
                .course(
                        Course.builder()
                                .name("Java Enterprise")
                                .build()
                )
                .build();
        StudentProfile studentProfile = StudentProfile.builder()
                .mark(0.0)
                .build();
        Configuration configuration = new Configuration();
        configuration.configure();
        @Cleanup var sessionFactory = configuration.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        Course course = session.createQuery(" FROM Course ", Course.class).getResultList()
                .stream()
                .filter(it -> it.getName().equals("Java Enterprise"))
                .toList().get(0);
        course.addStudent(student);
        studentProfile.setStudent(student);
        session.persist(studentProfile);
        student.setStudentProfile(studentProfile);
        session.getTransaction().commit();
    }

    // удалить студента
    @Test
    public void deleteJavaEnterprise() {
        Configuration configuration = new Configuration();
        configuration.configure();
        @Cleanup var sessionFactory = configuration.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        Course course = session.createQuery(" FROM Course ", Course.class).getResultList()
                .stream()
                .filter(it -> it.getName().equals("Java Enterprise"))
                .toList().get(0);
        session.delete(course);
        session.getTransaction().commit();
    }

    // добавить тренера и курсы его
    @Test
    public void addTrainer() {
        Configuration configuration = new Configuration();
        configuration.configure();

        Course course1 = Course.builder()
                .name("Java Enterprise")
                .build();

        Course course2 = Course.builder()
                .name("Java Web")
                .build();
        Trainer trainer = Trainer.builder()
                .name("Andrey")
                .experience(5)
                .build();

        @Cleanup var sessionFactory = configuration.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(course1);
        trainer.addCourse(course1);
        session.save(trainer);


        session.getTransaction().commit();
    }


    // изменить курс
    @Test
    public void testChangeCourse() {
        Configuration configuration = new Configuration();
        configuration.configure();
        @Cleanup var sessionFactory = configuration.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        Course course;
        session.beginTransaction();
        course = session.createQuery(" FROM Course ", Course.class).getResultList()
                .stream()
                .filter(it -> it.getName().equals("Java Enterprise"))
                .toList().get(0);
        Trainer trainer = session.createQuery("FROM Trainer ", Trainer.class).getResultList()
                .stream()
                .filter(it -> it.getName().equals("Alexander") && it.getExperience() == 4)
                .toList().get(0);
        course.getTrainers().add(trainer);
        session.getTransaction().commit();
    }

    @Test
    public void testDeleteCourse() {
        Configuration configuration = new Configuration();
        configuration.configure();
        @Cleanup var sessionFactory = configuration.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        Course course = session.createQuery(" FROM Course ", Course.class).getResultList()
                .stream()
                .filter(it -> it.getName().equals("Java Web"))
                .toList().get(0);
        session.delete(course);
        session.getTransaction().commit();
    }


}
