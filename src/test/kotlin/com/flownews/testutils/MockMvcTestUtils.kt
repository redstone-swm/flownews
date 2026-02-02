package com.flownews.testutils

import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

object MockMvcTestUtils {
    fun createMockMvc(
        controller: Any,
        restDocumentation: RestDocumentationContextProvider,
    ): MockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(MockCurrentUserArgumentResolver())
            .apply<StandaloneMockMvcBuilder>(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint()),
            )
            .build()

    fun createMockMvcWithoutAuth(
        controller: Any,
        restDocumentation: RestDocumentationContextProvider,
    ): MockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .apply<StandaloneMockMvcBuilder>(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint()),
            )
            .build()
}
