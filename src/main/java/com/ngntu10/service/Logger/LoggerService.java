package com.ngntu10.service.Logger;

import com.ngntu10.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggerService {

    private void setMDC() {
        MDC.put("ip", RequestUtil.getClientIp());
        MDC.put("user", RequestUtil.getUserEmail());
    }
    
    private void clearMDC() {
        MDC.clear();
    }
    
    public void logError(String message, Exception ex) {
        try {
            setMDC();
            log.error("{} - Exception: {}", message, ex.getMessage(), ex);
        } finally {
            clearMDC();
        }
    }

    public void logError(String message) {
        try {
            setMDC();
            log.error(message);
        } finally {
            clearMDC();
        }
    }

    public void logInfo(String message) {
        try {
            setMDC();
            log.info(message);
        } finally {
            clearMDC();
        }
    }

    public void logDebug(String message) {
        try {
            setMDC();
            log.debug(message);
        } finally {
            clearMDC();
        }
    }

    public void logWarn(String message) {
        try {
            setMDC();
            log.warn(message);
        } finally {
            clearMDC();
        }
    }
}
