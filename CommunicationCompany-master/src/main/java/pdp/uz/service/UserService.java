package pdp.uz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pdp.uz.entity.Client;
import pdp.uz.entity.Employee;
import pdp.uz.repository.ClientRepo;
import pdp.uz.repository.EmployeeRepo;


import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ClientRepo clientRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (employeeRepo.existsByUsernameAndActiveTrue(username)) {
            Optional<Employee> optionalEmployee = employeeRepo.findByUsernameAndActiveTrue(username);
            if (!optionalEmployee.isPresent()) {
                throw new UsernameNotFoundException("User not found");
            }
            Employee employee = optionalEmployee.get();
            Collection<GrantedAuthority> authorities = new HashSet<>();
            employee.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
            return new User(employee.getUsername(), employee.getPassword(), authorities);
        }
        Optional<Client> optionalClient = clientRepo.findByUsername(username);
        if (!optionalClient.isPresent())
            throw new UsernameNotFoundException("User not found");
        Client client = optionalClient.get();
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(client.getRole().getName()));
        return new User(client.getUsername(), client.getPassword(), authorities);
    }
}
