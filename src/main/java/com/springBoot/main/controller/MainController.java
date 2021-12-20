package com.springBoot.main.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.springBoot.main.model.QuestionForm;
import com.springBoot.main.model.Result;
import com.springBoot.main.service.QuizService;

@Controller
public class MainController {
//	@Autowired
//	HttpServletRequest request;

//	@Autowired
//	Result result;

	@Autowired
	QuizService qService;

//	@ModelAttribute("result")
//	public Result getResult() {
//		return result;
//	}
//	@Autowired
//	HttpSession session;

	@GetMapping("/")
	public String home() {
		return "index";
	}

	private final String[] str ={"cc","cho","ngu","loz","cac","dm"};
	public boolean testname(String name){
		String [] check = name.split(" ");
		if(check.length >=5 ) return false;
		name = name.toLowerCase(Locale.ROOT);
		for(String ss : str){
			if(name.contains(ss)) return false;
		}
		return true;
	}
	@GetMapping("/select")
	public String select(@RequestParam String username,
						 Model model,
						 RedirectAttributes ra){
		if(username.equals("adminBB") || username.equals("NDAadmin97") ) return "redirect:/admin";
		if(username.equals("")) {
			ra.addFlashAttribute("warning", "You must enter your name");
			return "redirect:/";
		}else if(!testname(username) || username.length() >= 30) {
			ra.addFlashAttribute("warning", "tên bạn quá dài hoặc chứa từ nhạy cảm");
			return "redirect:/";
		}
		System.out.println(username.length());
		model.addAttribute("username",username);
		return "select";
	}
	@PostMapping("/quiz")
	public String quiz(@RequestParam(value = "username") String username,
					   Model m,
					   @RequestParam(value = "choose") String option) {
//		if(username.equals("")) {
//			ra.addFlashAttribute("warning", "You must enter your name");
//			return "redirect:/";
//		}

		Result result = new Result();
		result.setUsername(username);
		result.setOption(option);
//		session.setAttribute("result",result);
//		session.setMaxInactiveInterval(1500);
//		int itemInOnePage = 1;
//		Page<Question> page = qService.findAllWithPage(nowPage,itemInOnePage);

		int numOption = switch (option) {
			case "eng" -> 1;
			case "japan" -> 2;
			case "java" -> 3;
			default -> 0;
		};
		QuestionForm qForm = qService.getQuestions(numOption);
		qForm.setResult(result);
//		QuestionForm qForm = new QuestionForm();
//		qForm.setQuestions(qService.getAllQuestion());
		m.addAttribute("qForm", qForm);

//		m.addAttribute("cPage",nowPage);
//		m.addAttribute("totalPage",page.getTotalPages());
//		m.addAttribute("totalItem",page.getTotalElements());

//		return "quiz";
		return "quiz";
	}

	@PostMapping("/submit")
	public String submit(@ModelAttribute(value = "qForm") QuestionForm qForm, Model m ){

		Result result = qForm.getResult();
//		Result result = (Result) session.getAttribute("result");
//		session.invalidate();

		if(result.getIsSubmit() !=0)  return "redirect:/";
		if(result.getIsSubmit() == 0) {
			result.setTotalCorrect(qService.getResult(qForm));
			qService.saveScore(result);
		}
		qForm.getResult().setIsSubmit(1);

		m.addAttribute("messTrue","bạn chọn đúng rồi nhé");
		m.addAttribute("messFalse","bạn chọn sai rồi nhé");
		m.addAttribute("messNone","chọn đáp án đi bạn nhé");

		m.addAttribute("qForm",qForm);
//		for(Question q : qForm.getQuestions()) System.out.println(q.toString());
		return "result";
	}
	
	@GetMapping("/score")
	public String score(Model m) {
		List<Result> sList = qService.getTopScore();
		m.addAttribute("sList", sList);
		
		return "scoreboard";
	}

}
