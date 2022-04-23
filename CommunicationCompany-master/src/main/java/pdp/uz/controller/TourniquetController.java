package pdp.uz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pdp.uz.payload.TourniquetCardDto;
import pdp.uz.payload.TourniquetHistoryDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.service.TourniquetService;


import javax.validation.Valid;


@RestController
@RequestMapping(path = "/api/tourniquet")
@RequiredArgsConstructor
public class TourniquetController {

   private final TourniquetService tourniquetService;

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER', 'ROLE_BRANCH_DIRECTOR')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody TourniquetCardDto dto) {
        ApiResponse apiResponse = tourniquetService.create(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_BRANCH_DIRECTOR', 'ROLE_MANAGER')")
    @PatchMapping("/update/{username}")
    public ResponseEntity<?> edit(@PathVariable String username, @Valid @RequestBody TourniquetCardDto dto){
        ApiResponse apiResponse = tourniquetService.edit(dto,username);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_BRANCH_DIRECTOR')")
    @PatchMapping("/update")
    public ResponseEntity<?> activate(@Valid @RequestBody TourniquetHistoryDto dto) {
        ApiResponse apiResponse = tourniquetService.activate(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/enter")
    public ResponseEntity<?> enter(@Valid @RequestBody TourniquetHistoryDto dto){
        ApiResponse apiResponse = tourniquetService.enter(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }

    @PostMapping("/exit")
    public ResponseEntity<?> exit(@Valid @RequestBody TourniquetHistoryDto dto){
        ApiResponse apiResponse = tourniquetService.exit(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_DIRECTOR', 'ROLE_BRANCH_DIRECTOR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        ApiResponse apiResponse = tourniquetService.delete(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }
}

