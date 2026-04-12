package com.firs.service;

import com.firs.model.User;
import com.firs.repository.UserRepository;
import com.firs.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User registerUser(User user) {
        user.setPassword(PasswordUtil.hash(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && PasswordUtil.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User updateUser(Long id, User updatedData) {
        Optional<User> existing = userRepository.findById(id);
        if (existing.isPresent()) {
            User user = existing.get();
            if (updatedData.getName() != null) user.setName(updatedData.getName());
            if (updatedData.getDob() != null) user.setDob(updatedData.getDob());
            if (updatedData.getNid() != null) user.setNid(updatedData.getNid());
            if (updatedData.getPhoneNumber() != null) user.setPhoneNumber(updatedData.getPhoneNumber());
            if (updatedData.getAddress() != null) user.setAddress(updatedData.getAddress());
            if (updatedData.getCity() != null) user.setCity(updatedData.getCity());
            if (updatedData.getState() != null) user.setState(updatedData.getState());
            if (updatedData.getZipCode() != null) user.setZipCode(updatedData.getZipCode());
            if (updatedData.getProfileImage() != null) user.setProfileImage(updatedData.getProfileImage());
            if (updatedData.getPassword() != null && !updatedData.getPassword().isEmpty()) {
                user.setPassword(PasswordUtil.hash(updatedData.getPassword()));
            }
            return userRepository.save(user);
        }
        return null;
    }

    @Transactional
    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent() && PasswordUtil.matches(oldPassword, user.get().getPassword())) {
            user.get().setPassword(PasswordUtil.hash(newPassword));
            userRepository.save(user.get());
            return true;
        }
        return false;
    }
}
