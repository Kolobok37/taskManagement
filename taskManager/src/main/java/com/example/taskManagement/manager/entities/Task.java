package com.example.taskManagement.manager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusTask status;
    @Column(name = "priority")
    private String priority;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "completion_date")
    private LocalDateTime completionDate;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
    @ManyToOne
    @JoinColumn(name = "performer_id")
    private User performer;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id")
    private List<Comment> comments;
}
