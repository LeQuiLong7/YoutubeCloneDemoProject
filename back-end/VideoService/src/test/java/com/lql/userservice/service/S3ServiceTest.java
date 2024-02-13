package com.lql.userservice.service;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class S3ServiceTest {

    @Autowired
    private S3Service s3Service;
    @Autowired
    private  AmazonS3 amazonS3;
    private final String BUCKET_NAME = "my-youtube-clone-demo-project-bucket";

    @Test
    public void generateUrlTest() {
        String objectId = "289889625_753446105782856_7346881778479559908_n.jpg";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 24 * 7);
        Date expiresDate = calendar.getTime();

        String s = s3Service.generateUrl(BUCKET_NAME, objectId, expiresDate);

        System.err.println(s);
        assertThat(s).isNotNull();
        assertThat(s).contains("https://");

    }
    @Test
    public void deleteObjectTest() {
        String objectId = "af98e6f3-d4ab-4272-9acd-62b379c3e356.png";

        s3Service.deleteObject(BUCKET_NAME, objectId);
        System.err.println("ok");
//        amazonS3.
    }
}
