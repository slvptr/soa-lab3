package com.soa.ejb.service;

import com.soa.ejb.model.domain.StudyGroup;
import jakarta.ejb.Remote;

import java.util.List;
import java.util.Optional;

@Remote
public interface RemoteAdditionalService {
    Long countTransferred();
    List<StudyGroup> moveStudents(long from, long to);
}
