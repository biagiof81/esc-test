package it.biagio.esc.test.ejb.entity;

import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user", schema = "hresc")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false, length = 36)
	private Integer id;

	@OneToMany(mappedBy = "users", fetch = LAZY)
	private List<Location> location;

	@Column(name = "username", nullable = false, length = 36)
	private String username;

	@Column(name = "firstname", nullable = false, length = 36)
	private String firstname;

	@Column(name = "lastname", nullable = false, length = 36)
	private String lastname;

	@Column(name = "active", nullable = false, length = 36)
	private Integer active;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Location> getLocation() {
		return location;
	}

	public void setLocation(List<Location> location) {
		this.location = location;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

}
