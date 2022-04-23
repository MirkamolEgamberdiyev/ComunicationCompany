package pdp.uz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pdp.uz.payload.ClientDto;
import pdp.uz.payload.ClientUpdateDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.service.ClientService;


import javax.validation.Valid;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_WORKER', 'ROLE_BRANCH_MANAGER')")
    public ResponseEntity<?> getClients() {
        ApiResponse apiResponse = clientService.getAll();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_WORKER', 'ROLE_BRANCH_MANAGER')")
    public ResponseEntity<?> getClient(@PathVariable Long id) {
        ApiResponse apiResponse = clientService.get(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_WORKER')")
    public ResponseEntity<?> createClient(@Valid @RequestBody ClientDto dto) {
        ApiResponse apiResponse = clientService.create(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_USER')")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @Valid @RequestBody ClientUpdateDto dto) {
        ApiResponse apiResponse = clientService.update(id, dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }
}
