package pdp.uz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pdp.uz.payload.USSDCodeDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.service.USSDService;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/ussd")
@RequiredArgsConstructor
public class USSDController {

    private final USSDService ussdService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_USER','ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> getCodes() {
        ApiResponse apiResponse = ussdService.getCodes();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/get/{code}")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_USER','ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> getCode(@PathVariable String code) {
        ApiResponse apiResponse = ussdService.getCode(code);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> create(@Valid @RequestBody USSDCodeDto dto) {
        ApiResponse apiResponse = ussdService.create(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody USSDCodeDto dto) {
        ApiResponse apiResponse = ussdService.update(dto, id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @DeleteMapping("/deActivate/{code}")
    @PreAuthorize("hasRole('ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> deActivate(@PathVariable String code) {
        ApiResponse apiResponse = ussdService.deActivate(code);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }
}
