package com.jira.ibm.web.controllers;

import com.jira.ibm.domain.Issue;
import com.jira.ibm.repositories.IssueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class IssueControllerTest {

    @Mock
    IssueRepository issueRepository;

    @InjectMocks
    IssueController controller;
    List<Issue> issueList;
    UUID uuid;
    Issue issue;

    MockMvc mockMvc;
    Page<Issue> issues;
    Page<Issue> pagedResponse;

    @BeforeEach
    void setUp() {
        issueList = new ArrayList<Issue>();
        issueList.add(Issue.builder().build());
        issueList.add(Issue.builder().build());
        pagedResponse = new PageImpl(issueList);

        final String id = "493410b3-dd0b-4b78-97bf-289f50f6e74f";
        uuid = UUID.fromString(id);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void findIssues() throws Exception {
        mockMvc.perform(get("/issues/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("issues/findIssues"))
                .andExpect(model().attributeExists("issue"));
        verifyZeroInteractions(issueRepository);
    }

    void processFindFormReturnMany() throws Exception {
        when(issueRepository.findAllByIssueTitle(anyString(), PageRequest.of(0,
                10, Sort.Direction.DESC, "issueTitle"))).thenReturn(pagedResponse);
        mockMvc.perform(get("/issues"))
                .andExpect(status().isOk())
                .andExpect(view().name("issues/issueList"))
                .andExpect(model().attribute("selections", hasSize(2)));
    }

    @Test
    void showIssue() throws Exception {
        when(issueRepository.findById(uuid)).thenReturn(Optional.of(Issue.builder().id(uuid).build()));
        mockMvc.perform(get("/issues/" + uuid))
                .andExpect(status().isOk())
                .andExpect(view().name("issues/issueDetails"))
                .andExpect(model().attribute("issue", hasProperty("id", is(uuid))));
    }

    @Test
    void initCreationForm() throws Exception {
        mockMvc.perform(get("/issues/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("issues/createIssue"))
                .andExpect(model().attributeExists("issue"));
        verifyZeroInteractions(issueRepository);
    }

    @Test
    void processCreationForm() throws Exception {
        when(issueRepository.save(ArgumentMatchers.any())).thenReturn(Issue.builder().id(uuid).build());
        mockMvc.perform(post("/issues/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/issues/" + uuid))
                .andExpect(model().attributeExists("issue"));
        verify(issueRepository).save(ArgumentMatchers.any());
    }

    @Test
    void initUpdateIssueForm() throws Exception {
        when(issueRepository.findById(uuid)).thenReturn(Optional.of(Issue.builder().id(uuid).build()));
        mockMvc.perform(get("/issues/" + uuid + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("issues/createOrUpdateIssue"))
                .andExpect(model().attributeExists("issue"));
        verifyZeroInteractions(issueRepository);
    }

    @Test
    void processUpdationForm() throws Exception {
        when(issueRepository.save(ArgumentMatchers.any())).thenReturn(Issue.builder().id(uuid).build());

        mockMvc.perform(post("/issues/" + uuid + "/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/issues/" + uuid))
                .andExpect(model().attributeExists("issue"));

        verify(issueRepository).save(ArgumentMatchers.any());
    }
}
