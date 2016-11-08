package me.gevorg.birthday.util;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Tests for {@link BirthdayUtil}.
 *
 * @author Gevorg Harutyunyan.
 */
public class BirthdayUtilTest {

    /**
     * Test for {@link BirthdayUtil#parseEventDateString} with polish string.
     */
    @Test
    public void testParseEventDateStringPolish() {
        // Source.
        Date result = BirthdayUtil.parseEventDateString("18 kwi 1991");

        // Expected.
        Calendar expected = Calendar.getInstance();
        expected.set(1991, 3, 18, 0, 0, 0);

        // Assertion.
        assertEquals("Result date is wrong!", expected.getTime().toString(),
                result == null ? null : result.toString());
    }

    /**
     * Test for {@link BirthdayUtil#parseEventDateString} with full russian string.
     */
    @Test
    public void testParseEventDateStringFullRussian() {
        // Source.
        Date result = BirthdayUtil.parseEventDateString("4 марта 1990 г.");

        // Expected.
        Calendar expected = Calendar.getInstance();
        expected.set(1990, 2, 4, 0, 0, 0);

        // Assertion.
        assertEquals("Result date is wrong!", expected.getTime().toString(),
                result == null ? null : result.toString());
    }

    /**
     * Test for {@link BirthdayUtil#parseEventDateString} with russian string.
     */
    @Test
    public void testParseEventDateStringRussian() {
        // Source.
        Date result = BirthdayUtil.parseEventDateString("15 Июн 1988 г.");

        // Expected.
        Calendar expected = Calendar.getInstance();
        expected.set(1988, 5, 15, 0, 0, 0);

        // Assertion.
        assertEquals("Result date is wrong!", expected.getTime().toString(),
                result == null ? null : result.toString());
    }

    /**
     * Test for {@link BirthdayUtil#parseEventDateString} with full string.
     */
    @Test
    public void testParseEventDateStringFullString() {
        // Source.
        Date result = BirthdayUtil.parseEventDateString("22 Aug 1944");

        // Expected.
        Calendar expected = Calendar.getInstance();
        expected.set(1944, 7, 22, 0, 0, 0);

        // Assertion.
        assertEquals("Result date is wrong!", expected.getTime().toString(),
                result == null ? null : result.toString());
    }

    /**
     * Test for {@link BirthdayUtil#parseEventDateString} with dash string.
     */
    @Test
    public void testParseEventDateStringDashString() {
        // Source.
        Date result = BirthdayUtil.parseEventDateString("07-Nov-1960");

        // Expected.
        Calendar expected = Calendar.getInstance();
        expected.set(1960, 10, 7, 0, 0, 0);

        // Assertion.
        assertEquals("Result date is wrong!", expected.getTime().toString(),
                result == null ? null : result.toString());
    }


}
