package it.biagio.esc.test.ejb.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "location", schema = "hresc")
public class Location implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false, length = 36)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uid",insertable=false, updatable=false,foreignKey = @ForeignKey(name = "loc_uid_idx"))
	private User users;

	@Column(name = "lng", nullable = false, length = 36)
	private double longitudine;

	@Column(name = "lat", nullable = false, length = 36)
	private double latitude;

	@Column(name = "insdate", nullable = false, length = 36)
	private Long insdate;
	@Column(name = "uid", nullable = false, length = 36)
	private Integer uid;

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(double longitudine) {
		this.longitudine = longitudine;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Long getInsdate() {
		return insdate;
	}

	public void setInsdate(Long insdate) {
		this.insdate = insdate;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public User getUsers() {
		return users;
	}

	public void setUsers(User users) {
		this.users = users;
	}

}
