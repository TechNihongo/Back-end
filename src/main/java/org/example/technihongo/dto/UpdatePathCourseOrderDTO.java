package org.example.technihongo.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePathCourseOrderDTO {
    private List<PathCourseOrderItem> newPathCourseOrders;

    @Data
    public static class PathCourseOrderItem {
        private Integer pathCourseId;
        private Integer courseOrder;
    }
}
