package net.latroquette.common.database.data.item.wish;

public interface Wish extends SuitableItem{

	String getUid();
	String getName();
	String getSource();
	boolean equals(Wish wish);
}
