package com.techelevator.util;

public class LoggerTest {
    public static void main(String[] args) {
        // Log a test message
        BasicLogger.log("This is a test log entry");

        // Close the logger to ensure the file is properly flushed and closed
        BasicLogger.close();
    }
}

