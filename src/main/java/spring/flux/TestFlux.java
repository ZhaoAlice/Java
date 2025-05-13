package spring.flux;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 〈test〉<br>
 *
 * @author 0027009101
 * @create 2024/12/25
 * @since 1.0.0
 */
public class TestFlux {
    public static void main(String[] args) throws JsonProcessingException {
        //BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //System.out.println(bCryptPasswordEncoder.encode("11"));
        System.out.println(new ObjectMapper().writeValueAsString(new User("1", "testResponse")));
    }

    public static class User {
        private String id;
        private String userName;

        public User(String id, String userName) {
            this.id = id;
            this.userName = userName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}