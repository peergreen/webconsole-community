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

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.StaticServiceProperty;

import com.peergreen.webconsole.Constants;
import com.peergreen.webconsole.IConsole;

/**
 * Peergreen Administration Unsecured Console
 *
 * @author Mohammed Boukada
 */
@Component(name = Constants.DEVELOPMENT_MODE_CONSOLE_PID)
@Provides(properties = {@StaticServiceProperty(name = Constants.CONSOLE_NAME, type = "java.lang.String", mandatory = true),
        @StaticServiceProperty(name = Constants.CONSOLE_ALIAS, type = "java.lang.String", mandatory = true),
        @StaticServiceProperty(name = Constants.DEFAULT_ROLES, type = "java.lang.String[]", mandatory = false)})
public class UnsecuredConsole implements IConsole {
}
