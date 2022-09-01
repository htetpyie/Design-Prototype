package com.ace.ai.student.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ace.ai.student.datamodel.Chapter;
import com.ace.ai.student.datamodel.ChapterBatch;
import com.ace.ai.student.datamodel.CustomChapter;
import com.ace.ai.student.datamodel.Student;
import com.ace.ai.student.dtomodel.ChapterBatchDTO;
import com.ace.ai.student.dtomodel.StuChapterDTO;
import com.ace.ai.student.dtomodel.StuCustomChapterDTO;
import com.ace.ai.student.dtomodel.StuReplyPostDTO;
import com.ace.ai.student.dtomodel.StuReplyViewDTO;
import com.ace.ai.student.service.StudentCourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/student")
public class StudentHomeController {
    @Autowired
    StudentCourseService studentCourseService;

    @GetMapping(value="/home")
    public ModelAndView getStudentHome(@RequestParam("stuId")int stuId,ModelMap model) {
        Student student = studentCourseService.getStudentById(stuId);
        List<ChapterBatchDTO> chapterList = studentCourseService.getChapterList(student.getBatch().getId());
        List<CustomChapter> customChapterList = studentCourseService.getCustomChapterList(student.getBatch().getId());

        List<StuChapterDTO> upCommingChapterList = new ArrayList<>();
        List<StuChapterDTO> doneChapterList = new ArrayList<>();
        List<StuChapterDTO> inProgressChapterList = new ArrayList<>();
        List<StuCustomChapterDTO> upCommingCustomChapterList = new ArrayList<>();
        List<StuCustomChapterDTO> doneCustomChapterList = new ArrayList<>();
        List<StuCustomChapterDTO> inProgressCustomChapterList = new ArrayList<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        for(ChapterBatchDTO chapter : chapterList){
            
            StuChapterDTO stuChapterDTO = new StuChapterDTO();
            LocalDate startDate = LocalDate.parse(chapter.getStartDate(),df);
            LocalDate endDate = LocalDate.parse(chapter.getEndDate(),df);
            if(startDate.isBefore(LocalDate.now())){
                if((startDate.isEqual(LocalDate.now())||startDate.isAfter(LocalDate.now()))&&endDate.isBefore(LocalDate.now())){
                    stuChapterDTO.setId(chapter.getId());
                    stuChapterDTO.setName(chapter.getName());
                    stuChapterDTO.setStatus("inProgress");
                    inProgressChapterList.add(stuChapterDTO);
                }else if(endDate.isEqual(LocalDate.now())||endDate.isAfter(LocalDate.now())){
                    stuChapterDTO.setId(chapter.getId());
                    stuChapterDTO.setName(chapter.getName());
                    stuChapterDTO.setStatus("done");
                    doneChapterList.add(stuChapterDTO);
                }
            }else{
                stuChapterDTO.setId(chapter.getId());
                stuChapterDTO.setName(chapter.getName());
                stuChapterDTO.setStatus("upComming");
                upCommingChapterList.add(stuChapterDTO);
            }
        }
        for(CustomChapter customChapter : customChapterList){
            StuCustomChapterDTO stuCustomChapterDTO = new StuCustomChapterDTO();
            LocalDate startDate = LocalDate.parse(customChapter.getStartDate(),df);
            LocalDate endDate = LocalDate.parse(customChapter.getEndDate(),df);

            if(startDate.isBefore(LocalDate.now())){
                if((startDate.isEqual(LocalDate.now())||startDate.isAfter(LocalDate.now()))&&endDate.isBefore(LocalDate.now())){
                    stuCustomChapterDTO.setId(customChapter.getId());
                    stuCustomChapterDTO.setName(customChapter.getName());
                    stuCustomChapterDTO.setStatus("inProgress");
                    inProgressCustomChapterList.add(stuCustomChapterDTO);
                }else if(endDate.isEqual(LocalDate.now())||endDate.isAfter(LocalDate.now())){
                    stuCustomChapterDTO.setId(customChapter.getId());
                    stuCustomChapterDTO.setName(customChapter.getName());
                    stuCustomChapterDTO.setStatus("done");
                    doneCustomChapterList.add(stuCustomChapterDTO);
                }
            }else{
                stuCustomChapterDTO.setId(customChapter.getId());
                stuCustomChapterDTO.setName(customChapter.getName());
                stuCustomChapterDTO.setStatus("upComming");
                upCommingCustomChapterList.add(stuCustomChapterDTO);
            }

        }

        model.addAttribute("upCommingChapterList", upCommingChapterList);
        model.addAttribute("doneChapterList", doneChapterList);
        model.addAttribute("inProgressChapterList", inProgressChapterList);
        model.addAttribute("upCommingCustomChapterList", upCommingCustomChapterList);
        model.addAttribute("doneCustomChapterList", doneCustomChapterList);
        model.addAttribute("inProgressCustomChapterList", inProgressCustomChapterList);
       
        // model.addAttribute("replayList", attributeValue)
        return new ModelAndView("","StuReplyPostDTO",new StuReplyPostDTO());
    }
    
    
}
