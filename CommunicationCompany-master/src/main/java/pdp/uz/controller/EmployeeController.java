package pdp.uz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pdp.uz.payload.AttachRoleDto;
import pdp.uz.payload.EmployeeDto;
import pdp.uz.payload.RoleDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.service.EmployeeService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @GetMapping("/get/all")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    public ResponseEntity<?> getAll() {
        ApiResponse apiResponse = employeeService.getAll();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER','ROLE_BRANCH_MANAGER','ROLE_BRANCH_DIRECTOR')")
    public ResponseEntity<?> getEmployee(@PathVariable Long id) {
        ApiResponse apiResponse = employeeService.getEmployee(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 404).body(apiResponse);
    }

    @GetMapping("/get/{username}")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER','ROLE_BRANCH_MANAGER','ROLE_BRANCH_DIRECTOR')")
    public ResponseEntity<?> getEmployee(@PathVariable String username) {
        ApiResponse apiResponse = employeeService.get(username);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 404).body(apiResponse);
    }

    @PostMapping("/add/employee")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_MANAGER', 'ROLE_BRANCH_MANAGER', 'ROLE_BRANCH_DIRECTOR')")
    public ResponseEntity<?> addEmployee(@Valid @RequestBody EmployeeDto dto) {
        ApiResponse apiResponse = employeeService.addEmployee(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 400).body(apiResponse);
    }

    @PostMapping("/add/role")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public ResponseEntity<?> addRole(@Valid @RequestBody RoleDto dto) {
        ApiResponse apiResponse = employeeService.addRole(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 404).body(apiResponse);
    }

    @PostMapping("/attach")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_BRANCH_MANAGER', 'ROLE_BRANCH_DIRECTOR')")
    public ResponseEntity<?> attachRole(@Valid @RequestBody AttachRoleDto dto) {
        ApiResponse apiResponse = employeeService.attachRole(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_MANAGER', 'ROLE_BRANCH_MANAGER', 'ROLE_BRANCH_DIRECTOR')")
    public ResponseEntity<?> update(@Valid @RequestBody EmployeeDto dto, @PathVariable Long id){
        ApiResponse apiResponse = employeeService.update(dto, id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_BRANCH_MANAGER', 'ROLE_BRANCH_DIRECTOR')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ApiResponse apiResponse = employeeService.delete(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 404).body(apiResponse);
    }
}
