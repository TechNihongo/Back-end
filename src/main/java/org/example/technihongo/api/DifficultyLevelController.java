package org.example.technihongo.api;

import org.example.technihongo.entities.DifficultyLevel;
import org.example.technihongo.enums.DifficultyLevelEnum;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.DifficultyLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/difficulty-level")
public class DifficultyLevelController {
    @Autowired
    private DifficultyLevelService difficultyLevelService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllDifficultyLevel() throws Exception {
        List<DifficultyLevel> difficultyLevelList = difficultyLevelService.viewAllDifficultyLevels();
        if(difficultyLevelList.isEmpty()){
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(false)
                    .message("List DifficultyLevel is empty!")
                    .build());
        }else{
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Get All Difficulty Level: ")
                    .data(difficultyLevelList)
                    .build());
        }
    }
    @GetMapping("/tag/{tag}")
    public ResponseEntity<ApiResponse> viewDifficultyLevelByTag(@PathVariable DifficultyLevelEnum tag) {
        try {
            DifficultyLevel difficultyLevel = difficultyLevelService.viewDifficultyLevelByTag(tag);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Difficulty Level found")
                    .data(difficultyLevel)
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

}
