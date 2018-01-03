package fr.nantes.eni.alterplanning.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.util.Date;

/**
 * Created by ughostephan on 25/06/2017.
 */
@Service
public class MailService {

    private static final Logger LOGGER = Logger.getLogger(MailService.class);

    @Value("${mail.default.from}")
    private String defaultFrom;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private VelocityEngine velocityEngine;

    /**
     * The constant BREAK_LINE.
     */
    public static final String BREAK_LINE = "\n";
    /**
     * The constant NEW_LINE.
     */
    public static final String NEW_LINE = BREAK_LINE + BREAK_LINE;

    /**
     * Send mail html.
     *
     * @param to          the to
     * @param bcc         the bcc
     * @param subject     the subject
     * @param contentHtml the content html
     */
    public void sendMailHtml(final String to, final String bcc, final String subject, final String contentHtml) {
        send(defaultFrom, to, bcc, subject, contentHtml, true);
    }

    /**
     * Send mail text.
     *
     * @param to          the to
     * @param bcc         the bcc
     * @param subject     the subject
     * @param contentText the content text
     */
    public void sendMailText(final String to, final String bcc, final String subject, final String contentText) {
        send(defaultFrom, to, bcc, subject, contentText, false);
    }

    /**
     * Resolve template string.
     *
     * @param templateName the template name
     * @param context      the context
     * @return the string
     */
    public String resolveTemplate(final String templateName, final VelocityContext context) {
        StringWriter writer = new StringWriter();
        velocityEngine.mergeTemplate(templateName, "UTF-8", context, writer);
        return writer.toString();
    }

    private void send(final String from, final String to, final String bcc, final String subject, final String content, final boolean isHtml) {
        try {
            final MimeMessagePreparator preparator = mimeMessage -> {
                final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(to);
                if (StringUtils.isNotEmpty(bcc)) {
                    message.setBcc(bcc);
                }
                message.setFrom(from);
                message.setSubject(subject);
                message.setSentDate(new Date());
                message.setText(content, isHtml);
            };

            mailSender.send(preparator);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
