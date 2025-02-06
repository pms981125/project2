package com.lec.project.human_resources.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "persistent_logins")
@Getter
@Setter
@NoArgsConstructor
public class PersistentLogin {
	@Column(nullable = false, length = 64)
    private String username;

    @Id
    @Column(length = 64)
    private String series;

    @Column(nullable = false, length = 64)
    private String token;

    @Column(name = "last_used", nullable = false)
    private LocalDateTime lastUsed;
}
