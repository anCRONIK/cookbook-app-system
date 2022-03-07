package net.ancronik.cookbook.backend.web.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.domain.service.CdnService;
import net.ancronik.cookbook.backend.web.dto.UploadImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for uploading image.
 *
 * @author Nikola Presecki
 */
@RestController
@RequestMapping(UploadImageController.DEFAULT_MAPPING)
@Slf4j
public class UploadImageController {

    public static final String DEFAULT_MAPPING = "/api/v1/cdn/upload-image";

    private final CdnService cdnService;

    @Autowired
    public UploadImageController(CdnService cdnService) {
        this.cdnService = cdnService;
    }

    @SneakyThrows
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    //Secure only authorized user
    public UploadImageResponse uploadImage(@RequestParam(required = false) boolean createThumbnail, @RequestParam("imageFile") MultipartFile file) {
        LOG.info("Uploading new image");

        return cdnService.uploadImage(true, file);
    }

}
