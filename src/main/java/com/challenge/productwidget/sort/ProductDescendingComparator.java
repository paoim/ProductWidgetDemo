package com.challenge.productwidget.sort;

import java.util.Comparator;

import com.challenge.productwidget.model.Product;

public class ProductDescendingComparator implements Comparator<Product> {

	@Override
	public int compare(Product o1, Product o2) {
		return ((o1.getWidgets() >= o2.getWidgets()) ? -1 : 1);
	}
}
