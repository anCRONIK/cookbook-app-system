package net.ancronik.cookbook.backend.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.application.exceptions.CdnException;
import net.ancronik.cookbook.backend.domain.service.CdnService;
import net.ancronik.cookbook.backend.web.dto.UploadImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * Controller for uploading image.
 *
 * @author Nikola Presecki
 */
@RestController
@RequestMapping(UploadImageController.DEFAULT_MAPPING)
@Slf4j
@Validated
public class UploadImageController {

    public static final String DEFAULT_MAPPING = "/api/v1/cdn/upload-image";

    private final CdnService cdnService;

    @Autowired
    public UploadImageController(CdnService cdnService) {
        this.cdnService = cdnService;
    }

    @Transactional
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    //Secure only authorized user
    public UploadImageResponse uploadImage(@RequestParam(required = false) boolean createThumbnail,
                                           @RequestParam("imageFile") @NotNull MultipartFile file) throws CdnException {
        LOG.debug("Uploading new image");

        return cdnService.uploadImage(createThumbnail, file);
    }

}
