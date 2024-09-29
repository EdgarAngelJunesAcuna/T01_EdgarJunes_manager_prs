package com.inventary.enriqueta.application.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            // JSON de credenciales directamente en el código
            String firebaseConfig = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"webmariaenriquetadominici\",\n" +
                    "  \"private_key_id\": \"8d1374ce69ccff15d543da3c084fd43c001f0e75\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQChxlrfkWTv8Gvx\\nBQmnpsH6MQGBcJ7tVTpJrVSC/PNRJiHIWMlcpY5LvyTUVt2tlcFtcjLy67VTde0C\\nAq0jeDBEK4ZkshUPquQbT1iD1ugvYE1/0vidTV30wQrsHJgn22oKfWyGxPQ+CXAf\\nreWgPG0n2ftaaXGsVI7EBqCsR+H1UwKjzFKG/2cM8yXl9P7HpDTS4VIBl2XrXu3Q\\n8faXi4NDZYvKUp/L0W4G4X46gsS5QOIp18La1JBs5wQ7FBH6klDFvA8Vh75QMvbZ\\nB/ZI5VrHq0kYZa/fqLltv4T96Xwuj8n+OZv+TL6NurPmvIcJwYIGGpVOVfaUutil\\nYFg+JyaxAgMBAAECggEABA2rsGSHeQFBvHSB8SwwZI6D8+Rola2YFrQBL9YFhQqV\\nopw3kWV0NbQonf8Th1pUUiPKyy46PnOFjdJs2FHA3dhnAeYPY94w4HlMuw2bBuLc\\nwOo+QKIUC6z8PdMalzcRcV6tUIl7TYRK2v8Dsk/enqFrgZtY6zaO1t0L+2fCrYpN\\nvxxYN8v6oJ55NNQXWl/GfkYiyUhMtm3kMq88GpkQ6QTiO5axc69daNkGH3xFC2DP\\n+SNWAw7ledYFd16lijPwxpTs0bMn94+uExs7/0/O5Seqt428rT8JmwkGbItpgGAv\\neE3/K5fM6E3vVQRF+7EPnulxGbXD+LZLQW05rCPXMQKBgQDijuAhutBN/jhgnd00\\n4ZikSRlrX8Ft2JD1LAP8N44K8aNqGPFE2Lv4PR676br6nnB9qEtN+1bDkAn9bu01\\npB/NHdQhhn6sW694uaaBsn2akLyFamva2IzhsxfpB86Gs/2Yu13T6pq7iNJ4Vn+A\\n4XfN/VYH7/zn1ub43N0CHABpWQKBgQC2zEWwztsD0F31rikkpoj6/zR9D9yLJ6p4\\npuOKLet9AlIXbw59BQPNgBvO5ymIp+ZBM9l5NzmkDwzrJGzfqThT/8aoq6+SH2Oa\\nX6rDlRjzSlhx+tdu8TKMllKf1cF3JG71C+i89IHMv/I6WYpJB93tkL3Lbo5sw/G7\\n5FgVupslGQKBgQCpiSUr2HIciwqBMmDgO5lQBuugsA09N/xxmzh4OtJuWhhhQrz5\\nOf6fao1fLS6gcIPZZGDFwDlTKqxvG90icOjjQ1kEpXKRZCLRAowZLgpT6c9JPzPq\\nlnYGR1CzLwZbQsGY000JUdvsThzBtGjBdveCC/ufAPWOQACjZhN0iqO8MQKBgF/p\\nGf/E6SE9KK+JK8kxH9oiIRYlRmA09brMRcLwiQhUOWJWQNHT2cTvHKgYnbA3+BXp\\n8IcrNLEhvHN77ywDC6z3HPLO8hXHJmmPHHf2ONsd/P4A0RxCrXUaQkveyd2LXjRw\\nhTLvT1NwutVmuanL2OOW+B5O6aejv6gIDTeJa3GxAoGAZ0WqrPxbMYkYip+OalC5\\nRH9Cv6YpNm2Nl6jDJBipLk2pqGIFLUOHVg7J+8dOcyeeWxrphesgpb4V2d5pE5EK\\n4JlvJJchKMjj9ETgVxR87PPM0rYLt0emBEBS+589MrKduI64lO+5jv9AytKu5efF\\nYnabr2r8FnVmMzcUBnABpt4=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-dus6v@webmariaenriquetadominici.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"100178683818435619353\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-dus6v%40webmariaenriquetadominici.iam.gserviceaccount.com\"\n" +
                    "}";

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(firebaseConfig.getBytes())))
                    .build();

            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }
}
