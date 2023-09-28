package net;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2023/3/2
 * @since 1.0.0
 */
public class test {


    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("11"));
    }
}