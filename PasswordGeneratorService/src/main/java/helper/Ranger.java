package helper;

public interface Ranger {

    /**
     * @param min (int) - Ascii table Min
     * @param max (int) - Ascii table Max
     * @param rand (float) - Float between 0 and 1.
     * @return char - Corresponding Ascii char between min and max.
     */
    public char generate_char_between(int min, int max, float rand);
}
