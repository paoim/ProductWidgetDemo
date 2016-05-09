package com.challenge.productwidget.service;

import java.io.InputStream;
import java.util.Collection;

public interface DataService<T> {

	public abstract Collection<T> findAll();

	public abstract T findOne(Long id);

	public abstract T create(T entity);

	public abstract Collection<T> uploadCsv(InputStream is,
			String fileName);

}