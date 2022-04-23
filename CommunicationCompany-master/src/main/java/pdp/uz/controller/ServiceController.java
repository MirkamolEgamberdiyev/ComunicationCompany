package pdp.uz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pdp.uz.payload.ServiceDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.service.CommunicationService;

import javax.validation.Valid;

@RestController
@RequestMapping( "/api/service")
@RequiredArgsConstructor
public class ServiceController {

    private final CommunicationService communicationService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_USER', 'ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> getServices() {
        ApiResponse apiResponse = communicationService.getAll();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_USER', 'ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> get(@PathVariable Long id) {
        ApiResponse apiResponse = communicationService.get(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> create(@Valid @RequestBody ServiceDto dto) {
        ApiResponse apiResponse = communicationService.create(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 400).body(apiResponse);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> update(@Valid @RequestBody ServiceDto dto, @PathVariable Long id) {
        ApiResponse apiResponse = communicationService.update(dto, id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        ApiResponse apiResponse = communicationService.deleteService(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/top")
    @PreAuthorize("hasRole('ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> getTopServices() {
        ApiResponse apiResponse = communicationService.getTopServices();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/poor")
    @PreAuthorize("hasRole('ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> getPoorServices() {
        ApiResponse apiResponse = communicationService.getPoorServices();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }
}
