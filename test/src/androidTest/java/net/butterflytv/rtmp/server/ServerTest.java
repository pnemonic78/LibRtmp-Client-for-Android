package net.butterflytv.rtmp.server;

import net.butterflytv.rtmp_client.RtmpClient;

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

    private static final int PORT = RtmpServerService.DEFAULT_PORT;
    private static final String URL_PATH = "rtmp://127.0.0.1:" + PORT + "/test";
    private static final long SLEEP = 5 * SECOND_IN_MILLIS;

    @Test
    public void test0() throws Exception {
        RtmpServerService service = new RtmpServerService();
        service.start();
        assertEquals(PORT, service.getPort());
        assertTrue(service.isRunning());
        sleep(SLEEP);
        assertTrue(service.isRunning());
        service.stop();
        assertFalse(service.isRunning());
    }

    @Test
    public void testConnectClient() throws Exception {
        RtmpServerService service = new RtmpServerService();
        service.start();
        assertEquals(PORT, service.getPort());
        assertTrue(service.isRunning());

        RtmpClient client = new RtmpClient();
        assertFalse(client.isConnected());
        client.open(URL_PATH, false);
        assertTrue(client.isConnected());

        sleep(SLEEP);
        assertTrue(client.isConnected());
        client.close();
        assertFalse(client.isConnected());

        sleep(SLEEP);
        assertTrue(service.isRunning());
        service.stop();
        assertFalse(service.isRunning());
    }

    @Test
    public void testPublisher() throws Exception {
        RtmpServerService service = new RtmpServerService();
        service.start();
        assertEquals(PORT, service.getPort());
        assertTrue(service.isRunning());

        RtmpClient publisher = new RtmpClient();
        assertFalse(publisher.isConnected());
        publisher.open(URL_PATH, true);
        assertTrue(publisher.isConnected());

        RtmpClient client = new RtmpClient();
        assertFalse(client.isConnected());
        client.open(URL_PATH, false);
        assertTrue(client.isConnected());

        sleep(SLEEP);
        assertTrue(publisher.isConnected());
        publisher.close();
        assertFalse(publisher.isConnected());
        assertTrue(client.isConnected());
        client.close();
        assertFalse(client.isConnected());

        sleep(SLEEP);
        assertTrue(service.isRunning());
        service.stop();
        assertFalse(service.isRunning());
    }
}
