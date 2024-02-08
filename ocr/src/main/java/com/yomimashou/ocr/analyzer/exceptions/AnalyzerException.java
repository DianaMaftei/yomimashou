package com.yomimashou.ocr.analyzer.exceptions;

public class AnalyzerException extends RuntimeException {

    public AnalyzerException(String message) {
        super(message);
    }

    public AnalyzerException(String message, Throwable cause) {
        super(message, cause);
    }

}
