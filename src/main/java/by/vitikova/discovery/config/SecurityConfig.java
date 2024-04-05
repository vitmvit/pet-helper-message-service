package by.vitikova.discovery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасности для веб-приложения.
 * Определяет правила доступа к различным конечным точкам API и включает фильтр безопасности для аутентификации и авторизации.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Конфигурирует правила доступа к различным конечным точкам API и добавляет фильтр безопасности.
     *
     * @param httpSecurity объект HttpSecurity для настройки правил доступа
     * @return SecurityFilterChain объект, представляющий цепочку фильтров безопасности
     * @throws Exception при возникновении ошибки во время выполнения конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    /**
     * Получает AuthenticationManager из AuthenticationConfiguration.
     *
     * @param authenticationConfiguration объект AuthenticationConfiguration, который предоставляет AuthenticationManager
     * @return AuthenticationManager объект для аутентификации
     * @throws Exception при возникновении ошибки во время получения AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}