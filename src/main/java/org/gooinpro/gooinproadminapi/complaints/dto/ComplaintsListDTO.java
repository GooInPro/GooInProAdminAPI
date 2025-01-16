package org.gooinpro.gooinproadminapi.complaints.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ComplaintsListDTO {

    private Long cno; // pk

    private String ctitle; // 신고 제목

    private String pname; // 근로자 이름

    private String ename; // 고용인 이름

    private Timestamp cregdate; // 등록 시간

    private Timestamp ccheckedTime; // 확인 시간

    private boolean cdelete; // 삭제 여부

}