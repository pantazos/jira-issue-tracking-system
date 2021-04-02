package com.jira.ibm.services;

import com.jira.ibm.web.model.IssueDto;
import com.jira.ibm.web.model.IssuePagedList;
import com.jira.ibm.web.model.IssueStyleEnum;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface IssueService {

    IssuePagedList listIssues(String issueTitle, IssueStyleEnum issueStyle, PageRequest pageRequest, Boolean showBoard);

    IssueDto findIssueById(UUID issueId, Boolean showBoard);

    IssueDto saveIssue(IssueDto issueDto);

    void updateIssue(UUID issueId, IssueDto issueDto);

    void deleteById(UUID issueId);

}
