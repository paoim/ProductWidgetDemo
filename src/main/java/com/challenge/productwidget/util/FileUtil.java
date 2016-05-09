package com.challenge.productwidget.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.challenge.productwidget.model.Item;

public class FileUtil {

	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
	
	public static List<Item> readInputStreamCsv(InputStream in) {
		String line = "";
		String cvsSplitBy = ",";
		BufferedReader br = null;
		boolean isSkipFirstLine = true;
		List<Item> dataList = new ArrayList<Item>();
		try {
			br = getBufferedReader(in);
			while ((line = br.readLine()) != null) {
				if (!isSkipFirstLine) {
					String[] cells = line.split(cvsSplitBy);
					Item item = ConvertUtil.convertCells(cells);
					dataList.add(item);
				} else {
					isSkipFirstLine = false;
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return dataList;
	}
	
	public static List<Row> readExcelRow(InputStream is, String fileName) {
		List<Row> rowList = new ArrayList<Row>();
		try {
			Workbook workbook = getWorkbook(is, fileName);
			rowList = getRowList(workbook);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return rowList;
	}

	public static Workbook getWorkbook(InputStream is, String fileName)
			throws IOException {
		// Create Workbook instance for xlsx/xls file input stream
		Workbook workbook = null;
		if (fileName.toLowerCase().endsWith("xlsx")) {
			workbook = new XSSFWorkbook(is);
		} else if (fileName.toLowerCase().endsWith("xls")) {
			workbook = new HSSFWorkbook(is);
		} else {
			throw new IOException("invalid file name, should be xls or xlsx");
		}
		return workbook;
	}
	
	public static List<Row> getRowList(Workbook workbook) {
		List<Row> rowList = new ArrayList<Row>();
		if (workbook != null) {
			// Get the number of sheets in the xlsx file
			int numberOfSheets = workbook.getNumberOfSheets();

			// loop through each of the sheets
			for (int i = 0; i < numberOfSheets; i++) {

				// Get the nth sheet from the workbook
				Sheet sheet = workbook.getSheetAt(i);

				// every sheet has rows, iterate over them
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()) {
					// Get the row object
					Row row = rowIterator.next();
					rowList.add(row);
				}
			}
		}
		return rowList;
	}
	
	public static StringBuilder readFileCsv(String csvFile) {
		String line = "";
		InputStream in = null;
		StringBuilder out = null;
		BufferedReader br = null;
		try {
			out = new StringBuilder();
			in = new FileInputStream(new File(csvFile));
			br = getBufferedReader(in);
			while ((line = br.readLine()) != null) {
				out.append(line);
			}
		} catch (FileNotFoundException e) {
			out = null;
			e.printStackTrace();
		} catch (Exception e) {
			out = null;
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e2) {
					out = null;
					e2.printStackTrace();
				}
			}
		}
		return out;
	}
	
	public static StringBuilder readFileReaderCsv(String csvFile) {
		String line = "";
		BufferedReader br = null;
		StringBuilder out = null;
		try {
			out = new StringBuilder();
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				out.append(line);
			}
		} catch (FileNotFoundException e) {
			out = null;
			e.printStackTrace();
		} catch (Exception e1) {
			out = null;
			e1.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e2) {
					out = null;
					e2.printStackTrace();
				}
			}
		}
		return out;
	}
	
	public static BufferedReader getBufferedReader(InputStream in) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in));
		} catch (Exception e) {
			br = null;
			e.printStackTrace();
		}
		return br;
	}
	
	public static InputStream getResourceFileInputStream(Resource resource) {
		log.info(">> Start on getResourceFileInputStream...");
		InputStream in = null;
		try {
			in = resource.getInputStream();
			log.info("Resource InputStream is OK to go!");
		} catch (FileNotFoundException e) {
			in = null;
			log.error(e.getMessage());
		} catch (Exception e) {
			in = null;
			log.error(e.getMessage());
		}
		log.info(">> End getResourceFileInputStream...");
		return in;
	}
	
	public static InputStream getInputStream(MultipartFile file) {
		log.info(">> Start on getInputStream...");
		InputStream in = null;
		try {
			in = file.getInputStream();
			log.info("MultipartFile is OK to go!");
		} catch (FileNotFoundException e) {
			in = null;
			log.error(e.getMessage());
		} catch (Exception e) {
			in = null;
			log.error(e.getMessage());
		}
		log.info(">> End on getInputStream...");
		return in;
	}
}
