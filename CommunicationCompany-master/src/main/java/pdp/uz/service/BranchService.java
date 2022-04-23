package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import pdp.uz.entity.Branch;
import pdp.uz.entity.Employee;
import pdp.uz.entity.enums.BranchType;
import pdp.uz.entity.enums.RoleName;
import pdp.uz.payload.BranchDto;
import pdp.uz.payload.BranchUpdateDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.repository.BranchRepo;
import pdp.uz.repository.EmployeeRepo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import static pdp.uz.payload.helpers.Checking.hasRole;

@Service
@Transactional
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepo branchRepo;

    private final EmployeeService employeeService;

    private final EmployeeRepo employeeRepo;

    public ApiResponse getAll() {
        List<Branch> branches = branchRepo.findAllByActiveTrue();
        return new ApiResponse("OK", true, branches);
    }

    public ApiResponse get(Long id) {
        Optional<Branch> optionalBranch = branchRepo.findByIdAndActiveTrue(id);
        if (optionalBranch.isPresent()) {
            Branch branch = optionalBranch.get();
            if (branch.getEmployee().getUsername().equals(getCurrentUser().getUsername()) || hasRole(getCurrentUser(), RoleName.ROLE_DIRECTOR))
                return new ApiResponse("OK", true, branch);
            return new ApiResponse("Yo don't have access", false);
        }
        return new ApiResponse("Branch not found", false);
    }

    public Employee getCurrentUser() {
        return employeeRepo.findByUsernameAndActiveTrue(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get();
    }

    public ApiResponse getBranchEmployees(Long id) {
        if (getCurrentUser().getBranch().getType() == BranchType.BRANCH) {
            if (!getCurrentUser().getBranch().getId().equals(id))
                return new ApiResponse("You don't have access", false);
        }
        if (branchRepo.findByIdAndActiveTrue(id).isPresent())
            return new ApiResponse("OK", true, employeeService.getBranchEmployee(id));
        return new ApiResponse("Branch not found", false);
    }

    public ApiResponse addBranch(BranchDto dto) {
        if (branchRepo.existsByName(dto.getName())) {
            return new ApiResponse("Conflict", false);
        }
        Branch branch = new Branch();
        if (dto.getBranchType() == null) {
            branch.setType(BranchType.BRANCH);
        } else branch.setType(BranchType.MAIN);
        branch.setName(dto.getName());
        Optional<Employee> optionalEmployee = employeeRepo.findByUsernameAndActiveTrue(dto.getManagerUsername());
        if (!optionalEmployee.isPresent()) {
            return new ApiResponse("Employee not found", false);
        }
        Employee manager = optionalEmployee.get();
        if (!hasRole(manager,RoleName.ROLE_BRANCH_MANAGER) || manager.getBranch().getType()!=BranchType.MAIN) {
            return new ApiResponse("Selected employee has not access to rule branch", false);
        }
        branch.setEmployee(manager);
        return new ApiResponse("Created", true, branchRepo.save(branch));
    }

    public ApiResponse updateBranch(BranchUpdateDto dto, Long id) {
        if (branchRepo.existsByNameAndIdNot(dto.getName(), id))
            return new ApiResponse("This branch has already existed", false);
        Optional<Branch> optionalBranch = branchRepo.findByIdAndActiveTrue(id);
        if (!optionalBranch.isPresent())
            return new ApiResponse("Branch not found", false);
        Optional<Employee> optionalEmployee = employeeRepo.findByUsernameAndActiveTrue(dto.getManagerLogin());
        if (!optionalEmployee.isPresent())
            return new ApiResponse("Employee not found", false);
        Employee manager = optionalEmployee.get();
        if (!hasRole(manager,RoleName.ROLE_BRANCH_MANAGER) || manager.getBranch().getType() != BranchType.MAIN)
            return new ApiResponse("Selected employee has not access to rule branch", false);

        Branch branch = optionalBranch.get();
        branch.setName(dto.getName());
        branch.setEmployee(manager);
        return new ApiResponse("Updated", true, branchRepo.save(branch));
    }

    public ApiResponse deleteBranch(Long id) {
        Optional<Branch> optionalBranch = branchRepo.findByIdAndActiveTrue(id);
        if (!optionalBranch.isPresent())
            return new ApiResponse("Branch not found", false);
        Branch branch = optionalBranch.get();
        branch.setActive(false);
        branchRepo.save(branch);
        return new ApiResponse("Deleted", true);
    }
}
