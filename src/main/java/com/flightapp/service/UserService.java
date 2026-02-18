package com.flightapp.service;

import com.flightapp.model.User;
import com.flightapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("User not found"));
    }

    public User createUser(User user) {
        user.setWalletBalance(1000.0); // give default wallet of Rs.1000
        return userRepository.save(user);
    }

    // Add money to wallet
    public User topUpWallet(Long userId, double amount) {
        User user = getUserById(userId);
        user.setWalletBalance(user.getWalletBalance() + amount);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
