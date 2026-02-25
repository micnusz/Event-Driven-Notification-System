package com.micnusz.edns.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public record TaskAssignedPayload (
         String taskName,
         String taskDescription,
         String assignedBy,
         String dueDate
) implements EventPayload {

}
