function modifyMenu(hform) {
  hform.fields.push({field:'latest', type: 'text', html: {caption: '* Latest', text : ' (show latest objects, time in minutes before now)' , attr: 'style="width: 100px"'}});
  hform.record.latest = '';
  }
  
function modifyRequest(hform) {
  return "&latest=" + hform.record.latest;
  }
  
function searchDetails(url) {
  var dd = document.getElementsByClassName('details');
  var keys = "";
  for (var i = 0; i < dd.length; i++) {
    if (dd[i].checked) {
      keys += dd[i].value + ",";
      }
    }
  if (keys === "") {
    for (var i = 0; i < dd.length; i++) {
      keys += dd[i].value + ",";
      }
    }
  loadPane("result", url + "&krefix=" + keys);  
  }
