package kartbllansh.entity;

import kartbllansh.supplement.PriorityNotes;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Notes")
public class NoteTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "Name")
    private String name;
    @Column(name = "Priority")
    @Enumerated(EnumType.STRING)
    private PriorityNotes priority;
    @ManyToOne
    @JoinColumn(name = "Creator")
    private UserTable creator;
    @Column(name = "Text", columnDefinition = "TEXT")
    private String text;
    @Column(name = "TimeOfCreation")
    private LocalDateTime timeOfCreation;
    @Column(name = "TimeOfEdit")
    private LocalDateTime timeOfEdit;
}
