package com.soa.ejb.service;

import com.soa.ejb.model.domain.*;
import com.soa.ejb.model.requests.GroupView;
import com.soa.ejb.retrofit.GroupService;
import jakarta.inject.Inject;
import jakarta.ejb.Stateless;
import org.jboss.ejb3.annotation.Pool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
@Pool("slsb-strict-max-pool")
public class AdditionalService implements RemoteAdditionalService {
    @Inject
    private GroupService groupService;

    public Long countTransferred() {
        try {
            long sum = 0;
            StudyGroupPage groupPage = groupService.getGroups();
            List<StudyGroup> groupList = groupPage.getStudyGroups();
            if (groupList == null) return 0L;
            for (StudyGroup group : groupList) {
                sum += group.getTransferredStudents();
            }
            return sum;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StudyGroup> moveStudents(long from, long to) {
        try {
            StudyGroup groupFrom = groupService.getGroupById(from);
            StudyGroup groupTo = groupService.getGroupById(to);

            if (groupFrom == null || groupTo == null) {
                throw new RuntimeException();
            }

            GroupView groupViewFrom = new GroupView(
                    groupFrom.getName(),
                    groupFrom.getCoordinates(),
                    0L,
                    0L,
                    groupFrom.getAverageMark(),
                    groupFrom.getSemesterEnum().getValue(),
                    groupFrom.getGroupAdmin()
            );
            GroupView groupViewTo = new GroupView(
                    groupTo.getName(),
                    groupTo.getCoordinates(),
                    groupTo.getStudentsCount() + groupFrom.getStudentsCount(),
                    groupTo.getTransferredStudents() + groupFrom.getTransferredStudents(),
                    groupTo.getAverageMark(),
                    groupTo.getSemesterEnum().getValue(),
                    groupTo.getGroupAdmin()
            );

            groupFrom = groupService.updateGroup(from, groupViewFrom);
            groupTo = groupService.updateGroup(to, groupViewTo);

            List<StudyGroup> resultList = new ArrayList<StudyGroup>();
            resultList.add(groupFrom);
            resultList.add(groupTo);

            return resultList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
