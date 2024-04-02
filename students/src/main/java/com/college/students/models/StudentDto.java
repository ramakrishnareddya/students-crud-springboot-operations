package com.college.students.models;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class StudentDto {
	
	@NotEmpty(message="This Field is Required")
	private String name;
	
	@NotEmpty(message="This Field is Required")
	private String studentid;
	
	@NotEmpty(message="This Field is Required")
	private String results;
		
	@Min(0)
	private double marks;
	
	private MultipartFile imageFileName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStudentid() {
		return studentid;
	}

	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public double getMarks() {
		return marks;
	}

	public void setMarks(double marks) {
		this.marks = marks;
	}

	public MultipartFile getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(MultipartFile imageFileName) {
		this.imageFileName = imageFileName;
	}

	
}
