package br.com.event.custom.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TwitterAccount {

	public static final String ID_FIELD_NAME = "id";
	
	public static final String ACTIVE_FIELD_NAME = "active"; 
	
	@DatabaseField (generatedId = true, columnName = ID_FIELD_NAME)
	private Integer id;
	
	@DatabaseField (dataType = DataType.LONG_OBJ)
	private Long userId;

	@DatabaseField (index = true, canBeNull = false)
	private String screenName;
	
	@DatabaseField (canBeNull = false)
	private String key;
	
	@DatabaseField (canBeNull = false)
	private String secret;
	
	@DatabaseField (dataType = DataType.BOOLEAN_OBJ, 
					columnName = ACTIVE_FIELD_NAME, 
					canBeNull = false)
	private Boolean active;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean isActive() {
		return active;
	}

	@Override
	public String toString() {
		return "TwitterAccount [id=" + id + ", userId=" + userId
				+ ", screenName=" + screenName + ", key=" + key + ", secret="
				+ secret + ", active=" + active + "]";
	}
	
}