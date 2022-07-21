package net.ancronik.cookbook.backend.api.domain.service.impl;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.ancronik.cookbook.backend.api.application.exceptions.CdnException;
import net.ancronik.cookbook.backend.api.domain.service.CdnService;
import net.ancronik.cookbook.backend.api.web.dto.UploadImageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service(value = "CdnServiceMock")
@AllArgsConstructor
public class CdnServiceMockImpl implements CdnService {

    @Override
    public UploadImageResponse uploadImage(boolean createThumbnail, @NonNull MultipartFile file) throws CdnException {
        return new UploadImageResponse();
    }
}
