package com.micnusz.edns.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignedPayload implements EventPayload {
    private String taskName;
    private String taskDescription;
    private String assignedBy;
    private String dueDate;
}
