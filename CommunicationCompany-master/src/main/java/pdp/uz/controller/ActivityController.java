package pdp.uz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pdp.uz.payload.ActivityDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.service.ActivityService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/get/all")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_BRANCH_DIRECOTR', 'ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> getAll() {
        ApiResponse apiResponse = activityService.getAll();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_BRANCH_DIRECOTR', 'ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> get(@PathVariable Long id) {
        ApiResponse apiResponse = activityService.get(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addActivity(@Valid @RequestBody ActivityDto dto) {
        ApiResponse apiResponse = activityService.add(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 400).body(apiResponse);
    }

    @GetMapping("/report/excel/{number}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getMyActivityExcel(@PathVariable String number, @RequestParam String from, @RequestParam String to, HttpServletResponse response) {
        ApiResponse apiResponse = activityService.getMyActivityExcel(number, from, to, response);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/report/pdf/{number}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getMyActivityPdf(@PathVariable String number, @RequestParam String from, @RequestParam String to, HttpServletResponse response) {
        ApiResponse apiResponse = activityService.getMyActivityPdf(number, from, to, response);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }
}
