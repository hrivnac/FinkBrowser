<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.Lomikel.Januser.StringGremlinClient" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="org.apache.log4j.Logger" %>

<%! static Logger log = Logger.getLogger(Client_jsp.class); %>

<%
  String host = request.getParameter("api");
  String port = request.getParameter("importStatus");
  StringGremlinClient client = new StringGremlinClient("134.158.74.85", 24444);
  req = URLDecoder.decode(req, "UTF-8");
  String output = gc.interpret2JSON(req);
  log.info("Interpreting: " + req + " => " + output);
  gc.close();
  %>
<%=output%>


