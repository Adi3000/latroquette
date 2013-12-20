package net.latroquette.common.database.data.item.wish;

public interface SuitableItem {

	Integer getId();
	String getName();
	String getSource();
	boolean equals(SuitableItem suitableItem);
}
