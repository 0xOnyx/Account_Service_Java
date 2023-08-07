package account.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorCustomMessage errorCustom = new ErrorCustomMessage(new Date(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(errorCustom, new HttpHeaders(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFound(UsernameNotFoundException ex, HttpServletRequest httpRequest) {
        ErrorCustomMessage errorCustom = new ErrorCustomMessage(new Date(), HttpStatus.BAD_REQUEST.value(), "", ex.getMessage(), httpRequest.getRequestURI());
        return new ResponseEntity<>(errorCustom, new HttpHeaders(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({UserException.class})
    protected ResponseEntity<Object> handleUserAlreadyExist(UserException ex, HttpServletRequest httpRequest) {
        ErrorCustomMessage errorCustom = new ErrorCustomMessage(new Date(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), httpRequest.getRequestURI());
        return new ResponseEntity<>(errorCustom, new HttpHeaders(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({UserNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex, HttpServletRequest httpRequest) {
        ErrorCustomMessage errorCustom = new ErrorCustomMessage(new Date(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), httpRequest.getRequestURI());
        return new ResponseEntity<>(errorCustom, new HttpHeaders(), HttpStatus.NOT_FOUND.value());
    }


    @ExceptionHandler({ jakarta.validation.ConstraintViolationException.class })
    protected ResponseEntity<Object> handleConstraintViolation(jakarta.validation.ConstraintViolationException ex, HttpServletRequest httpRequest) {
        ErrorCustom errorCustom = new ErrorCustom(new Date(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), httpRequest.getRequestURI());
        return new ResponseEntity<>(errorCustom, new HttpHeaders(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest httpRequest) {
        ErrorCustomMessage errorCustom = new ErrorCustomMessage(new Date(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Error!", httpRequest.getRequestURI());
        return new ResponseEntity<>(errorCustom, new HttpHeaders(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest httpRequest) {
        ErrorCustomMessage errorCustom = new ErrorCustomMessage(new Date(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Error!", httpRequest.getRequestURI());
        return new ResponseEntity<>(errorCustom, new HttpHeaders(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({LockedException.class})
    public ResponseEntity<String> handleLockedException(LockedException e) {
        System.out.println("User is locked");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.LOCKED);
    }
//    @ExceptionHandler({ Exception.class })
//    protected ResponseEntity<Object> handleAll(Exception ex, HttpServletRequest httpRequest) {
//        ErrorCustom errorCustom = new ErrorCustom(new Date(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), httpRequest.getRequestURI());
//        return new ResponseEntity<>(errorCustom, new HttpHeaders(), HttpStatus.BAD_REQUEST.value());
//    }

}
