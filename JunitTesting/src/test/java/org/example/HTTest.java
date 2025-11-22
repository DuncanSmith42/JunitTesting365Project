import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class HTTest {

    private HT ht;

    // ---------- SETUP / TEARDOWN ----------

    @BeforeEach
    void setUp() {
        ht = new HT();
        ht.add("alpha");
        ht.add("alpha"); // alpha count = 2
        ht.add("gamma");
    }

    @AfterEach
    void tearDown() {
        ht = null;
    }

    // ---------- ADD() TESTS ----------

    // E1: new key inserted
    @Test
    void testAdd_NewKey() {
        int initialSize = ht.size;
        ht.add("delta");
        assertTrue(ht.contains("delta"));
        assertEquals(initialSize + 1, ht.size);
    }

    // E2: duplicate key increments count
    @Test
    void testAdd_DuplicateKeyIncrementsCount() {
        HT.Node node = ht.getNode("gamma");
        int originalCount = node.count;

        ht.add("gamma");

        assertEquals(originalCount + 1, ht.getNode("gamma").count);
        assertEquals(2, ht.getNode("alpha").count); // control
    }

    // E3: hash collision but not equal
    @Test
    void testAdd_HashCollisionDifferentKeys() {
        CollisionKey k1 = new CollisionKey("A");
        CollisionKey k2 = new CollisionKey("B");

        ht.add(k1);
        ht.add(k2);

        assertNotNull(ht.getNode(k1));
        assertNotNull(ht.getNode(k2));
        assertEquals(2, ht.size);
    }

    // E.a: null key
    @Test
    void testAdd_NullKeyThrowsException() {
        assertThrows(NullPointerException.class, () -> ht.add(null));
    }


    // ---------- GETNODE() TESTS ----------

    // D1: existing key
    @Test
    void testGetNode_ExistingKey() {
        HT.Node node = ht.getNode("alpha");
        assertNotNull(node);
        assertEquals("alpha", node.key);
        assertEquals(2, node.count);
    }

    // D2: absent key
    @Test
    void testGetNode_AbsentKey() {
        assertNull(ht.getNode("beta"));
    }

    // D3: hash collision but different key
    @Test
    void testGetNode_HashCollision() {
        CollisionKey k1 = new CollisionKey("A");
        CollisionKey k2 = new CollisionKey("B");

        ht.add(k1);

        assertNotNull(ht.getNode(k1));
        assertNull(ht.getNode(k2));
    }

    // D.a: null input
    @Test
    void testGetNode_NullKeyThrowsException() {
        assertThrows(NullPointerException.class, () -> ht.getNode(null));
    }


    // ---------- CONTAINS() TESTS ----------

    // J1: existing string
    @Test
    void testContains_ExistingKey() {
        assertTrue(ht.contains("alpha"));
    }

    // J2: missing string
    @Test
    void testContains_MissingKey() {
        assertFalse(ht.contains("beta"));
    }

    // J3: non-String input
    @Test
    void testContains_NonString() {
        assertFalse(ht.contains(9));
    }

    // ---------- TEST SUITE ----------

    @Test
    void runAllTestsSuiteMarker() {
        assertTrue(true); // Marker to show suite execution
    }

    // ---------- HELPER CLASS FOR HASH COLLISION ----------

    static class CollisionKey {
        private final String value;

        CollisionKey(String value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return 42; // Force collision
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof CollisionKey)) return false;
            return value.equals(((CollisionKey) obj).value);
        }
    }
}
