package com.AWS.s3.manager;

import com.AWS.s3.models.S3Object;
import com.AWS.s3.repo.S3ObjectRepository;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class APIManager {

    @Autowired
    private S3ObjectRepository s3ObjectRepository;

    String bucket_name = "cloudaware-test";
    List<String> versionList = new ArrayList<>();

    final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

    public void executeObjects() {
        List<S3ObjectSummary> result = s3.listObjectsV2(bucket_name).getObjectSummaries();
        List<S3ObjectSummary> res = result.subList(0, 10);

        s3ObjectRepository.deleteAll();

        for (S3ObjectSummary el : res) {

            S3Object s3Object = new S3Object();

            s3Object.setName(el.getKey());
            s3Object.seteTag(el.getETag());
            s3Object.setLastModified(el.getLastModified());
            s3Object.setSize(el.getSize());
            s3Object.setStorageClass(el.getStorageClass());

            List<S3VersionSummary> versions = s3.listVersions(bucket_name, el.getKey()).getVersionSummaries();

            versionList.clear();

            for (S3VersionSummary item : versions) {
                versionList.add(item.getVersionId());

                s3Object.setVersionId(versionList.toString().replace("[", "").replace("]", ""));
            }
            s3ObjectRepository.save(s3Object);
        }
    }
}
