// SPDX-License-Identifier: MIT
package com.mercedesbenz.sechub.sharedkernel.storage;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mercedesbenz.sechub.commons.core.util.SecHubStorageUtil;
import com.mercedesbenz.sechub.storage.core.JobStorage;
import com.mercedesbenz.sechub.storage.core.JobStorageFactory;
import com.mercedesbenz.sechub.storage.core.S3Setup;
import com.mercedesbenz.sechub.storage.core.SharedVolumeSetup;
import com.mercedesbenz.sechub.storage.core.StorageService;
import com.mercedesbenz.sechub.storage.s3.AwsS3JobStorageFactory;
import com.mercedesbenz.sechub.storage.sharevolume.spring.SharedVolumeJobStorageFactory;

/**
 * MultiStorageService - will provide job storage objects depending on
 * configuration / setup situation. Provides access to a shared volume (e.g. a
 * NFS) or native S3 access
 *
 * @author Albert Tregnaghi
 *
 */
@Service
public class MultiStorageService implements StorageService {

    private static final Logger LOG = LoggerFactory.getLogger(MultiStorageService.class);

    private JobStorageFactory jobStorageFactory;

    @Autowired
    public MultiStorageService(SharedVolumeSetup sharedVolumeSetup, S3Setup s3Setup) {

        if (s3Setup.isAvailable()) {
            jobStorageFactory = new AwsS3JobStorageFactory(s3Setup);

        } else if (sharedVolumeSetup.isAvailable()) {
            jobStorageFactory = new SharedVolumeJobStorageFactory(sharedVolumeSetup);

        }

        if (jobStorageFactory == null) {
            throw new IllegalStateException("Did not found any available storage setup! At least one must be set!");
        }
        LOG.info("Created storage factory: {}", jobStorageFactory.getClass().getSimpleName());

    }

    @Override
    public JobStorage getJobStorage(String projectId, UUID jobUUID) {
        /*
         * we use here "jobstorage/${projectId} - so we have same job storage path as
         * before in sechub itself - for PDS own prefix (storageId) is used insdide
         * storagePath. We could have changed to something like
         * "sechub/jobstarge/${projectId}" but this would have forced migration issues.
         * So we keep this "old style"
         */
        return jobStorageFactory.createJobStorage(SecHubStorageUtil.createStoragePath(projectId), jobUUID);
    }

}