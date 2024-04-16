package me.skean.synologyphotosutil.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class JumpController {
    @RequestMapping("/")
    public String index() {
        return "doc.html";
    }
}