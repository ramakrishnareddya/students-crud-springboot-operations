package com.college.students.controllers;

	import java.io.InputStream;
	import java.nio.file.Files;
	import java.nio.file.Path;
	import java.nio.file.Paths;
	import java.nio.file.StandardCopyOption;
	import java.util.Date;
	import java.util.List;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.validation.BindingResult;
	import org.springframework.validation.FieldError;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.ModelAttribute;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.multipart.MultipartFile;

	import com.college.students.models.Student;
	import com.college.students.models.StudentDto;
	import com.college.students.services.StudentRepository;

	import jakarta.validation.Valid;

	@Controller
	@RequestMapping("students")
	public class StudentController {
		
		
		@Autowired
		private StudentRepository repo;
		
		@GetMapping({" ","/"})
		public String showStudentList(Model model) {
			List<Student> students = repo.findAll();
			model.addAttribute("students",students);
			return "students/index";
		}
		
		@GetMapping("/create")
		public String showCreatePage(Model model) {
			StudentDto studentDto = new StudentDto();
			model.addAttribute("studentDto", studentDto);
			return "students/Create";
		}
		
		@PostMapping("/create")
		public String createStudentData(@Valid @ModelAttribute StudentDto studentDto, BindingResult result) {
			if (studentDto.getImageFileName().isEmpty()) {
				result.addError(new FieldError("studentDto", "imageFileName", "The image File is Required"));
			}
		if (result.hasErrors()) {
			return "students/Create";
		}
		
		// SAVING THE IMAGE FILE
		
		MultipartFile image = studentDto.getImageFileName();
		String storeFileName = image.getOriginalFilename();
		
		try {
			String upload ="public/Images/";
			Path uploadpath =Paths.get(upload);
		if (!Files.exists(uploadpath)) {
			Files.createDirectories(uploadpath);
		}
		
		try(InputStream inputstream = image.getInputStream()) {
			Files.copy(inputstream, Paths.get(upload + storeFileName), StandardCopyOption.REPLACE_EXISTING);
		}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		Student student=new Student();
		student.setName(studentDto.getName());
		student.setStudentid(studentDto.getStudentid());
		student.setMarks(studentDto.getMarks());
		student.setResults(studentDto.getResults());
		student.setImageFileName(storeFileName);
		
		repo.save(student);
		
		return "redirect:/students/";
		}

		@GetMapping("/edit")
		public String showEditPage(Model model, @RequestParam int id) {
				
		try {
			Student student = repo.findById(id).get();
			model.addAttribute("student",student);
					
			StudentDto studentDto = new StudentDto();
			studentDto.setName(student.getName());
			studentDto.setStudentid(student.getStudentid());
			studentDto.setMarks(student.getMarks());
			studentDto.setResults(student.getResults());
					
			model.addAttribute("studentDto", studentDto);
			}
				
	catch(Exception ex) {
		System.out.println("Exception: " + ex.getMessage());
		return "redirect:/students";
		}
		return "students/Edit";
		}
					
	@PostMapping("/edit")
		public String updateProduct(Model model, @RequestParam int id,@Valid @ModelAttribute StudentDto studentDto,BindingResult result) {
						
		try {
			Student student=repo.findById(id).get();
			model.addAttribute("student", student);
							
		if(result.hasErrors()) {
			return "students/Edit";
		}
							
		if(!studentDto.getImageFileName().isEmpty()) {
//for deleting the old image
			String uploadDir="public/images/";
			Path oldImagePath = Paths.get(uploadDir + student.getImageFileName());
								
			try {
				Files.delete(oldImagePath);
			}
								
			catch(Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}
//save the new image
								
			MultipartFile  image = studentDto.getImageFileName();
				Date createdAt = new Date();
				String storageFileName = createdAt.getTime()+"_"+image.getOriginalFilename();
								
			try(InputStream inputStream = image.getInputStream()){
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
				StandardCopyOption.REPLACE_EXISTING);
			}
				student.setImageFileName(storageFileName);
			}
			student.setName(studentDto.getName());
			student.setStudentid(studentDto.getStudentid());
			student.setMarks(studentDto.getMarks());
			student.setResults(studentDto.getResults());
			repo.save(student);
			}
						
		catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
			return "redirect:/students/";
		}
					
	@GetMapping("/delete")
		public String deleteProduct(@RequestParam int id) {
						
		try {
			Student student = repo.findById(id).get();
			
			//For Deleting Image File
							
			Path imagePath = Paths.get("public/images/" + student.getImageFileName());
							
		try {
			Files.delete(imagePath);
		}
							
		catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
							
		//To delete the product
		repo.delete(student);
		}
						
						
		catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
			return "redirect:/students/";
		}
}
