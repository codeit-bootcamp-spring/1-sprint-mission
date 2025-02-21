//package com.sprint.mission.discodeit.service;
//
//import com.sprint.mission.discodeit.dto.ReadStatusDTO;
//import com.sprint.mission.discodeit.entity.ReadStatus;
//import com.sprint.mission.discodeit.repository.ReadStatusRepository;
//import com.sprint.mission.discodeit.basic.BasicReadStatusService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.Instant;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ReadStatusServiceTest {
//
//    @Mock
//    private ReadStatusRepository readStatusRepository;
//
//    @InjectMocks
//    private BasicReadStatusService readStatusService;
//
//    private ReadStatus readStatus;
//
//    @BeforeEach
//    void setUp() {
//        readStatus = new ReadStatus("testUser", "testChannel");
//    }
//
//    @Test
//    void testUpdateReadStatus() {
//        ReadStatusDTO dto = new ReadStatusDTO("testUser", "testChannel", Instant.now());
//
//        when(readStatusRepository.findByUserIdAndChannelId("testUser", "testChannel"))
//                .thenReturn(Optional.of(readStatus));
//        when(readStatusRepository.save(any(ReadStatus.class))).thenReturn(readStatus);
//
//        ReadStatusDTO updatedStatus = readStatusService.updateReadStatus(dto);
//
//        assertNotNull(updatedStatus);
//        assertEquals("testUser", updatedStatus.getUserId());
//        verify(readStatusRepository, times(1)).save(any(ReadStatus.class));
//    }
//
//    @Test
//    void testFindReadStatus() {
//        when(readStatusRepository.findByUserIdAndChannelId("testUser", "testChannel"))
//                .thenReturn(Optional.of(readStatus));
//
//        ReadStatusDTO foundStatus = readStatusService.findReadStatus("testUser", "testChannel");
//
//        assertNotNull(foundStatus);
//        assertEquals("testUser", foundStatus.getUserId());
//        assertEquals("testChannel", foundStatus.getChannelId());
//    }
//}