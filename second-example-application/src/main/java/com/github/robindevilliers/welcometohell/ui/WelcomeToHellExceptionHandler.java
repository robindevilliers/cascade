package com.github.robindevilliers.welcometohell.ui;

import com.github.robindevilliers.welcometohell.wizard.exception.SessionExpiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class WelcomeToHellExceptionHandler {

    @ExceptionHandler(SessionExpiredException.class)
    public void handleNoRecord(HttpServletRequest req, HttpServletResponse res, Exception exception) throws IOException {
        res.sendRedirect("/");
    }
}
