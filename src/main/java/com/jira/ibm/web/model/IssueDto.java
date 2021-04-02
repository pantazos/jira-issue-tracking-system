package com.jira.ibm.web.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IssueDto extends BaseItem {

    @Builder
    public IssueDto(UUID id, Integer version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate,
                    String issueTitle, IssueStyleEnum issueStyle, Integer quantity) {
        super(id, version, createdDate, lastModifiedDate);
        this.issueTitle = issueTitle;
        this.issueStyle = issueStyle;
        this.quantity = quantity;
    }

    private String issueTitle;
    private IssueStyleEnum issueStyle;
    private Integer quantity;

}
