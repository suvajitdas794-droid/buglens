const $=s=>document.querySelector(s), $$=s=>document.querySelectorAll(s);
const samples={
mysql:`2026-09-30 18:22:41 ERROR [main] org.hibernate.engine.jdbc.spi.SqlExceptionHelper
java.sql.SQLException: Access denied for user 'root'@'localhost' (using password: YES)
at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:825)
at com.mysql.cj.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:955)
Caused by: java.sql.SQLException: Access denied for user 'root'@'localhost'`,
null:`java.lang.NullPointerException: Cannot invoke "String.length()" because "username" is null
at com.example.service.UserService.login(UserService.java:42)
at com.example.controller.LoginController.login(LoginController.java:28)
at java.base/java.lang.reflect.Method.invoke(Method.java:568)`,
404:`org.springframework.web.servlet.NoHandlerFoundException: No handler found for GET /dashboard
HTTP Status 404 - Not Found
Whitelabel Error Page
There was an unexpected error (type=Not Found, status=404).`,
tomcat:`org.apache.jasper.JasperException: Unable to compile class for JSP
HTTP Status 404 - JSP file [/WEB-INF/views/home.jsp] not found
at org.apache.jasper.servlet.JspServletWrapper.service(JspServletWrapper.java:466)`
};
function go(page){location.hash=page}
function route(){
 let page=location.hash.replace('#','')||'dashboard';
 $$('.page').forEach(x=>x.classList.toggle('active-page',x.id===page));
 $$('.nav').forEach(x=>x.classList.toggle('active',x.dataset.page===page));
 const titles={dashboard:'Good evening, debugger.',analyzer:'Turn stack traces into answers.',history:'Your debugging archive.',playground:'Explore the BugLens engine.',settings:'Tune your workspace.'};
 $('#pageTitle').textContent=titles[page]||titles.dashboard;
 if(page==='history')loadHistory();
 if(page==='dashboard')loadDashboard();
}
window.addEventListener('hashchange',route);
function toast(msg){let t=$('#toast');t.textContent=msg;t.classList.add('show');setTimeout(()=>t.classList.remove('show'),2200)}
function sample(k){$('#logInput').value=samples[k];$('#charCount').textContent=samples[k].length+' characters'}
function loadSample(){sample('mysql');$('#titleInput').value='MySQL login failure'}
$('#logInput').addEventListener('input',e=>$('#charCount').textContent=e.target.value.length+' characters');

async function analyze(){
 const log=$('#logInput').value.trim(); if(!log){toast('Paste an error first.');return}
 const btn=$('#analyzeBtn');btn.disabled=true;btn.innerHTML='Analyzing <span>…</span>';
 try{
  const r=await fetch('/api/analyze',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({title:$('#titleInput').value,log})});
  const a=await r.json();renderResult(a);toast('Analysis saved successfully');
 }catch(e){toast('Could not connect to BugLens API')}
 finally{btn.disabled=false;btn.innerHTML='Analyze with BugLens <span>✦</span>'}
}
function renderResult(a){
 $('#resultPanel').classList.remove('empty');
 $('#resultPanel').innerHTML=`<div class="result-top"><div><span class="eyebrow">${a.category}</span><h3>${escapeHtml(a.exceptionType)}</h3></div><span class="confidence">${a.confidence}% confidence</span></div>
 <div class="result-block"><label>Severity</label><div><span class="sev ${a.severity}">${a.severity}</span></div></div>
 <div class="result-block"><label>Likely root cause</label><div>${escapeHtml(a.rootCause)}</div></div>
 <div class="result-block"><label>Recommended debugging plan</label><div>${escapeHtml(a.solution)}</div></div>
 <div class="result-block"><label>Signal detected</label><div>${escapeHtml((a.logText||'').slice(0,260))}${(a.logText||'').length>260?'…':''}</div></div>`;
}
async function loadDashboard(){
 try{
  const [s,r]=await Promise.all([fetch('/api/stats').then(x=>x.json()),fetch('/api/analyses').then(x=>x.json())]);
  ['total','critical','high','database'].forEach(k=>$('#'+k).textContent=s[k]??0);
  $('#recent').innerHTML=(r.slice(0,6).map(a=>`<div class="recent-card"><span class="sev ${a.severity}">${a.severity}</span><h4>${escapeHtml(a.title)}</h4><p>${escapeHtml(a.category)} · ${escapeHtml(a.exceptionType)}</p></div>`).join(''))||'<div class="panel"><p>No investigations yet. Start with Analyzer.</p></div>';
 }catch(e){}
}
async function loadHistory(){
 try{const r=await fetch('/api/analyses').then(x=>x.json());
 $('#historyList').innerHTML=r.length?r.map(a=>`<div class="history-row"><div><b>${escapeHtml(a.title)}</b><br><span>${escapeHtml(a.exceptionType)}</span></div><span>${escapeHtml(a.category)}</span><span class="sev ${a.severity}">${a.severity}</span><button class="delete" onclick="removeAnalysis(${a.id})">✕</button></div>`).join(''):'<div class="panel"><p>No saved analyses.</p></div>';
 }catch(e){}
}
async function removeAnalysis(id){if(!confirm('Delete this investigation?'))return;await fetch('/api/analyses/'+id,{method:'DELETE'});toast('Investigation deleted');loadHistory();loadDashboard()}
function escapeHtml(s=''){return s.replace(/[&<>"']/g,m=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'}[m]))}
$('#themeBtn').onclick=()=>{document.body.classList.toggle('light');toast('Theme preference toggled')}
$('#compact').onchange=e=>document.body.classList.toggle('compact',e.target.checked);
$('#autosample').onchange=e=>{if(e.target.checked)loadSample()};
route();loadDashboard();