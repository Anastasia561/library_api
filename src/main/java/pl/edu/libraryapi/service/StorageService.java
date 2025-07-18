package pl.edu.libraryapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.libraryapi.exception.FolderNotFoundException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;

@Service
public class StorageService {
    private final S3Client s3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public StorageService(S3Client s3) {
        this.s3 = s3;
    }

    public void uploadFiles(List<MultipartFile> files, String folderName) {

        for (MultipartFile file : files) {
            try {
                String key = folderName + "/" + file.getOriginalFilename();

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build();

                s3.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file to S3: " + file.getOriginalFilename(), e);
            }
        }
    }

    public void deleteFolder(String folderName) {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(folderName + "/")
                .build();

        ListObjectsV2Response listResponse = s3.listObjectsV2(listRequest);
        List<ObjectIdentifier> toDelete = listResponse.contents().stream()
                .map(s3Object -> ObjectIdentifier.builder().key(s3Object.key()).build())
                .toList();

        if (toDelete.isEmpty()) {
            throw new FolderNotFoundException("Folder with name - " + folderName + " not found");
        }

        DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(Delete.builder().objects(toDelete).build())
                .build();

        s3.deleteObjects(deleteRequest);
    }
}
