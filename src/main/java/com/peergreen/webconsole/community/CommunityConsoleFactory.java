package com.peergreen.webconsole.community;

import com.peergreen.webconsole.Constants;
import com.peergreen.webconsole.IConsole;
import com.peergreen.webconsole.core.osgi.VaadinOSGiServlet;
import com.peergreen.webconsole.core.vaadin7.UIProviderFactory;
import com.vaadin.server.UIProvider;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import javax.servlet.ServletException;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Community Console factory
 *
 * @author Mohammed Boukada
 */
@Component(publicFactory = false)
@Instantiate
public class CommunityConsoleFactory {

    /**
     * Http Service
     */
    @Requires
    private HttpService httpService;

    /**
     * UI provider factory
     */
    @Requires
    private UIProviderFactory uiProviderFactory;

    private List<String> aliases = new CopyOnWriteArrayList<>();

    /**
     * Bind a console
     *
     * @param console
     */
    @Bind(aggregate = true, optional = true)
    public void bindConsole(IConsole console, Dictionary properties) {
        if (!Constants.SECURED_CONSOLE_PID.equals(properties.get("factory.name")) &&
                !Constants.UNSECURED_CONSOLE_PID.equals(properties.get("factory.name"))) {
            return;
        }
        properties.put(Constants.ENABLE_SECURITY, ((String) properties.get("instance.name")).contains(Constants.SECURED_CONSOLE_PID));
        // Create an UI provider for the console UI
        UIProvider uiProvider = uiProviderFactory.createUIProvider(properties);
        // Create a servlet
        VaadinOSGiServlet servlet = new VaadinOSGiServlet(uiProvider);
        try {
            // Register the servlet with the console alias
            String alias = (String) properties.get(Constants.CONSOLE_ALIAS);
            httpService.registerServlet(alias, servlet, null, null);
            aliases.add(alias);
        } catch (ServletException | NamespaceException e) {
            // ignore update
        }
    }

    /**
     * Unbind a console
     *
     * @param console
     */
    @Unbind
    public void unbindConsole(IConsole console, Dictionary properties) {
        // Unregister its servlet
        uiProviderFactory.stopUIProvider(properties);
        httpService.unregister((String) properties.get(Constants.CONSOLE_ALIAS));
    }

    @Invalidate
    public void stop() {
        for (String alias : aliases) {
            httpService.unregister(alias);
        }
    }
}
