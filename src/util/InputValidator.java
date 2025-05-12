package util;

public class InputValidator {

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    public static boolean isValidEmailFormat(String email) {
        if (isNullOrEmpty(email)) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.trim().matches(emailRegex);
    }

    public static boolean isValidInteger(String input) {
        if (isNullOrEmpty(input)) {
            return false;
        }
        try {
            Integer.parseInt(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidPositiveInteger(String input) {
        if (!isValidInteger(input)) {
            return false;
        }
        return Integer.parseInt(input.trim()) > 0;
    }

    public static boolean isValidDouble(String input) {
        if (isNullOrEmpty(input)) {
            return false;
        }
        try {
            Double.parseDouble(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidNonNegativeDouble(String input) {
        if (!isValidDouble(input)) {
            return false;
        }
        return Double.parseDouble(input.trim()) >= 0;
    }

    public static boolean isLengthSufficient(String input, int minLength) {
        if (input == null || minLength <= 0) {
            return false;
        }
        return input.length() >= minLength;
    }

    public static boolean isValidDateFormat(String dateString) {
        if (isNullOrEmpty(dateString)) {
            return false;
        }
        String dateRegex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
        return dateString.trim().matches(dateRegex);
    }

    public static boolean isValidTimeFormat(String timeString) {
        if (isNullOrEmpty(timeString)) {
            return false;
        }
        String timeRegex = "^([01][0-9]|2[0-3]):[0-5][0-9]$";
        return timeString.trim().matches(timeRegex);
    }
}
