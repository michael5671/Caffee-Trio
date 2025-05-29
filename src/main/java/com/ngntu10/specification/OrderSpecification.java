package com.ngntu10.specification;

import com.ngntu10.entity.Order;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class OrderSpecification {
    public static Specification<Order> byStatus(String status){
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            List<String> statusList = Arrays.asList(status.split("\\|"));
            return root.get("orderStatus").in(statusList);
        };
    }

    public static Specification<Order> hasUserId(UUID userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }


    public static Specification<Order> filterOrderByUser(String statuses, UUID userId, String cityId, boolean fetchOrderItems) {
        if (statuses == null && userId == null && cityId == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) -> {
            if (fetchOrderItems) {
                root.fetch("orderItemList", JoinType.LEFT);
                query.distinct(true);
            }
            query.distinct(true);
            return Specification.where(byStatus(statuses))
                    .and(hasUserId(userId))
                    .toPredicate(root, query, criteriaBuilder);
        };
    }


    public static Specification<Order> filterOrder(String statuses, String cityId, String search) {
        if (statuses == null && cityId == null && search == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return Specification.where(byStatus(statuses))
                    .toPredicate(root, query, criteriaBuilder);
        };
    }
}
