package helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class PasswordTest {

    /**
     * Test instantiation of Password object with invalid size creates null
     * password
     */
    @Test
    public void test_construct_null_password() {
        System.out.println("test_construct_null_password");
        for (int len = 0; len >= -10000; len--) {
            String result = new Password(len).getPassword();
            System.out.println(result + " is null?");
            assertNull(result);
        }
    }

    /**
     * Tests valid password generates string
     */
    @Test
    public void test_construct_password_type() {
        System.out.println("test_construct_password_type");
        String result = new Password(12).getPassword();
        System.out.println(result + " is String?");
        assertTrue(result instanceof String);
    }

    /**
     * Tests instantiation of Password object creates correct length password
     * which can be retrieved through a getter.
     */
    @Test
    public void test_construct_password_length() {
        System.out.println("test_construct_password_length");
        for (int len = 1; len <= 10000; len++) {
            String result = new Password(len).getPassword();
            System.out.println(result);
            System.out.println("Len Expected = " + len);
            assertEquals(result.length(), len);
        }
    }

    /**
     * Tests same object can have a password which changes when generate
     * password called multiple times.
     */
    @Test
    public void test_construct() {
        System.out.println("test_construct");
        String prev_password = " ";
        for (int len = 1; len <= 10000; len++) {
            String result = new Password(len).getPassword();
            System.out.println("Prev Password: " + prev_password);
            System.out.println("New Password: " + result);
            assertFalse(prev_password.equals(result));
            prev_password = result;
        }
    }

    /**
     * Tests Generate Password returns the current member var password
     */
    @Test
    public void test_one_password() {
        System.out.println("test_one_password");
        Password password = new Password(0);
        String gen_pass_response = password.generate_password(100);
        String get_pass_response = password.getPassword();
        System.out.println(
                "Comp - P1: " + gen_pass_response + " P2: "
                + get_pass_response);
        assertSame(gen_pass_response, get_pass_response);
        assertTrue(gen_pass_response.equals(get_pass_response));
    }
}
