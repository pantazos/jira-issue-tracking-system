package com.jira.ibm.services;

import com.jira.ibm.domain.Issue;
import com.jira.ibm.repositories.IssueRepository;
import com.jira.ibm.web.mappers.IssueMapper;
import com.jira.ibm.web.model.IssueDto;
import com.jira.ibm.web.model.IssuePagedList;
import com.jira.ibm.web.model.IssueStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;

    @Override
    public IssuePagedList listIssues(String issueTitle, IssueStyleEnum issueStyle, PageRequest pageRequest,
                                     Boolean showBoard) {
        log.info("Listing all available issues");

        IssuePagedList issuePagedList;
        Page<Issue> issuePage;

        if (!StringUtils.isEmpty(issueTitle) && !StringUtils.isEmpty(issueStyle)) {
            // search ...
            issuePage = issueRepository.findAllByIssueTitleAndIssueStyle(issueTitle, issueStyle, pageRequest);
        } else if (!StringUtils.isEmpty(issueTitle) && !StringUtils.isEmpty(issueStyle)) {
            // search for issue_service name
            issuePage = issueRepository.findAllByIssueTitle(issueTitle, pageRequest);
        } else if (StringUtils.isEmpty(issueTitle) && !StringUtils.isEmpty(issueStyle)) {
            // search for issue_service style
            issuePage = issueRepository.findAllByIssueStyle(issueStyle, pageRequest);
        } else {
            issuePage = issueRepository.findAll(pageRequest);
        }

        if (showBoard) {
            issuePagedList = new IssuePagedList(issuePage
                    .getContent()
                    .stream()
                    .map(issueMapper::issueToIssueDto)
                    .collect(Collectors.toList()),
                    PageRequest
                            .of(issuePage.getPageable().getPageNumber(),
                                    issuePage.getPageable().getPageSize()),
                    issuePage.getTotalElements());

        } else {
            issuePagedList = new IssuePagedList(issuePage
                    .getContent()
                    .stream()
                    .map(issueMapper::issueToIssueDto)
                    .collect(Collectors.toList()),
                    PageRequest
                            .of(issuePage.getPageable().getPageNumber(),
                                    issuePage.getPageable().getPageSize()),
                    issuePage.getTotalElements());
        }

        return issuePagedList;
    }

    @Override
    public IssueDto findIssueById(UUID issueId, Boolean showBoard) {
        log.info("Finding issue by id: " + issueId);

        Optional<Issue> issueOptional = issueRepository.findById(issueId);

        if (issueOptional.isPresent()) {
            log.info("Found IssueId: " + issueId);

            if (showBoard) {
                return issueMapper.issueToIssueDto((issueOptional.get()));
            } else {
                return issueMapper.issueToIssueDto((issueOptional.get()));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found. UUID: " + issueId);
        }
    }

    @Override
    public IssueDto saveIssue(IssueDto issueDto) {
        return issueMapper.issueToIssueDto(issueRepository.save(issueMapper.issueDtoToIssue(issueDto)));
    }

    @Override
    public void updateIssue(UUID issueId, IssueDto issueDto) {
        Optional<Issue> issueOptional = issueRepository.findById(issueId);

        issueOptional.ifPresentOrElse(issue -> {
            issue.setIssueTitle(issueDto.getIssueTitle());
            // description
            issueRepository.save(issue);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found. UUID: " + issueId);
        });
    }

    @Override
    public void deleteById(UUID issueId) {
        issueRepository.deleteById(issueId);
    }
}
