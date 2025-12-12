package com.chikawa.user_service.utils;

import com.chikawa.user_service.services.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Component
public class SendEmail {
    EmailService emailService;

    public void sendEmailRegister(String confirmToken, String email) {
//        String link = "http://localhost:8085/api/v1/auth/confirm?token=" + confirmToken;
        String link = "https://fearsome-ollie-correspondently.ngrok-free.dev/api/v1/auth/confirm?token=" + confirmToken;
        String htmlContent =
                "<div style='font-family:Arial, sans-serif; line-height:1.6; padding:20px; color:#333;'>"
                        + "<h2 style='color:#ff6f61;'>Chiikawa Goods Shop</h2>"
                        + "<p>Hello,</p>"
                        + "<p>Thank you for registering an account at <strong>Chiikawa Goods Shop</strong>.</p>"
                        + "<p>To complete your registration and activate your account, please click the button below:</p>"

                        + "<div style='margin:30px 0;'>"
                        + "    <a href='" + link + "' "
                        + "       style='display:inline-block; padding:12px 22px; background-color:#ff6f61; "
                        + "              color:white; text-decoration:none; border-radius:6px; font-weight:bold;'>"
                        + "        Confirm Account"
                        + "    </a>"
                        + "</div>"

                        + "<p>If you did not request to create an account, please ignore this email.</p>"
                        + "<p>Best regards,<br><strong>Chiikawa Goods Shop Team</strong></p>"

                        + "<hr style='margin-top:40px; border:none; border-top:1px solid #eee;'/>"
                        + "<p style='font-size:12px; color:#777;'>"
                        + "This is an automated email, please do not reply."
                        + "</p>"
                        + "</div>";

        emailService.sendMail(email, "Chiikawa Goods Shop - Account Confirmation", htmlContent);
    }

    public void sendEmailForgotPassword(String confirmToken, String email) {

//        String link = "http://localhost:8085/api/v1/auth/confirm-forgot?token=" + confirmToken;
        String link = "https://fearsome-ollie-correspondently.ngrok-free.dev/api/v1/auth/confirm-forgot?token=" + confirmToken;

        String htmlContent =
                "<div style='font-family:Arial, sans-serif; line-height:1.6; padding:20px; color:#333;'>"
                        + "<h2 style='color:#ff6f61;'>Chiikawa Goods Shop</h2>"
                        + "<p>Hello,</p>"
                        + "<p>We received a request to reset your password.</p>"
                        + "<p>Please click the button below to continue resetting your password:</p>"

                        + "<div style='margin:30px 0;'>"
                        + "    <a href='" + link + "' "
                        + "       style='display:inline-block; padding:12px 22px; background-color:#ff6f61; "
                        + "              color:white; text-decoration:none; border-radius:6px; font-weight:bold;'>"
                        + "        Reset Password"
                        + "    </a>"
                        + "</div>"

                        + "<p>If you did not request a password reset, please ignore this email.</p>"
                        + "<p>Best regards,<br><strong>Chiikawa Goods Shop Team</strong></p>"

                        + "<hr style='margin-top:40px; border:none; border-top:1px solid #eee;'/>"
                        + "<p style='font-size:12px; color:#777;'>"
                        + "This is an automated email, please do not reply."
                        + "</p>"
                        + "</div>";

        emailService.sendMail(email, "Chiikawa Goods Shop - Reset Your Password", htmlContent);
    }
}
