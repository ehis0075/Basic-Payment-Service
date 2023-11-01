package com.payment.util;

import com.payment.exception.GeneralException;
import com.payment.general.enums.ResponseCodeAndMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class GeneralUtil {

    public static boolean invalidEmail(String email) {
        if (stringIsNullOrEmpty(email)) return true;

        return !EmailValidator.getInstance().isValid(email);
    }


    public static String generateUniqueReferenceNumber(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        String uniqueValue = dateFormat.format(date) + generateUniqueReferenceNumber();
        return uniqueValue.substring(0, 20);
    }

    public static String generateUniqueReferenceNumber() {
        log.info("Generating unique payment reference");
        String ref = UUID.randomUUID().toString().replaceAll("[^0-9]", "");
        ref = ref.length() > 12 ? ref.substring(0, 13) : ref;

        ref = getString(ref);
        return ref;
    }

    private static String getString(String ref) {
        if (ref.length() < 14) {

            int checkLength = ref.length();

            StringBuilder refBuilder = new StringBuilder(ref);
            while (checkLength != 13) {
                refBuilder.append("0");
                ++checkLength;
            }
            ref = refBuilder.toString();
        }
        return ref;
    }

    public static BigDecimal getAmount(BigDecimal amount) {
        log.info("Validating amount for payment");

        if (Objects.isNull(amount)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode,
                    "Amount cannot be empty");
        }

        try {
            if (amountLessThanOne(amount)) {
                throw new GeneralException(ResponseCodeAndMessage.INVALID_JSON_REQUEST_DATA_90.responseCode,
                        "Invalid amount, must be greater than 0");
            }
            return amount;
        } catch (NumberFormatException e) {
            throw new GeneralException(ResponseCodeAndMessage.INVALID_JSON_REQUEST_DATA_90.responseCode,
                    "Invalid amount data, amount should be a digit");
        }
    }

    private static boolean amountLessThanOne(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 1;
    }

    public static boolean stringIsNullOrEmpty(String arg) {
        if ((arg == null)) return true;
        else
            return ("".equals(arg)) || (arg.trim().length() == 0);
    }
}
