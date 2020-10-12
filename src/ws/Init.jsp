<%@ page import="com.Lomikel.Utils.Info" %>
<%@ page import="com.Lomikel.Utils.NotifierURL" %>
<%@ page import="com.Lomikel.HBaser.Evaluator" %>

<%
  Evaluator.setAuxFuctions("com.astrolabsoftware.FinkBrowser.Apps.FinkEvaluatorFunctions",
                           "com/astrolabsoftware/FinkBrowser/Apps/FinkEvaluatorFunctions.bsh"); 
  NotifierURL.notify("", "FinkBrowserWS", Info.release());
  %>
