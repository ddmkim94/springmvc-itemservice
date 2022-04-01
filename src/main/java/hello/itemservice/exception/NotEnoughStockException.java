package hello.itemservice.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotEnoughStockException extends RuntimeException{

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }
}
