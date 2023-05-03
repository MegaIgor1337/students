package org.example.entity;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Course {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Student> students = new ArrayList<>();


    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<TrainerCourse> trainerCourses = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
        student.setCourse(this);
    }

    public void addTrainerCourse(TrainerCourse trainerCourse) {
        this.trainerCourses.add(trainerCourse);
        trainerCourse.setCourse(this);
    }
}
