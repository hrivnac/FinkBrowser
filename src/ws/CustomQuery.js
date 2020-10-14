// Additions to the default menu

function modifyMenu(hform) {
  hform.fields.push({field:'latest', type: 'text', html: {caption: '* Latest', text : ' (show latest objects, time in minutes before now)' , attr: 'style="width: 100px"'}});
  hform.record.latest = '';
  }
  
function modifyRequest(hform) {
  return "&latest=" + hform.record.latest;
  }
  
// New search from the 'latests' alerts page
  
function searchDetails(url) {
  var dd = document.getElementsByClassName('details');
  var keys = "";
  for (var i = 0; i < dd.length; i++) {
    if (dd[i].checked) {
      keys += "key:key:" + dd[i].value + ",";
      }
    }
  if (keys === "") {
    for (var i = 0; i < dd.length; i++) {
      keys +=  "key:key:" + dd[i].value + ",";
      }
    }
  loadPane("result", url + "&filters=" + keys);  
  }
  
// New ranges search
  
function searchRanges(url) {
  var ramin  = $("#slider-ra" ).slider("values", 0);
  var ramax  = $("#slider-ra" ).slider("values", 1);
  var decmin = $("#slider-dec").slider("values", 0);
  var decmax = $("#slider-dec").slider("values", 1);
  var ff  = document.getElementById('ffselector').value;
  if (ff != "") {
    formula     = ff;
    formulaArgs = "";
    }
  else {
    formula     = "isWithinGeoLimits(" + ramin + "," + ramax + "," + decmin + "," + decmax + ")";
    formulaArgs = "ra,dec";
    }
  loadPane("result", url + "&formula=" + formula + "&formulaArgs=" + formulaArgs); 
  }
