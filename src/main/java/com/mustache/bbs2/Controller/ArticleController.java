package com.mustache.bbs2.Controller;

import com.mustache.bbs2.domain.dto.ArticleDto;
import com.mustache.bbs2.domain.entity.Article;
import com.mustache.bbs2.domain.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/articles")
@Slf4j // 로거 추가
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/new")
    public String createPage() {
        return "new";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }

    @PostMapping // /articles
    public String add(ArticleDto articleDto) {
        log.info(articleDto.getTitle());
        Article savedArticle = articleRepository.save(articleDto.toEntity());
        log.info("generatedId:{}", savedArticle.getId());
        return String.format("redirect:/articles/%d", savedArticle.getId());
    }

    @GetMapping(value = "/{id}")
    public String readSingle(@PathVariable Long id, Model model) {
        log.info("id :" + id);
        // 1. id로 데이터 가져옴
        Article articleEntity = articleRepository.findById(id).orElse(null);
        // 만약 해당 아이디가 없다면 null을 반환해라 .orElse(null)
        // java8 부터 Optional<>로 감싸도돼.
        // 2. 가져온 데이터를 모델에 등록
        model.addAttribute("article", articleEntity);
        // 3. 보여줄 페이지 선택
        return "show";
    }

    //    @GetMapping(value = "/{id}")
//    public String read1(@PathVariable Long id, Model model) {
//        log.info("id :" + id);
//
//        Optional<Article> optionalArticle = articleRepository.findById(id);
//        if(!optionalArticle.isEmpty()){
//            model.addAttribute("article", optionalArticle.get());
//            return "show";
//        }
//        else
//            return "";
//    }

    @GetMapping(value = "/list")
    public String readList(Model model) {
        List<Article> articles= articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "list";
    }

    @GetMapping(value = "/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if(!optionalArticle.isEmpty()){
            model.addAttribute("article", optionalArticle.get());
            return "edit";
        }
        else{
            model.addAttribute("massage", String.format("%d가 없습니다.", id));
            return "error";
        }
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, ArticleDto articleDto, Model model) {
        log.info("title:{} content:{}", articleDto.getTitle(), articleDto.getContent());
        Article article = articleRepository.save(articleDto.toEntity());
        model.addAttribute("article", article);
        return String.format("redirect:/articles/%d", article.getId());
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        articleRepository.deleteById(id);
        return "redirect:/articles/list";
    }

}