package com.jira.ibm.web.controllers.api;

import com.jira.ibm.services.IssueService;
import com.jira.ibm.web.model.IssueDto;
import com.jira.ibm.web.model.IssuePagedList;
import com.jira.ibm.web.model.IssueStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@RestController
public class IssueRestController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final IssueService issueService;

    @GetMapping(produces = {"application/json"}, path = "issue")
    public ResponseEntity<IssuePagedList> listIssues(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                     @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                     @RequestParam(value = "issueTitle", required = false) String issueTitle,
                                                     @RequestParam(value = "issueStyle", required = false) IssueStyleEnum issueStyle,
                                                     @RequestParam(value = "showBoard", required = false) Boolean showBoard) {
        log.info("Listing all available issues");

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        IssuePagedList issueList = issueService.listIssues(issueTitle, issueStyle, PageRequest.of(pageNumber, pageSize), showBoard);

        return new ResponseEntity<>(issueList, HttpStatus.OK);
    }

    @GetMapping(path = {"issue/{issueId}"}, produces = {"application/json"})
    public ResponseEntity<IssueDto> getIssueById(@PathVariable("issueId") UUID issueId,
                                                 @RequestParam(value = "showBoard", required = false) Boolean showBoard) {
        log.info("Get request for IssueId: " + issueId);

        if (showBoard == null) {
            showBoard = false;
        }

        return new ResponseEntity<>(issueService.findIssueById(issueId, showBoard), HttpStatus.OK);
    }

    @PostMapping(path = "issue")
    public ResponseEntity saveNewIssue(@Valid @RequestBody IssueDto issueDto) {
        IssueDto savedDto = issueService.saveIssue(issueDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/api/v1/issue_service/" + savedDto.getId().toString());

        return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(path = {"issue/{issueId}"}, produces = {"application/json"})
    public ResponseEntity updateIssue(@PathVariable("issueId") UUID issueId, @Valid @RequestBody IssueDto issueDto) {
        issueService.updateIssue(issueId, issueDto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping({"issue/{issueId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@PathVariable("issueId") UUID issueId) {
        issueService.deleteById(issueId);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<List> badRequestHandler(ConstraintViolationException cve) {
        List<String> errors = new ArrayList<>(cve.getConstraintViolations().size());

        cve.getConstraintViolations().forEach(constraintViolation -> {
            errors.add(constraintViolation.getPropertyPath().toString() + " : " + constraintViolation.getMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
