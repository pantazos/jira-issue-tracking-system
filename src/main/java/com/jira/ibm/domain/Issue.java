package com.jira.ibm.domain;

import com.jira.ibm.web.model.IssueStyleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Issue extends BaseEntity {

    private String issueTitle;
    private IssueStyleEnum issueStyle;
    private String description;

    private String assignedTo;

    private String lastModifiedUser;
    private String issueCreatedUser;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private Set<IssueBoard> issueBoard = new HashSet<>();

    @Builder
    public Issue(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String issueTitle,
                 IssueStyleEnum issueStyle, String description, String assignedTo,
                 String lastModifiedUser, String issueCreatedUser) {
        super(id, version, createdDate, lastModifiedDate);
        this.issueTitle = issueTitle;
        this.issueStyle = issueStyle;
        this.description = description;
        this.assignedTo = assignedTo;
        this.lastModifiedUser = lastModifiedUser;
        this.issueCreatedUser = issueCreatedUser;
    }
}
