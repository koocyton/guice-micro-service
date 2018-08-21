package com.doopp.gauss.server.configuration;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Import({

})

@ComponentScan(basePackages = {"com.doopp.gauss"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)
})

@Configuration
// @EnableCaching
public class ApplicationConfiguration {

}
