package com.techelevator.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//
//public class BasicLogger {
//
//	private static PrintWriter pw = null;
//	private static final String DIRECTORY_NAME = "tenmo-client";
//
//	public static void log(String message) {
//		try {
//			if (pw == null) {
//				String userDir = System.getProperty("user.dir");
//				System.out.println("Current directory: " + userDir);
//
//				if (!userDir.endsWith(DIRECTORY_NAME)) {
//					userDir += File.separator + DIRECTORY_NAME;
//					System.out.println("Updated directory: " + userDir);
//				}
//
//				String logFilename = userDir + File.separator + "logs" + File.separator + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".log";
//				System.out.println("Log file path: " + logFilename);
//
//				File logFile = new File(logFilename);
//				if (!logFile.getParentFile().exists()) {
//					logFile.getParentFile().mkdirs();
//					System.out.println("Created directory: " + logFile.getParentFile().getAbsolutePath());
//				}
//
//				pw = new PrintWriter(new FileOutputStream(logFilename, true));
//			}
//			pw.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " " + message);
//			pw.flush();
//			System.out.println("Logged message: " + message);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace(); // Print the stack trace to debug if an error occurs
//			throw new BasicLoggerException(e.getMessage());
//		}
//	}
//
//	// Method to close the PrintWriter
//	public static void close() {
//		if (pw != null) {
//			pw.close();
//			pw = null;
//		}
//	}
//}
//
//
public class BasicLogger {

	private static PrintWriter pw = null;

	public static void log(String message) {
		try {
			if (pw == null) {
				String userDir = System.getProperty("user.dir");
				String logFilename = userDir + File.separator + "logs" + File.separator + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".log";
				File logFile = new File(logFilename);
				logFile.getParentFile().mkdirs();  // Ensure that the directory exists
				pw = new PrintWriter(new FileOutputStream(logFilename, true));
			}
			pw.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " " + message);
			pw.flush();
		} catch (FileNotFoundException e) {
			throw new BasicLoggerException(e.getMessage());
		}
	}

	public static void close() {
		if (pw != null) {
			pw.close();
		}
	}
}
//
//
//public class BasicLogger {
//
//	private static PrintWriter pw = null;
//	private static final String DIRECTORY_NAME = "tenmo-client/logs";
//
//	public static void log(String message) {
//		try {
//			if (pw == null) {
//				String userDir = System.getProperty("user.dir");
//
//				if (!userDir.endsWith(DIRECTORY_NAME)) {
//					userDir += File.separator + DIRECTORY_NAME;
//				}
//
//				// Ensure the directory exists
//				File logDir = new File(userDir);
//				if (!logDir.exists()) {
//					logDir.mkdirs();  // Create the directory and any necessary parent directories
//				}
//
//				String logFilename = userDir + File.separator + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".log";
//				pw = new PrintWriter(new FileOutputStream(logFilename, true));
//			}
//			pw.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " " + message);
//			pw.flush();
//		} catch (FileNotFoundException e) {
//			throw new BasicLoggerException(e.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace(); // For any unexpected exceptions
//		}
//	}
//
//	// Ensure the PrintWriter is closed properly
//	public static void close() {
//		if (pw != null) {
//			pw.close();
//			pw = null;
//		}
//	}
//}
//

//import com.techelevator.util.BasicLoggerException;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.PrintWriter;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.concurrent.atomic.AtomicReference;
//
//private static final String DIRECTORY_NAME = "module-2-capstone/tenmo-client/logs";
//
//public static void log(String message) {
//	try {
//		AtomicReference<PrintWriter> pw;
//		if (pw.get() == null) {
//			String userDir = System.getProperty("user.dir");
//
//			if (!userDir.endsWith(DIRECTORY_NAME)) {
//				userDir += File.separator + DIRECTORY_NAME;
//			}
//
//			String logDir = userDir + File.separator + "logs";
//			String logFilename = logDir + File.separator + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".log";
//
//			// Ensure the logs directory exists
//			File dir = new File(logDir);
//			if (!dir.exists()) {
//				dir.mkdirs();  // Create the directory if it doesn't exist
//			}
//
//			pw.set(new PrintWriter(new FileOutputStream(logFilename, true)));
//		}
//		pw.get().println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " " + message);
//		pw.get().flush();
//	} catch (FileNotFoundException e) {
//		throw new BasicLoggerException(e.getMessage());
//	}
//}

//
//
//package com.techelevator.util;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.PrintWriter;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//public class BasicLogger {
//
//	private static PrintWriter pw = null;
//	private static final String DIRECTORY_NAME = "tenmo-client";
//
//	public static void log(String message) {
//		try {
//			if (pw == null) {
//				String userDir = System.getProperty("user.dir");
//
//				if(!userDir.endsWith(DIRECTORY_NAME)) {
//					userDir += File.separator + DIRECTORY_NAME;
//				}
//
//				String logFilename = userDir + File.separator + "logs" +File.separator + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".log";
//				System.out.println(logFilename);
//				pw = new PrintWriter(new FileOutputStream(logFilename, true));
//			}
//
//			pw.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " " + message);
//			pw.flush();
//		}
//		catch (FileNotFoundException e) {
//			throw new BasicLoggerException(e.getMessage());
//
//		}
//		finally{
//			pw.close();
//		}
//	}
//
//}
