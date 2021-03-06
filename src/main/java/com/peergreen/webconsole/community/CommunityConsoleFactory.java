/**
 * Peergreen S.A.S. All rights reserved.
 * Proprietary and confidential.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.peergreen.webconsole.community;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.ow2.util.log.Log;
import org.ow2.util.log.LogFactory;

import com.peergreen.webconsole.Constants;
import com.peergreen.webconsole.IConsole;
import com.peergreen.webconsole.core.osgi.VaadinOSGiServlet;
import com.peergreen.webconsole.core.vaadin7.UIProviderFactory;
import com.vaadin.server.UIProvider;

/**
 * Community Console factory
 *
 * @author Mohammed Boukada
 */
@Component(publicFactory = false)
@Instantiate
public class CommunityConsoleFactory {

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog(CommunityConsoleFactory.class);

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

    public CommunityConsoleFactory(@Requires ConfigurationAdmin configurationAdmin) throws IOException {
        // Create default instance
        if (!isDefaultConsolePresent(configurationAdmin)) {
            Configuration configuration = configurationAdmin.createFactoryConfiguration(Constants.DEVELOPMENT_MODE_CONSOLE_PID, null);
            Dictionary<String, String> properties = new Hashtable<>();
            properties.put("console.name", "Peergreen Administration Console");
            properties.put("console.alias", "/admin");
            properties.put("default.roles", "admin");
            configuration.update(properties);
        }
    }

    private boolean isDefaultConsolePresent(ConfigurationAdmin configurationAdmin) {
        String filter = String.format("(&(%s=%s)(%s=%s))", ConfigurationAdmin.SERVICE_FACTORYPID, Constants.DEVELOPMENT_MODE_CONSOLE_PID, "console.alias", "/admin");
        try {
            Configuration[] configurations = configurationAdmin.listConfigurations(filter);
            return configurations != null && configurations.length != 0;
        } catch (IOException | InvalidSyntaxException e) {
            LOGGER.error("Fail to get default configuration for webconsole", e);
            return false;
        }
    }

    /**
     * Bind a console
     *
     * @param console
     */
    @Bind(aggregate = true, optional = true)
    public void bindConsole(IConsole console, Dictionary properties) {
        if (!Constants.PRODUCTION_MODE_CONSOLE_PID.equals(properties.get("factory.name")) &&
                !Constants.DEVELOPMENT_MODE_CONSOLE_PID.equals(properties.get("factory.name"))) {
            return;
        }
        properties.put(Constants.ENABLE_SECURITY, ((String) properties.get("instance.name")).contains(Constants.PRODUCTION_MODE_CONSOLE_PID));
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
            LOGGER.warn(e);
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
