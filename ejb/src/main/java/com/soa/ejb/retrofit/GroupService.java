package com.soa.ejb.retrofit;

import com.soa.ejb.model.domain.StudyGroup;
import com.soa.ejb.model.domain.StudyGroupPage;
import com.soa.ejb.model.requests.GroupView;
import jakarta.ejb.Stateless;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Stateless
public class GroupService {
    private final String BASE_URL = "https://localhost:8081/api/v1/groups/";
    private final GroupAPI groupAPI;

    public GroupService() throws Exception {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getTlsTrustOkHttpClient())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        this.groupAPI = retrofit.create(GroupAPI.class);
    }

    public StudyGroup getGroupById(long id) throws IOException {
        return groupAPI.getGroupById(id).execute().body();
    }

    public StudyGroupPage getGroups() throws IOException {
        return groupAPI.getGroups(10000).execute().body();
    }

    public StudyGroup updateGroup(long id, GroupView groupView) throws IOException {
        return groupAPI.updateGroup(id, groupView).execute().body();
    }

    private OkHttpClient getTlsTrustOkHttpClient() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}
            @Override
            public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
        }};
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }
}
