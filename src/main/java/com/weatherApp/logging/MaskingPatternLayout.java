package com.weatherApp.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

public class MaskingPatternLayout extends PatternLayout {
    
    private static final String DEFAULT_MASK = "[REDACTED]";
    
    private final List<MaskRule> maskRules = new ArrayList<>();
    private String maskString = DEFAULT_MASK;
    
    public void addMaskPattern(String maskPattern) {
        if(maskPattern == null || maskPattern.trim().isEmpty()) {
            addWarn("Empty Mask pattern provided, Skipping");
            return;
        }
        
        try {
            Pattern compiled = Pattern.compile(maskPattern, Pattern.MULTILINE);
            maskRules.add(new MaskRule(compiled, maskString));
        } catch (Exception e) {
            addError("Invalid regex pattern: " + maskPattern, e);
        }
    }
    
    public void setMaskString(String maskString) {
        this.maskString = maskString != null ? maskString : DEFAULT_MASK;
    }
    
    @Override
    public String doLayout(ILoggingEvent event) {
        String formattedMessage = super.doLayout(event);
        return maskMessage(formattedMessage);
    }
    
    private String maskMessage(String message) {
        if(message == null || message.isEmpty() || maskRules.isEmpty()) {
            return message;
        }
        
        try {
            String result = message;
            // Apply each mask rule sequentially
            for (MaskRule rule : maskRules) {
                result = applyMaskRule(result, rule);
            }
            return result;
        } catch (Exception e) {
            addError("Error while masking message", e);
            return message;
        }
    }
    
    private String applyMaskRule(String message, MaskRule rule) {
        Matcher matcher = rule.getPattern().matcher(message);
        StringBuffer result = new StringBuffer();
        
        while(matcher.find()) {
            String replacement = createReplacement(matcher, rule);
            if (replacement != null) {
                matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }
    
    private String createReplacement(Matcher matcher, MaskRule rule) {
        String fullMatch = matcher.group(0);
        int groupCount = matcher.groupCount();
        
        // Pattern 1: Key=Value with email (2 groups where key suggests email)
        if (groupCount == 2) {
            String prefix = matcher.group(1);
            String value = matcher.group(2);
            
            // Skip masking if value is "null"
            if (value != null && value.equalsIgnoreCase("null")) {
                return null; // Don't replace
            }
            
            if (value != null) {
                // Check if this is an email field by looking at the prefix
                if (prefix.toLowerCase().contains("email") || prefix.toLowerCase().contains("e-mail")) {
                    // Apply email masking
                    return prefix + maskEmail(value);
                }
                // Otherwise, full redaction for passwords, tokens, secrets, etc.
                return prefix + rule.getMaskString();
            }
        }
        
        // Pattern 2: Standalone email pattern (1 group - the email itself)
        if (groupCount == 1 && fullMatch.contains("@")) {
            String email = matcher.group(1);
            if (email != null && !email.equalsIgnoreCase("null")) {
                return maskEmail(email);
            }
            return null; // Don't replace
        }
        
        // Pattern 3: Single group (just a prefix or just a value)
        if (groupCount == 1) {
            String captured = matcher.group(1);
            
            // Skip if it's "null"
            if (captured != null && captured.equalsIgnoreCase("null")) {
                return null; // Don't replace
            }
            
            // Return prefix + mask
            return captured + rule.getMaskString();
        }
        
        // No groups - mask entire match
        return rule.getMaskString();
    }
    
    private String maskEmail(String email) {
        if (email == null || email.isEmpty()) {
            return maskString;
        }
        
        int atIndex = email.indexOf('@');
        if (atIndex > 0) {
            String local = email.substring(0, atIndex);
            String domain = email.substring(atIndex);
            
            if (local.length() > 2) {
                return local.charAt(0) + "***" + local.charAt(local.length() - 1) + domain;
            } else if (local.length() == 2) {
                return local.charAt(0) + "***" + domain;
            } else {
                return "***" + domain;
            }
        }
        return maskString;
    }
    
    @Data
    @AllArgsConstructor
    private static class MaskRule {
        private final Pattern pattern;
        private final String maskString;
    }
}