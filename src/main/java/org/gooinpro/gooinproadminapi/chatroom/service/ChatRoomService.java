package org.gooinpro.gooinproadminapi.chatroom.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.gooinpro.gooinproadminapi.chatmessage.service.ChatService;
import org.gooinpro.gooinproadminapi.chatroom.domain.ChatRoomEntity;
import org.gooinpro.gooinproadminapi.chatroom.dto.ChatRoomAddDTO;
import org.gooinpro.gooinproadminapi.chatroom.dto.ChatRoomGetDTO;
import org.gooinpro.gooinproadminapi.chatroom.dto.ChatRoomAddPartDTO;
import org.gooinpro.gooinproadminapi.chatroom.repository.ChatRoomRepository;
import org.gooinpro.gooinproadminapi.employer.domain.EmployerEntity;
import org.gooinpro.gooinproadminapi.employer.repository.EmployerRepository;
import org.gooinpro.gooinproadminapi.parttimer.domain.PartTimerEntity;
import org.gooinpro.gooinproadminapi.parttimer.repository.PartTimerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service

@RequiredArgsConstructor
@Log4j2
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final EmployerRepository employerRepository;
    private final PartTimerRepository partTimerRepository;
    private final ChatService chatService;


    public ChatRoomGetDTO findChatRoom(Long eno) { // 고용인 채팅방 찾기

        // 채팅방이 있는지 확인
        Optional<ChatRoomEntity> existingChatRoom = chatRoomRepository.findByEmployer_Eno(eno);

        if (existingChatRoom.isPresent()) {
            // 채팅방 있으면 그냥 채팅방 반환
            log.info("11111111111111111");
            return chatRoomRepository.GetRoomNumber(eno);
        } else {
            // 채팅방이 없으면 생성하고 방 번호를 가져오기
            ChatRoomAddDTO chatRoomAddDTO = new ChatRoomAddDTO();
            chatRoomAddDTO.setEno(eno);

            ChatRoomGetDTO dto = new ChatRoomGetDTO();
            dto.setRno(addChatRoom(chatRoomAddDTO)); // 채팅방 생성

            return dto;
        }
    }

    public Long addChatRoom(ChatRoomAddDTO chatRoomAddDTO) { // 고용인 채팅방 새로 만들기

        Optional<ChatRoomEntity> existingChatRoom = chatRoomRepository.findByEmployer_Eno(chatRoomAddDTO.getEno());

        if (existingChatRoom.isPresent()) {
            return existingChatRoom.get().getRno();
        }
        log.info("44444444444444444");
        Optional<EmployerEntity> eno = employerRepository.findByEno(chatRoomAddDTO.getEno());

        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                .employer(eno.get())
                .build();

        log.info("555555555555555");
        // saveAndFlush 사용하여 저장 후 즉시 DB에 반영
        return chatRoomRepository.save(chatRoomEntity).getRno();
    }

    public ChatRoomGetDTO findChatPartRoom(Long pno) { // 파트타이머 채팅방 찾기

        // 채팅방이 있는지 확인
        Optional<ChatRoomEntity> existingChatRoom = chatRoomRepository.findByPartTimer_Pno(pno);

        if (existingChatRoom.isPresent()) {
            // 채팅방 있으면 그냥 채팅방 반환
            return chatRoomRepository.GetRoomPartNumber(pno);
        } else {
            // 채팅방이 없으면 생성하고 방 번호를 가져오기
            ChatRoomAddPartDTO chatRoomAddPartDTO = new ChatRoomAddPartDTO();
            chatRoomAddPartDTO.setPno(pno);

            ChatRoomGetDTO dto = new ChatRoomGetDTO();
            dto.setRno(addChatPartRoom(chatRoomAddPartDTO)); // 채팅방 생성

            return dto;
        }
    }


    public Long addChatPartRoom(ChatRoomAddPartDTO chatRoomAddPartDTO ) { // 파트타이머 채팅방 새로 만들기

        Optional<ChatRoomEntity> existingChatRoom = chatRoomRepository.findByPartTimer_Pno(chatRoomAddPartDTO.getPno());

        if (existingChatRoom.isPresent()) {
            return existingChatRoom.get().getRno();
        }

        Optional<PartTimerEntity> pno = partTimerRepository.findByPno(chatRoomAddPartDTO.getPno());

        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                .partTimer(pno.get())
                .build();

        return chatRoomRepository.save(chatRoomEntity).getRno();
    }

//    public String deleteChatRoom(Long eno) { // 고용인의 채팅방 삭제
//
//        Optional<ChatRoomEntity> existingChatRoom = chatRoomRepository.findByEmployer_Eno(eno);
//
//        if(existingChatRoom.isPresent()) {
//            ChatRoomEntity chatRoomEntity = existingChatRoom.get();
//            Long roomId = chatRoomEntity.getRno();
//
//            chatRoomRepository.delete(existingChatRoom.get()); // 채팅방 삭제
//            chatService.deleteMessage(roomId.toString()); // mongodb의 채팅내역 삭제
//        }
//
//        return "success delete chat room";
//
//    }
//
//    public String deleteChatPartRoom(Long pno) { // 파트타이머의 채팅방 삭제
//
//        Optional<ChatRoomEntity> existingChatRoom = chatRoomRepository.findByPartTimer_Pno(pno);
//
//        if(existingChatRoom.isPresent()) {
//            ChatRoomEntity chatRoomEntity = existingChatRoom.get();
//            Long roomId = chatRoomEntity.getRno();
//
//            chatRoomRepository.delete(existingChatRoom.get()); // 채팅방 삭제
//            chatService.deleteMessage(roomId.toString()); // mongodb의 채팅내역 삭제
//
//        }
//
//        return "success delete chat room";
//
//    }

    public String deleteChat(Long rno) {

        chatRoomRepository.deleteById(rno);
        chatService.deleteMessage(rno.toString());

        return "success delete chat room";

    }


}