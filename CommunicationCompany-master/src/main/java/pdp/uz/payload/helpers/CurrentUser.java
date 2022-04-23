package pdp.uz.payload.helpers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import pdp.uz.entity.Client;
import pdp.uz.entity.Employee;
import pdp.uz.repository.ClientRepo;
import pdp.uz.repository.EmployeeRepo;

@RequiredArgsConstructor
@Component
public class CurrentUser {

    private final EmployeeRepo employeeRepo;

    private final ClientRepo clientRepo;

    public Employee getCurrentUser() {
        return employeeRepo.findByUsernameAndActiveTrue(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get();
    }

    public Client getCurrentClient() {
        return clientRepo.findByUsername(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get();
    }
}
