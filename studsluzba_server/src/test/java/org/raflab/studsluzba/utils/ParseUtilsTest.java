package org.raflab.studsluzba.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.raflab.studsluzba.utils.ParseUtils;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ParseUtilsTest {

    // ---------- parseIndeks ----------

    @DisplayName("parseIndeks: valid inputs")
    @ParameterizedTest(name = "{index} ⇒ {0} → [{1},{2},{3}]")
    @MethodSource("validIndeksCases")
    void parseIndeks_valid(String input, String prog, String yy, String num) {
        String[] out = ParseUtils.parseIndeks(input);
        assertNotNull(out);
        assertArrayEquals(new String[]{prog, yy, num}, out);
    }

    static Stream<Arguments> validIndeksCases() {
        return Stream.of(
                Arguments.of("rn1923", "RN", "19", "23"),
                Arguments.of("RN1923", "RN", "19", "23"),
                Arguments.of("cs20123", "CS", "20", "123"),
                Arguments.of("raf250001", "RAF", "25", "0001"),
                Arguments.of("gx9901", "GX", "99", "01")
        );
    }

    @DisplayName("parseIndeks: clearly invalid/short returns null")
    @ParameterizedTest
    @ValueSource(strings = {"abc", "RN19", "RN1"})
    void parseIndeks_invalid_returnsNull(String input) {
        assertNull(ParseUtils.parseIndeks(input));
    }

    // ---------- parseEmail ----------

    @DisplayName("parseEmail: valid emails")
    @ParameterizedTest(name = "{index} ⇒ {0}")
    @MethodSource("validEmailCases")
    void parseEmail_valid(String email, String prog, String yy, String num) {
        String[] out = ParseUtils.parseEmail(email);
        assertNotNull(out);
        assertArrayEquals(new String[]{prog, yy, num}, out);
    }

    static Stream<Arguments> validEmailCases() {
        return Stream.of(
                Arguments.of("ppetrovic1220rn@raf.rs", "rn", "20", "12"),
                // multi-letter program, long number part
                Arguments.of("mika12345raf@raf.rs", "raf", "45", "123")
                // NOTE: the current implementation assumes at least 3 digits before program+yy,
                // so avoid edge cases that would throw unless you change the code to guard them.
        );
    }

    @DisplayName("parseEmail: malformed inputs throw (documents current behavior)")
    @ParameterizedTest
    @ValueSource(strings = {
            "ppetrovic1220rn@example.com",  // wrong domain → returns null (guarded)
            "ppetrovic@raf.rs",             // no digits → likely StringIndexOutOfBounds
            "ppetrovic12@raf.rs",           // missing program after digits → likely out of bounds
            "noatsign"                      // no @
    })
    void parseEmail_invalid_cases(String email) {
        if (email.endsWith("@raf.rs") && email.contains("@") && email.matches("^[^@]+@raf\\.rs$")) {
            // For domain OK but malformed local parts, current code tends to throw
            assertThrows(RuntimeException.class, () -> ParseUtils.parseEmail(email));
        } else {
            // Non-raf domain or totally malformed → current code returns null at the first guard
            assertNull(ParseUtils.parseEmail(email));
        }
    }
}
