package by.vitikova.discovery.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "imageClient", value = "${feign.image-service.value}", url = "${feign.image-service.url}")
public interface ImageClient {

    @DeleteMapping("/images/remove")
    void removeImage(@RequestParam String uuid);

    @DeleteMapping("/avatars/remove")
    void removeAvatar(@RequestParam String uuid);
}
