package com.victorious.user;

import java.sql.Blob;
import java.util.List;

//import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
//import javax.persistence.ManyToMany;
//import javax.persistence.ManyToOne;
import javax.persistence.Table;

//import com.victorious.team.Team;
//import com.victorious.tournament.Tournament;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String encodedPassword;
	
	@Lob
	private Blob imageFile;
	
	private boolean image;
	/*
	@ManyToOne(cascade = CascadeType.ALL)
	private List<Team> teams; 
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Tournament> tournaments; 
	*/
	@ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;
	
	//Game Accounts 
	private String riot;
	private String blizzard;
	private String psn;
	private String xbox;
	private String steam;
	
	public User() {}
	
	public User(String name, String email, String encodedPassword, String... roles) {
		this.name = name;
		this.email = email;
		this.encodedPassword = encodedPassword;
		//this.avatar = "default";
		this.roles = List.of(roles);
		//this.setTeams(new ArrayList<Team>());
		//this.setTournaments(new ArrayList<Team>());
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
/*
	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public List<Tournament> getTournaments() {
		return tournaments;
	}

	public void setTournaments(List<Tournament> tournaments) {
		this.tournaments = tournaments;
	}
*/
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
