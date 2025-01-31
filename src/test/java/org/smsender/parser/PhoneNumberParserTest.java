package org.smsender.parser;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PhoneNumberParserTest {


    @Test
    void parsePhone() throws IOException {
        // actual
        List<String> actual = PhoneNumberParser.parsePhone("src/test/resources/test1.txt");
        // expected
        List<String> expected = List.of("+380673333444", "+380678888999", "+380673344555");
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void testThatNotFoundException(){
        assertThrows(FileNotFoundException.class, () -> PhoneNumberParser.parsePhone(""));
    }

    @Test
    void testWrongFormatNumberValidation() throws IOException {
        List<String> actual = PhoneNumberParser.parsePhone("src/test/resources/wrongPhoneFormat.txt");
        List<String> expected = List.of("+380677788999", "+380978344550");

        assertArrayEquals(expected.toArray(), actual.toArray());
    }
}