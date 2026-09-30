package com.buglens.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="analyses")
public class Analysis {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String category;
    private String severity;
    private String exceptionType;
    private Integer confidence;
    private String rootCause;
    @Column(columnDefinition="TEXT")
    private String logText;
    @Column(columnDefinition="TEXT")
    private String solution;
    private LocalDateTime createdAt;

    public Analysis() {}
    public Analysis(String title,String category,String severity,String exceptionType,Integer confidence,
                    String rootCause,String logText,String solution) {
        this.title=title; this.category=category; this.severity=severity; this.exceptionType=exceptionType;
        this.confidence=confidence; this.rootCause=rootCause; this.logText=logText; this.solution=solution;
        this.createdAt=LocalDateTime.now();
    }
    public Long getId(){return id;} public String getTitle(){return title;}
    public String getCategory(){return category;} public String getSeverity(){return severity;}
    public String getExceptionType(){return exceptionType;} public Integer getConfidence(){return confidence;}
    public String getRootCause(){return rootCause;} public String getLogText(){return logText;}
    public String getSolution(){return solution;} public LocalDateTime getCreatedAt(){return createdAt;}
    public void setTitle(String v){title=v;}
}