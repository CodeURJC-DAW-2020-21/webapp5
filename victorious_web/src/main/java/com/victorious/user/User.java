package com.victorious.user;

import java.sql.Blob;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.victorious.team.Team;

@Entity
public class User {
	
	public interface Basic{}
	public interface UserTeam{}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Basic.class)
	private Long id;
	
	@Column(nullable = false)
	@JsonView(Basic.class)
	private String name;
	
	@Column(nullable = false)
	@JsonView(Basic.class)
	private String email;
	
	@Column(nullable = false)
	private String encodedPassword;
	
	@Lob
	@JsonIgnore
	private Blob imageFile;
	
	@JsonIgnore
	private boolean image;
	
	@ManyToOne
	@JsonView(UserTeam.class)
	private Team team; 
	
	@ElementCollection(fetch = FetchType.EAGER)
	@JsonView(Basic.class)
    private List<String> roles;
	
	//Game Accounts
	@JsonView(Basic.class)
	private String riot;
	@JsonView(Basic.class)
	private String blizzard;
	@JsonView(Basic.class)
	private String psn;
	@JsonView(Basic.class)
	private String xbox;
	@JsonView(Basic.class)
	private String steam;
	
	public User() {}
	
	public User(String name, String email, String encodedPassword, String... roles) {
		this.name = name;
		this.email = email;
		this.encodedPassword = encodedPassword;
		this.roles = List.of(roles);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEncodedPassword() {
		return encodedPassword;
	}

	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	public Blob getImageFile() {
		return imageFile;
	}

	public void setImageFile(Blob image) {
		this.imageFile = image;
	}
	
	public boolean hasImage(){
		return this.image;
	}

	public void setImage(boolean image){
		this.image = image;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public String getRiot() {
		return riot;
	}

	public void setRiot(String riot) {
		this.riot = riot;
	}

	public String getBlizzard() {
		return blizzard;
	}

	public void setBlizzard(String blizzard) {
		this.blizzard = blizzard;
	}

	public String getPsn() {
		return psn;
	}

	public void setPsn(String psn) {
		this.psn = psn;
	}

	public String getXbox() {
		return xbox;
	}

	public void setXbox(String xbox) {
		this.xbox = xbox;
	}

	public String getSteam() {
		return steam;
	}

	public void setSteam(String steam) {
		this.steam = steam;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", encodedPassword=" + encodedPassword + ", roles=" + roles + "]";
	}
}
