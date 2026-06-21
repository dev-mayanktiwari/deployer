package tech.mayanktiwari.deployer.common.exception;

public class ConflictException extends AppException {

    public ConflictException(String message) {
        super(ErrorCode.RESOURCE_ALREADY_EXISTS, message);
    }
}
