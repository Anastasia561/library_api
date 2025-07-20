package pl.edu.libraryapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.libraryapi.exception.FolderNotFoundException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

@Service
public class StorageService {
    private final S3Client s3;
    private final S3Presigner s3Presigner;

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    public StorageService(S3Client s3, S3Presigner s3Presigner) {
        this.s3 = s3;
        this.s3Presigner = s3Presigner;
    }

    public void uploadFiles(List<MultipartFile> files, String folderName) {

        for (MultipartFile file : files) {
            try {
                String key = assignFileName(folderName, file);
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

    private static String assignFileName(String folderName, MultipartFile file) {
        String contentType = file.getContentType();
        String key;

        if (contentType != null && contentType.startsWith("image/")) {
            key = folderName + "/cover";
        } else if ("application/pdf".equals(contentType)) {
            key = folderName + "/book.pdf";
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }
        return key;
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

    public URL generateBookCoverURL(String folderName) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(folderName + "/cover")
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(request)
                .signatureDuration(Duration.ofMinutes(5))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url();
    }

    public URL generateBookDownloadURL(String folderName, String bookName, boolean isFull) {
        String suffix = isFull ? "_full.pdf\"" : "_preview.pdf\"";
        String keySuffix = isFull ? "/book.pdf" : "/preview.pdf";
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .responseContentDisposition("attachment; filename=\"" + sanitize(bookName) + suffix)
                .key(folderName + keySuffix)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(request)
                .signatureDuration(Duration.ofMinutes(5))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url();
    }

    private String sanitize(String title) {
        return title.replaceAll("[^a-zA-Z0-9\\s.-]", "").replaceAll("\\s+", "_");
    }
}
