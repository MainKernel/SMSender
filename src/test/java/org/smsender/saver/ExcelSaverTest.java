package org.smsender.saver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.smsender.entity.PhoneObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

class ExcelSaverTest {

    @TempDir
    Path tempPath;

    @Test
    void testSaving() throws IOException {
        // Before
        List<PhoneObject> phoneObjects = List.of(new PhoneObject("+380638849200", "WhatsApp"),
                new PhoneObject("+380636493188", "Viber"),
                new PhoneObject("+380654866492", "Viber"),
                new PhoneObject("+380675839224", "SMS"),
                new PhoneObject("+380613846322", "SMS"));

        ExcelSaver.saveNumber(phoneObjects, tempPath, "testfile");
        File file = new File(tempPath.toString());
        File[] files = file.listFiles();
        boolean testfile = false;
        if(files != null){
            testfile = Arrays.stream(files).anyMatch(file1 -> file1.getName().startsWith("testfile"));
        }
        Assertions.assertTrue(testfile);
    }

}