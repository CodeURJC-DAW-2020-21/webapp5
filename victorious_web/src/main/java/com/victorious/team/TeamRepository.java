package com.victorious.team;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

	Optional<Team> findByName(String name);
	
}
