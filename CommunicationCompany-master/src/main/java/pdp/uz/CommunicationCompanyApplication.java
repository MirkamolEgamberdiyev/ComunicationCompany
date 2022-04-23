package pdp.uz;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pdp.uz.entity.enums.BranchType;
import pdp.uz.payload.AttachRoleDto;
import pdp.uz.payload.BranchDto;
import pdp.uz.payload.EmployeeDto;
import pdp.uz.payload.RoleDto;
import pdp.uz.service.BranchService;
import pdp.uz.service.EmployeeService;

@SpringBootApplication
public class CommunicationCompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunicationCompanyApplication.class, args);
    }
}
