package com.springBoot.main.repository;

import com.springBoot.main.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Integer> {

//    List<Question> findAllByPhone(int quesOption);
}