package com.victorious.user;

import java.util.Optional;

public interface UserService {
	
	public Optional<User> findById(Long id);
	public Optional<User> findByName(String name);
	public Optional<User> findByEmail(String email);
	public User saveUser(User user);
	public User updateUser(User user);
}
