package org.example;

import lombok.Cleanup;
import org.example.entity.*;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

import java.time.Instant;
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
        @Cleanup var sessionFactory = configuration.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        var trainer = Trainer.builder()
                .name("Andrey")
                .experience(5)
                .build();
        var course = Course.builder().name("Java Web").build();
        session.save(trainer);
        session.persist(course);
        TrainerCourse trainerCourse = TrainerCourse.builder()
                .date(Instant.now()).build();
        trainer.addTrainerCourse(trainerCourse);
        course.addTrainerCourse(trainerCourse);
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
        course.setName("Java Web");
        session.saveOrUpdate(course);
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
