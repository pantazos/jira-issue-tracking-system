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
@NoArgsConstructor
@Entity
public class IssueBoard extends BaseEntity {

    @Builder
    public IssueBoard(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, Issue issue, Integer quantity) {
        super(id, version, createdDate, lastModifiedDate);
        this.issue = issue;
        this.quantity = quantity;
    }

    @ManyToOne
    private Issue issue;

    private Integer quantity = 0;

}
