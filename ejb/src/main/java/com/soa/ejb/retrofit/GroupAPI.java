package com.soa.ejb.retrofit;


import com.soa.ejb.model.domain.StudyGroup;
import com.soa.ejb.model.domain.StudyGroupPage;
import com.soa.ejb.model.requests.GroupView;
import retrofit2.Call;
import retrofit2.http.*;

public interface GroupAPI {
    @GET("{id}")
    Call<StudyGroup> getGroupById(@Path("id") long id);

    @GET("study-groups")
    Call<StudyGroupPage> getGroups(@Query("pageSize") int pageSize);

    @PUT("{id}")
    Call<StudyGroup> updateGroup(@Path("id") long id, @Body GroupView groupView);
}
