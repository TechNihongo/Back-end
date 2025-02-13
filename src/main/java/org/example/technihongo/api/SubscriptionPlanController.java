package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.SubscriptionDTO;
import org.example.technihongo.dto.UpdateSubscriptionDTO;
import org.example.technihongo.entities.SubscriptionPlan;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription")
@RequiredArgsConstructor

public class SubscriptionPlanController {
    @Autowired
    private SubscriptionPlanService subscriptionPlanService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createSubscriptionPlan(@RequestBody SubscriptionDTO subscriptionDTO) {
        try {
            SubscriptionPlan created = subscriptionPlanService.createSubscriptionPlan(subscriptionDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Subscription plan created successfully")
                    .data(created)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateSubscriptionPlan(
            @PathVariable Integer id,
            @RequestBody UpdateSubscriptionDTO updateDTO) {
        try {
            SubscriptionPlan updated = subscriptionPlanService.updateSubscriptionPlan(id, updateDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Subscription plan updated successfully")
                    .data(updated)
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteSubscriptionPlan(@PathVariable Integer id) {
        try {
            subscriptionPlanService.deleteSubscriptionPlan(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Subscription plan deleted successfully")
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllSubscription() throws Exception {
        List<SubscriptionPlan> subscriptionList = subscriptionPlanService.subscriptionList();
        if(subscriptionList.isEmpty()){
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(false)
                    .message("List is empty!")
                    .build());
        }else{
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Get All Subscription Plan")
                    .data(subscriptionList)
                    .build());
        }
    }
}
