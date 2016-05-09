package com.challenge.productwidget.sort;

import java.util.Comparator;

public class WidgetAscendingComparator implements Comparator<Double> {

	@Override
	public int compare(Double o1, Double o2) {
		return ((o1 <= o2) ? -1 : 1);
	}
}
