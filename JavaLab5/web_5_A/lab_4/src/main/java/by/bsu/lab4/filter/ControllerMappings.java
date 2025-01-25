/*
 * =============================================================================
 *
 *   Copyright (c) 2011-2016, The THYMELEAF team (http://www.thymeleaf.org)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 * =============================================================================
 */
package by.bsu.lab4.filter;

import by.bsu.lab4.contr.*;
import org.thymeleaf.web.IWebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;


public class ControllerMappings {


    private static Map<String, IController> controllersByURL;


    static {
        controllersByURL = new HashMap<String, IController>();

        controllersByURL.put("/car/info", new CarInfoController());
        controllersByURL.put("/cookies", new CarInfoController());
        controllersByURL.put("/car/list", new CarController());
        controllersByURL.put("/client/list", new ClientController());
        controllersByURL.put("/request/list", new RequestController());

        ///
        controllersByURL.put("/auth*", new AuthController());
        controllersByURL.put("/home", new HomeController());
        controllersByURL.put("/register*", new RegisterController());
        controllersByURL.put("/logout*", new LogoutController());
    }




public static IController resolveControllerForRequest(final IWebRequest request) {
    final String path = request.getPathWithinApplication();
    var result = controllersByURL.entrySet().stream()
            .filter(x -> Pattern.matches(x.getKey().replaceAll("\\*", ".*"), path))
            .findFirst()
            .orElse(null);
    if (Objects.isNull(result) || path.equals("/")) return new HomeController();
    else {
        return result.getValue();
    }
}

    private ControllerMappings() {
        super();
    }


}
