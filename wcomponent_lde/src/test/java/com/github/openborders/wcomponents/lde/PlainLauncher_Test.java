package com.github.openborders.wcomponents.lde;

import java.net.URL;
import java.net.URLConnection;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

import com.github.openborders.AbstractWComponent;
import com.github.openborders.RenderContext;
import com.github.openborders.Request;
import com.github.openborders.UIContext;
import com.github.openborders.UIContextHolder;
import com.github.openborders.UIContextImpl;
import com.github.openborders.WApplication;
import com.github.openborders.WComponent;
import com.github.openborders.WText;
import com.github.openborders.WebUtilities;
import com.github.openborders.servlet.WServlet.WServletEnvironment;
import com.github.openborders.util.Config;
import com.github.openborders.util.StreamUtil;
import com.github.openborders.util.mock.servlet.MockHttpServletRequest;

/**
 * PlainLauncher_Test - unit tests for {@link PlainLauncher}.
 *
 * @author Yiannis Paschalidis.
 * @since 1.0.0
 */
public class PlainLauncher_Test
{
    private PlainLauncher launcher;

    @After
    public void tearDown() throws InterruptedException
    {
        Config.reset();
        UIContextHolder.reset();

        // Shut down the server
        if (launcher != null)
        {
            launcher.stop();
            launcher = null;
        }
    }

    @Test
    public void testCreateUI()
    {
        Config.getInstance().clearProperty(PlainLauncher.COMPONENT_TO_LAUNCH_PARAM_KEY);

        launcher = new PlainLauncher();
        WComponent ui = launcher.createUI();
        Assert.assertTrue("UI should be an instance of WText", ui instanceof WText);
        Assert.assertTrue("UI should contain instructions on configuring the LDE",
                          ((WText) ui).getText().contains(PlainLauncher.COMPONENT_TO_LAUNCH_PARAM_KEY));
    }

    @Test
    public void testGetUI()
    {
        Config.getInstance().setProperty(PlainLauncher.COMPONENT_TO_LAUNCH_PARAM_KEY, MyTestApp.class.getName());
        launcher = new PlainLauncher();

        WComponent ui1 = launcher.getUI(new MockHttpServletRequest());
        Assert.assertTrue("UI should be an instance of MyTestComponent", ui1 instanceof MyTestApp);

        // Call getUI again, the same instance should be returned
        WComponent ui2 = launcher.getUI(new MockHttpServletRequest());
        Assert.assertSame("Should have returned the same UI instance", ui1, ui2);
    }

    @Test
    public void testGetUINonWApplication()
    {
        Config.getInstance().setProperty(PlainLauncher.COMPONENT_TO_LAUNCH_PARAM_KEY, MyTestComponent.class.getName());
        PlainLauncher launcher = new PlainLauncher();

        WComponent ui1 = launcher.getUI(new MockHttpServletRequest());
        Assert.assertTrue("Root UI should be a WApplication", ui1 instanceof WApplication);

        ui1 = ((WApplication) ui1).getChildAt(0);
        Assert.assertTrue("UI should be an instance of MyTestComponent", ui1 instanceof MyTestComponent);

        // Call getUI again, the same instance should be returned
        WComponent ui2 = ((WApplication) launcher.getUI(new MockHttpServletRequest())).getChildAt(0);
        Assert.assertSame("Should have returned the same UI instance", ui1, ui2);
    }

    @Test
    public void testServer() throws Exception
    {
        Config.getInstance().setProperty(PlainLauncher.COMPONENT_TO_LAUNCH_PARAM_KEY, MyTestApp.class.getName());
        Config.getInstance().setProperty("wcomponent.lde.server.port", "0"); // random port
        Config.getInstance().setProperty("wcomponent.whitespaceFilter.enabled", "false");

        launcher = new PlainLauncher();
        launcher.run();

        // Access the server and record the output
        URL url = new URL(launcher.getUrl());
        URLConnection conn = url.openConnection();
        byte[] result = StreamUtil.getBytes(conn.getInputStream());
        String content = new String(result, "UTF-8");

        Assert.assertEquals("HandleRequest should have been called once", 1, MyTestApp.handleRequestCount);
        Assert.assertEquals("PaintComponent should have been called once", 1, MyTestApp.paintCount);

        // Extract session token
        String tokenSearch = "\"wc_t\" value=\"";
        int start = content.indexOf(tokenSearch);
        int end = content.indexOf("\"", start + tokenSearch.length());
        String token = content.substring(start + tokenSearch.length(), end);

        // Recreate the output outside of the LDE.
        UIContext uic = new UIContextImpl();
        WServletEnvironment env = new WServletEnvironment(url.getPath(), url.toString(), "");
        env.setStep(1); // the step count will be set to one after the request
        env.setSessionToken(token);
        uic.setEnvironment(env);
        UIContextHolder.pushContext(uic);
        String expected = WebUtilities.render(new MyTestApp());

        Assert.assertTrue("Content should contain the rendered application", content.contains(expected));
    }

    /**
     * A non-WApplication component, to test that LDE component wrapping works.
     */
    public static final class MyTestComponent extends AbstractWComponent
    {
    }

    /**
     * A Test component to use as the UI.
     */
    public static final class MyTestApp extends WApplication
    {
        /** Some text which is rendered to the client. */
        private static final String HELLO_WORLD = "Hello world!";

        /** The number of times that the handleRequest method has been called. */
        private static int handleRequestCount = 0;

        /** The number of times that the paint method has been called. */
        private static int paintCount = 0;

        /** Creates the test component. */
        public MyTestApp()
        {
            add(new WText(HELLO_WORLD));
        }

        /** {@inheritDoc} */
        @Override
        public void handleRequest(final Request request)
        {
            super.handleRequest(request);

            synchronized (MyTestApp.class)
            {
                handleRequestCount++;
            }
        }

        /** {@inheritDoc} */
        @Override
        protected void paintComponent(final RenderContext renderContext)
        {
            super.paintComponent(renderContext);

            synchronized (MyTestApp.class)
            {
                paintCount++;
            }
        }
    }
}
