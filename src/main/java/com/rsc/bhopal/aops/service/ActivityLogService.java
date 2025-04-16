package com.rsc.bhopal.aops.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rsc.bhopal.dtos.ActivityLogDTO;
import com.rsc.bhopal.dtos.LogPayload;
import com.rsc.bhopal.entity.ActivityLog;
import com.rsc.bhopal.repos.ActivityLogRepo;
import com.rsc.bhopal.repos.UserDetailsRepository;
import com.rsc.bhopal.utills.CommonUtills;

@Service
public class ActivityLogService {

	@Autowired
	private ActivityLogRepo logRepo;

	@Autowired
	private UserDetailsRepository userRepo;

	public void log(ActivityLogDTO dto) {
		ActivityLog log = new ActivityLog();
		BeanUtils.copyProperties(dto, log);
		log.setActionAt(new Date());
		log.setActionBy(userRepo.findByUsername(dto.getActionBy()).get());
		try {
			log.setPayload(CommonUtills.convertToJSON(dto.getPayload()));
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logRepo.save(log);
	}

	public List<ActivityLogDTO> getAllLogs(Integer rows){
		List<ActivityLogDTO> logDTOs = new ArrayList<ActivityLogDTO>();
		List<ActivityLog> logs = logRepo.recentLogs(PageRequest.of(0, rows, Sort.by(Direction.DESC, "id")));
		logs.forEach(log -> {
			ActivityLogDTO dto = new ActivityLogDTO();
			BeanUtils.copyProperties(log, dto);
			dto.setActionBy(log.getActionBy().getUsername());
			if(log.getPayload() != null) {
				dto.setPayload(CommonUtills.convertJSONToObject(log.getPayload(), LogPayload.class));
				try {
					dto.setArgsPayload(CommonUtills.convertToJSON(dto.getPayload().getArgs()));
				}
				catch(JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			logDTOs.add(dto);
		});
		return logDTOs;
	}
}
