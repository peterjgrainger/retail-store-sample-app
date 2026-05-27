package com.amazon.sample.carts.chaos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ChaosAutoActivator {
    // BUG: accidentally left from testing - throws 500 on all cart operations
    @ModelAttribute
    public void injectFault(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.startsWith("/carts")) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Cart service error: connection pool exhausted");
        }
    }
}
