package com.lec.project.human_resources.service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lec.project.Member;
import com.lec.project.MemberRepository;
import com.lec.project.MemberRole;
import com.lec.project.MemberSecurityDTO;
import com.lec.project.human_resources.domain.Admin;
import com.lec.project.human_resources.domain.Attendance;
import com.lec.project.human_resources.domain.AttendanceEnum;
import com.lec.project.human_resources.domain.WorkLog;
import com.lec.project.human_resources.domain.WorkLogEnum;
import com.lec.project.human_resources.dto.AdminDTO;
import com.lec.project.human_resources.dto.CalendarDTO;
import com.lec.project.human_resources.repository.AdminRepository;
import com.lec.project.human_resources.repository.AttendanceRepository;
import com.lec.project.human_resources.repository.WorkLogRepository;
import com.lec.project.shoppingmall.domain.cart.order.Ordered;
import com.lec.project.shoppingmall.repository.OrderedRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class HRServiceImpl implements HRService {
	private final MemberRepository memberRepository;
	private final AdminRepository adminRepository;
	private final AttendanceRepository attendanceRepository;
	private final WorkLogRepository workLogRepository;
	private final OrderedRepository orderedRepository;
	private final PasswordEncoder passwordEncoder;

	/*	@Override
		public AdminDTO getAdmin(String id) {
			Optional<Admin> result = adminRepository.findById(id);
			Admin admin = result.orElseThrow();
			AdminDTO adminDTO = modelMapper.map(admin, AdminDTO.class);
			
			return adminDTO;
		}*/

	@Override
	public MemberSecurityDTO getUser(String id) {
		Optional<Member> result = memberRepository.findById(id);
		Member member = result.orElseThrow();

		MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member.getId(), 
																	member.getPassword(),
																	member.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
																	.collect(Collectors.toList()),
																	member.getName(),
																	member.getSsn(),
																	member.getPhone(),
																	member.getEmail(),
																	member.getDetailedAddress(),
																	member.getAnnualSalary(),
																	member.getTotalSpent());
		
		return memberSecurityDTO;
	}

	@Override
	public List<MemberSecurityDTO> getUserList() {
		List<Member> memberList = memberRepository.findAll();
		List<MemberSecurityDTO> memberSecurityDTOList = new ArrayList<>();
		Set<MemberRole> roleSet = new HashSet<>();

		for (Member member : memberList) {
			roleSet = member.getRoleSet();

			if (!roleSet.contains(MemberRole.ADMIN) && !roleSet.contains(MemberRole.SUPER_ADMIN)) {
				MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member.getId(), 
																			member.getPassword(),
																			member.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
																			.collect(Collectors.toList()),
																			member.getName(),
																			member.getSsn(),
																			member.getPhone(),
																			member.getEmail(),
																			member.getDetailedAddress(),
																			member.getAnnualSalary(),
																			member.getTotalSpent());
				
				memberSecurityDTOList.add(memberSecurityDTO);
			}
		}

		return memberSecurityDTOList;
	}

	@Override
	public List<MemberSecurityDTO> getAllUserList() {
		List<Member> memberList = memberRepository.findAll();
		List<MemberSecurityDTO> memberSecurityDTOList = new ArrayList<>();

		for (Member member : memberList) {
			MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member.getId(), 
																		member.getPassword(),
																		member.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
																		.collect(Collectors.toList()),
																		member.getName(),
																		member.getSsn(),
																		member.getPhone(),
																		member.getEmail(),
																		member.getDetailedAddress(),
																		member.getAnnualSalary(),
																		member.getTotalSpent());
			
			memberSecurityDTOList.add(memberSecurityDTO);
		}

		return memberSecurityDTOList;
	}

	@Override
	public void update(String id, String name, String ssn, String phone, String email, String address, int annualSalary) {
		Optional<Member> result = memberRepository.findById(id);
		Member member = result.orElseThrow();

		// member.setId(newId); // 기본키는 수정하지 않는다.
		// member.setPassword(password); // password 수정, 암호화 도입으로 수행 X
		member.setName(name);
		member.setSsn(ssn);
		member.setPhone(phone);
		member.setEmail(email);
		member.setDetailedAddress(address);
		member.setAnnualSalary(annualSalary);

		memberRepository.save(member);
	}

	@Override
	public void remove(String id, boolean isAdmin) {
		Optional<Member> result = memberRepository.findById(id);
		Member member = result.orElseThrow();
		List<Ordered> orderedList = orderedRepository.findByMemberOrderByOrderDateDesc(member);
		
		if (isAdmin) {
			adminRepository.deleteById(id);
		}
		
		for (Ordered ordered : orderedList) {
			orderedRepository.delete(ordered);
		}
		
		memberRepository.deleteById(id);
	}

	@Override
	public void addAdmin(String id, String password, String name, String ssn, String phone, String email, String address, int annualSalary, String job) {
		String[] nums = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
		String numberString = "";

		numberString += nums[(int) (Math.random() * 9)];

		for (int i = 1; i < 9; i++) {
			numberString += nums[(int) (Math.random() * 10)];
		}

		int no = Integer.parseInt(numberString);

		password = passwordEncoder.encode(password);

		Admin admin = Admin.builder().id(id).password(password).no(no).name(name).ssn(ssn).phone(phone).email(email).detailedAddress(address).annualSalary(annualSalary).build();

		adminRepository.save(admin);

		Member member = Member.builder().id(id).password(password).name(name).email(email).ssn(ssn).phone(phone).detailedAddress(address).annualSalary(annualSalary).build();

		if (job.equals("hr")) {
			member.addRole(MemberRole.SUPER_ADMIN);
		} else if (job.equals("manager")) {
			member.addRole(MemberRole.MANAGER);
		}
		
		memberRepository.save(member);
	}

	@Override
	public void delagateAuthority(String superAdminId, String adminId) {
		Optional<Member> result = memberRepository.findById(superAdminId);
		Member member = result.orElseThrow();

		member.removeRole(MemberRole.SUPER_ADMIN);
		memberRepository.save(member);

		result = memberRepository.findById(adminId);
		member = result.orElseThrow();

		member.removeRole(MemberRole.ADMIN);
		member.addRole(MemberRole.SUPER_ADMIN);
		memberRepository.save(member);
	}

	@Override
	public Page<MemberSecurityDTO> getUserListWithPaging(Pageable pageable, int size) {
		int page = pageable.getPageNumber() - 1;
		int limit = size;

		Page<Member> pages = memberRepository.findAll(PageRequest.of(page, limit));
		Page<MemberSecurityDTO> DTOPages = new PageImpl<>(
				pages.getContent().stream().filter(member -> member.getRoleSet().contains(MemberRole.USER))
						.map(member -> new MemberSecurityDTO(member.getId(), 
 															 member.getPassword(),
															 member.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
															 .collect(Collectors.toList()),
															 member.getName(),
															 member.getSsn(),
															 member.getPhone(),
															 member.getEmail(),
															 member.getDetailedAddress(),
															 member.getAnnualSalary(),
															 member.getTotalSpent()))
						.collect(Collectors.toList()),
				pages.getPageable(), // 기존 Pageable 유지
				pages.getTotalElements() // 원래의 총 요소 개수
		);

		return DTOPages;
	}

	@Override
	public Page<MemberSecurityDTO> getAllUserListWithPaging(Pageable pageable, int size) {
		int page = pageable.getPageNumber() - 1;
		int limit = size;

		Page<Member> pages = memberRepository.findAll(PageRequest.of(page, limit));
		Page<MemberSecurityDTO> DTOPages = pages.map(member -> new MemberSecurityDTO(member.getId(), 
												  	 member.getPassword(),
													 member.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
													 .collect(Collectors.toList()),
													 member.getName(),
													 member.getSsn(),
													 member.getPhone(),
													 member.getEmail(),
													 member.getDetailedAddress(),
													 member.getAnnualSalary(),
													 member.getTotalSpent()));
		
		return DTOPages;
	}

	@Override
	public Page<MemberSecurityDTO> getAdminListWithPaging(int p, Pageable pageable, int size) {
		int page = pageable.getPageNumber() - 1;
		int limit = size;
		
		List<Member> listSize = memberRepository.findAll().stream().filter(member -> member.getRoleSet().contains(MemberRole.SUPER_ADMIN) || member.getRoleSet().contains(MemberRole.MANAGER)).collect(Collectors.toList());
		Page<Member> pages = memberRepository.findAll(PageRequest.of(page, limit));
		List<MemberSecurityDTO> DTOPages = pages.getContent().stream()
												.filter(member -> member.getRoleSet().contains(MemberRole.SUPER_ADMIN) || member.getRoleSet().contains(MemberRole.MANAGER))
												.map(member -> new MemberSecurityDTO(
													 member.getId(),
													 member.getPassword(),
													 member.getRoleSet().stream()
													 	   .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
													 	   .collect(Collectors.toList())))
												.collect(Collectors.toList());
		
		return new PageImpl<>(DTOPages, pageable.withPage(p), listSize.size());
	}

	@Override
	public void initializePassword(String memberId) throws AddressException, MessagingException {
		Optional<Member> result = memberRepository.findById(memberId);
		Member member = result.orElseThrow();
		String password = generatePassword();
		
		member.setPassword(passwordEncoder.encode(password));
		
		memberRepository.save(member); // admin?
		
		// String address = member.getEmail(); 회원의 이메일
		String address = "pms981125@naver.com"; // 메일 수신자 - 네이버
		String sender = "pms141683@gmail.com"; // 메일 송신자 - 구글
		String senderPassword = "khed zxab durs cgoy"; // 앱 비밀번호
        String host = "smtp.gmail.com"; // 구글 메일 서버 호스트 이름

        Properties props = new Properties(); // SMTP 프로토콜 설정
        
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.ssl.trust", host);
        props.setProperty("mail.debug", "true");
        props.setProperty("mail.smtp.localhost", "localhost");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        Session session = Session.getInstance(props, new Authenticator() {
        	protected PasswordAuthentication getPasswordAuthentication() {
        		return new PasswordAuthentication(sender, senderPassword);
        	}
        });
        
        Message message = new MimeMessage(session);
        
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
        message.setSubject("비밀번호 재발급");
        message.setText(memberId + "님의 초기화된 비밀번호는 " + password + " 입니다");
        
        Transport.send(message);
	}

	private String generatePassword() {
		String string = "";
		
		for (int i = 0; i < 10; i++) {
			int n = (int) ((Math.random() * 57) + 65);
			
			while (!(n < 91 || n > 96)) {
				n = (int) ((Math.random() * 57) + 65);
			}

			string += (char) n;
		}
		
		return string;
	}

	@Override
	public void addMember(String id, String password, String name, String ssn, String phone, String email, String address) {
		Member member = Member.builder().id(id)
										.password(passwordEncoder.encode(password))
										.name(name)
										.ssn(ssn)
										.phone(phone)
										.email(email)
										.detailedAddress(address)
										.build();
		
		member.addRole(MemberRole.USER);
		
		memberRepository.save(member);
		
	}

	@Override
	public void exaltation(String id, int annualSalary) {
		Optional<Member> result = memberRepository.findById(id);
		Member member = result.orElseThrow();
		
		remove(id, false);
		addAdmin(id, member.getPassword(), member.getName(), member.getSsn(), member.getPhone(), member.getEmail(), member.getDetailedAddress(), annualSalary, "hr"); // 계좌가 있으면 작동 X
	}

	@Override
	public boolean confirmId(String id) {
		return memberRepository.findById(id).isPresent();
	}

	@Override
	public void attendance(String id, LocalTime localTime) {
		Optional<Admin> result = adminRepository.findById(id);
		Admin admin = result.orElseThrow();
		LocalTime normalityAttendanceTime = LocalTime.of(10, 00);
		AttendanceEnum attendanceEnum;
		
		if (localTime.isAfter(normalityAttendanceTime)) {
			attendanceEnum = AttendanceEnum.지각;
		} else {
			attendanceEnum = AttendanceEnum.출근;
		}
		
		Attendance attendance = Attendance.builder().employee(admin).inTime(localTime).status(attendanceEnum).build();
		
		attendanceRepository.save(attendance);
	}

	@Override
	public void leave(String id, LocalTime localTime) {
		Optional<Admin> result = adminRepository.findById(id);
		Admin admin = result.orElseThrow();
		LocalTime normalityLeaveTime = LocalTime.of(18, 00);
		AttendanceEnum attendanceEnum;
		
		if (localTime.isBefore(normalityLeaveTime)) {
			attendanceEnum = AttendanceEnum.조퇴;
		} else {
			attendanceEnum = AttendanceEnum.퇴근;
		}
		
		AttendanceEnum status = AttendanceEnum.출근;
		Optional<Attendance> result2 = attendanceRepository.findByIdWithAttendance(admin, status);
		Attendance attendance;
		
		try {
			attendance = result2.orElseThrow();
		} catch (Exception e) {
			status = AttendanceEnum.지각;
			
			result2 = attendanceRepository.findByIdWithAttendance(admin, status);
			attendance = result2.orElseThrow();
		}

		Attendance leave = Attendance.builder().id(attendance.getId()).employee(admin).inTime(attendance.getInTime()).outTime(localTime).status(attendanceEnum).build();

		attendanceRepository.save(leave);
		
		Date date = new Date();
		int time = (int) ChronoUnit.HOURS.between(leave.getInTime(), leave.getOutTime());
		WorkLogEnum workLogEnum;
		
		if (leave.getOutTime().isBefore(normalityLeaveTime)) {
			workLogEnum = WorkLogEnum.조퇴;
		} else if (leave.getInTime().isAfter(LocalTime.of(10, 30))) {
			workLogEnum = WorkLogEnum.지각;
		} else if (leave.getOutTime().isAfter(LocalTime.of(19, 00))) {
			workLogEnum = WorkLogEnum.야근;
		} else {
			workLogEnum = WorkLogEnum.근무;
		}
		
		WorkLog workLog = WorkLog.builder().employee(admin).workDate(date).workTime(time).status(workLogEnum).build();
		
		workLogRepository.save(workLog);
	}

	@Override
	public CalendarDTO[] getWorkLog(String id) {
		Optional<Admin> result = adminRepository.findById(id);
		Admin admin = result.orElseThrow();
		List<WorkLog> workLogList = workLogRepository.findAllById(admin);
		CalendarDTO[] calendarDTOs = new CalendarDTO[workLogList.size()];
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		for (int i = 0; i < calendarDTOs.length; i++) {
			WorkLog workLog = workLogList.get(i);
			
			calendarDTOs[i] = CalendarDTO.builder().start(simpleDateFormat.format(workLog.getWorkDate())).title(workLog.getStatus().toString()).build();
		}
		
		return calendarDTOs;
	}

	@Override
	public AdminDTO getAdmin(String id) {
		Optional<Admin> result = adminRepository.findById(id);
		Admin admin = result.orElseThrow();
		AdminDTO adminDTO = AdminDTO.builder().id(id).no(admin.getNo()).name(admin.getName()).ssn(admin.getSsn()).email(admin.getEmail()).build();
		
		if (attendanceRepository.findByIdWithAttendance(admin, AttendanceEnum.출근).isPresent() || attendanceRepository.findByIdWithAttendance(admin, AttendanceEnum.지각).isPresent()) {
			adminDTO.setAttendance(true);
		} else {
			adminDTO.setAttendance(false);
		}
		
		return adminDTO;
	}
	
	public boolean checkPassword(String id, String inputPassword) {
		Optional<Member> result = memberRepository.findById(id);
		Member member = result.orElseThrow();
		 
		return passwordEncoder.matches(inputPassword, member.getPassword());
	}
	
	public void changePassword(String id, String inputPassword) {
		Optional<Member> result = memberRepository.findById(id);
		Member member = result.orElseThrow();
		
		inputPassword = passwordEncoder.encode(inputPassword);
		member.setPassword(inputPassword);
		
		memberRepository.save(member);
		
		Optional<Admin> result2 = adminRepository.findById(id);
		
		if (result2.isPresent()) {
			Admin admin = result2.orElseThrow();
			
			admin.setPassword(inputPassword);
			
			adminRepository.save(admin);
		}
	}
}