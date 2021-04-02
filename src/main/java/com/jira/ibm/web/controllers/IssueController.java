package com.jira.ibm.web.controllers;

import com.jira.ibm.domain.Issue;
import com.jira.ibm.repositories.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/issues")
@Controller
public class IssueController {

    private final IssueRepository issueRepository;

    @RequestMapping("/find")
    public String findIssues(Model model) {
        model.addAttribute("issue", Issue.builder().build());
        return "issues/findIssues";
    }

    @GetMapping
    public String processFindFormReturnMany(Issue issue, BindingResult result, Model model) {
        // find issues by name
        Page<Issue> pagedResult = issueRepository.findAllByIssueTitleIsLike("%" + issue.getIssueTitle() + "%",
                createPageRequest(0, 10, Sort.Direction.DESC, "issueTitle"));
        List<Issue> issueList = pagedResult.getContent();

        if (issueList.isEmpty()) {
            // no issues found
            result.rejectValue("issueTitle", "notFound", "not found");
            return "issues/findIssues";
        } else if (issueList.size() == 1) {
            // 1 issue found
            issue = issueList.get(0);
            return "redirect:/issues/" + issue.getId();
        } else {
            //  multiple issues found
            model.addAttribute("selections", issueList);
            return "issues/issueList";
        }
    }

    @GetMapping("/{issueId}")
    public ModelAndView showIssue(@PathVariable UUID issueId) {
        ModelAndView mav = new ModelAndView("issues/issueDetails");
        mav.addObject(issueRepository.findById(issueId).get());
        return mav;
    }

    @GetMapping("/new")
    public String initCreationForm(Model model) {
        model.addAttribute("issue", Issue.builder().build());
        return "issues/createIssue";
    }

    @PostMapping("/new")
    public String processCreationForm(Issue issue) {
        Issue newIssue = Issue.builder()
                .issueTitle(issue.getIssueTitle())
                .issueStyle(issue.getIssueStyle())
                .description(issue.getDescription())
                .build();

        Issue savedIssue = issueRepository.save(newIssue);

        return "redirect:/issues/" + savedIssue.getId();
    }

    @GetMapping("/{issueId}/edit")
    public String initUpdateIssueForm(@PathVariable UUID issueId, Model model) {
        if (issueRepository.findById(issueId).isPresent())
            model.addAttribute("issue", issueRepository.findById(issueId).get());
        return "issues/createOrUpdateIssue";
    }

    @PostMapping("/{issueId}/edit")
    public String processUpdateForm(@Valid Issue issue, BindingResult result) {
        if (result.hasErrors()) {
            return "issues/createOrUpdateIssue";
        } else {
            Issue savedIssue = issueRepository.save(issue);

            return "redirect:/issues/" + savedIssue.getId();
        }
    }

    private PageRequest createPageRequest(int page, int size, Sort.Direction sortDirection, String propertyName) {
        return PageRequest.of(page, size, Sort.by(sortDirection, propertyName));
    }

}
