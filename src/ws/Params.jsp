<%@ page import="com.Lomikel.WebService.HBase2Table" %>
<%@ page import="com.astrolabsoftware.FinkBrowser.HBaser.FinkHBaseClient" %>
<%@ page import="com.astrolabsoftware.FinkBrowser.WebService.FinkHBaseColumnsProcessor" %>

<%
  String hbaseRowName = "alert";
  String hbaseRowKey  = "rowkey";
  HBase2Table.changeColumnsProcessor(new FinkHBaseColumnsProcessor());
  %>
