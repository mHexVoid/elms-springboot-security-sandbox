package com.hexvoid.employeeportal.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Custom implementation of {@link UserDetailsService} and {@link UserDetailsManager}
 * that handles user authentication with **case-sensitive usernames**.
 *
 * This manager is particularly useful when you want to differentiate between
 * users like "John" and "john", which Spring Security's default implementations
 * treat as the same due to case-insensitive matching.
 *
 * It also provides basic user management operations like create, update, delete,
 * and password change — all in-memory, backed by a thread-safe map.
 */
public class CaseSensitiveUserDetailsManager implements UserDetailsService, UserDetailsManager {

    // In-memory, thread-safe map to store user details with case-sensitive keys
    private final Map<String, UserDetails> users = new ConcurrentHashMap<>();

    /**
     * Constructor-based initialization to pre-register users.
     *
     * @param users Varargs of users to be preloaded into the in-memory store.
     */
    public CaseSensitiveUserDetailsManager(UserDetails... users) {
        for(UserDetails user : users){
            createUser(user);
        }
    }

    /**
     * Registers a new user in the system.
     * 
     * @param user The user to be added.
     * @throws IllegalArgumentException if user with same username already exists (case-sensitive).
     */
    @Override
    public void createUser(UserDetails user) {
        if (users.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("User already exists: " + user.getUsername());
        }
        users.put(user.getUsername(), user);
    }

    /**
     * Loads user details by username with strict case sensitivity.
     *
     * @param username The username to search for.
     * @return UserDetails if found.
     * @throws UsernameNotFoundException if no matching user is found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = users.get(username); // Case-sensitive check
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }

    /**
     * Updates the stored details for an existing user.
     *
     * @param user The updated user object.
     */
    @Override
    public void updateUser(UserDetails user) {
        users.put(user.getUsername(), user);
    }

    /**
     * Deletes a user from the system.
     *
     * @param username Username to remove (case-sensitive).
     */
    @Override
    public void deleteUser(String username) {
        users.remove(username);
    }

    /**
     * Checks if a user exists in the system.
     *
     * @param username The username to look for.
     * @return true if the user exists, false otherwise.
     */
    @Override
    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    /**
     * Changes password for any matching user whose old password matches the input.
     * Note: This simplistic implementation compares raw passwords using {noop} encoding.
     *
     * @param oldPassword The old password to verify.
     * @param newPassword The new password to set.
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        users.values().forEach(user -> {
            if (user.getPassword().equals("{noop}" + oldPassword)) {
                // Clone user with updated password
                UserDetails updatedUser = User.withUserDetails(user)
                        .password("{noop}" + newPassword)
                        .build();
                users.put(user.getUsername(), updatedUser);
            }
        });
    }
}
