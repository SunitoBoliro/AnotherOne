package com.example.anotherone.service;

import com.example.anotherone.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    public List<User> getAllUsers(){
        return new ArrayList<>(users.values());
    }

    public User getUserById(int id){
        return users.get(id);
    }

    public User createUser(User user){
        user.setId(++currentId);
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(int id, User updateduser){
        if (users.containsKey(id)){
            updateduser.setId(id);
            users.put(id, updateduser);
            return updateduser;
        }
        return null;
    }

    public boolean deleteUser(int id) {
        return users.remove(id) != null;
    }
}
