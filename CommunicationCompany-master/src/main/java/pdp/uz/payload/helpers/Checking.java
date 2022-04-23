package pdp.uz.payload.helpers;

import org.springframework.stereotype.Component;
import pdp.uz.entity.Employee;
import pdp.uz.entity.Role;
import pdp.uz.entity.enums.BranchType;
import pdp.uz.entity.enums.RoleName;

@Component
public class Checking {

    public static boolean hasRole(Employee employee, RoleName roleName) {
        for (Role role : employee.getRoles()) {
            if (role.getName().equals(roleName.name()))
                return true;
        }
        return false;
    }

    public static boolean accessRole(String from, String to, BranchType fromBranch, BranchType toBranch) {
        if (fromBranch == BranchType.MAIN && toBranch == BranchType.MAIN) {
            return (from.equals(RoleName.ROLE_DIRECTOR.name()) || to.equals(RoleName.ROLE_WORKER.name()));
        } else if (fromBranch == BranchType.MAIN && toBranch == BranchType.BRANCH) {
            return (from.equals(RoleName.ROLE_DIRECTOR.name()) || from.equals(RoleName.ROLE_BRANCH_MANAGER.name()));
        } else if (fromBranch == BranchType.BRANCH && toBranch == BranchType.BRANCH) {
            return (from.equals(RoleName.ROLE_BRANCH_DIRECTOR.name()) || to.equals(RoleName.ROLE_WORKER.name()));
        }
        return false;
    }

    public static boolean access(Employee from, Employee to) {
        if (from.getBranch().getType() == BranchType.BRANCH
                && from.getBranch().getId().equals(to.getBranch().getId()) || from.getBranch().getType() == BranchType.MAIN) {
            if (hasRole(to, RoleName.ROLE_DIRECTOR)) {
                return false;
            }
            for (Role fromRole : from.getRoles()) {
                for (Role toRole : to.getRoles()) {
                    return (accessRole(fromRole.getName(), toRole.getName(), from.getBranch().getType(), to.getBranch().getType()));
                }
            }
        }
        return false;
    }
}
