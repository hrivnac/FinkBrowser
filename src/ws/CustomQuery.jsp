<%
  String latestS = request.getParameter("latest");
  latestS = (latestS == null || latestS.equals("null")) ? "" : latestS.trim();
  int latest = 0;
  if (!latestS.equals("")) {
    out.println("showing <b>" + latestS + "</b> latests results<br/>");
    latest = Integer.valueOf(latestS);
    h.latests("i:objectId", null, 0, false);
    }
  %>
    
