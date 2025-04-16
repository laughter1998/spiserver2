package org.zerock.spiserver2.service;

import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.spiserver2.domain.Member;
import org.zerock.spiserver2.dto.MemberDTO;
import org.zerock.spiserver2.dto.MemberModifyDTO;

@Transactional
public interface MemberService {
  
  MemberDTO getKakaoMember(String accessToken);

  void modifyMember(MemberModifyDTO memberModifyDTO);


  default MemberDTO entityToDTO(Member member) {
    MemberDTO dto = new MemberDTO(
      member.getEmail(), 
      member.getPw(), 
      member.getNickname(), 
      member.isSocial(), 
      member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList()));
    return dto;
  }

}