package com.jira.ibm.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User extends BaseEntity {

    private String userId;

    @Column(length = 36, columnDefinition = "varchar")
    private UUID apiKey;

    @OneToMany(mappedBy = "user")
    private Set<UserIssue> userIssues;

    @Builder
    public User(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String userId, UUID apiKey, Set<UserIssue> userIssues) {
        super(id, version, createdDate, lastModifiedDate);
        this.userId = userId;
        this.apiKey = apiKey;
        this.userIssues = userIssues;
    }
}
