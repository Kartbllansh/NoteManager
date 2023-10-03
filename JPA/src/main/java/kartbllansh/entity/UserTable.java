package kartbllansh.entity;


import kartbllansh.supplement.StatusUser;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appUser")
public class UserTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "TelegramId")
    private Long telegramUserId;

    @CreationTimestamp
    @Column(name = "LoginDate")
    private LocalDateTime firstLoginDate;
    @Column(name = "StatusAddNote")
    @Enumerated(EnumType.STRING)
    private StatusUser statusUser;
    @Column(name = "FirstName")
    private String firstName;
    @Column(name = "LastName")
    private String lastName;
    @Column(name = "UserName")
    private String userName;
    @OneToOne
    @JoinColumn(name = "ActiveAddNote")
    private NoteTable activeAddNote;



}
