package pdp.uz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pdp.uz.payload.PackageDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.service.PackageService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/package")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll () {
        ApiResponse apiResponse = packageService.getAll();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get (@PathVariable Long id) {
        ApiResponse apiResponse = packageService.get(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> create (@Valid @RequestBody PackageDto dto) {
        ApiResponse apiResponse = packageService.create(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 400).body(apiResponse);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody PackageDto dto) {
        ApiResponse apiResponse = packageService.update(id,dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ApiResponse apiResponse = packageService.delete(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/report/top")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> getReport(){
        ApiResponse apiResponse = packageService.getReport();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }
}