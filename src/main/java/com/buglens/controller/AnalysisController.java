package com.buglens.controller;

import com.buglens.model.Analysis;
import com.buglens.repository.AnalysisRepository;
import com.buglens.service.AnalyzerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AnalysisController {
    private final AnalysisRepository repo;
    private final AnalyzerService analyzer;
    public AnalysisController(AnalysisRepository repo, AnalyzerService analyzer){this.repo=repo;this.analyzer=analyzer;}

    @PostMapping("/analyze")
    public Analysis analyze(@RequestBody Map<String,String> body){
        Analysis a=analyzer.analyze(body.get("log"),body.get("title"));
        return repo.save(a);
    }
    @GetMapping("/analyses")
    public List<Analysis> all(){return repo.findTop20ByOrderByCreatedAtDesc();}
    @GetMapping("/analyses/{id}")
    public ResponseEntity<Analysis> one(@PathVariable Long id){return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());}
    @DeleteMapping("/analyses/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){repo.deleteById(id);return ResponseEntity.noContent().build();}
    @GetMapping("/stats")
    public Map<String,Object> stats(){
        Map<String,Object> m=new LinkedHashMap<>();
        m.put("total",repo.count());
        m.put("critical",repo.countBySeverity("Critical"));
        m.put("high",repo.countBySeverity("High"));
        m.put("medium",repo.countBySeverity("Medium"));
        m.put("low",repo.countBySeverity("Low"));
        m.put("database",repo.countByCategory("Database"));
        m.put("javaRuntime",repo.countByCategory("Java Runtime"));
        m.put("security",repo.countByCategory("Security"));
        return m;
    }
}