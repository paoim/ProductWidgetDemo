package com.challenge.productwidget.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.challenge.productwidget.model.Hour;
import com.challenge.productwidget.model.Item;
import com.challenge.productwidget.util.ConvertUtil;
import com.challenge.productwidget.util.FileUtil;

@Service
public class HourServiceImpl implements DataService<Hour> {

	private static Long nextId;
	private static Map<Long, Hour> hourMap;
	private InputStream resourceInputStream;
	private static final Logger log = LoggerFactory.getLogger(HourServiceImpl.class);
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	private static Hour save(Hour hour) {
		if (hourMap == null) {
			hourMap = new HashMap<Long, Hour>();
			nextId = new Long(1);
		}
		if (hour.getId() != null && hourMap.get(hour.getId()) != null) {
			//Update
			hourMap.remove(hour.getId());
		} else {
			//Create
			hour.setId(nextId);
			nextId += 1;
		}
		hourMap.put(hour.getId(), hour);
		return hour;
	}
	
	/*static {
		Hour hour = new Hour();
		long employeeId = 1;
		hour.setEmployeeId(employeeId);
		hour.setStartTime(ConvertUtil.getMMDDYYYYHHMMDate("12/5/2011 8:17"));
		hour.setEndTime(ConvertUtil.getMMDDYYYYHHMMDate("12/5/2011 16:07"));
		save(hour);
	}*/
	
	@PostConstruct
	public void init() {
		log.info(">> Start to load Data from hours.csv");
		Resource resource = resourceLoader.getResource("classpath:hours.csv");
		resourceInputStream = FileUtil.getResourceFileInputStream(resource);
		if (resourceInputStream != null) {
			loadDataFromCsv(resourceInputStream);
		}
	}
	
	@PreDestroy
	public void preDestroy() {
		if (resourceInputStream != null) {
			try {
				resourceInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
	}
	
	@Override
	public Collection<Hour> findAll() {
		Collection<Hour> hourList = hourMap != null ? hourMap.values() : new ArrayList<Hour>();
		return hourList;
	}
	
	@Override
	public Hour findOne(Long id) {
		Hour hour = hourMap != null ? hourMap.get(id) : null;
		return hour;
	}
	
	@Override
	public Hour create(Hour entity) {
		Hour hour = save(entity);
		return hour;
	}
	
	@Override
	public Collection<Hour> uploadCsv(InputStream in, String fileName) {
		log.info(">>Start on uploadCsv...");
		loadDataFromCsv(in);
		Collection<Hour> hourResultList = findAll();
		log.info(">>End on uploadCsv...");
		return hourResultList;
	}
	
	private void loadDataFromCsv(InputStream in) {
		List<Item> items = FileUtil.readInputStreamCsv(in);
		List<Hour> hourList = ConvertUtil.convertItemHour(items);
		for (Hour hour : hourList) {
			save(hour);
		}
	}
}
