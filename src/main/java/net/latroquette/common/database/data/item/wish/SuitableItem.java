package net.latroquette.common.database.data.item.wish;

public interface SuitableItem {

	Integer getId();
	void setId(Integer id);
	String getName();
	void setName(String name);
	String getSource();
	boolean equals(SuitableItem suitableItem);
}
