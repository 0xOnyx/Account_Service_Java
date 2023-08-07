package account.controller;


import account.DTO.LockUpdateDTO;
import account.DTO.RoleUpdateDTO;
import account.DTO.StatusMessage;
import account.DTO.StatusMessageUser;
import account.database.User;
import account.service.AdminService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class Admin {

    AdminService adminService;

    @Autowired
    Admin(AdminService adminService)
    {
        this.adminService = adminService;
    }

    @PutMapping("/api/admin/user/role")
    public User   putChangeRole(@RequestBody RoleUpdateDTO roleUpdateDTO) {
        return adminService.putChangeRole(roleUpdateDTO);
    }

    @DeleteMapping("/api/admin/user/{email}")
    public StatusMessageUser deleteUser(
            @PathVariable @NotEmpty
            @Email String email) {
        return this.adminService.deleteUser(email);
    }

    @GetMapping("/api/admin/user/")
    public Iterable<User>   getUser() {
        return adminService.getUser();
    }

    @PutMapping("/api/admin/user/access")
    public StatusMessage putChangeAccess(@RequestBody LockUpdateDTO lockUpdateDTO) {
        return adminService.putChangeAccess(lockUpdateDTO);
    }

}
