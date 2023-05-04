package pdp.uz.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pdp.uz.entity.Role;
import pdp.uz.entity.enums.RoleNames;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRoleNames(RoleNames roleNames);
}
