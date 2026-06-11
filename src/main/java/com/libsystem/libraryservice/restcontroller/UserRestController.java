package com.libsystem.libraryservice.restcontroller;

import com.libsystem.libraryservice.entity.BorrowRecord;
import com.libsystem.libraryservice.entity.User;
import com.libsystem.libraryservice.service.BorrowRecordService;
import com.libsystem.libraryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/users")
public class UserRestController {


    UserService userService;
    BorrowRecordService borrowRecordService;

    @Autowired
    public UserRestController(UserService userService, BorrowRecordService borrowRecordService) {
        this.userService = userService;
        this.borrowRecordService = borrowRecordService;
    }

    @GetMapping()
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{userID}")
    public User getUserById(@PathVariable Long userID){
        return userService.findUserById(userID);
    }

    @PostMapping
    public User addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable String userId, @RequestBody User user){
        return userService.editUser(user);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId){
        userService.deleteUserById(userId);
        return "User with id - " + userId + " deleted";
    }

    @GetMapping("/{userId}/borrow-records")
    public List<BorrowRecord> getBorrowRecordsByUserId(@PathVariable Long userId){
        return borrowRecordService.findAllBorrowRecordByUserId(userId);
    }

    @GetMapping("/borrow-records")
    public List<Long> getAllBorrowedBookIds(){
        return borrowRecordService.findAllBorrowedBookIds();
    }
}
