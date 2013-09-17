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

import com.peergreen.webconsole.Constants;
import com.peergreen.webconsole.IConsole;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.StaticServiceProperty;

/**
 * Peergreen Administration Secured Console
 *
 * @author Mohammed Boukada
 */
@Component(name = Constants.SECURED_CONSOLE_PID)
@Provides(properties = {@StaticServiceProperty(name = Constants.CONSOLE_NAME, type = "java.lang.String", mandatory = true),
        @StaticServiceProperty(name = Constants.CONSOLE_ALIAS, type = "java.lang.String", mandatory = true)})
public class SecuredConsole implements IConsole {
}
