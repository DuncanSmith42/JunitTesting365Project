import org.junit.jupiter.api.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {


    public void setUp() {
        // If stopwords is static you may want to clear/add words here
        // Otherwise leave empty
    }


    public void tearDown() {}

    @Test
    public void testConvert_LiveWikipedia() throws Exception {
        // Real URL
        String url = "https://en.wikipedia.org/wiki/Game";

        HT table = Main.convert(url);

        assertNotNull(table);
        // The Game article has well over 100 words
        assertTrue(table.getTotalWordCount() > 50);
        
        assertFalse(table.contains("[1]"));
    }

    @Test
    public void testConvert_RemovesFootnotes() throws Exception {
        String url = "https://en.wikipedia.org/wiki/Game";

        HT table = Main.convert(url);

        for (int j = 0; j < table.table.length; j++) {
            for (HT.Node e = table.table[j]; e != null; e = e.next) {
                assertFalse(e.toString().matches("\\[\\d+\\]"));
            }
        }
    }

    @Test
    public void testConvert_StopwordsRemoved() throws Exception {
        String url = "https://en.wikipedia.org/wiki/Game";

        HT table = Main.convert(url);
        
        assertFalse(table.contains("the"));
        assertFalse(table.contains("and"));
        assertFalse(table.contains("of"));
    }

    @Test
    public void testConvert_MultipleLivePages() throws Exception {
        String[] urls = {
                "https://en.wikipedia.org/wiki/Game",
                "https://en.wikipedia.org/wiki/Computer",
                "https://en.wikipedia.org/wiki/Science"
        };

        for (String url : urls) {
            HT table = Main.convert(url);
            assertNotNull(table);
            assertTrue(table.getTotalWordCount() > 50);
        }
    }

    
    @Test
    public void testConvert_404Page() {
        String badUrl =
                "https://en.wikipedia.org/wiki/ThisPageDoesNotExist_483892349";

        try {
            Main.convert(badUrl);
            fail("Expected IOException due to 404");
        } catch (Exception expected) {}
    }

    @Test
    public void testConvert_InvalidURL() {
        String badUrl = "notAURL";

        try {
            Main.convert(badUrl);
            fail("Expected IllegalArgumentException or IOException");
        } catch (Exception expected) {}
    }
}
