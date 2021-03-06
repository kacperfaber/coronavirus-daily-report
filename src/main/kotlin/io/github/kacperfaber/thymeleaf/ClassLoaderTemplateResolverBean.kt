package io.github.kacperfaber.thymeleaf

import org.springframework.context.annotation.*
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.*

@Configuration
open class ClassLoaderTemplateResolverBean {
    @Bean
    open fun classLoaderTemplateResolver(): ClassLoaderTemplateResolver {
        return ClassLoaderTemplateResolver().apply {
            prefix = "th-templates/"
            suffix = ".html"
            templateMode = TemplateMode.HTML
            characterEncoding = "UTF-8"
        }
    }
}