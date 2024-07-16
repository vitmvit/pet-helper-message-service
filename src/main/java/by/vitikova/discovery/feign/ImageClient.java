package by.vitikova.discovery.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign-клиент для взаимодействия с микросервисом работы с изображениями
 */
@FeignClient(contextId = "imageClient", value = "${feign.image-service.value}", url = "${feign.image-service.url}")
public interface ImageClient {

    /**
     * Удаление изображения по UUID.
     *
     * @param uuid UUID изображения
     */
    @DeleteMapping("/images/remove")
    void removeImage(@RequestParam String uuid);

    /**
     * Удаление аватара по UUID.
     *
     * @param uuid UUID аватара
     */
    @DeleteMapping("/avatars/remove")
    void removeAvatar(@RequestParam String uuid);
}
