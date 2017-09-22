package application.entity;

import javax.persistence.Entity;

@Entity
public class TablesAndTables {

	Long id;
	String column1;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColumn1() {
		return column1;
	}

	public void setColumn1(String column1) {
		this.column1 = column1;
	}

}
