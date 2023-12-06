package io.github.tacticalminky.mangawatcher;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * RouteController
 */
@Controller
public class RouteController {

    @RequestMapping(value = { "/{path:[^\\.]*}" })
    public String redirect(HttpServletRequest request) {
        return "forward:/";
    }
}
