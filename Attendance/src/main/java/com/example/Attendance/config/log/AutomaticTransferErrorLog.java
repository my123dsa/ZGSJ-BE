package com.example.Attendance.config.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.example.Attendance.error.log.ErrorType;
import com.example.Attendance.service.EmailService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class AutomaticTransferErrorLog extends AppenderBase<ILoggingEvent> {
    private final EmailService emailService;
//    @Value("${EMAIL_USERNAME}")
    private String EMAIL_USERNAME = "dealon77@naver.com";

    @Override
    protected void append(ILoggingEvent event) {
        if (event.getLevel().equals(Level.ERROR)
                && event.getLoggerName().contains("salaryProcessor")) {
            sendEmailAutomaticTransferFail(event);
        }
    }
    
    private void sendEmailAutomaticTransferFail(ILoggingEvent event)
    {
        Map<String, String> extractedInfo = extractInfo(event.getFormattedMessage());

        ErrorType errorType = ErrorType.valueOf(extractedInfo.get("type"));
        String detailedMessage = errorType.getMessage();

        emailService.sendBankFailToAdmin(
                EMAIL_USERNAME,
                extractedInfo.get("president_account"),
                extractedInfo.get("employee_account"),
                LocalDate.now(),
                detailedMessage
                );
    }

    private Map<String, String> extractInfo(String logMessage) {
        Map<String, String> result = new HashMap<>();
        String[] patterns = {
                "president_account=([^,]+)",
                "employee_account=([^,]+)",
                "error=([^,]+)",
                "type=([^,}]+)"
        };
        String[] keys = {"president_account", "employee_account", "error", "type"};

        for (int i = 0; i < patterns.length; i++) {
            Pattern p = Pattern.compile(patterns[i]);
            Matcher m = p.matcher(logMessage);
            result.put(keys[i], m.find() ? m.group(1) : "정보 없음");
        }

        return result;
    }

    @PostConstruct
    public void init() {
        this.start();
    }
}
