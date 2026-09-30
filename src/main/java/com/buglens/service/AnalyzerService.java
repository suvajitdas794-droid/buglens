package com.buglens.service;

import com.buglens.model.Analysis;
import org.springframework.stereotype.Service;
import java.util.regex.*;

@Service
public class AnalyzerService {
    public Analysis analyze(String log, String title) {
        String s = log == null ? "" : log;
        String lower = s.toLowerCase();
        String category="General", severity="Medium", type="UnknownException", cause="The log indicates an application error that needs investigation.", solution="Inspect the first meaningful exception, surrounding context and recent code/configuration changes.";
        int confidence=55;

        if(lower.contains("access denied") && lower.contains("mysql")) {
            category="Database"; severity="High"; type="SQLException";
            cause="MySQL rejected the connection because the configured credentials or privileges are invalid.";
            solution="Verify spring.datasource.username/password, database name, host and user privileges. Test the same credentials directly in MySQL.";
            confidence=96;
        } else if(lower.contains("jdbc") || lower.contains("sql") || lower.contains("hibernate")) {
            category="Database"; severity="High"; type="DatabaseException";
            cause="A database, JDBC or ORM operation failed.";
            solution="Check datasource URL, credentials, schema, SQL constraints and the first SQL-related exception in the stack trace.";
            confidence=89;
        } else if(lower.contains("nullpointerexception")) {
            category="Java Runtime"; severity="High"; type="NullPointerException";
            cause="Code attempted to use an object reference whose value is null.";
            solution="Find the referenced line, identify which variable can be null, then initialize it or add appropriate null handling.";
            confidence=97;
        } else if(lower.contains("404") || lower.contains("not found")) {
            category="Web / Routing"; severity="Medium"; type="NotFoundException";
            cause="The requested route, controller mapping or resource could not be found.";
            solution="Verify the URL, controller mapping, application context path and that the requested resource is deployed.";
            confidence=92;
        } else if(lower.contains("whitelabel")) {
            category="Spring Boot"; severity="Medium"; type="WhitelabelError";
            cause="Spring Boot could not resolve a suitable handler/resource for the request.";
            solution="Check controller mappings, templates/static resources and the exact URL being requested.";
            confidence=94;
        } else if(lower.contains("tomcat") && lower.contains("jsp")) {
            category="Servlet / JSP"; severity="High"; type="JspServletException";
            cause="Tomcat could not compile, locate or serve the requested JSP resource.";
            solution="Check JSP location, servlet mapping, WAR structure and Tomcat/Jakarta compatibility.";
            confidence=90;
        } else if(lower.contains("outofmemory") || lower.contains("heap space")) {
            category="JVM"; severity="Critical"; type="OutOfMemoryError";
            cause="The JVM exhausted available heap memory.";
            solution="Inspect memory-heavy operations, object retention and heap usage; use a profiler before increasing heap limits.";
            confidence=96;
        } else if(lower.contains("timeout") || lower.contains("timed out")) {
            category="Network"; severity="High"; type="TimeoutException";
            cause="An operation did not complete within the configured time limit.";
            solution="Check network dependency health, timeout settings, query performance and downstream service latency.";
            confidence=86;
        } else if(lower.contains("unauthorized") || lower.contains("403")) {
            category="Security"; severity="High"; type="AuthorizationException";
            cause="The request was rejected by an authentication or authorization layer.";
            solution="Verify credentials/token validity, roles, authorities, endpoint security rules and CSRF configuration.";
            confidence=88;
        }
        return new Analysis(title==null||title.isBlank()?"Untitled analysis":title,category,severity,type,confidence,cause,s,solution);
    }
}