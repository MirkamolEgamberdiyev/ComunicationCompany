package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import pdp.uz.entity.Employee;
import pdp.uz.entity.TourniquetCard;
import pdp.uz.entity.TourniquetHistory;
import pdp.uz.payload.TourniquetCardDto;
import pdp.uz.payload.TourniquetHistoryDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.repository.EmployeeRepo;
import pdp.uz.repository.TourniquetHistoryRepo;
import pdp.uz.repository.TourniquetRepo;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static pdp.uz.payload.helpers.Checking.access;

@Service
@RequiredArgsConstructor
public class TourniquetService {

    private final EmployeeRepo employeeRepo;
    private final TourniquetRepo tourniquetRepo;
    private final TourniquetHistoryRepo tourniquetHistoryRepo;

    public ApiResponse create(TourniquetCardDto dto) {
        Optional<Employee> optionalEmployee = employeeRepo.findByUsernameAndActiveTrue(dto.getEmployeeUsername());
        if (!optionalEmployee.isPresent()) {
            return new ApiResponse("Employee not found", false);
        }
        Employee employee = optionalEmployee.get();
        if (!access(getCurrentUser(), employee)) {
            return new ApiResponse("You don't have access", false);
        }
        TourniquetCard card = new TourniquetCard();
        card.setBranch(employee.getBranch());
        card.setEmployee(employee);
        tourniquetRepo.save(card);
        return new ApiResponse("Tourniquet card is successfully created", true);
    }

    public ApiResponse edit(TourniquetCardDto dto, String username) {
        Optional<Employee> optionalEmployee = employeeRepo.findByUsernameAndActiveTrue(dto.getEmployeeUsername());
        if (!optionalEmployee.isPresent()) {
            return new ApiResponse("Employee not found", false);
        }
        Optional<TourniquetCard> optionalTourniquetCard = tourniquetRepo.findByEmployee_UsernameAndStatusTrue(username);
        Employee employee = optionalEmployee.get();
        if (!access(getCurrentUser(), employee)) {
            return new ApiResponse("You don't have access", false);
        }
        if (optionalTourniquetCard.isPresent()) {
            TourniquetCard card = optionalTourniquetCard.get();
            card.setEmployee(employee);
            tourniquetRepo.save(card);
            return new ApiResponse("Card updated", true);
        }
        return new ApiResponse("Card not found", false);
    }

    public ApiResponse enter(TourniquetHistoryDto dto) {
        Optional<TourniquetCard> cardOptional = tourniquetRepo.findById(UUID.fromString(dto.getCardId()));
        if (!cardOptional.isPresent()) {
            return new ApiResponse("Card not found", false);
        }
        TourniquetCard card = cardOptional.get();
        if (card.isStatus()) {
            TourniquetHistory tourniquetHistory = new TourniquetHistory();
            tourniquetHistory.setEnteredAt(LocalDateTime.now());
            tourniquetHistory.setTourniquetCard(card);
            tourniquetHistoryRepo.save(tourniquetHistory);
            return new ApiResponse("Entered", true);
        }
        return new ApiResponse("Expiration date of the card", false);
    }

    public ApiResponse exit(TourniquetHistoryDto dto) {
        Optional<TourniquetCard> cardOptional = tourniquetRepo.findById(UUID.fromString(dto.getCardId()));
        if (!cardOptional.isPresent()) {
            return new ApiResponse("Card not found", false);
        }
        TourniquetCard card = cardOptional.get();
        if (card.isStatus()) {
            TourniquetHistory tourniquetHistory = new TourniquetHistory();
            tourniquetHistory.setExitedAt(LocalDateTime.now());
            tourniquetHistory.setTourniquetCard(card);
            tourniquetHistoryRepo.save(tourniquetHistory);
            return new ApiResponse("Exited", true);
        }
        return new ApiResponse("Expiration date of the card", false);
    }

    public ApiResponse activate(TourniquetHistoryDto dto) {
        Optional<TourniquetCard> optionalTourniquetCard =
                tourniquetRepo.findById(UUID.fromString(dto.getCardId()));
        if (optionalTourniquetCard.isPresent()) {
            TourniquetCard card = optionalTourniquetCard.get();
            card.setStatus(true);
            tourniquetRepo.save(card);
            return new ApiResponse("Card activated", true);
        }
        return new ApiResponse("Card not found", true);
    }

    public ApiResponse delete(String id) {
        Optional<TourniquetCard> optionalTourniquetCard =
                tourniquetRepo.findById(UUID.fromString(id));
        if (!optionalTourniquetCard.isPresent()) {
            return new ApiResponse("Card not found", true);
        }
        TourniquetCard card = optionalTourniquetCard.get();
        card.setStatus(false);
        tourniquetRepo.save(card);
        return new ApiResponse("Card deleted", true);
    }

    public Employee getCurrentUser() {
        return employeeRepo.findByUsernameAndActiveTrue(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get();
    }
}
