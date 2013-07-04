package com.peergreen.webconsole.community;

import com.peergreen.webconsole.Constants;
import com.peergreen.webconsole.IConsole;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.StaticServiceProperty;

/**
 * Peergreen Administration Unsecured Console
 * @author Mohammed Boukada
 */
@Component(name = Constants.UNSECURED_CONSOLE_PID)
@Provides(properties = {@StaticServiceProperty(name = Constants.CONSOLE_NAME, type = "java.lang.String", mandatory = true),
                        @StaticServiceProperty(name = Constants.CONSOLE_ALIAS, type = "java.lang.String", mandatory = true),
                        @StaticServiceProperty(name = Constants.DEFAULT_ROLES, type = "java.lang.String[]", mandatory = false)})
public class UnsecuredConsole implements IConsole {
}
