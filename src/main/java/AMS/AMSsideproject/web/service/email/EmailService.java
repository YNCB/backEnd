package AMS.AMSsideproject.web.service.email;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.exception.user.AlreadyJoinedUser;
import AMS.AMSsideproject.web.exception.user.UserNullException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final UserService userService;
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private String authNum;

    //랜덤 인증 코드 생성
    public void createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0;i<8;i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        this.authNum = key.toString();
    }

    //메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {

        createCode(); //인증 코드 생성

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
        message.setSubject(EmailInfo.TITLE); //제목 설정
        message.setFrom(EmailInfo.FROM); //보내는 이메일
        message.setText(setContext(authNum), "utf-8", "html"); //보낼 내용

        return message;
    }

    //타임리프를 이용한 context 설정
    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("email/email", context); //email.html
    }

    //실제 메일 전송
    @Transactional(readOnly = true)
    public String sendEmail(String toEmail) throws MessagingException, UnsupportedEncodingException {

        /**
         * 여기서 나중에 가입된 이메일 사용자는 인증 번호자체가 안보내지게??!!! 설계?!!!
         */
        try { //이미 회원가입된 사용자 -> 인증코드 전송 안되는 경우
            User findUser = userService.findUserByEmail(toEmail);
            throw new AlreadyJoinedUser("이미 회원가입된 이메일 입니다.");

        }catch (UserNullException e) { //전송되는 경우
            //메일전송에 필요한 정보 설정
            MimeMessage emailForm = createEmailForm(toEmail);
            //실제 메일 전송
            emailSender.send(emailForm);
            return authNum; //인증 코드 반환
        }
    }

}
