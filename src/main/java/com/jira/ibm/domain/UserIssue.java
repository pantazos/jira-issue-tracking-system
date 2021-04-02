package com.jira.ibm.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class UserIssue extends BaseEntity {

    @Builder
    public UserIssue(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String userRef,
                     User user, IssueStatusEnum issueStatus, String issueStatusCallbackUrl) {
        super(id, version, createdDate, lastModifiedDate);
        this.userRef = userRef;
        this.user = user;
        this.issueStatus = issueStatus;
        this.issueStatusCallbackUrl = issueStatusCallbackUrl;
    }

    private String userRef;

    @ManyToOne
    private User user;

    private IssueStatusEnum issueStatus = IssueStatusEnum.BUG;
    private String issueStatusCallbackUrl;
}
