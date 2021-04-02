package com.jira.ibm.web.mappers;

import com.jira.ibm.domain.Issue;
import com.jira.ibm.web.model.IssueDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(uses = DateMapper.class)
@DecoratedWith(IssueMapperDecorator.class)
public interface IssueMapper {

    IssueDto issueToIssueDto(Issue issue);

    Issue issueDtoToIssue(IssueDto issueDto);

}
