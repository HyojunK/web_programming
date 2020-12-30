package com.hj.myweb.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ReplyDto {
	private int r_num;
	private int r_bnum;
	private String r_contents;
	@JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss")
	private Timestamp r_date;
	private String r_id;
}
