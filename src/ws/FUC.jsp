<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.astrolabsoftware.FinkBrowser.Apps.FUC" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="org.apache.log4j.Logger" %>

<%! static Logger log = Logger.getLogger(FUC_jsp.class); %>

<%
  String api          = request.getParameter("api");
  String importStatus = request.getParameter("importStatus");
  String[] args       = new String[4]{"--api", api, "--importStatus", importStatus};
  out.println(FUC.doit(args));
  %>
<%=output%>


