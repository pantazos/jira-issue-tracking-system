package com.jira.ibm.repositories;

import com.jira.ibm.domain.Issue;
import com.jira.ibm.web.model.IssueStyleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IssueRepository extends JpaRepository<Issue, UUID> {

    Page<Issue> findAllByIssueTitle(String issueTitle, Pageable pageable);

    Page<Issue> findAllByIssueTitleIsLike(String issueTitle, Pageable pageable);

    Page<Issue> findAllByIssueStyle(IssueStyleEnum issueStyle, Pageable pageable);

    Page<Issue> findAllByIssueTitleAndIssueStyle(String issueTitle, IssueStyleEnum issueStyleEnum, Pageable pageable);

}
