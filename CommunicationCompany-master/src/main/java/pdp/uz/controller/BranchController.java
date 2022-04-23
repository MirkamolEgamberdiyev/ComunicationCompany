package pdp.uz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pdp.uz.payload.BranchDto;
import pdp.uz.payload.BranchUpdateDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.service.BranchService;


import javax.validation.Valid;


@RestController
@RequestMapping("/api/branch")
@RequiredArgsConstructor
public class BranchController {

   private final BranchService branchService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public ResponseEntity<?> getAll(){
        ApiResponse apiResponse = branchService.getAll();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_BRANCH_MANAGER')")
    public ResponseEntity<?> get(@PathVariable Long id){
        ApiResponse apiResponse = branchService.get(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get/employees/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_BRANCH_MANAGER', 'ROLE_BRANCH_DIRECTOR')")
    public ResponseEntity<?> getBranchEmployees(@PathVariable Long id){
        ApiResponse apiResponse = branchService.getBranchEmployees(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public ResponseEntity<?> addBranch(@Valid @RequestBody BranchDto dto){
        ApiResponse apiResponse = branchService.addBranch(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 400).body(apiResponse);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public ResponseEntity<?> updateBranch(@PathVariable Long id, @Valid @RequestBody BranchUpdateDto dto){
        ApiResponse apiResponse = branchService.updateBranch(dto,id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public ResponseEntity<?> deleteBranch(@PathVariable Long id){
        ApiResponse apiResponse = branchService.deleteBranch(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 404).body(apiResponse);
    }
}
