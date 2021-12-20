package com.springBoot.main.controller;

import com.springBoot.main.model.Result;
import com.springBoot.main.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    QuizService qService;

    @GetMapping("/admin")
    public String resultControl(Model model){

        List<Result> sList = qService.getTopScore();
        model.addAttribute("sList", sList);

        return "adminScore";
    }

    @GetMapping("/deleteR")
    public String Delete(Model model, @RequestParam(value = "id") String id){
        int idNum = Integer.parseInt(id);
        qService.deleteService(idNum);
        return "redirect:/admin";
    }
}
