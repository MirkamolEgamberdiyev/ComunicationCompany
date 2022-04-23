package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pdp.uz.entity.Branch;
import pdp.uz.entity.Employee;
import pdp.uz.entity.Role;
import pdp.uz.entity.enums.BranchType;
import pdp.uz.entity.enums.RoleName;
import pdp.uz.payload.AttachRoleDto;
import pdp.uz.payload.EmployeeDto;
import pdp.uz.payload.RoleDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.payload.helpers.Checking;
import pdp.uz.payload.helpers.CurrentUser;
import pdp.uz.repository.BranchRepo;
import pdp.uz.repository.EmployeeRepo;
import pdp.uz.repository.RoleRepo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static pdp.uz.payload.helpers.Checking.access;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepo employeeRepo;

    private final RoleRepo roleRepo;

    private final BranchRepo branchRepo;

    private final PasswordEncoder passwordEncoder;

    private final CurrentUser currentUser;


    public ApiResponse getAll() {
        List<Employee> employees = employeeRepo.findAllByActiveTrue();
        return new ApiResponse("OK", true, employees);
    }



    public ApiResponse getEmployee(Long id) {
        Optional<Employee> optionalEmployee = employeeRepo.findByIdAndActiveTrue(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            return new ApiResponse("OK", true, employee);
        }
        return new ApiResponse("Employee not found", false);
    }



    public ApiResponse get(String username) {
        Optional<Employee> employeeOptional = employeeRepo.findByUsernameAndActiveTrue(username);
        if (!employeeOptional.isPresent()) {
            return new ApiResponse("Employee not found", false);
        }
        Employee employee = employeeOptional.get();
        return new ApiResponse("OK", true, employee);
    }

    public ApiResponse addRole(RoleDto dto) {
        Role role = Role.builder().name(dto.getName()).build();
        return new ApiResponse("Created", true, roleRepo.save(role));
    }



    public ApiResponse addEmployee(EmployeeDto dto) {
        if (employeeRepo.existsByUsername(dto.getUsername())) {
            return new ApiResponse("This login has already existed", false);
        }
        Optional<Branch> optionalBranch = branchRepo.findByIdAndActiveTrue(dto.getBranchId());
        if (!optionalBranch.isPresent()) {
            return new ApiResponse("Branch not found", false);
        }
        Branch branch = optionalBranch.get();
        Employee employee = new Employee();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee active = employeeRepo.findByUsernameAndActiveTrue(user.getUsername()).get();
        if (active.getBranch().getType() == BranchType.BRANCH
                && !branch.getId().equals(active.getBranch().getId())) {
            return new ApiResponse("You don't have access", false);
        }

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setUsername(dto.getUsername());
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employee.setBranch(branch);
        employeeRepo.save(employee);
        attachRole(new AttachRoleDto(dto.getUsername(), RoleName.ROLE_WORKER.name()));
        return new ApiResponse("Created", true);
    }



    public ApiResponse attachRole(AttachRoleDto dto) {
        Optional<Employee> optionalEmployee = employeeRepo.findByUsernameAndActiveTrue(dto.getUsername());
        if (!optionalEmployee.isPresent()) {
            return new ApiResponse("Employee not found", false);
        }
        Optional<Role> optionalRole = roleRepo.findByName(dto.getRoleName());
        if (!optionalRole.isPresent()) {
            return new ApiResponse("Role not found", false);
        }
        Role role = optionalRole.get();
        Employee employee = optionalEmployee.get();

        Employee active = currentUser.getCurrentUser(); ;
        boolean access = false;
        if ((active.getBranch().getType() == BranchType.BRANCH
                && active.getBranch().getId().equals(employee.getBranch().getId())) || active.getBranch().getType() == BranchType.MAIN) {
            for (Role activeRole : active.getRoles()) {
                if (Checking.accessRole(activeRole.getName(), role.getName(), active.getBranch().getType(), employee.getBranch().getType())) {
                    access = true;
                    break;
                }
            }
        }

        if (access) {
            employee.getRoles().add(role);
            employeeRepo.save(employee);
            return new ApiResponse("OK", true);
        }
        return new ApiResponse("You cannot attach director", false);
    }


    public ApiResponse update(EmployeeDto dto, Long id) {
        if (employeeRepo.existsByUsernameAndIdNot(dto.getUsername(), id)) {
            return new ApiResponse("This login has already existed", false);
        }
        Optional<Employee> optionalEmployee = employeeRepo.findByIdAndActiveTrue(id);
        if (!optionalEmployee.isPresent())
            return new ApiResponse("Employee not found", false);
        Optional<Branch> optionalBranch = branchRepo.findByIdAndActiveTrue(dto.getBranchId());
        if (!optionalBranch.isPresent())
            return new ApiResponse("Branch not found", false);

        Employee employee = optionalEmployee.get();
        if (!access(currentUser.getCurrentUser(), employee))
            return new ApiResponse("You don't have access", false);
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employee.setBranch(optionalBranch.get());
        employee.setLastName(dto.getLastName());
        employee.setFirstName(dto.getFirstName());
        employeeRepo.save(employee);
        return new ApiResponse("Updated", true);
    }


    public ApiResponse delete(Long id) {
        Optional<Employee> optionalEmployee = employeeRepo.findByIdAndActiveTrue(id);
        if (!optionalEmployee.isPresent()) {
            return new ApiResponse("Employee not found", false);
        }
        Employee employee = optionalEmployee.get();
        if (access(currentUser.getCurrentUser(), employee)){
            employee.setActive(false);
            employeeRepo.save(employee);
            return new ApiResponse("Deleted", true);
        }
        return new ApiResponse("You don't have access", false);
    }

    public List<Employee> getBranchEmployee(Long id) {
        return employeeRepo.findByActiveTrueAndBranchId(id);
    }
}
