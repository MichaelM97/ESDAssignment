package helper;

import java.util.Random;

public final class Password {

    final int ALPHA_MIN_LEFT = 97;
    final int ALPHA_MAX_RIGHT = 122;
    final int SYMB_MIN_LEFT = 33;
    final int SYMB_MAX_RIGHT = 47;

    private String password;

    public Password(int length) {
        generate_password(length);
    }

    /**
     * @param length (int) - Length of password to be generated.
     * @return String - Password
     */
    public String generate_password(int length) {
        Ranger ranger = (min, max, rand) -> {
            return (char) (min + (int) (rand * (max - min + 1)));
        };

        if (length <  1) {
            this.password = null;
            return this.password;
        }

        Random rand = new Random();
        StringBuilder passwBuffer = new StringBuilder(length);

        // Build up String
        for (int i = 0; i < length; i++) {
            float roll = rand.nextFloat();
            if (roll < 0.33) {
                // Int
                passwBuffer.append(rand.nextInt(10));
            } else if (roll >= 0.33 && roll < 0.66) {
                // Char
                char charValue = ranger.generate_char_between(ALPHA_MIN_LEFT,
                        ALPHA_MAX_RIGHT, rand.nextFloat());
                if (rand.nextFloat() > 0.5) {
                    charValue = Character.toUpperCase(charValue);
                }
                passwBuffer.append(charValue);
            } else {
                // Symbol
                char charValue = ranger.generate_char_between(SYMB_MIN_LEFT,
                        SYMB_MAX_RIGHT, rand.nextFloat());
                passwBuffer.append(charValue);
            }
        }
        this.password = passwBuffer.toString();
        return this.password;
    }

    public String getPassword() {
        return this.password;
    }

}
