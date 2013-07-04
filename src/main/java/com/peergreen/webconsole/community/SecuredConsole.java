package com.peergreen.webconsole.community;

import com.peergreen.webconsole.Constants;
import com.peergreen.webconsole.IConsole;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.StaticServiceProperty;

/**
 * Peergreen Administration Secured Console
 * @author Mohammed Boukada
 */
@Component(name = Constants.SECURED_CONSOLE_PID)
@Provides(properties = {@StaticServiceProperty(name = Constants.CONSOLE_NAME, type = "java.lang.String", mandatory = true),
                        @StaticServiceProperty(name = Constants.CONSOLE_ALIAS, type = "java.lang.String", mandatory = true)})
public class SecuredConsole implements IConsole {
}
