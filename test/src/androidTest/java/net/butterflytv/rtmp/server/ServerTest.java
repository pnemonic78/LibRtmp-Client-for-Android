package net.butterflytv.rtmp.server;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import static android.text.format.DateUtils.SECOND_IN_MILLIS;
import static java.lang.Thread.sleep;

/**
 * Test the server.
 */
@RunWith(AndroidJUnit4.class)
public class ServerTest extends AndroidTestCase {

    @Test
    public void testInit() throws Exception {
        RtmpServerService service = new RtmpServerService();
        service.start();
        assertEquals(RtmpServerService.DEFAULT_PORT, service.getPort());
        assertTrue(service.isStarted());
        sleep(SECOND_IN_MILLIS);
        service.stop();
        assertFalse(service.isStarted());
    }
}
