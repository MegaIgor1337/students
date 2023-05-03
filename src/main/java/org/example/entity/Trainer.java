package org.example.entity;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trainer {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int experience;
    @Builder.Default
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    private List<TrainerCourse> trainerCourses = new ArrayList<>();


    public void addTrainerCourse(TrainerCourse trainerCourse) {
        this.trainerCourses.add(trainerCourse);
        trainerCourse.setTrainer(this);
    }
}
