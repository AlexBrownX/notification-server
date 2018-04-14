package com.landg.services.notification.configuration

import com.google.common.collect.Sets
import java.util.Arrays
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseMessageBuilder
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ResponseMessage
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
@Profile("default")
class SwaggerConfiguration : WebMvcConfigurer {

    companion object {
        private val API_ERROR_MODEL = ModelRef("ApiError")
    }

    private val globalResponseMessages: List<ResponseMessage>
        get() = Arrays.asList(
                ResponseMessageBuilder().code(200).message("The request was completed successful").build(),

                ResponseMessageBuilder().code(400).message("A request was made which is in error, such as an invalid property value")
                        .responseModel(API_ERROR_MODEL).build(),

                ResponseMessageBuilder().code(404).message("A resource was requested which does not exist")
                        .responseModel(API_ERROR_MODEL).build(),

                ResponseMessageBuilder().code(405).message("An HTTP method was requested which is not supported")
                        .responseModel(API_ERROR_MODEL).build(),

                ResponseMessageBuilder().code(415).message("A media type was used which is not supported")
                        .responseModel(API_ERROR_MODEL).build(),

                ResponseMessageBuilder().code(500).message("Something went unexpectedly wrong within the service while executing the request")
                        .responseModel(API_ERROR_MODEL).build())

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.landg"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .produces(Sets.newHashSet("application/json"))
                .consumes(Sets.newHashSet("application/json"))
                .globalResponseMessage(RequestMethod.GET, globalResponseMessages)
                .globalResponseMessage(RequestMethod.POST, globalResponseMessages)
                .globalResponseMessage(RequestMethod.PUT, globalResponseMessages)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("Web Push Notification Service API")
                .description("A service that sends notifications to web applications using Web Push.")
                .version("1")
                .build()
    }
}


