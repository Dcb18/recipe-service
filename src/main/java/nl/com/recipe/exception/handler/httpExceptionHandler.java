package nl.com.recipe.exception.handler;

import nl.com.recipe.exception.RecipeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class httpExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Error> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ResponseEntity(new Error(ex.getClass().getName(), LocalDateTime.now().toString(), HttpStatus.BAD_REQUEST.toString(), ex.getMessage()), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Error> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return new ResponseEntity(new Error(ex.getClass().getName(), LocalDateTime.now().toString(), HttpStatus.BAD_REQUEST.toString(), ex.getMessage()), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({MethodNotAllowedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<Error> handleMethodNotAllowedException(MethodNotAllowedException ex) {
        return new ResponseEntity(new Error(ex.getClass().getName(), LocalDateTime.now().toString(), HttpStatus.METHOD_NOT_ALLOWED.toString(), ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);

    }

    @ExceptionHandler(RecipeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Error> handleRecipeNotFoundException(RecipeNotFoundException ex) {
        return new ResponseEntity(new Error(ex.getClass().getName(), LocalDateTime.now().toString(), HttpStatus.NOT_FOUND.toString(), ex.getMessage()), HttpStatus.NOT_FOUND);

    }

    record Error(String name, String time, String status, String message) {
    }
}
