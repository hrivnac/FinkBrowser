function actions(url, key) {
  return "<a href='#' onclick='loadPane(\"result\", \"" + url + "&key="             + key               + "\")'        title='concrete alert'                 >alert</a> - " +
         "<a href='#' onclick='loadPane(\"result\", \"" + url + "&filters=key:key:" + key.split("_")[0] + ":prefix\")' title='all alerts of the same objectId'>alerts</a>"
  }
