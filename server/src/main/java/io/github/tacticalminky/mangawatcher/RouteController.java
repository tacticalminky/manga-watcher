package io.github.tacticalminky.mangawatcher;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * RouteController
 *
 * @author Andrew Mink
 * @version Dec 17, 2023
 * @since 1.0.0-b.4
 */
@Controller
public class RouteController implements ErrorController {

    @RequestMapping(value = { "${server.error.path:${error.path:/error}}" })
    public String redirect() {
        return "forward:/";
    }
}
