package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class FileException extends DiscodeitException {
    public FileException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FileException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static class FileNotFoundException extends FileException {
        public FileNotFoundException() {
            super(ErrorCode.FILE_NOT_FOUND);
        }

        public FileNotFoundException(String message) {
            super(ErrorCode.FILE_NOT_FOUND, message);
        }
    }

    public static class FileUploadFailedException extends FileException {
        public FileUploadFailedException() {
            super(ErrorCode.FILE_UPLOAD_FAILED);
        }

        public FileUploadFailedException(String message) {
            super(ErrorCode.FILE_UPLOAD_FAILED, message);
        }
    }

    public static class FileDownloadFailedException extends FileException {
        public FileDownloadFailedException() {
            super(ErrorCode.FILE_DOWNLOAD_FAILED);
        }

        public FileDownloadFailedException(String message) {
            super(ErrorCode.FILE_DOWNLOAD_FAILED, message);
        }
    }

    public static class FileDeleteFailedException extends FileException {
        public FileDeleteFailedException() {
            super(ErrorCode.FILE_DELETE_FAILED);
        }

        public FileDeleteFailedException(String message) {
            super(ErrorCode.FILE_DELETE_FAILED, message);
        }
    }

    public static class InvalidFileTypeException extends FileException {
        public InvalidFileTypeException() {
            super(ErrorCode.INVALID_FILE_TYPE);
        }

        public InvalidFileTypeException(String message) {
            super(ErrorCode.INVALID_FILE_TYPE, message);
        }
    }

    public static class FileSizeExceededException extends FileException {
        public FileSizeExceededException() {
            super(ErrorCode.FILE_SIZE_EXCEEDED);
        }

        public FileSizeExceededException(String message) {
            super(ErrorCode.FILE_SIZE_EXCEEDED, message);
        }
    }
} 