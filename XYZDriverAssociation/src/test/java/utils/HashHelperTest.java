package utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class HashHelperTest {

    @Test
    public void shouldHashString() {
        // Given
        String textToHash = "password";

        // When
        String hashedText = HashHelper.hashString(textToHash);

        // Then
        assertNotNull(hashedText);
    }

    @Test
    public void shouldHashStringWithNumbers() {
        // Given
        String textToHash = "password123";

        // When
        String hashedText = HashHelper.hashString(textToHash);

        // Then
        assertNotNull(hashedText);
    }

    @Test
    public void shouldHashStringWithSymbols() {
        // Given
        String textToHash = "password£@!$%^&*()_+-={}[]:;\"'|\\<,>.?/~`'";

        // When
        String hashedText = HashHelper.hashString(textToHash);

        // Then
        assertNotNull(hashedText);
    }

    @Test
    public void hashedStringsShouldMatch() {
        // Given
        String textToHash = "password£@!$%^&*()_+-={}[]:;\"'|\\<,>.?/~`'";

        // When
        String hashedText1 = HashHelper.hashString(textToHash);
        String hashedText2 = HashHelper.hashString(textToHash);

        // Then
        assertEquals(hashedText1, hashedText2);
    }

}
