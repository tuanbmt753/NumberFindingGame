package com.example.numberfindinggame.utils;

public class Validator {

    public static String validateUsername(String username) {

        if (username.isEmpty()) {
            return "Vui lòng nhập tên đăng nhập.";
        }

        if (username.length() < 3) {
            return "Tên đăng nhập phải có ít nhất 3 ký tự.";
        }

        if (username.length() > 50) {
            return "Tên đăng nhập không được vượt quá 50 ký tự.";
        }

        if (!username.matches("^[a-zA-Z0-9_.]+$")) {
            return "Tên đăng nhập chỉ được chứa chữ cái, số, dấu _ và dấu .";
        }

        return null;
    }

    public static String validateEmail(String email) {

        String emailRegex =
                "^[a-zA-Z0-9._%+-]+@(?!(?:[0-9]+\\.)+[a-zA-Z]{2,})[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (email.isEmpty()) {
            return "Vui lòng nhập email.";
        }

        if (email.length() > 100) {
            return "Email không được vượt quá 100 ký tự.";
        }

        if (!email.matches(emailRegex)) {
            return "Email không đúng định dạng.";
        }

        return null;
    }

    public static String validatePhone(String phone) {

        if (phone.isEmpty()) {
            return "Vui lòng nhập số điện thoại.";
        }

        if (phone.length() > 20) {
            return "Số điện thoại không được vượt quá 20 ký tự.";
        }

        if (phone.matches(".*[０-９].*")) {
            return "Không được sử dụng ký tự số đặc biệt.";
        }

        if (!phone.matches("^(0|\\+84)[0-9]{9,10}$")) {
            return "Số điện thoại phải bắt đầu bằng 0 hoặc +84 và chứa 10-11 chữ số.";
        }

        return null;
    }

    public static String validatePassword(String password) {

        if (password.isEmpty()) {
            return "Vui lòng nhập mật khẩu.";
        }

        if (password.length() < 6) {
            return "Mật khẩu phải có ít nhất 6 ký tự.";
        }

        if (password.length() > 225) {
            return "Mật khẩu không được vượt quá 225 ký tự.";
        }

        return null;
    }

    public static String validateOTP(String otp) {

        if (otp.isEmpty()) {
            return "Vui lòng nhập otp.";
        }

        if (!otp.matches("\\d{6}")) {
            return "OTP phải gồm 6 chữ số.";
        }

        return null;
    }

    public static String validateNhapLaiPassword(String password, String nhapLai) {

        if (nhapLai.isEmpty()) {
            return "Vui lòng nhập mật khẩu.";
        }

        if (nhapLai.length() < 6) {
            return "Mật khẩu phải có ít nhất 6 ký tự.";
        }

        if (nhapLai.length() > 225) {
            return "Mật khẩu không được vượt quá 225 ký tự.";
        }

        if (!nhapLai.equals(password)) {
            return "Mật khẩu nhập lại không khớp.";
        }

        return null;
    }
}