package uk.co.malbec.welcometohell.ui;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.co.malbec.welcometohell.wizard.exception.SessionExpiredException;

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
