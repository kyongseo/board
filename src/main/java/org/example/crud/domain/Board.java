package org.example.crud.domain;

import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class Board {

    @Id
    private Long id;
    private String name;
    private String title;
    private LocalDateTime createdAt;

    private String content;
    private String password;

}
