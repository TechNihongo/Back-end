package org.example.technihongo.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JWTAthenticationEntryPoint point;

    @Autowired
    private JWTAuthenticationFilter filter;

    @Autowired
    private CorsFilter corsFilter; // Inject the CorsFilter

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/api/v1/payment/callback", "/api/v1/payment/ipn-handler",
                                "/*/*/login", "/*/*/register", "/*/*/logout", "/*/*/google-auth",
                                "/*/*/getUser/*", "/*/user/*/username", "/*/user/searchStudentName",
                                "/*/user/getUserByStudentId/*", "/*/user/*/password", "/*/user/forgot-password",
                                "/*/user/reset-password", "/*/user/resend-verification-email", "/*/user/verify-email",
                                "/*/user/check-token", "/*/dashboard/student/*", "/*/difficulty-level/**",
                                "/*/achievement/all", "/*/course/all", "/*/course/{id:[0-9]+}", "/*/course/all/*",
                                "/*/domain/all", "/*/domain/parentDomain", "/*/domain/childrenDomain",
                                "/*/domain/getDomain/*", "/*/domain/searchDomainName", "/*/domain/getDomainByTag",
                                "/*/domain/*/getChildrenTag", "/*/flashcard/getFlashcard/*", "/*/flashcard/studentFlashcardSet/*",
                                "/*/flashcard/systemFlashcardSet/*", "/*/learning-path/all", "/*/learning-path/{id:[0-9]+}",
                                "/*/learning-path/search/*", "/*/learning-resource/{id:[0-9]+}", "/*/lesson/{id:[0-9]+}",
                                "/*/lesson/*/paginated/*", "/*/lesson-resource/{id:[0-9]+}", "/*/lesson-resource/lesson/*",
                                "/*/meeting/all", "/*/meeting/{id:[0-9]+}", "/*/script/meeting/*", "/*/script/{id:[0-9]+}",
                                "/*/payment-method/all", "/*/payment/*").permitAll()


                        .requestMatchers("/*/lesson-progress/view", "/*/favorite/view", "/*/learning-log/view",
                                "/*/achievement/student/**", "/*/flashcard/*/studentCreate", "/*/flashcard-progress/*",
                                "/*/folder-item/**", "/*/student/**").hasRole("Student")


                        .requestMatchers("/*/course/create", "/*/course/update/*", "/*/course/creator",
                                "/*/course/creator/*", "/*/course/domain/*", "/*/domain/create", "/*/domain/update/*",
                                "/*/domain/delete/*", "/*/*/*/systemCreate", "/*/learning-path/create",
                                "/*/learning-path/update/*", "/*/learning-path/delete/*", "/*/learning-resource/all",
                                "/*/learning-resource/create", "/*/learning-resource/update/*", "/*/learning-resource/update-status/*",
                                "/*/learning-resource/delete/*", "/*/lesson/create", "/*/lesson/update/*",
                                "/*/*/update-order/*", "/*/*/set-order/*", "/*/lesson-resource/create",
                                "/*/lesson-resource/update/*", "/*/lesson-resource/delete/*", "/*/lesson-resource/study-plan",
                                "/*/meeting/create", "/*/meeting/update/*", "/*/script/create", "/*/script/update/*",
                                "/*/script/delete/*", "/*/option/create", "/*/option/update", "/*/question/all",
                                "/*/question/create", "/*/question/update/*", "/*/question/options/**", "/*/quiz/all",
                                "/*/quiz/create", "/*/quiz/update/*", "/*/quiz/update-status/*", "/api/quiz-question/create",
                                "/*/quiz-question/delete/*", "/*/quiz-question/create-new-question").hasRole("Content Manager")


                        .requestMatchers("/*/user/content-manager", "/*/*/*/overview").hasRole("Administrator")


                        .requestMatchers("/*/flashcard/*/update", "/*/flashcard/delete/*", "/*/option/question/*",
                                "/*/option/{id:[0-9]+}", "/*/question/{id:[0-9]+}", "/*/quiz/{id:[0-9]+}",
                                "/*/quiz-question/quiz/*", "/*/quiz-question/{id:[0-9]+}", "/*/*/questions-options/{id:[0-9]+}").hasAnyRole("Student", "Content Manager")


                        .requestMatchers("/*/user/paginated", "/*/user/student/*", "/*/user/content-managers/*",
                                "/*/user/searchContentManagerName", "/*/learning-path/creator", "/*/learning-resource/creator",
                                "/*/payment-method/*/update", "/*/quiz/creator").hasAnyRole("Content Manager", "Administrator")


                        .requestMatchers("/*/statistics/view").hasAnyRole("Student", "Administrator")


                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}



