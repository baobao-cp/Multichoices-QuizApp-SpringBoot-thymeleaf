package com.springBoot.main.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.springBoot.main.model.Question;
import com.springBoot.main.repository.ResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.springBoot.main.model.QuestionForm;
import com.springBoot.main.model.Result;
import com.springBoot.main.repository.QuestionRepo;

@Service
public class QuizService {
	
	@Autowired
    Question question;
	@Autowired
	QuestionForm qForm;
	@Autowired
	QuestionRepo qRepo;
	@Autowired
	Result result;
	@Autowired
    ResultRepo rRepo;

//	public Page<Question> findAllWithPage(int currenPage,int totalItemInPage){
//		return qRepo.findAll(PageRequest.of(currenPage-1,totalItemInPage));
//	}
	public List<Question> getAllQuestion(){
		return qRepo.findAll();
	}

	public QuestionForm getQuestions(int option) {
		List<Question> all = qRepo.findAll();
//		List<Question> allQues = qRepo.findAll();
		List<Question> allQues = new ArrayList<>();
		for(Question question : all) {
			if(question.getPhone() == option) allQues.add(question);
		}

		List<Question> qList = new ArrayList<Question>();
		
		Random random = new Random();
		
		for(int i=0; i< 10; i++) {
			int rand = random.nextInt(allQues.size());
			qList.add(allQues.get(rand));
			allQues.remove(rand);
		}

		qForm.setQuestions(qList);
		
		return qForm;
	}
	
	public int getResult(QuestionForm qForm) {
		int correct = 0;
		
		for(Question q: qForm.getQuestions())
			if(q.getAns() == q.getChose())
				correct++;
		
		return correct;
	}

	public void saveScore(Result result) {
		Result saveResult = new Result();
		saveResult.setUsername(result.getUsername());
		saveResult.setTotalCorrect(result.getTotalCorrect());
		saveResult.setIsSubmit(result.getIsSubmit());
		saveResult.setOption(result.getOption());
		rRepo.save(saveResult);
	}
	
	public List<Result> getTopScore() {
		List<Result> sList = rRepo.findAll(Sort.by(Sort.Direction.DESC, "totalCorrect"));
		return sList;
	}

	public void deleteService(int id){
		rRepo.deleteById(id);
	}

//	public Page<Question> questionPage(QuestionForm form,int page_now,int pageIn1){
//		Pageable p = PageRequest.of(page_now-1,pageIn1);
//
//	}
}
