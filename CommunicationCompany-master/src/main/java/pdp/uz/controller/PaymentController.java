package pdp.uz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pdp.uz.payload.PaymentDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.service.PaymentService;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> getAll() {
        ApiResponse apiResponse = paymentService.getAll();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> getPayment(@PathVariable Long id) {
        ApiResponse apiResponse = paymentService.get(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PaymentDto dto) {
        ApiResponse apiResponse = paymentService.create(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 400).body(apiResponse);
    }
}
