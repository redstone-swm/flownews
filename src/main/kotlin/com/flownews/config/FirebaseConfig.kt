package com.flownews.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import javax.annotation.PostConstruct

@Configuration
class FirebaseConfig {
    private val logger = LoggerFactory.getLogger(FirebaseConfig::class.java)

    @PostConstruct
    fun initFirebase() {
        val resource = ClassPathResource("serviceAccountKey.json")
        if (!resource.exists()) {
            logger.warn("Firebase service account file not found in classpath")
            return
        }
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(resource.inputStream))
            .build()
        FirebaseApp.initializeApp(options)
        logger.info("FirebaseApp initialized successfully.")
    }
}
