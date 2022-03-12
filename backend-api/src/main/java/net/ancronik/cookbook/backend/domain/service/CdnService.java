package net.ancronik.cookbook.backend.domain.service;

import lombok.NonNull;
import net.ancronik.cookbook.backend.application.exceptions.CdnException;
import net.ancronik.cookbook.backend.web.dto.UploadImageResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for handling CDN requests.
 *
 * @author Nikola Presecki
 */
public interface CdnService {

    /**
     * Method for uploading image to the CDN and creating thumbnail if needed
     *
     * @param createThumbnail if {@literal true} thumbnail will be created
     * @param file            image file, not null
     * @return response
     */
    @Transactional
    UploadImageResponse uploadImage(boolean createThumbnail, @NonNull MultipartFile file) throws CdnException;

}
