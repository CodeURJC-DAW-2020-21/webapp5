package com.victorious.user;


import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByName(String name);
	Optional<User> findByEmail(String email);
	
}
