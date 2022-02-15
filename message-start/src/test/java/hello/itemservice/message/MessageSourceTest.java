package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource ms;

    @Test
    void helloMessageTest() {
        String hello = ms.getMessage("hello", null, null);
        assertThat(hello).isEqualTo("안녕");
    }

    @Test
    void notFoundMessageCode() {
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    void defaultMessage() {
        String message = ms.getMessage("no_code", null, "기본메시지", null);
        assertThat(message).isEqualTo("기본메시지");
    }

    @Test
    void argumentMessage() {
        String message = ms.getMessage("hello.name", new Object[]{"Spring!"}, null);
        assertThat(message).isEqualTo("안녕 Spring!");
    }

    @Test
    void defaultLang() {
        String message = ms.getMessage("hello", null, Locale.KOREA);
        assertThat(message).isEqualTo("안녕");
        assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }
}
