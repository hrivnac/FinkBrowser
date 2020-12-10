function actions(url, key) {
  return "<a href='#' onclick='loadPane(\"result\", \"" + url + "&key="             + key               + "\")'        title='see details of this concrete alert'                            >alert</a>  - " +
         "<a href='#' onclick='loadPane(\"result\", \"" + url + "&filters=key:key:" + key.split("_")[0] + ":prefix\")' title='see details of all alerts of the same objectId'                >alerts</a> - " +
         "<a href='http://134.158.75.151:24000/"                                    + key.split("_")[0] + "'           title='analyse with Fink Scuence Portal'               target='_blank'>Analyse&#8599;</a>";
  }
