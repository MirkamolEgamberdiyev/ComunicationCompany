package pdp.uz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pdp.uz.payload.AttachPackageDto;
import pdp.uz.payload.AttachServiceDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.service.SubscriberService;


import javax.validation.Valid;


@RestController
@RequestMapping("/api/subscriber")
@RequiredArgsConstructor
public class SubscriberController {

    private final SubscriberService subscriberService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> getSubscribers() {
        ApiResponse apiResponse = subscriberService.getSubscribers();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> getSubscriber(@PathVariable Long id) {
        ApiResponse apiResponse = subscriberService.getSubscriber(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/number/{number}")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> getSubscriber(@PathVariable String number) {
        ApiResponse apiResponse = subscriberService.getSubscriber(number);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @PutMapping("/number/{number}/tariff/{tariffId}")
    @PreAuthorize("hasAnyRole('ROLE_WORKER','ROLE_USER')")
    public ResponseEntity<?> changeTariff(@PathVariable String number, @PathVariable Long tariffId) {
        ApiResponse apiResponse = subscriberService.changeTariff(number, tariffId);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @PostMapping("/attach/to/service")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_USER')")
    public ResponseEntity<?> attachToService(@Valid @RequestBody AttachServiceDto dto) {
        ApiResponse apiResponse = subscriberService.attachToService(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @PostMapping("/attach/to/package")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_USER')")
    public ResponseEntity<?> attachToPackage(@Valid @RequestBody AttachPackageDto dto) {
        ApiResponse apiResponse = subscriberService.attachToPackage(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }
}
