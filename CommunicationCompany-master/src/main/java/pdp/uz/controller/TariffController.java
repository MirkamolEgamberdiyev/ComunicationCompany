package pdp.uz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pdp.uz.payload.TariffDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.service.TariffService;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/tariff")
@RequiredArgsConstructor
public class TariffController {

    private final TariffService tariffService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getTariffs () {
        ApiResponse apiResponse = tariffService.getTariffs();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getTariff (@PathVariable Long id) {
        ApiResponse apiResponse = tariffService.getTariff(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> createTariff(@Valid @RequestBody TariffDto dto){
        ApiResponse apiResponse = tariffService.createTariff(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 400).body(apiResponse);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> editTariff(@PathVariable Long id, @Valid @RequestBody TariffDto dto){
        ApiResponse apiResponse = tariffService.editTariff(id, dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> deleteTariff(@PathVariable Long id){
        ApiResponse apiResponse = tariffService.deleteTariff(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/report/top")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_NUMBER_MANAGER')")
    public ResponseEntity<?> getReport(){
        ApiResponse apiResponse = tariffService.getReport();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }
}