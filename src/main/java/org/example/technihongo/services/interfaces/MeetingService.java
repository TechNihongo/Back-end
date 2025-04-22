package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.MeetingDTO;
import org.example.technihongo.dto.PageResponseDTO;
import org.example.technihongo.entities.Meeting;

public interface MeetingService {
    PageResponseDTO<Meeting> getAllMeetings(int pageNo, int pageSize, String sortBy, String sortDir);
    PageResponseDTO<Meeting> getAllActiveMeetings(int pageNo, int pageSize, String sortBy, String sortDir);
    Meeting createMeeting(Integer creatorId, MeetingDTO dto);
    void updateMeeting(Integer meetingId, MeetingDTO dto);
    Meeting getMeetingById(Integer meetingId);
    Meeting getActiveMeetingById(Integer meetingId);
}
