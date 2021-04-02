package com.jira.ibm.web.mappers;

import com.jira.ibm.domain.Issue;
import com.jira.ibm.domain.IssueBoard;
import com.jira.ibm.web.model.IssueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class IssueMapperDecorator implements IssueMapper {

    private IssueMapper issueMapper;

    @Autowired
    @Qualifier("delegate")
    public void setIssueMapper(IssueMapper issueMapper) {
        this.issueMapper = issueMapper;
    }

    @Override
    public IssueDto issueToIssueDto(Issue issue) {
        IssueDto issueDto = issueMapper.issueToIssueDto(issue);

        if (issue.getIssueBoard() != null &&
                issue.getIssueBoard().size() > 0) {
            issueDto.setQuantity(issue.getIssueBoard()
                    .stream().map(IssueBoard::getQuantity)
                    .reduce(0, Integer::sum));
        }

        return issueDto;
    }
}
